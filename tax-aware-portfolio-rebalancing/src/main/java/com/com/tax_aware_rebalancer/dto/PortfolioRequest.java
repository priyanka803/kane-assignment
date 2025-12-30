package com.com.tax_aware_rebalancer.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PortfolioRequest {

    @NotBlank(message = "Portfolio name is required")
    private String name;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Target stock percent is required")
    @DecimalMin(value = "0.0", message = "Stock percent must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Stock percent must not exceed 100")
    private BigDecimal targetStockPercent;

    @NotNull(message = "Target bond percent is required")
    @DecimalMin(value = "0.0", message = "Bond percent must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Bond percent must not exceed 100")
    private BigDecimal targetBondPercent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTargetStockPercent() {
		return targetStockPercent;
	}

	public void setTargetStockPercent(BigDecimal targetStockPercent) {
		this.targetStockPercent = targetStockPercent;
	}

	public BigDecimal getTargetBondPercent() {
		return targetBondPercent;
	}

	public void setTargetBondPercent(BigDecimal targetBondPercent) {
		this.targetBondPercent = targetBondPercent;
	}
    
}
