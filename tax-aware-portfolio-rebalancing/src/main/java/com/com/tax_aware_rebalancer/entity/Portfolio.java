package com.com.tax_aware_rebalancer.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal targetStockPercent;
    private BigDecimal targetBondPercent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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

