package com.homebudget.repository;

import androidx.room.DatabaseView;

@DatabaseView(viewName = "v_payment", value = "Select contractor.contractor,contractor.service,contractor.account,payment.id,payment.amount, payment.term, payment.status from payment INNER JOIN contractor ON payment.contractorid = contractor.id")
public class PaymentView {

    private String contractor;
    private String service;
    private String account;
    private long id;
    private String amount;
    private String term;
    private String status;

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
