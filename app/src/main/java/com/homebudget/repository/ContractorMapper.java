package com.homebudget.repository;

import com.homebudget.domain.ContractorDetail;
import com.homebudget.utils.SystemTimestamp;

import java.time.format.DateTimeFormatter;

public class ContractorMapper {

    public static ContractorEntity mapToContractorEntity(ContractorDetail contractorDetail) {
        ContractorEntity contractorEntity = new ContractorEntity();
        contractorEntity.setContractorName(contractorDetail.getContractorName());
        contractorEntity.setServiceName(contractorDetail.getServiceName());
        contractorEntity.setAccountNumber(contractorDetail.getAccountNumber());
        contractorEntity.setPaymentCycle(contractorDetail.getPaymentCycle());
        contractorEntity.setPaymentTerm(contractorDetail.getPaymentTerm().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        contractorEntity.setUpdateStamp(SystemTimestamp.getTimestamp());
        contractorEntity.setInsertStamp(SystemTimestamp.getTimestamp());
        return contractorEntity;
    }
}
