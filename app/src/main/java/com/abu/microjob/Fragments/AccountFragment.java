package com.abu.microjob.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.abu.microjob.Controller.AccountViewPagerAdapter;
import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MyFragmetSendData;
import com.abu.microjob.Interface.MyGotoFragmant;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.Model.RecruiterModel;
import com.abu.microjob.R;
import com.abu.microjob.View.MyAuth;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

//public class AccountFragment extends Fragment implements MyAllFireBaseWork.MyGetUserAccountDataListener {
public class AccountFragment extends Fragment implements MyAllFireBaseWork.MyGetUserDataAsUserModelClassListener {


    public AccountFragment() {

    }
    //TODO: This page will contain 3 fragment page, sign in, signup, main account

    Context context;
    private static FirebaseAuth firebaseAuth;
    private MyAllFireBaseWork myAllFireBaseWork;
    private de.hdodenhof.circleimageview.CircleImageView acc_iv_profile_img;
    private TextView acc_tv_type, acc_tv_id, acc_tv_name, acc_tv_gender, acc_tv_phone, acc_tv_dob;
    private FrameLayout acc_btn_signOut;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        ((MainActivity) getActivity()).mSetMainActivityTitle("Account Details");//this line for setting a title for this fragment.
        mGetReadyFirebaseAuth();
        myAllFireBaseWork = new MyAllFireBaseWork(getActivity());
        myAllFireBaseWork.MySetUserAccountDataListener((MyAllFireBaseWork.MyGetUserDataAsUserModelClassListener) new AccountFragment());

    }

    private static View v;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_account, container, false);
        init();

        mGetAccountDataAndAssignedIntoField();

        acc_btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignedOut();
                ((MainActivity) getActivity()).finish();
                Intent intent = new Intent(getActivity(), MyAuth.class);
                startActivity(intent);
            }
        });


        return v;
    }

    private void init() {
        acc_iv_profile_img = (CircleImageView) v.findViewById(R.id.account_profile_iv_img_id);
        acc_tv_type = (TextView) v.findViewById(R.id.account_profile_tv_user_type_id);
        acc_tv_id = (TextView) v.findViewById(R.id.account_profile_tv_user_id_id);
        acc_tv_name = (TextView) v.findViewById(R.id.account_profile_tv_user_name_id);
        acc_tv_gender = (TextView) v.findViewById(R.id.account_profile_tv_user_gender_id);
        acc_tv_phone = (TextView) v.findViewById(R.id.account_profile_tv_user_phone_id);
        acc_tv_dob = (TextView) v.findViewById(R.id.account_profile_tv_user_dob_id);
        acc_btn_signOut = (FrameLayout) v.findViewById(R.id.account_profile_fl_user_sign_out_id);

    }

    private void mGetReadyFirebaseAuth(){
        MyFirebaseAuthProvider provider = (MyFirebaseAuthProvider) getActivity();
        if (provider != null){
            this.firebaseAuth = provider.getFirebaseAuth();
        }else {
            Toast.makeText(getActivity(), "FirebaseAuthProvider is null", Toast.LENGTH_SHORT).show();
        }
    };
    private void mGetAccountDataAndAssignedIntoField() {
        if (firebaseAuth != null){
            if (firebaseAuth.getCurrentUser() != null){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String email =user.getEmail();
                String phn = email.substring(0,10);//only phone number part get from mail

                String user_type_indicator = email.substring(11, email.indexOf('.'));
                if (user_type_indicator.equals("recruiter")){
                    myAllFireBaseWork.setUser_type(RecriterAuthFragment.USER_TYPE);
                } else if (user_type_indicator.equals("candidate")) {
                    myAllFireBaseWork.setUser_type(CandidateAuthFragment.USER_TYPE);
                }
                if (isInternetAvailable()){
                    myAllFireBaseWork.mRetrieveUserData(phn, requireActivity());
                }else{
                    mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
                }



//                Log.d("step2", email+"\t\t:"+email.substring(0,10));

            }

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
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    private void mSignedOut(){
        if (firebaseAuth != null){
            if (firebaseAuth.getCurrentUser() != null){
                firebaseAuth.signOut();
            }
        }
    }

    private static String profile_url ;



//    @Override
//    public void mGetUserAccountData(RecruiterModel user_model, Activity activity) {
//
//
//        context = new MainActivity();
//        init();
//        acc_tv_type.setText("Recruiter");
//        acc_tv_id.setText(user_model.getR_id());
//        acc_tv_name.setText(user_model.getR_name());
//        acc_tv_gender.setText(mGetGenderText(user_model.getR_gender()));
//        acc_tv_phone.setText("0"+user_model.getR_phone());
//        acc_tv_dob.setText(mGetCurrentAge(user_model.getR_bod_day(), user_model.getR_bod_month(), user_model.getR_bod_year())+" years old.");
//
//        profile_url = user_model.getR_img_url();
//        Log.d("step3", profile_url+"   :st ");
//        if (activity != null){
//            Glide.with(activity)
//                        .load(user_model.getR_img_url())
//                        .into(acc_iv_profile_img);
//        }
//
//    }
//



    public String mGetGenderText(String g){
        if (g.equals("m")){
            return "Male";
        }else {
            return "Female";
        }
    };
    public String mGetCurrentAge(String d, String m, String y){
        int bod_day = Integer.valueOf(d);
        int bod_month = Integer.valueOf(m);
        int bod_year = Integer.valueOf(y);

        Calendar today = Calendar.getInstance();
        int current_year = today.get(Calendar.YEAR);
        int current_month = today.get(Calendar.MONTH)+1;
        int current_day = today.get(Calendar.DAY_OF_MONTH);

        int age = current_year - bod_year;
        if (current_month<bod_month || (current_month == bod_month && current_day<bod_day )){
            age--;
        }
        return String.valueOf(age);
    };

    @Override
    public void mGetUserAccountData(Object user_model, Activity activity) {
        context = new MainActivity();
        init();

        if (user_model instanceof  RecruiterModel){
            RecruiterModel user = (RecruiterModel) user_model;
            acc_tv_type.setText("Recruiter");
            acc_tv_id.setText(user.getR_id());
            acc_tv_name.setText(user.getR_name());
            acc_tv_gender.setText(mGetGenderText(user.getR_gender()));
            acc_tv_phone.setText("0"+user.getR_phone());
            acc_tv_dob.setText(mGetCurrentAge(user.getR_bod_day(), user.getR_bod_month(), user.getR_bod_year())+" years old.");

            profile_url = user.getR_img_url();
            Log.d("step3", profile_url+"   :st ");
            if (activity != null){
                Glide.with(activity)
                        .load(user.getR_img_url())
                        .into(acc_iv_profile_img);
            }
        } else if (user_model instanceof CandidateModel) {
            CandidateModel user = (CandidateModel) user_model;
            acc_tv_type.setText("Candidate");
            acc_tv_id.setText(user.getC_id());
            acc_tv_name.setText(user.getC_name());
            acc_tv_gender.setText(mGetGenderText(user.getC_gender()));
            acc_tv_phone.setText("0"+user.getC_phone());
            acc_tv_dob.setText(mGetCurrentAge(user.getC_bod_day(), user.getC_bod_month(), user.getC_bod_year())+" years old.");

            profile_url = user.getC_img_url();
            Log.d("step3", profile_url+"   :st ");
            if (activity != null){
                Glide.with(activity)
                        .load(user.getC_img_url())
                        .into(acc_iv_profile_img);
            }
        }



    }



//    public interface MyFirebaseAuthProvider{
//        FirebaseAuth getFirebaseAuth();
//    }
}