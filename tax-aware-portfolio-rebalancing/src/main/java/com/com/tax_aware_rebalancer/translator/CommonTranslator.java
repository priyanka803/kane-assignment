package com.com.tax_aware_rebalancer.translator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.com.tax_aware_rebalancer.dto.PortfolioRequest;
import com.com.tax_aware_rebalancer.dto.TaxLotRequest;
import com.com.tax_aware_rebalancer.entity.Portfolio;
import com.com.tax_aware_rebalancer.entity.TaxLot;

@Component
public class CommonTranslator {

	@Autowired
	private ModelMapper mapper;
	
	public Portfolio translatePorfolioRequestToEntity(PortfolioRequest request) {
		return mapper.map(request, Portfolio.class);
	}
	
	public TaxLot translateTaxLotRequestToEntity(TaxLotRequest request) {
		return mapper.map(request, TaxLot.class);
	}
}
