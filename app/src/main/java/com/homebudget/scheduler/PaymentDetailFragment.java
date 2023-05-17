package com.homebudget.scheduler;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.addcontractor.AddContractorActivity;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentStatus;
import com.homebudget.repository.PaymentView;
import com.homebudget.utils.ContractorObligation;
import com.homebudget.utils.DateFormatter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaymentDetailFragment extends Fragment {

    public PaymentDetailFragment() {
    }

    public PaymentDetailFragment(List<PaymentView> dayPayments, String date, CalendarFragment calendarFragment) {
        this.dayPayments = dayPayments;
        this.date = date;
        this.calendarFragment = calendarFragment;
    }

    private List<PaymentView> dayPayments;
    private String date;
    private CalendarFragment calendarFragment;
    private TextView contractor;
    private TextView service;
    private TextView account;
    private TextView sum;
    private TableRow amountRow;
    private TableRow sumRow;
    private LinearLayout buttonPanel;
    private Button save;
    private Button previousPayment;
    private Button nextPayment;
    private Button paid;
    private Button edit;
    private EditText amount;
    private PaymentView paymentView;
    private Integer paymentViewCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        save = getView().findViewById(R.id.save);
        contractor = getView().findViewById(R.id.contractor);
        service = getView().findViewById(R.id.service);
        account = getView().findViewById(R.id.account_number);
        sum = getView().findViewById(R.id.sum);
        amountRow = getView().findViewById(R.id.amountTableRow);
        sumRow = getView().findViewById(R.id.sum_row);
        previousPayment = getView().findViewById(R.id.previousPayment);
        nextPayment = getView().findViewById(R.id.nextPayment);
        amount = getView().findViewById(R.id.amount);
        buttonPanel = getView().findViewById(R.id.control_panel);
        paid = getView().findViewById(R.id.paid);
        edit = getView().findViewById(R.id.edit);
    }

    @Override
    public void onStart() {
        super.onStart();
        save.setOnClickListener(save());
        paid.setOnClickListener(paid());
        edit.setOnClickListener(edition());
        previousPayment.setOnClickListener(previousPayment());
        nextPayment.setOnClickListener(nextPayment());
        loadToView(paymentViewCounter);
    }

    private View.OnClickListener previousPayment() {
        return view -> {
            paymentViewCounter--;
            loadToView(paymentViewCounter);
        };
    }

    private View.OnClickListener nextPayment() {
        return view -> {
            paymentViewCounter++;
            loadToView(paymentViewCounter);
        };
    }

    private View.OnClickListener edition() {
        return view -> {
            Intent intent = new Intent(getActivity(), AddContractorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("edit", true);
            bundle.putLong("paymentid", paymentView.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        };
    }

    private View.OnClickListener paid() {
        return view -> {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getContext());

            builder.setMessage("Czy potwierdzasz płatność?")
                    .setCancelable(false)
                    .setPositiveButton("Tak", (dialog, id) -> {
                        Runnable statusUpdate = () -> {
                            AppDatabase.getDatabase(getContext()).paymentDAO().updatePaymentStatus(PaymentStatus.PAID.getValue(), dayPayments.get(paymentViewCounter).getId());
                        };
                        AppDatabase.getExecutorService().execute(statusUpdate);
                        Toast.makeText(getContext(), "Płatność potwierdzona", Toast.LENGTH_LONG).show();
                        buttonPanel.setVisibility(View.GONE);
                        CalendarFragment calendar = new CalendarFragment(calendarFragment.getYear(), calendarFragment.getMonth(), calendarFragment.getMonths());

                        for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.of(calendar.getYear(), calendar.getMonth(), 1), PaymentStatus.UNPAID.getValue(), getContext())) {
                            calendar.setDayColor(dayToPay, ColorStateList.valueOf(Color.RED));
                        }
                        for (Integer dayToPay : ContractorObligation.getPaymentsInMonth(LocalDate.now(), PaymentStatus.PAID.getValue(), getContext())) {
                            calendar.setDayColor(dayToPay, ColorStateList.valueOf(Color.GREEN));
                        }
                        calendar.setDayColor(DateFormatter.formatStringToLocalDate(date).getDayOfMonth(), ColorStateList.valueOf(Color.GREEN));
                        getParentFragmentManager().beginTransaction().replace(R.id.calendar, calendar).commit();
                    })
                    .setNegativeButton("Nie", (dialog, id) -> {
                        //  Action for 'No' Button
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Potwierdzenie płatności");
            alert.show();
        };
    }

    private View.OnClickListener save() {
        return view -> {
            String value = amount.getText().toString().replace(".", ",");
            paymentView.setAmount(value);
            sum.setText(value);
            amount.setText("");
            Runnable update = () -> {
                AppDatabase.getDatabase(getContext()).paymentDAO().updatePaymentValue(value, dayPayments.get(paymentViewCounter).getId());
            };
            AppDatabase.getExecutorService().execute(update);
            Log.i("", "Payment value updated");
            amountRow.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            sumRow.setVisibility(View.VISIBLE);
        };
    }

    private void loadToView(Integer position) {

        if (dayPayments == null) {
            return;
        }else if (dayPayments.size() > position) {
            paymentView = dayPayments.get(position);
        } else {
            return;
        }

        if (isNextPayment(position)) {
            nextPayment.setVisibility(View.VISIBLE);
        }else{
            nextPayment.setVisibility(View.INVISIBLE);
        }

        if(isPreviousPayment(position)){
            previousPayment.setVisibility(View.VISIBLE);
        }else{
            previousPayment.setVisibility(View.INVISIBLE);
        }

        contractor.setText(paymentView.getContractor());
        service.setText(paymentView.getService());
        account.setText(paymentView.getAccount());
        if (paymentView.getAmount() != null) {
            amountRow.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            sumRow.setVisibility(View.VISIBLE);
            buttonPanel.setVisibility(View.VISIBLE);
            String value = paymentView.getAmount() + " zł";
            sum.setText(value);
        } else {
            save.setVisibility(View.VISIBLE);
            amountRow.setVisibility(View.VISIBLE);
            sumRow.setVisibility(View.GONE);
            buttonPanel.setVisibility(View.GONE);
        }
        if (isPaid(paymentView)) {
            buttonPanel.setVisibility(View.GONE);
        }

    }

    private boolean isPaid(PaymentView paymentView) {
        return paymentView.getStatus().equals(PaymentStatus.PAID.getValue());
    }

    private boolean isNextPayment(Integer position) {
        return dayPayments.size() != position + 1;
    }

    private boolean isPreviousPayment(Integer position) {
        return position !=0;
    }
}