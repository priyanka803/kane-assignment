package com.com.tax_aware_rebalancer.service;

import java.util.List;

import com.com.tax_aware_rebalancer.dto.AssetLotRequest;
import com.com.tax_aware_rebalancer.dto.TaxLotRequest;
import com.com.tax_aware_rebalancer.entity.TaxLot;

public interface TaxLotService {
	TaxLot saveTaxLot(TaxLotRequest taxLot);

	List<TaxLot> getTaxLotByUserId(Long userId);

	List<TaxLot> getAllTaxLot();

	String deleteTaxLotById(Long id);
	TaxLot updateTaxLot(AssetLotRequest request);
}
