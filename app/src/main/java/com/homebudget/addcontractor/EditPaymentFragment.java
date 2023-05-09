package com.homebudget.addcontractor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homebudget.R;
import com.homebudget.repository.AppDatabase;
import com.homebudget.scheduler.SchedulerActivity;
import com.homebudget.utils.DateFormatter;

public class EditPaymentFragment extends Fragment {

    public EditPaymentFragment() {
    }

    public EditPaymentFragment(long paymentId) {
        this.paymentId = paymentId;
    }

    private long paymentId;
    private EditText paymentTerm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.edit_button).setOnClickListener(edition());
        paymentTerm = getActivity().findViewById(R.id.payment_term);
    }

    private View.OnClickListener edition() {
        return view -> {
            InputFormFragment fragment = (InputFormFragment) getParentFragmentManager().findFragmentById(R.id.input_form);
            long paymentId = fragment.getPaymentId();
            String date = DateFormatter.formatLocalDateToString(fragment.getLocalDateTerm());
            Runnable update = () -> AppDatabase.getDatabase(getContext()).paymentDAO().updatePaymentTerm(paymentId, date);
            AppDatabase.getExecutorService().execute(update);
            Toast.makeText(getContext(), "Zmieniono dane płatności", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), SchedulerActivity.class));
        };
    }
}