package com.homebudget.repository;

public class PaymentStatus {

    private String status;

    private PaymentStatus(String status) {
        this.status = status;
    }

    public static final PaymentStatus UNPAID = new PaymentStatus("U");

    public static final PaymentStatus PAID = new PaymentStatus("P");

    public String getValue() {
        return status;
    }
}
