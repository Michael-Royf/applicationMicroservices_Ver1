package com.michael.fraud.service;

import com.michael.fraud.entity.FraudCheckHistory;
import com.michael.fraud.repository.FraudCheckHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class FraudCheckHistoryService {
    @Autowired
    private FraudCheckHistoryRepository fraudCheckHistoryRepository;


    public Boolean isFraudulentCustomer(Long customerId) {
        fraudCheckHistoryRepository.save(
                FraudCheckHistory.builder()
                        .isFraudster(false)
                        .customerId(customerId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        log.info("Fraud check request for customer {}", customerId);
        return false;
    }


}
