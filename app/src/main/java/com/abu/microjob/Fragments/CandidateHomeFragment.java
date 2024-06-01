package com.abu.microjob.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.Controller.CandidateHomeRecyclerAdapter;
import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MyGetAllCurrentJobModelListener;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CandidateHomeFragment extends Fragment implements View.OnClickListener, MyGetAllCurrentJobModelListener{



    public CandidateHomeFragment() {

    }
    private LinearLayout ll_part_time_id, ll_candidate_home_progress;
    private ConstraintLayout cl_hourly_btn_id, cl_contract_btn_id;
    private RecyclerView rvCandidateHomeCurrentJob;
    private TextView tv_partTimeCount, tv_HourlyCount, tv_ContractCount;
    private FirebaseAuth firebaseAuth;
    private MyAllFireBaseWork myAllFireBaseWork;
    private static List<JobModel> myAllCurrentJobList, myAllPartTimeJobList, myAllContractJobList,myAllHourlyJobList;
    private static List<AppliedJobModel> AllAppliedJobList;
    private static String USER_ID;
    private static Activity IMPORTANT_ACTIVITI;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        myAllFireBaseWork = new MyAllFireBaseWork(getActivity());
        myAllFireBaseWork.MySetOnGetAllCurrentData((MyGetAllCurrentJobModelListener) new CandidateHomeFragment());
        mGetReadyFirebaseAuth();
        ((MainActivity) getActivity()).mSetMainActivityTitle("Micro Jobs");//this line for setting a title for this fragment.

    }

    private static View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_candidate_home, container, false);

        init();

        mMakeReadyAllMicroJobs();

        ll_part_time_id.setOnClickListener(this);
        cl_hourly_btn_id.setOnClickListener(this);
        cl_contract_btn_id.setOnClickListener(this);

        return v;
    }

    private void mMakeReadyAllMicroJobs() {
        if (firebaseAuth != null){
            String e = firebaseAuth.getCurrentUser().getEmail();
            myAllFireBaseWork.mGetBackAllMicroJobs(requireActivity(), e.substring(0, 10));
        }
    }

    private void init() {
        tv_partTimeCount = (TextView) v.findViewById(R.id.tv_can_home_partTime_tv_id);
        tv_ContractCount = (TextView) v.findViewById(R.id.tv_can_home_contract_tv_id);
        tv_HourlyCount = (TextView) v.findViewById(R.id.tv_can_home_hourly_tv_id);
        rvCandidateHomeCurrentJob = (RecyclerView) v.findViewById(R.id.rv_candidate_home_recyclerview_id);
        ll_part_time_id = (LinearLayout) v.findViewById(R.id.ll_candidate_home_part_time_id);
        ll_candidate_home_progress = (LinearLayout) v.findViewById(R.id.ll_candidate_home_progress_id);
        cl_hourly_btn_id = (ConstraintLayout) v.findViewById(R.id.cl_candidate_home_hourly_id);
        cl_contract_btn_id = (ConstraintLayout) v.findViewById(R.id.cl_candidate_home_contract_id);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_candidate_home_part_time_id){
//            mNotice("Look", "you clicked on part time");
            mPartTimeJobFragment();
        } else if (v.getId() == R.id.cl_candidate_home_hourly_id) {
            mHorulyJobFragment();
//            mNotice("Look", "you clicked on hourly");
        } else if (v.getId() == R.id.cl_candidate_home_contract_id) {
            mContractJobFragment();
//            mNotice("Look", "you clicked on contract");
        }
    }

    private void mHorulyJobFragment() {
        if (myAllHourlyJobList != null){
            if (myAllHourlyJobList.size()>0){
                ((MainActivity) getActivity()).mNavigateToJobFilteredFragment(new HourlyJobListFragment(),myAllHourlyJobList, AllAppliedJobList, USER_ID, IMPORTANT_ACTIVITI);
            }
        }
    }
    private void mPartTimeJobFragment() {
        if (myAllPartTimeJobList != null){
            if (myAllPartTimeJobList.size()>0){
                ((MainActivity) getActivity()).mNavigateToJobFilteredFragment(new PartTimeJobListFragment(),myAllPartTimeJobList, AllAppliedJobList, USER_ID, IMPORTANT_ACTIVITI);
            }
        }
    }
    private void mContractJobFragment() {
        if (myAllContractJobList != null){
            if (myAllContractJobList.size()>0){
                ((MainActivity) getActivity()).mNavigateToJobFilteredFragment(new ContractJobListFragment(),myAllContractJobList, AllAppliedJobList, USER_ID, IMPORTANT_ACTIVITI);
            }
        }
    }

    @Override
    public void mGetAllCurrentJob(List<JobModel> allCurrentJob, List<AppliedJobModel> allAppliedJob, String userID, Activity activity) {
        if (activity != null){
            init();
            myAllCurrentJobList = sortByTimeAssending(allCurrentJob);

//            Log.d("step10",allAppliedJob.size()+",   "+allAppliedJob.get(0).getJob_id()+"   :aabbcc");

//            CandidateHomeRecyclerAdapter adapter = new CandidateHomeRecyclerAdapter(myAllCurrentJobList, activity, new CandidateHomeRecyclerAdapter.MySendJobItemsDataToDetailsViewListener() {
            CandidateHomeRecyclerAdapter adapter = new CandidateHomeRecyclerAdapter(myAllCurrentJobList, allAppliedJob,userID, activity, new CandidateHomeRecyclerAdapter.MySendJobItemsDataToDetailsViewListener() {
                @Override
                public void mSendItemDataToDisplay(JobModel item) {
                    ((MainActivity) activity).mNavigateToDetailsFragment(item, new CurrentJobItemDetails());
                }
            });

            rvCandidateHomeCurrentJob.setAdapter(adapter);
            rvCandidateHomeCurrentJob.setLayoutManager(new LinearLayoutManager(getActivity()));

            List<JobModel>[] allThreeTypeJobs= mGetThereTypeOfList(myAllCurrentJobList);
            myAllPartTimeJobList = allThreeTypeJobs[0];//Part Time
            myAllHourlyJobList = allThreeTypeJobs[1];//Hourly
            myAllContractJobList = allThreeTypeJobs[2];//Contract
            AllAppliedJobList = allAppliedJob;
            USER_ID = userID;
            IMPORTANT_ACTIVITI = activity;





            tv_partTimeCount.setText(myAllPartTimeJobList.size()+" Job Found");
            tv_ContractCount.setText(myAllContractJobList.size()+" Job Found");
            tv_HourlyCount.setText(myAllHourlyJobList.size()+" Job Found");

        }


    }

    private static List<JobModel> sortByTimeAssending(List<JobModel> job_list){

        Collections.sort(job_list, new Comparator<JobModel>() {
            @Override
            public int compare(JobModel o1, JobModel o2) {
                long time1 = o1.getWork_order_place_time();
                long time2 = o2.getWork_order_place_time();

                return Long.compare(time2, time1);
            }
        });
        return job_list;
    };
    private static List<JobModel>[] mGetThereTypeOfList(List<JobModel> job_list){
        List<JobModel>[] mThreeList = new List[3];//TODO: IMPORTANT: 0 is for part time, 1 for hourly, 2 for contract. thats it
        mThreeList[0] = new ArrayList<>();//Part Time
        mThreeList[1] = new ArrayList<>();//Hourly
        mThreeList[2] = new ArrayList<>();//Contract
        for (JobModel item: job_list){
            switch (item.getJob_type()){
                case "Part Time":
                    mThreeList[0].add(item);
                    break;
                case "Hourly":
                    mThreeList[1].add(item);
                    break;
                case "Contract":
                    mThreeList[2].add(item);
                    break;

            }

//            if (item.getJob_type().equals("Part Time")){
//                mThreeList[0].add(item);
//            }
        }
        return mThreeList;

    };


    private void mGetReadyFirebaseAuth(){
        MyFirebaseAuthProvider provider = (MyFirebaseAuthProvider) getActivity();
        if (provider != null){
            this.firebaseAuth = provider.getFirebaseAuth();
        }else {
            Toast.makeText(getActivity(), "FirebaseAuthProvider is null", Toast.LENGTH_SHORT).show();
        }
    };

    private void mShowProg(){

//        mNotice("hi", " by");
        ll_candidate_home_progress = (LinearLayout) v.findViewById(R.id.ll_candidate_home_progress_id);
        if (ll_candidate_home_progress.getVisibility() == View.GONE){
            ll_candidate_home_progress.setVisibility(View.VISIBLE);
        }
    }
    private void mHideProg(){

        ll_candidate_home_progress = (LinearLayout) v.findViewById(R.id.ll_candidate_home_progress_id);
        if (ll_candidate_home_progress.getVisibility() == View.VISIBLE){
            ll_candidate_home_progress.setVisibility(View.GONE);
        }
    }


    AlertDialog dialog;
    private void mNotice(String title,String msg, Drawable img){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setIcon(img);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
    private void mNotice(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
    private void mNotice(String title,String msg, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }



}