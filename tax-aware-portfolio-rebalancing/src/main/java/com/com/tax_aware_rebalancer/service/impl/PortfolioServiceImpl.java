package com.com.tax_aware_rebalancer.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.com.tax_aware_rebalancer.dto.PortfolioRequest;
import com.com.tax_aware_rebalancer.entity.Portfolio;
import com.com.tax_aware_rebalancer.entity.TaxLot;
import com.com.tax_aware_rebalancer.exception.CustomException;
import com.com.tax_aware_rebalancer.exception.InvalidPercentageException;
import com.com.tax_aware_rebalancer.repository.PortfolioRepository;
import com.com.tax_aware_rebalancer.repository.TaxLotRepository;
import com.com.tax_aware_rebalancer.service.PortfolioService;
import com.com.tax_aware_rebalancer.translator.CommonTranslator;


@Service
public class PortfolioServiceImpl implements PortfolioService {
	@Autowired
	private PortfolioRepository portfolioRepository;

	@Autowired
	private TaxLotRepository taxLotRepository;

	@Autowired
	private CommonTranslator translator;

    private static final Logger log = LoggerFactory.getLogger(TaxLotServiceImpl.class);

	@Override
	public Portfolio save(PortfolioRequest request) {

		try {
		 BigDecimal total = request.getTargetStockPercent().add(request.getTargetBondPercent());

		    if (total.compareTo(new BigDecimal("100")) > 0) {
		        throw new InvalidPercentageException(
		            "Total allocation should be 100% or less.", HttpStatus.BAD_REQUEST
		        );
		    }

			Portfolio saved = portfolioRepository.save(translator.translatePorfolioRequestToEntity(request));
			log.info("Portfolio saved successfully with ID: " + saved.getId());
			return saved;
		} catch (Exception ex) {
			log.error("Error while saving Portfolio ", ex);
			throw new RuntimeException("Failed to save Portfolio", ex);
		}
	}

	@Override
	public Portfolio getByUserId(Long userId) {
		log.info("Fetching Portfolio with ID: " + userId);
		try {
			Portfolio portfolio = portfolioRepository.findById(userId).orElseThrow(() -> {
				log.warn("Portfolio not found with ID: " + userId);
				return new CustomException("Portfolio not found with ID: " + userId, HttpStatus.NOT_FOUND);
			});
			return portfolio;
		} catch (Exception ex) {
			log.error("Error while fetching Portfolio with ID: " + userId, ex);
			throw new CustomException("Error fetching Portfolio with ID: " + userId, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public String rebalancePortfolio(Long userId) {
		log.info("Rebalancing started for UserId: " + userId);

		try {
			Portfolio portfolio = getByUserId(userId);
			List<TaxLot> lots = taxLotRepository.findByUserId(userId);

			if (lots.isEmpty()) {
				return "No tax lots available for rebalance";
			}

			BigDecimal stockTotal = BigDecimal.ZERO;
			BigDecimal bondTotal = BigDecimal.ZERO;

			for (TaxLot lot : lots) {
				if (lot.getAssetType().equalsIgnoreCase("BOND")) {
					bondTotal = bondTotal.add(lot.getAmountAfterProfit());
				} else {
					stockTotal = stockTotal.add(lot.getAmountAfterProfit());
				}
			}

			BigDecimal total = stockTotal.add(bondTotal);
			if (total.compareTo(BigDecimal.ZERO) == 0) {
				return "Total portfolio value is zero, cannot rebalance";
			}


			BigDecimal currentStockPercent = stockTotal.multiply(new BigDecimal("100")).divide(total, 2);

			BigDecimal highestValue  = portfolio.getTargetStockPercent().add(new BigDecimal("5"));
			BigDecimal lowestValue = portfolio.getTargetStockPercent().subtract(new BigDecimal("5"));

			if (currentStockPercent.compareTo(highestValue) <= 0 && currentStockPercent.compareTo(lowestValue) >= 0) {
				return "No Rebalance Needed – everything is already balanced";
			}

			BigDecimal stockShare  = total.multiply(portfolio.getTargetStockPercent()).divide(new BigDecimal("100"), 2);

			BigDecimal bondShare  = total.multiply(portfolio.getTargetBondPercent()).divide(new BigDecimal("100"), 2);

			BigDecimal difference   = stockTotal.subtract(stockShare);
			log.info("Amount to adjust: " + difference  );

			if (difference .abs().compareTo(new BigDecimal("100")) < 0) {
				return "No Rebalance – adjustment amount is too small";
			}

			lots.sort((a, b) -> getPriority(a).compareTo(getPriority(b)));

			BigDecimal gains = BigDecimal.ZERO;
			BigDecimal losses = BigDecimal.ZERO;

			for (TaxLot lot : lots) {
				if (lot.getAssetType().equalsIgnoreCase("BOND"))
					continue;

				if (lot.getProfit().compareTo(BigDecimal.ZERO) > 0) {
					gains = gains.add(lot.getProfit());
				}
				if (lot.isInLoss()) {
					BigDecimal lossValue = lot.getAmountAfterProfit().subtract(lot.getAmount());
					losses = losses.add(lossValue);
				}
			}

			BigDecimal adjustValue = losses.min(gains);
			BigDecimal finalResult = gains.subtract(adjustValue );

			BigDecimal valueLeft  = difference.abs();
			BigDecimal taxPaid = BigDecimal.ZERO;

			for (TaxLot lot : lots) {
				if (valueLeft .compareTo(BigDecimal.ZERO) <= 0)
					break;
				if (lot.getAssetType().equalsIgnoreCase("BOND"))
					continue;

				BigDecimal sellAmount = lot.getAmountAfterProfit();
				if (sellAmount.compareTo(valueLeft ) > 0) {
					sellAmount = valueLeft ;
				}

				if (lot.getProfit().compareTo(BigDecimal.ZERO) > 0 && finalResult.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal tax = sellAmount.multiply(lot.getTaxPercent()).divide(new BigDecimal("100"), 2);
					taxPaid = taxPaid.add(tax);
				}

				if (lot.getBuyDate().plusDays(30).isAfter(LocalDate.now()) && lot.isInLoss()) {
					lot.setTaxPercent(BigDecimal.ZERO);
					lot.setProfit(BigDecimal.ZERO);
				}

				lot.setAmount(lot.getAmountAfterProfit().subtract(sellAmount));
				lot.setAmountAfterProfit(lot.getAmountAfterProfit().subtract(sellAmount));
				taxLotRepository.save(lot);

				valueLeft  = valueLeft .subtract(sellAmount);
			}

			BigDecimal bondAfter = BigDecimal.ZERO;
			List<TaxLot> updated = taxLotRepository.findByUserId(userId);

			for (TaxLot lot : updated) {
				if (lot.getAssetType().equalsIgnoreCase("BOND")) {
					bondAfter = bondAfter.add(lot.getAmountAfterProfit());
				}
			}

			BigDecimal bondNeeded = bondShare.subtract(bondAfter);
			if (bondNeeded.compareTo(BigDecimal.ZERO) > 0) {
				TaxLot newBond = new TaxLot();
				newBond.setUserId(userId);
				newBond.setAssetType("BOND");
				newBond.setAssetName("BOND_DYNAMIC");
				newBond.setAmount(bondNeeded);
				newBond.setAmountAfterProfit(bondNeeded);
				newBond.setBuyDate(LocalDate.now());
				newBond.setProfit(BigDecimal.ZERO);
				newBond.setTaxPercent(BigDecimal.ZERO);
				newBond.setTerm("LONG");

				taxLotRepository.save(newBond);
			}

			return "Rebalanced Successfully\nTax Paid: " + taxPaid;

		} catch (Exception e) {
			log.error("Rebalancing failed", e);
			throw new CustomException("Rebalancing failed for user: " + userId, HttpStatus.NOT_FOUND);
		}
	}

	private BigDecimal getPriority(TaxLot lot) {
		if (lot.isInLoss() && lot.getTerm().equals("SHORT"))
			return new BigDecimal("1");
		if (lot.isInLoss() && lot.getTerm().equals("LONG"))
			return new BigDecimal("2");
		if (lot.getProfit().compareTo(BigDecimal.ZERO) > 0 && lot.getTerm().equals("LONG"))
			return new BigDecimal("3");
		if (lot.getProfit().compareTo(BigDecimal.ZERO) > 0 && lot.getTerm().equals("SHORT"))
			return new BigDecimal("4");
		return new BigDecimal("5");
	}

}
