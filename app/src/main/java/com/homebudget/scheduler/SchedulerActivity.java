package com.homebudget.scheduler;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.homebudget.R;
import com.homebudget.addcontractor.AddContractorActivity;
import com.homebudget.removecontractor.RemoveContractorActivity;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentStatus;
import com.homebudget.utils.ContractorObligation;

import java.time.LocalDate;


public class SchedulerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        Toolbar toolbar = findViewById(R.id.application_toolbar);
        setSupportActionBar(toolbar);

        Runnable contractors = () -> {
            AppDatabase.getDatabase(getApplicationContext()).contractorDAO().findAllContractors();
        };
        AppDatabase.getExecutorService().submit(contractors);

        CalendarFragment calendarFragment = new CalendarFragment();

        for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.now(), PaymentStatus.PAID.getValue(), getApplicationContext())) {
            calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.GREEN));
        }
        for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.now(), PaymentStatus.UNPAID.getValue(), getApplicationContext())) {
            calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.RED));
        }

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.calendar, calendarFragment, null).commit();
        findViewById(R.id.payment_detail).setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddContractorActivity.class);
        int itemId = item.getItemId();
        if (itemId == R.id.add_contractor_menu) {
            startActivity(intent);
        }
        if(itemId == R.id.remove_contractor_menu){
            startActivity(new Intent(this, RemoveContractorActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}