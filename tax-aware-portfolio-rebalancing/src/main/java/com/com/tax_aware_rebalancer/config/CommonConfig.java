package com.com.tax_aware_rebalancer.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}
