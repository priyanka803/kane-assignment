package com.com.tax_aware_rebalancer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class TaxLotRequest {

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Buy date is required")
    @PastOrPresent(message = "Buy date must be today or in the past")
    private LocalDate buyDate;

    @NotBlank(message = "Term is required")
    @Pattern(regexp = "SHORT|LONG", message = "Term must be either SHORT or LONG")
    private String term;
    
    @NotBlank(message = "Term is required")
    @Pattern(regexp = "STOCK|BOND", message = "Asset type must be either STOCK or BOND")
    private String assetType;

    @NotNull(message = "Tax percent is required")
    @DecimalMin(value = "0.0", message = "Tax percent must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Tax percent must not exceed 100")
    private BigDecimal taxPercent;


    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;


	public String getAssetName() {
		return assetName;
	}


	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public LocalDate getBuyDate() {
		return buyDate;
	}


	public void setBuyDate(LocalDate buyDate) {
		this.buyDate = buyDate;
	}


	public String getTerm() {
		return term;
	}


	public void setTerm(String term) {
		this.term = term;
	}


	public String getAssetType() {
		return assetType;
	}


	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}


	public BigDecimal getTaxPercent() {
		return taxPercent;
	}


	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}
    
}
