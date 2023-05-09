package com.homebudget.addcontractor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.PaymentView;
import com.homebudget.utils.DateFormatter;
import com.homebudget.utils.IBANFormatter;
import com.homebudget.utils.SoftKeyboard;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class InputFormFragment extends Fragment {

    public InputFormFragment() {
    }

    public InputFormFragment(long paymentId) {
        this.paymentId = paymentId;
    }

    private static final Integer[] periods = new Integer[]{1, 2, 3, 6, 12};
    private AutoCompleteTextView paymentCycle;
    private Button confirmButton;
    private DatePicker datePicker;
    private EditText contractor;
    private EditText service;
    private EditText account;
    private EditText paymentTerm;
    private TextView title;
    private Locale locale;
    private boolean isFormatterUsed = false;
    private LocalDate localDateTerm;

    public Long getPaymentId() {
        return paymentId;
    }

    public LocalDate getLocalDateTerm() {
        return localDateTerm;
    }

    private Long paymentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        locale = new Locale("pl");
        Locale.setDefault(locale);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paymentCycle = getView().findViewById(R.id.payment_cycle);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, periods);
        paymentCycle.setAdapter(adapter);

        //calendar_spinner initialization
        confirmButton = getView().findViewById(R.id.calendar_button);
        datePicker = getView().findViewById(R.id.calendar_spinner);
        contractor = getView().findViewById(R.id.contractor_name);
        service = getView().findViewById(R.id.service_name);
        account = getView().findViewById(R.id.account_number);
        paymentTerm = getView().findViewById(R.id.payment_term);
        title = getView().findViewById(R.id.form_title);
        title.setText(getString(R.string.basic_input_form));

        if (paymentId != null) {
            title.setText(getString(R.string.edit_input_form));
            getView().findViewById(R.id.payment_cycle_row).setVisibility(View.GONE);
            contractor.setKeyListener(null);
            service.setKeyListener(null);
            account.setKeyListener(null);
            editAction();
        }

        //listeners
        confirmButton.setOnClickListener(confirmDate());
        paymentTerm.setOnClickListener(calendarAction());
        paymentCycle.setOnClickListener(getDropDownList());
        account.addTextChangedListener(accountNumberFormatter());
    }

    private void editAction() {
        Callable<PaymentView> payment = () -> AppDatabase.getDatabase(getContext()).paymentDAO().findPaymentViewById(paymentId);
        try {
            PaymentView paymentView = AppDatabase.getExecutorService().submit(payment).get();
            contractor.setText(paymentView.getContractor());
            service.setText(paymentView.getService());
            account.setText(paymentView.getAccount());
            paymentTerm.setText(paymentView.getTerm());
            localDateTerm = DateFormatter.formatStringToLocalDate(paymentView.getTerm());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TextWatcher accountNumberFormatter() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("", charSequence.toString());
                if (isFormatterUsed) {
                    isFormatterUsed = false;
                    return;
                }
                isFormatterUsed = true;
                account.setText(IBANFormatter.textFormatter(charSequence.toString()));
                account.setSelection(account.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    private View.OnClickListener getDropDownList() {
        return view -> {
            paymentCycle.showDropDown();
            SoftKeyboard.hideSoftKeyboard(getActivity(), view);
        };
    }

    private View.OnClickListener confirmDate() {
        return view -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
            paymentTerm.setText(df.format(date));
            datePicker.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
            if (paymentId != null) {
                getActivity().findViewById(R.id.edit_contractor).setVisibility(View.VISIBLE);
            } else {
                getActivity().findViewById(R.id.add_contractor).setVisibility(View.VISIBLE);
            }
            localDateTerm = LocalDate.of(year, month + 1, day);
        };
    }

    private View.OnClickListener calendarAction() {
        return view -> {
            datePicker.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.add_contractor).setVisibility(View.GONE);
            getActivity().findViewById(R.id.edit_contractor).setVisibility(View.GONE);
            SoftKeyboard.hideSoftKeyboard(getActivity(), view);

        };
    }

}