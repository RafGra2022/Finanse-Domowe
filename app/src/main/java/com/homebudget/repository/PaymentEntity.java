package com.homebudget.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "payment")
public class PaymentEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "contractorid")
    private long contractorId;
    @ColumnInfo(name = "amount")
    private String amount;
    @ColumnInfo(name = "term")
    private String term;
    @ColumnInfo(name = "updtstmp")
    private String updateStamp;
    @ColumnInfo(name = "insstmp")
    private String insertStamp;
    @ColumnInfo(name = "status")
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContractorId() {
        return contractorId;
    }

    public void setContractorId(long contractorId) {
        this.contractorId = contractorId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
