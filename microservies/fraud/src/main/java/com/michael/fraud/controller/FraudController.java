package com.michael.fraud.controller;

import com.michael.clients.fraud.FraudCheckResponse;
import com.michael.fraud.service.FraudCheckHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fraud-check")
@Slf4j
public class FraudController {
    @Autowired
    private FraudCheckHistoryService fraudCheckHistoryService;


    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public FraudCheckResponse fraudCheckResponseisFraudster(@PathVariable Long customerId) {
        boolean isFraudsterCustomer = fraudCheckHistoryService.isFraudulentCustomer(customerId);
        return new FraudCheckResponse(isFraudsterCustomer);
    }


}
