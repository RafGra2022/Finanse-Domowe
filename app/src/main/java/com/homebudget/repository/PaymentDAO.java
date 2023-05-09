package com.homebudget.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PaymentDAO {

    @Insert
    void insert(PaymentEntity paymentEntity);

    @Query("Update payment set amount = :amount where term = :term")
    void updatePaymentValue(String amount, String term);

    @Query("Update payment set status = :status where term = :term")
    void updatePaymentStatus(String status, String term);

    @Query("Update payment set term = :term where id = :id")
    void updatePaymentTerm(Long id, String term);

    @Query("Delete from payment where contractorid = :contractorId")
    void deletePaymentByContractorId(Long contractorId);

    @Query("Select * from payment where contractorid = :contractorId")
    List<PaymentEntity> findPaymentByContractor(Long contractorId);

    @Query("Select * from payment where status = :status and term like :term")
    List<PaymentEntity> findPaymentsByTermAndStatus(String term, String status);

    @Query("Select * from v_payment where id = :id")
    PaymentView findPaymentViewById(long id);

    @Query("SELECT * FROM v_payment where term = :term")
    List<PaymentView> findPaymentViewByDate(String term);

    @Query("SELECT * FROM v_payment where status = :status and term like :term")
    List<PaymentView> findPaymentViewByTermAndStatus(String status, String term);

}
