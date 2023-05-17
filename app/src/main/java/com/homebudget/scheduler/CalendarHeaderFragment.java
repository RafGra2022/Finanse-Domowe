package com.homebudget.scheduler;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.domain.MonthsProvider;
import com.homebudget.repository.PaymentStatus;
import com.homebudget.utils.ContractorObligation;

import java.time.LocalDate;


public class CalendarHeaderFragment extends Fragment {

    public final LocalDate date = LocalDate.now();
    private int year = date.getYear();
    private int month = date.getMonth().getValue();
    private int monthsToSkip = 0;
    MonthsProvider monthsProvider = new MonthsProvider();
    TextView header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header = getView().findViewById(R.id.date);
    }

    @Override
    public void onStart() {
        super.onStart();
        setCalendarHeader();
    }

    private TextView setCalendarHeader() {
        Button nextMonth = getView().findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(nextMonthAction());
        Button previousMonth = getView().findViewById(R.id.previousMonth);
        previousMonth.setOnClickListener(previousMonthAction());
        header.setTextSize(20);
        String date = monthsProvider.getMonth(month) + " " + (year);
        header.setText(date);
        return header;
    }

    private View.OnClickListener previousMonthAction() {
        return view -> {
            getActivity().findViewById(R.id.payment_detail).setVisibility(View.GONE);
            monthsToSkip--;
            if (month != 1) {
                month = month - 1;
            } else {
                year = year - 1;
                month = 12;
            }
            CalendarFragment calendarFragment = new CalendarFragment(year, month, monthsToSkip);

            String date = monthsProvider.getMonth(month) + " " + (year);
            header.setText(date);

            for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.of(year, month, 1), PaymentStatus.PAID.getValue(), getContext())) {
                calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.GREEN));
            }
            for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.of(year, month, 1), PaymentStatus.UNPAID.getValue(), getContext())) {
                calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.RED));
            }
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.calendar, calendarFragment, null).commit();

        };
    }

    private View.OnClickListener nextMonthAction() {
        return view -> {
            getActivity().findViewById(R.id.payment_detail).setVisibility(View.GONE);
            monthsToSkip++;
            if (month != 12) {
                month = month + 1;
            } else {
                year = year + 1;
                month = 1;
            }

            CalendarFragment calendarFragment = new CalendarFragment(year, month, monthsToSkip);
            String date = monthsProvider.getMonth(month) + " " + (year);
            header.setText(date);

            for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.of(year, month, 1), PaymentStatus.PAID.getValue(), getContext())) {
                calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.GREEN));
            }
            for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.of(year, month, 1), PaymentStatus.UNPAID.getValue(), getContext())) {
                calendarFragment.setDayColor(dayToPay, ColorStateList.valueOf(Color.RED));
            }

            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.calendar, calendarFragment, null).commit();
        };
    }
}