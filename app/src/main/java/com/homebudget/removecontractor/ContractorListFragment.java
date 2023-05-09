package com.homebudget.removecontractor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homebudget.R;
import com.homebudget.repository.AppDatabase;
import com.homebudget.repository.ContractorEntity;
import com.homebudget.scheduler.SchedulerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ContractorListFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contractor_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTable();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, returnCallback());
    }

    private OnBackPressedCallback returnCallback() {
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getContext(), SchedulerActivity.class));
            }
        };
    }

    private void initTable(){
        Callable<List<ContractorEntity>> contractor = ()-> AppDatabase.getDatabase(getContext()).contractorDAO().findAllContractors();
        List<ContractorEntity> contractors = new ArrayList<>();
        try {
            contractors = AppDatabase.getExecutorService().submit(contractor).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for(ContractorEntity contractorEntity : contractors){
            String contractorDetail = contractorEntity.getContractorName()+" - "+ contractorEntity.getServiceName();
            long contractorId = contractorEntity.getId();
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_layout,new ContractorItemFragment(contractorDetail,contractorId), null).commit();
        }

    }
}