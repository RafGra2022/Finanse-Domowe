package com.homebudget.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobService;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.homebudget.R;
import com.homebudget.scheduler.SchedulerActivity;

public class PaymentReminder {

    private JobService activity;
    private NotificationCompat.Builder builder;
    private static final String CHANNEL_ID = "payment-reminder";

    public PaymentReminder(JobService jobService, String message) {
        this.activity = jobService;
        Intent intent = new Intent(activity, SchedulerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder = new NotificationCompat.Builder(jobService.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle(jobService.getString(R.string.payment_notification))
                .setContentText(jobService.getString(R.string.payment_expiration) + " " + message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
    }


    public void showNotification() {
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {

        CharSequence name = "test channel";
        String description = "test description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}
