package com.homebudget.removecontractor;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.homebudget.R;
import com.homebudget.repository.AppDatabase;

public class ContractorItemFragment extends Fragment {

    public ContractorItemFragment() {
    }

    public ContractorItemFragment(String title, long contractorId) {
        this.title = title;
        this.contractorId = contractorId;
    }

    private String title;
    private long contractorId;
    private ImageButton removeContractorBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contractor_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = getView().findViewById(R.id.view_title);
        removeContractorBtn = getView().findViewById(R.id.remove_contractor_button);
        removeContractorBtn.setOnClickListener(removeContractor());
        text.setText(title);
    }

    private View.OnClickListener removeContractor() {
        return view -> {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getContext());

            builder.setMessage("Usunąć kontraktora?")
                    .setCancelable(false)
                    .setPositiveButton("Tak", (dialog, id) -> {
                        Runnable delete = () -> {
                            AppDatabase db = AppDatabase.getDatabase(getContext());
                            db.contractorDAO().deleteContractorById(contractorId);
                            db.paymentDAO().deletePaymentByContractorId(contractorId);
                        };
                        AppDatabase.getExecutorService().execute(delete);
                        Toast.makeText(getContext(), "Usunięto", Toast.LENGTH_LONG).show();
                        getParentFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.remove_contractor_container,ContractorListFragment.class, null).commit();
                    })
                    .setNegativeButton("Nie", (dialog, id) -> {
                        //  Action for 'No' Button
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Usuń");
            alert.show();

        };
    }
}