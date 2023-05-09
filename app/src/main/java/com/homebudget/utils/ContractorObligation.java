package com.homebudget.utils;

import android.content.Context;

import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ContractorObligation {

    public static List<Integer> getPaymentsInMonth(LocalDate date, String status, Context context) {
        List<Integer> unpaidDays = new ArrayList<>();
        for (PaymentEntity paymentEntity : getMonthlyPayments(date, status, context)) {
            LocalDate dateToPay = DateFormatter.formatStringToLocalDate(paymentEntity.getTerm());
            if (dateToPay.getMonth() == date.getMonth()) {
                unpaidDays.add(dateToPay.getDayOfMonth());
            }
        }
        return unpaidDays;
    }

    private static List<PaymentEntity> getMonthlyPayments(LocalDate date, String status, Context context) {
        String queryTerm = "%" + date.getMonth().getValue() + "-" + date.getYear();
        Callable<List<PaymentEntity>> payment = () -> AppDatabase.getDatabase(context).paymentDAO().findPaymentsByTermAndStatus(queryTerm, status);
        List<PaymentEntity> payments = new ArrayList<>();
        try {
            payments = AppDatabase.getExecutorService().submit(payment).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return payments;
    }


}
