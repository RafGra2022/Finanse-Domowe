package com.homebudget.addcontractor;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.homebudget.R;

public class AddContractorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contractor);

        boolean editMode;
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            editMode = bundle.getBoolean("edit");
            long paymentId = bundle.getLong("paymentid");
            if (editMode) {
                findViewById(R.id.add_contractor).setVisibility(View.GONE);
                findViewById(R.id.edit_contractor).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.input_form, new InputFormFragment(paymentId), null).commit();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}