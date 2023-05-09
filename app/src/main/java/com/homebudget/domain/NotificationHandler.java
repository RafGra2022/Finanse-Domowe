package com.homebudget.domain;

import android.app.job.JobService;
import android.content.Context;

import com.homebudget.notification.PaymentReminder;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentStatus;
import com.homebudget.repository.PaymentView;
import com.homebudget.utils.DateFormatter;

import java.time.LocalDate;
import java.util.List;

public class NotificationHandler {

    public static void createNotification(Context context, JobService jobService, Integer daysToNotify){
        Runnable notify = () -> {
            LocalDate now = LocalDate.now();
            String queryTerm = "%" + now.getMonth().getValue() + "-" + now.getYear();
            List<PaymentView> payments = AppDatabase.getDatabase(context).paymentDAO().findPaymentViewByTermAndStatus(PaymentStatus.UNPAID.getValue(),queryTerm);
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
                PaymentReminder paymentReminder = new PaymentReminder(jobService, message.toString());
                paymentReminder.showNotification();
            }
        };
        AppDatabase.getExecutorService().execute(notify);
    }
}
