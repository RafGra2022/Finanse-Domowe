package com.homebudget;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.homebudget.R;
import com.homebudget.scheduler.SchedulerActivity;
import com.homebudget.service.NotificationService;
import com.homebudget.service.SchedulerService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, SchedulerService.class));
        Intent intent = new Intent(this, SchedulerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, SchedulerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ComponentName serviceComponent = new ComponentName(getApplicationContext(), NotificationService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setPersisted(true);
        builder.setPeriodic(3*60 * 60 * 1000);
        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(getApplicationContext().JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}