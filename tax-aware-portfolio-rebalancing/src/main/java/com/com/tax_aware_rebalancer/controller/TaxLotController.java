package com.com.tax_aware_rebalancer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.com.tax_aware_rebalancer.dto.AssetLotRequest;
import com.com.tax_aware_rebalancer.dto.TaxLotRequest;
import com.com.tax_aware_rebalancer.entity.TaxLot;
import com.com.tax_aware_rebalancer.service.TaxLotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/taxLot")
public class TaxLotController {

	@Autowired
    private TaxLotService taxLotService;

    @PostMapping("/save")
    public ResponseEntity<TaxLot> createTaxLot(@Valid @RequestBody TaxLotRequest taxLot) {
        return new ResponseEntity<>(taxLotService.saveTaxLot(taxLot), HttpStatus.OK);
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<List<TaxLot>> getTaxLotByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(taxLotService.getTaxLotByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TaxLot>> getAll() {
        return new ResponseEntity<>(taxLotService.getAllTaxLot(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTaxLotById(@PathVariable Long id) {
        return new ResponseEntity<>(taxLotService.deleteTaxLotById(id), HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<TaxLot> update(@Valid @RequestBody AssetLotRequest request) {
            return new ResponseEntity<>(taxLotService.updateTaxLot(request), HttpStatus.OK);
    }

}
