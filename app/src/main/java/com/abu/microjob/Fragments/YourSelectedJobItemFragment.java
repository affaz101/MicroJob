package com.abu.microjob.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.Controller.CandidateAdapter;
import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MyGetAppliedJobCandidateDataListener;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class YourSelectedJobItemFragment extends Fragment implements MyGetAppliedJobCandidateDataListener, CandidateAdapter.MyRecruiterListener {

    public YourSelectedJobItemFragment() {

    }

    private static String title, payment, loc, type_dur, posted_time, addr, JOB_ID;
    private TextView tv_title, tv_payment, tv_loc, tv_dur, tv_post_time, tv_addr;
    private RecyclerView rv_candidate_of_your_job;
    private static View v;

    private static FirebaseAuth firebaseAuth;
    MyAllFireBaseWork myAllFireBaseWork;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        myAllFireBaseWork = new MyAllFireBaseWork(getActivity());
        myAllFireBaseWork.MyOnSetAppliedJobCandidateList((MyGetAppliedJobCandidateDataListener) new YourSelectedJobItemFragment());

        mGetReadyFirebaseAuth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.your_selected_job_item_frament, container, false);
        init();
        Bundle bundle_selected_item = getArguments();
        if (bundle_selected_item != null) {
            title = bundle_selected_item.getString("title");
            payment = bundle_selected_item.getString("payment");
            loc = bundle_selected_item.getString("loc");
            type_dur = bundle_selected_item.getString("type_dur");
            posted_time = bundle_selected_item.getString("posted_time");
            addr = bundle_selected_item.getString("addr");
            JOB_ID = bundle_selected_item.getString("job_id");

            mSetDataIntoSelectedJob(title, payment, loc, type_dur, posted_time, addr);
        }
        if (isInternetAvailable()) {
            myAllFireBaseWork.mGetCandidateData(JOB_ID, requireActivity());

        } else {
            mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
        }


        return v;
    }

    @Override
    public void mGetRecruiterCandidateId(String cId, Activity activity) {

        if (firebaseAuth != null) {
            if (cId != null) {
                if (JOB_ID != null) {
                    if (isInternetAvailable(activity)) {
                        myAllFireBaseWork = new MyAllFireBaseWork(getActivity());
                        myAllFireBaseWork.mRecruiterFunctionalityBegin(cId, JOB_ID,getActivity());
//                        mNotice("Ohho", "all is ok.", activity);
                    }else {
                        mNotice("Ohho", "Maybe Internet is not available. Please check it again.", activity);
                    }
//                    myAllFireBaseWork.mRecruiterFunctionalityBegin(cId, JOB_ID, getActivity());
                } else {
                    mNotice("Oops", "JobId is missing", activity);
                }
            } else {
                mNotice("Oops", "Candidate Id is missing", activity);
            }
        }else{
            mNotice("Oops", "Auth is null", activity);
        }
    }



//    public void mRecruitePrepare(String cId){
////        mNotice("Oops", "clicked");
//        Log.d("step11", "clicked");
//        MyFirebaseAuthProvider provider = (MyFirebaseAuthProvider) getActivity();
//        firebaseAuth = provider.getFirebaseAuth();
//        if (firebaseAuth != null){
//            if (cId != null){
//                if (JOB_ID != null){
////                if (isInternetAvailable()){
////                    //myAllFireBaseWork.mRecruiterFunctionalityBegin(cId, JOB_ID);
////                }else{
////                    mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
////                }
////                    myAllFireBaseWork.mRecruiterFunctionalityBegin(cId, JOB_ID);
//
//                }else{
//                    mNotice("Oops", "JobId is missing");
//                }
//            }else{
//                mNotice("Oops", "Candidate Id is missing");
//            }
//        }else {
//            mNotice("Oops", "Auth is null");
//
//        }
//
//
//    };
    private boolean isInternetAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }






    AlertDialog dialog;

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


    @Override
    public void mGetAppliedJobData(List<CandidateModel> userModel, List<AppliedJobModel> allAppliedJobList, Activity activity) {
        if (activity != null){
//            init();

            rv_candidate_of_your_job = (RecyclerView) v.findViewById(R.id.rv_your_job_offer_rv_id);
            CandidateAdapter candidateAdapter = new CandidateAdapter(userModel, allAppliedJobList, JOB_ID, activity);
            rv_candidate_of_your_job.setAdapter(candidateAdapter);
            rv_candidate_of_your_job.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

    }

    private void mGetReadyFirebaseAuth(){
        MyFirebaseAuthProvider provider = (MyFirebaseAuthProvider) getActivity();
        if (provider != null){
            this.firebaseAuth = provider.getFirebaseAuth();
        }else {
            Toast.makeText(getActivity(), "FirebaseAuthProvider is null", Toast.LENGTH_SHORT).show();
        }
    };
    private void mSetDataIntoSelectedJob(String title, String payment, String loc, String typeDur, String postedTime, String addr) {
        tv_title.setText(title);
        tv_payment.setText(payment+" BDT");
        tv_loc.setText(loc);
        tv_dur.setText(typeDur);
        tv_post_time.setText(posted_time);
        tv_addr.setText(addr);

    }

    private void init() {
        tv_title = (TextView) v.findViewById(R.id.your_job_selected_item_tv_title_id);
        tv_payment = (TextView) v.findViewById(R.id.your_job_selected_item_tv_payment_id);
        tv_loc = (TextView) v.findViewById(R.id.your_job_selected_item_tv_loc_id);
        tv_dur = (TextView) v.findViewById(R.id.your_job_selected_item_tv_time_id);
        tv_post_time = (TextView) v.findViewById(R.id.your_job_selected_item_tv_posted_time_id);
        tv_addr = (TextView) v.findViewById(R.id.your_job_selected_item_tv_addr_id);
        rv_candidate_of_your_job = (RecyclerView) v.findViewById(R.id.rv_your_job_offer_rv_id);


    }



}