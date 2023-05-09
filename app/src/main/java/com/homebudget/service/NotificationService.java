package com.homebudget.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.homebudget.notification.PaymentReminder;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentStatus;
import com.homebudget.repository.PaymentView;
import com.homebudget.utils.DateFormatter;

import java.time.LocalDate;
import java.util.List;

public class NotificationService extends JobService {

    private final int daysToNotify = 2;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        showNotification(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void showNotification(JobParameters jobParameters) {

        Runnable notify = () -> {
            LocalDate now = LocalDate.now();
            String queryTerm = "%" + now.getMonth().getValue() + "-" + now.getYear();
            List<PaymentView> payments = AppDatabase.getDatabase(getApplicationContext()).paymentDAO().findPaymentViewByTermAndStatus(PaymentStatus.UNPAID.getValue(),queryTerm);
            StringBuilder message = new StringBuilder();
            for (PaymentView paymentView : payments) {
                LocalDate paymentEntityTerm = DateFormatter.formatStringToLocalDate(paymentView.getTerm());
                if (paymentEntityTerm.getMonth().getValue() == now.getMonth().getValue()) {
                    for (int i = 0; i <= daysToNotify; i++) {
                        if (paymentEntityTerm.getDayOfMonth() - i == now.getDayOfMonth()) {
                            if(message.length() != 0){
                                message.append(",");
                            }
                            message.append(paymentView.getService());
                            break;
                        }
                    }
                }
            }
            if(message.length() != 0){
                PaymentReminder paymentReminder = new PaymentReminder(this, message.toString());
                paymentReminder.showNotification();
            }
        };
        AppDatabase.getExecutorService().execute(notify);
        jobFinished(jobParameters, false);
    }

}
