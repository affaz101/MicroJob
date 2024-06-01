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


public class PartTimeJobListFragment extends Fragment {



    public PartTimeJobListFragment() {

    }

    List<JobModel> allHourlyJobList;
    List<AppliedJobModel> allApplied;
    String userId;
    Activity activity;




    public PartTimeJobListFragment(List<JobModel> allHourlyJobList, List<AppliedJobModel> allApplied, String userId, Activity activity) {
        this.allHourlyJobList = allHourlyJobList;
        this.allApplied = allApplied;
        this.userId = userId;
        this.activity = activity;
    }
    RecyclerView rv_partTime;
    private  static  View v;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).mSetMainActivityTitle("Part Time Jobs");//this line for setting a title for this fragment.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_part_time_job_list, container, false);

        rv_partTime = (RecyclerView) v.findViewById(R.id.rv_part_time_job_fragment_rv_id);

        CandidateHomeRecyclerAdapter adapter = new CandidateHomeRecyclerAdapter(allHourlyJobList, allApplied, userId, getActivity(), new CandidateHomeRecyclerAdapter.MySendJobItemsDataToDetailsViewListener() {
            @Override
            public void mSendItemDataToDisplay(JobModel item) {
                ((MainActivity) activity).mNavigateToDetailsFragment(item, new CurrentJobItemDetails());
            }
        });

        rv_partTime.setAdapter(adapter);
        rv_partTime.setLayoutManager(new LinearLayoutManager(getActivity()));




        return v;
    }
}