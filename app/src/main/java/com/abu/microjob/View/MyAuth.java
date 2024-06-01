package com.abu.microjob.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.abu.microjob.Controller.AccountViewPagerAdapter;
import com.abu.microjob.Fragments.CandidateAuthFragment;
import com.abu.microjob.Fragments.RecriterAuthFragment;
import com.abu.microjob.MainActivity;
import com.abu.microjob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;

public class MyAuth extends AppCompatActivity implements RecriterAuthFragment.MyOnGotoMainActivityListener, CandidateAuthFragment.MyOnGotoMainActivityListener {
    Bundle bundle_data;
    AccountViewPagerAdapter adapter;

    AppCompatButton btn_rec, btn_can;

    private FirebaseAuth firebaseAuth;
    RecriterAuthFragment recriterAuthFragment = new RecriterAuthFragment();
    CandidateAuthFragment candidateAuthFragment ;
    RecriterAuthFragment recruiter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_auth);

        mCheckIsAlreadySignedInOrNot();

        recruiter = new RecriterAuthFragment();
        recruiter.MySetGotoMainActivity((RecriterAuthFragment.MyOnGotoMainActivityListener) this);

        candidateAuthFragment = new CandidateAuthFragment();
        candidateAuthFragment.MySetGotoMainActivity((CandidateAuthFragment.MyOnGotoMainActivityListener) this);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.myauth_tablayout_id);
        btn_rec = (AppCompatButton) findViewById(R.id.myauth_btn_recruiter_id);
        btn_can = (AppCompatButton) findViewById(R.id.myauth_btn_candidate_id);
        ViewPager2 myauth_vp2 = (ViewPager2) findViewById(R.id.myauth_vp2_id);


        adapter = new AccountViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        myauth_vp2.setAdapter(adapter);

        myauth_vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        btn_rec.setBackgroundColor(getResources().getColor(R.color.selected));
                        btn_rec.setTextColor(getResources().getColor(R.color.white));
                        btn_can.setBackgroundColor(getResources().getColor(R.color.unselected));
                        btn_can.setTextColor(getResources().getColor(R.color.black));

                        break;
                    case 1:
                        btn_can.setBackgroundColor(getResources().getColor(R.color.selected));
                        btn_can.setTextColor(getResources().getColor(R.color.white));
                        btn_rec.setBackgroundColor(getResources().getColor(R.color.unselected));
                        btn_rec.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
            }
        });

        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myauth_vp2.setCurrentItem(0);

                btn_rec.setBackgroundColor(getResources().getColor(R.color.selected));
                btn_rec.setTextColor(getResources().getColor(R.color.white));
                btn_can.setBackgroundColor(getResources().getColor(R.color.unselected));
                btn_can.setTextColor(getResources().getColor(R.color.black));

            }
        });
        btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myauth_vp2.setCurrentItem(1);

                btn_can.setBackgroundColor(getResources().getColor(R.color.selected));
                btn_can.setTextColor(getResources().getColor(R.color.white));
                btn_rec.setBackgroundColor(getResources().getColor(R.color.unselected));
                btn_rec.setTextColor(getResources().getColor(R.color.black));

            }
        });


        
    }

    private void mCheckIsAlreadySignedInOrNot() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = firebaseAuth.getCurrentUser();
        if (current_user != null){
            String em = current_user.getEmail();
            if (isRecruiterOrCandidate(em)){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_type", RecriterAuthFragment.USER_TYPE);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_type", CandidateAuthFragment.USER_TYPE);
                startActivity(intent);
                finish();
            }

        }
    }
    private boolean isRecruiterOrCandidate(String email){
        return (email.contains("recruiter"))?true:false;
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode  == RESULT_OK && requestCode == RecriterAuthFragment.UCROPPED_CODE) {
            final Uri resultUri = UCrop.getOutput(data);
            if (recriterAuthFragment != null){
                recriterAuthFragment.mSetCroppedResult(resultUri);
                Log.d("dekh", resultUri.toString()+"      :cropped img is passed");
            }else{
                Log.d("dekh", "cropped listener is null");
            }
        }else if (resultCode  == RESULT_OK && requestCode == CandidateAuthFragment.UCROPPED_CODE) {
            final Uri resultUri = UCrop.getOutput(data);
            if (candidateAuthFragment != null){
                candidateAuthFragment.mSetCroppedResult(resultUri);
                Log.d("dekh", resultUri.toString()+"      :cropped img is passed");
            }else{
                Log.d("dekh", "cropped listener is null");
            }
        }
    }

    @Override
    public void mGotoMainActivity(String userType) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("user_type", userType);
        startActivity(intent);


    }
}