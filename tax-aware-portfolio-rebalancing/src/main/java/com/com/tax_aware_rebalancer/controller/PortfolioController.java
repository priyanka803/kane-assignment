package com.com.tax_aware_rebalancer.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.com.tax_aware_rebalancer.dto.PortfolioRequest;
import com.com.tax_aware_rebalancer.entity.Portfolio;
import com.com.tax_aware_rebalancer.service.PortfolioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired PortfolioService service;

    @PostMapping("/save")
    public ResponseEntity<Portfolio> create(@Valid @RequestBody PortfolioRequest request) {
        return new ResponseEntity<>(service.save(request), HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Portfolio> getByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/rebalance/{userId}")
    public ResponseEntity<String> rebalance(@PathVariable Long userId) {
        return new ResponseEntity<>(service.rebalancePortfolio(userId), HttpStatus.OK);
    }
}
