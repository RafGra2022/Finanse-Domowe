package com.homebudget.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contractor")
public class ContractorEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "contractor")
    private String contractorName;
    @ColumnInfo(name = "service")
    private String serviceName;
    @ColumnInfo(name = "account")
    private String accountNumber;
    @ColumnInfo(name = "cycle")
    private Integer paymentCycle;
    @ColumnInfo(name = "term")
    private String paymentTerm;
    @ColumnInfo(name = "updstmp")
    private String updateStamp;
    @ColumnInfo(name = "insstmp")
    private String insertStamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(Integer paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getUpdateStamp() {
        return updateStamp;
    }

    public void setUpdateStamp(String updateStamp) {
        this.updateStamp = updateStamp;
    }

    public String getInsertStamp() {
        return insertStamp;
    }

    public void setInsertStamp(String insertStamp) {
        this.insertStamp = insertStamp;
    }
}
