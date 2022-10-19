package com.michael.customer.exception.domain;

public class CustomerIsFraudsterException  extends RuntimeException{
    public CustomerIsFraudsterException(String message) {
        super(message);
    }
}
