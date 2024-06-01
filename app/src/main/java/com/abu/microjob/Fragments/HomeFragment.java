package com.abu.microjob.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Controller.YourJobAdapter;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MyOnYourSelectedJobItemIsSelectedListener;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class HomeFragment extends Fragment implements MyAllFireBaseWork.MyGetYourJobListListener{


    public HomeFragment() {
    }
    private RecyclerView rv_your_offered_job;
    private YourJobAdapter adapter;
    private List<JobModel> jobList;
    private static FirebaseAuth firebaseAuth;
    private MyAllFireBaseWork myAllFireBaseWork;
    AddJobFragment addJobFragment;
    Context context ;
    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);

        ((MainActivity) getActivity()).mSetMainActivityTitle("Your Offered Job");//this line for setting a title for this fragment.
        mGetReadyFirebaseAuth();
        myAllFireBaseWork = new MyAllFireBaseWork(getActivity());
        myAllFireBaseWork.MyOnSetGetYourJobList((MyAllFireBaseWork.MyGetYourJobListListener) new HomeFragment());

    }

    private static View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        rv_your_offered_job = (RecyclerView) v.findViewById(R.id.rv_your_job_offer_rv_id);

        mGetListOfYourJob();



        return v;
    }

    private void mGetListOfYourJob() {
        if (firebaseAuth != null){
            String e = firebaseAuth.getCurrentUser().getEmail();
            myAllFireBaseWork.mGetYourJobList(e.substring(0, 10), requireActivity());
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

    @Override
    public void mMyOnGetYourJobList(List<JobModel> jobModelList,  Activity activity) {
        rv_your_offered_job = (RecyclerView) v.findViewById(R.id.rv_your_job_offer_rv_id);

        if (activity != null){
            adapter = new YourJobAdapter(jobModelList, new YourJobAdapter.MySendJobItemDataListener() {
                @Override
                public void mOnSendJobItemData(JobModel item) {
                    ((MainActivity) activity).mNavigateToSecondFragment(item);
                }
            });

            rv_your_offered_job.setAdapter(adapter);
            rv_your_offered_job.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }




}