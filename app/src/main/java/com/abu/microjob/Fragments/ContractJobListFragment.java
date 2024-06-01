package com.abu.microjob.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.microjob.Controller.CandidateHomeRecyclerAdapter;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.R;

import java.util.List;

public class ContractJobListFragment extends Fragment {



    public ContractJobListFragment() {

    }
    List<JobModel> allHourlyJobList;
    List<AppliedJobModel> allApplied;
    String userId;
    Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).mSetMainActivityTitle("Contract Jobs");//this line for setting a title for this fragment.
    }

    public ContractJobListFragment(List<JobModel> allHourlyJobList, List<AppliedJobModel> allApplied, String userId, Activity activity) {
        this.allHourlyJobList = allHourlyJobList;
        this.allApplied = allApplied;
        this.userId = userId;
        this.activity = activity;
    }
    RecyclerView rv_contract;
    private  static  View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contract_job_list, container, false);
        rv_contract = (RecyclerView) v.findViewById(R.id.rv_contract_job_fragment_rv_id);

        CandidateHomeRecyclerAdapter adapter = new CandidateHomeRecyclerAdapter(allHourlyJobList, allApplied, userId, getActivity(), new CandidateHomeRecyclerAdapter.MySendJobItemsDataToDetailsViewListener() {
            @Override
            public void mSendItemDataToDisplay(JobModel item) {
                ((MainActivity) activity).mNavigateToDetailsFragment(item, new CurrentJobItemDetails());
            }
        });

        rv_contract.setAdapter(adapter);
        rv_contract.setLayoutManager(new LinearLayoutManager(getActivity()));




        return v;
    }
}