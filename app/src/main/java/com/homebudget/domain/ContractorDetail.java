package com.homebudget.domain;

import java.time.LocalDate;

public class ContractorDetail {

    public ContractorDetail(String contractorName, String serviceName, String accountNumber, Integer paymentCycle, LocalDate paymentTerm) {
        this.contractorName = contractorName;
        this.serviceName = serviceName;
        this.accountNumber = accountNumber;
        this.paymentCycle = paymentCycle;
        this.paymentTerm = paymentTerm;
    }

    private final String contractorName;
    private final String serviceName;
    private final String accountNumber;
    private final Integer paymentCycle;
    private final LocalDate paymentTerm;

    public String getContractorName() {
        return contractorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public LocalDate getPaymentTerm() {
        return paymentTerm;
    }
}
