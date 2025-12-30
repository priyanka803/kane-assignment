package com.com.tax_aware_rebalancer.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.com.tax_aware_rebalancer.dto.AssetLotRequest;
import com.com.tax_aware_rebalancer.dto.TaxLotRequest;
import com.com.tax_aware_rebalancer.entity.TaxLot;
import com.com.tax_aware_rebalancer.exception.CustomException;
import com.com.tax_aware_rebalancer.repository.TaxLotRepository;
import com.com.tax_aware_rebalancer.service.TaxLotService;
import com.com.tax_aware_rebalancer.translator.CommonTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TaxLotServiceImpl implements TaxLotService{

    @Autowired
    private TaxLotRepository taxLotRepository;
    
    @Autowired
    private CommonTranslator translator;
    
    private static final Logger log = LoggerFactory.getLogger(TaxLotServiceImpl.class);


    @Override
    public TaxLot saveTaxLot(TaxLotRequest request) {
        try {
        TaxLot taxLot=	translator.translateTaxLotRequestToEntity(request);
        taxLot.setId(null);
        taxLot.setProfit(new BigDecimal("0.0"));
        taxLot.setAmountAfterProfit(request.getAmount());
        	TaxLot saved = taxLotRepository.save(taxLot);
            log.info("TaxLot saved successfully with ID: "+saved.getId());
            return saved;
        } catch (Exception ex) {
            log.error("Error while saving TaxLot ", ex);
            throw new RuntimeException("Failed to save TaxLot", ex);
        }
    }

    @Override
    public List<TaxLot> getTaxLotByUserId(Long userId) {
        log.info("Fetching TaxLots for userId: "+ userId);
        try {
            List<TaxLot> lots = taxLotRepository.findByUserId(userId);
            if (lots.isEmpty()) {
                log.warn("No TaxLots found for userId: "+ userId);
                throw new CustomException("No TaxLots found for user ID: " + userId, HttpStatus.NOT_FOUND);
            }
            return lots;
        } catch (Exception ex) {
            log.error("Error while fetching TaxLots for userId: "+ userId, ex);
            throw new CustomException("Error fetching TaxLots for user ID: " + userId, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<TaxLot> getAllTaxLot() {
        log.info("Fetching all TaxLots");
        try {
            return taxLotRepository.findAll();
        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error("Error while fetching all TaxLots", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public String deleteTaxLotById(Long id) {
        log.info("Deleting TaxLot with ID: "+ id);
        try {
            if (!taxLotRepository.existsById(id)) {
                log.warn("TaxLot not found for delete with ID: "+ id);
                throw new CustomException("TaxLot not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
            taxLotRepository.deleteById(id);
            return "TaxLot deleted successfully.";
        } catch (Exception ex) {
            log.error("Error while deleting TaxLot with ID: "+ id, ex);
            throw new CustomException("Error deleting TaxLot with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public TaxLot updateTaxLot(AssetLotRequest request) {
        log.info("Updating TaxLot with ID: "+ request.getAssetId());
        try {
            TaxLot lot = taxLotRepository.findById(request.getAssetId()).get();

            if (lot == null) {
                log.warn("TaxLot not found for delete with ID: "+ request.getAssetId());
                throw new CustomException("Tax Lot not found with ID: " + request.getAssetId(), HttpStatus.NOT_FOUND);
            }
            lot.setProfit(request.getProfit());
            lot.setAmountAfterProfit(lot.getAmountAfterProfit().add(request.getProfit()));
            taxLotRepository.save(lot);
            return lot;

        } catch (Exception ex) {
        	log.error("Error while updating TaxLot with ID: "+ request.getAssetId(), ex);
            throw new CustomException("Error whuile updating TaxLot with ID: " + request.getAssetId(), HttpStatus.NOT_FOUND);
        }
    }

}
