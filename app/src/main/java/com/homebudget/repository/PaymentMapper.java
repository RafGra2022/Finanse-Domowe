package com.homebudget.repository;

import com.homebudget.utils.SystemTimestamp;

public class PaymentMapper {

    public static PaymentEntity mapToPaymentEntity(ContractorEntity contractorEntity, String term) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setContractorId(contractorEntity.getId());
        paymentEntity.setTerm(term);
        paymentEntity.setStatus(PaymentStatus.UNPAID.getValue());
        paymentEntity.setUpdateStamp(SystemTimestamp.getTimestamp());
        paymentEntity.setInsertStamp(SystemTimestamp.getTimestamp());
        return paymentEntity;
    }
}
