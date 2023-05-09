package com.homebudget.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.ContractorEntity;
import com.homebudget.repository.PaymentEntity;
import com.homebudget.repository.PaymentMapper;
import com.homebudget.utils.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SchedulerService extends Service {

    private ContractorEntity contractorEntity;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<ContractorEntity> contractors = getContractors();

        if (contractors != null) {
            for (ContractorEntity contractorEntity : contractors) {
                this.contractorEntity = contractorEntity;
                annualSchedule();
            }
        }

        return START_STICKY;
    }

    private void annualSchedule() {

        Callable<List<PaymentEntity>> payment = () -> AppDatabase
                .getDatabase(getApplicationContext())
                .paymentDAO()
                .findPaymentByContractor(contractorEntity.getId());
        Future<List<PaymentEntity>> futurePayment = AppDatabase.getExecutorService().submit(payment);

        try {
            fillAnnualPayment(futurePayment.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillAnnualPayment(List<PaymentEntity> payments) {
        Runnable write = () -> {
            if (payments.isEmpty()) {
                PaymentEntity paymentEntity = PaymentMapper.mapToPaymentEntity(contractorEntity, contractorEntity.getPaymentTerm());
                payments.add(paymentEntity);
                AppDatabase.getDatabase(getApplicationContext()).paymentDAO().insert(paymentEntity);
            }
            LocalDate lastPayment;
            lastPayment = findLastPayment(payments);

            boolean updated = false;

            while (lastPayment.isBefore(LocalDate.now().plusYears(1))) {
                updated = true;
                Log.i("", "Service " + contractorEntity.getServiceName() + " needs update");
                lastPayment = lastPayment.plusMonths(contractorEntity.getPaymentCycle());
                String nextDate = DateFormatter.formatLocalDateToString(lastPayment);
                Log.i("", nextDate);
                AppDatabase.getDatabase(getApplicationContext()).paymentDAO().insert(PaymentMapper.mapToPaymentEntity(contractorEntity, nextDate));
            }
            if (!updated) {
                Log.i("", "Everything is up to date!");
            }
        };

        AppDatabase.getExecutorService().execute(write);
    }

    private LocalDate findLastPayment(List<PaymentEntity> payments) {
        LocalDate lastPayment = null;
        for (PaymentEntity paymentEntity : payments) {
            if (lastPayment == null) {
                lastPayment = mapStringToLocalDate(paymentEntity.getTerm());
            }
            if (lastPayment.isBefore(mapStringToLocalDate(paymentEntity.getTerm()))) {
                lastPayment = mapStringToLocalDate(paymentEntity.getTerm());
            }
        }
        return lastPayment;
    }

    private LocalDate mapStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private List<ContractorEntity> getContractors() {
        Callable<List<ContractorEntity>> contractors = () -> AppDatabase.getDatabase(getApplicationContext()).contractorDAO().findAllContractors();
        try {
            return AppDatabase.getExecutorService().submit(contractors).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}