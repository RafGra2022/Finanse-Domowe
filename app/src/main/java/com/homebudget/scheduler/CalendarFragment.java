package com.homebudget.scheduler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentView;
import com.homebudget.utils.CustomCalendar;
import com.homebudget.utils.DateFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public class CalendarFragment extends Fragment {

    public final LocalDate date = LocalDate.now();
    private int year = date.getYear();
    private int month = date.getMonth().getValue();
    private int months = 0;
    private Map<Integer, Button> monthDays;
    private Map<Integer, ColorStateList> alertDays = new HashMap<>();
    private final LocalDate now = LocalDate.now();
    List<TableRow> weeks;
    TableLayout tl;

    CustomCalendar customCalendar = new CustomCalendar(year, month, date, months);

    public CalendarFragment() {
    }

    public CalendarFragment(int year, int month, int months) {
        this.year = year;
        this.month = month;
        this.months = months;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        createCalendar();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, returnCallback());
    }

    void createCalendar() {
        customCalendar = new CustomCalendar(year, month, date, months);
        monthDays = fillCalendar();
        for (Integer day : monthDays.keySet()) {
            monthDays.get(day).setOnClickListener(dayAction());
        }
        if (alertDays != null) {
            for (Integer day : alertDays.keySet()) {
                if (day == now.getDayOfMonth() && month == now.getMonth().getValue()) {
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(500); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    monthDays.get(day).startAnimation(anim);
                }
                monthDays.get(day).setBackgroundTintList(alertDays.get(day));
            }
        }
    }

    public void setDayColor(int day, ColorStateList colorStateList) {
        alertDays.put(day, colorStateList);
    }

    private OnBackPressedCallback returnCallback() {
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
    }

    private OnClickListener dayAction() {
        return view -> {
            int day = Integer.parseInt(((Button) view).getText().toString());
            String date = DateFormatter.dateToQueryString(day, month, year);
            Callable<List<PaymentView>> payment = () -> AppDatabase.getDatabase(getContext()).paymentDAO().findPaymentViewByDate(date);
            try {
                List<PaymentView> dayPayments = AppDatabase.getExecutorService().submit(payment).get();
                PaymentDetailFragment paymentDetailFragment = new PaymentDetailFragment(dayPayments, date, this);
                if (!dayPayments.isEmpty()) {
                    getParentFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.payment_detail, paymentDetailFragment, null).commit();
                    getActivity().findViewById(R.id.payment_detail).setVisibility(View.VISIBLE);
                } else {
                    getActivity().findViewById(R.id.payment_detail).setVisibility(View.GONE);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        };
    }

    private Map<Integer, Button> fillCalendar() {
        clearCalendar();
        Map<Integer, Button> dayButtons = new HashMap<>();
        Button dayButton;
        int week = 0;
        tl = getView().findViewById(R.id.calendar_frame);
        weeks = new ArrayList<>();
        weeks.add(new TableRow(tl.getContext()));
        weeks.get(week).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        int daysInPreviousMonth = customCalendar.getDaysInPreviousMonth();
        int lastMondayInPreviousMonth = customCalendar.getLastMondayInPreviousMonth();
        if (daysInPreviousMonth - lastMondayInPreviousMonth != 6) {
            for (int i = lastMondayInPreviousMonth; i <= daysInPreviousMonth; i++) {
                weeks.get(week).addView(getDisabledDay(tl, i));
            }
        }
        for (int i = 1; i <= customCalendar.getDaysInCurrentMonth(); i++) {
            if (weeks.get(week).getVirtualChildCount() < 7) {
                dayButton = getDay(tl, i);
                weeks.get(week).addView(dayButton);
                dayButtons.put(i, dayButton);
            } else if (customCalendar.isMonday(i)) {
                dayButton = getDay(tl, i);
                weeks.add(new TableRow(tl.getContext()));
                week++;
                weeks.get(week).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                weeks.get(week).addView(dayButton);
                dayButtons.put(i, dayButton);
            } else {
                dayButton = getDay(tl, i);
                weeks.get(week).addView(dayButton);
                dayButtons.put(i, dayButton);
            }
        }
        int day = 1;
        while (weeks.get(week).getVirtualChildCount() < 7) {
            weeks.get(week).addView(getDisabledDay(tl, day));
            day++;
        }
        for (TableRow row : weeks) {
            tl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
        return dayButtons;
    }

    private void clearCalendar() {
        if (weeks != null) {
            for (TableRow week : weeks) {
                tl.removeView(week);
            }
        }
    }

    private Button getDay(TableLayout tl, int i) {
        if (now.getDayOfMonth() == i && now.getMonth().getValue() == month) {
            Button b = new Button(tl.getContext());
            b.setText(String.valueOf(i));
            b.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            b.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            return b;
        } else {
            Button b = new Button(tl.getContext());
            b.setText(String.valueOf(i));
            b.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            return b;
        }
    }

    private Button getDisabledDay(TableLayout tl, int i) {
        Button b = new Button(tl.getContext());
        b.setText(String.valueOf(i));
        b.setEnabled(false);
        b.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return b;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getMonths() {
        return months;
    }
}