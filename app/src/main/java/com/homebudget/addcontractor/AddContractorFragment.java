package com.homebudget.addcontractor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.domain.ContractorDetail;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.ContractorEntity;
import com.homebudget.repository.ContractorMapper;
import com.homebudget.scheduler.SchedulerActivity;
import com.homebudget.service.SchedulerService;

import java.time.LocalDate;


public class AddContractorFragment extends Fragment {

    private AutoCompleteTextView paymentCycle;
    private Button addButton;
    private DatePicker datePicker;
    private EditText contractor;
    private EditText service;
    private EditText account;
    private EditText paymentTerm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contractor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButton = getView().findViewById(R.id.add_button);
        paymentCycle = getActivity().findViewById(R.id.payment_cycle);

        //calendar_spinner initialization
        datePicker = getActivity().findViewById(R.id.calendar_spinner);
        contractor = getActivity().findViewById(R.id.contractor_name);
        service = getActivity().findViewById(R.id.service_name);
        account = getActivity().findViewById(R.id.account_number);
        paymentTerm = getActivity().findViewById(R.id.payment_term);

        //listeners
        addButton.setOnClickListener(addAction());
    }

    private View.OnClickListener addAction() {
        return view -> {
            if (!isDataFilled()) {
                Toast toast = Toast.makeText(getContext(), "Wypełnij formularz", Toast.LENGTH_LONG);
                toast.show();
            } else {
                addContractor();
            }
        };
    }

    private void addContractor() {
        String contractor = this.contractor.getText().toString();
        String service = this.service.getText().toString();
        String account = this.account.getText().toString();
        Integer cycle = Integer.valueOf(paymentCycle.getText().toString());
        LocalDate paymentTerm = getDate(datePicker);
        ContractorEntity contractorEntity = ContractorMapper.mapToContractorEntity(new ContractorDetail(contractor, service, account, cycle, paymentTerm));

        Runnable write = () -> {
            AppDatabase.getDatabase(getContext()).contractorDAO().insert(contractorEntity);
        };
        AppDatabase.getExecutorService().execute(write);
        getActivity().startService(new Intent(getActivity(), SchedulerService.class));
        Toast.makeText(getContext(), "Kontraktor zapisany", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), SchedulerActivity.class));
    }

    private LocalDate getDate(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return LocalDate.of(year, month, day);
    }

    private boolean isDataFilled() {
        String contractor = this.contractor.getText().toString().trim();
        String service = this.service.getText().toString().trim();
        String account = this.account.getText().toString().trim();
        String cycle = this.paymentCycle.getText().toString().trim();
        String paymentTerm = this.paymentTerm.getText().toString().trim();
        return !contractor.isEmpty() && !service.isEmpty() && !account.isEmpty() && !cycle.isEmpty() && !paymentTerm.isEmpty();
    }
}