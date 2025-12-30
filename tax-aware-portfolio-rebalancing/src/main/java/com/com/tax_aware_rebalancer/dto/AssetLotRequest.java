package com.com.tax_aware_rebalancer.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AssetLotRequest {

	@NotNull(message = "Asset ID is required")
	@Positive(message = "Asset ID must be a positive number")
	private Long assetId;
	@NotNull(message = "Profit must not be null , positive = gain and negative = loss")
	private BigDecimal profit;
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	
	

}
