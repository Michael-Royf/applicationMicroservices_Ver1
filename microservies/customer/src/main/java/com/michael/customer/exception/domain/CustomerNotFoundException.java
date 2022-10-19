package com.michael.customer.exception.domain;

public class CustomerNotFoundException  extends  RuntimeException{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
