package com.com.tax_aware_rebalancer.service;

import com.com.tax_aware_rebalancer.dto.PortfolioRequest;
import com.com.tax_aware_rebalancer.entity.Portfolio;

public interface PortfolioService {

	 Portfolio save(PortfolioRequest request);
	 Portfolio getByUserId(Long userId);
	 String rebalancePortfolio(Long userId);
}
