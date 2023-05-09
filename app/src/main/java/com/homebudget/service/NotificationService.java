package com.homebudget.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.homebudget.domain.NotificationHandler;
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
        NotificationHandler.createNotification(getApplicationContext(),this,daysToNotify);
        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

}
