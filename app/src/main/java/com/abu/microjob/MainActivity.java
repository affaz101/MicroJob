package com.abu.microjob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Controller.MyAllFunction;
import com.abu.microjob.Fragments.AccountFragment;
import com.abu.microjob.Fragments.AddJobFragment;
import com.abu.microjob.Fragments.CandidateAuthFragment;
import com.abu.microjob.Fragments.CandidateHomeFragment;
import com.abu.microjob.Fragments.ContractJobListFragment;
import com.abu.microjob.Fragments.HomeFragment;
import com.abu.microjob.Fragments.HourlyJobListFragment;
import com.abu.microjob.Fragments.PartTimeJobListFragment;
import com.abu.microjob.Fragments.RecriterAuthFragment;
import com.abu.microjob.Fragments.YourSelectedJobItemFragment;
import com.abu.microjob.Interface.MyApplyFunctionalityListener;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MyFragmetSendData;
import com.abu.microjob.Interface.MyGotoFragmant;
import com.abu.microjob.Interface.MyJobAppliedResultListener;
import com.abu.microjob.Interface.MySetImageByIDFromFirebase;
import com.abu.microjob.Interface.MySetTitleInMainActivity;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.Model.RecruiterModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyFragmetSendData, MyGotoFragmant, MyFirebaseAuthProvider, MySetImageByIDFromFirebase,
        AddJobFragment.MyOnGotoHomeFragmentListener, MySetTitleInMainActivity, MyApplyFunctionalityListener, MyJobAppliedResultListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    private MyAllFireBaseWork myAllFireBaseWork;
    FirebaseUser mUser;
    MyAllFunction myAllFunction;
    CircleImageView iv_profile;
    TextView tv_main_profile_title, tv_main_profile_name,tv_main_profile_phone;
    Fragment fragment;
    YourSelectedJobItemFragment yourFragment;
    Bundle bundle;
    BottomNavigationView bottom_nav;
    AddJobFragment addJobFragment;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);


        yourFragment = new YourSelectedJobItemFragment();

        myAllFireBaseWork = new MyAllFireBaseWork(MainActivity.this);
        myAllFireBaseWork.MyOnSetImageByIDFromFirebase((MySetImageByIDFromFirebase) MainActivity.this);

        addJobFragment = new AddJobFragment();
        addJobFragment.MySetGotoHomeFragment((AddJobFragment.MyOnGotoHomeFragmentListener) MainActivity.this);


        bottom_nav = (BottomNavigationView)findViewById(R.id.bottom_nav_id);
        iv_profile = (CircleImageView) findViewById(R.id.main_iv_img_id);
        tv_main_profile_title = (TextView) findViewById(R.id.main_iv_title_id);
        tv_main_profile_name = (TextView) findViewById(R.id.main_tv_name_id);
        tv_main_profile_phone = (TextView) findViewById(R.id.main_tv_phone_id);



        mSetProfileData();

        myAllFunction = new MyAllFunction(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Menu menu = bottom_nav.getMenu();

        Intent get_intent = getIntent();
        if (get_intent != null){
            String user_type = get_intent.getStringExtra("user_type");
            if (user_type != null){
                if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
                    menu.findItem(R.id.candidate_nav_home).setVisible(false);
                } else if (user_type.equals(CandidateAuthFragment.USER_TYPE)) {
                    menu.findItem(R.id.nav_home).setVisible(false);
                    menu.findItem(R.id.nav_add).setVisible(false);
                    menu.findItem(R.id.candidate_nav_home).setVisible(true);

                }
            }
        }else{
            //TODO: get_intent is empty, don't remove this else part, its important
        }

//        //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
//        makeReadyFragment(new CandidateHomeFragment());
//        MenuItem selectedItem = menu.getItem(0);//home fragment is 2nd indexed, so item of it should be selected colored
//        selectedItem.setChecked(true);

        if (mAuth !=null){
            String e = mAuth.getCurrentUser().getEmail();
            if ((e.contains("recruiter"))) {
                //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
                makeReadyFragment(new HomeFragment());
                MenuItem selectedItem = menu.getItem(1);
                selectedItem.setChecked(true);
            }else{
                //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
                makeReadyFragment(new CandidateHomeFragment());
                MenuItem selectedItem = menu.getItem(0);//home fragment is 2nd indexed, so item of it should be selected colored
                selectedItem.setChecked(true);
            }
        }


//        //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
//        makeReadyFragment(new HomeFragment());
//        MenuItem selectedItem = menu.getItem(1);//home fragment is 2nd indexed, so item of it should be selected colored
//        selectedItem.setChecked(true);

        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.candidate_nav_home){
                    makeReadyFragment(new CandidateHomeFragment());
                }else if (item.getItemId() == R.id.nav_home){
                    makeReadyFragment(new HomeFragment());
                }else if (item.getItemId() == R.id.nav_add){
                    makeReadyFragment(new AddJobFragment());
                }else if (item.getItemId() == R.id.nav_profile){
                    makeReadyFragment(new AccountFragment());
                }
                return true;
            }
        });
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReadyFragment(new AccountFragment());
                MenuItem selectedItem = menu.getItem(3);// 3 because, 3rd is accountfragment index
                selectedItem.setChecked(true);

            }
        });




    }




    public void mNavigateToSecondFragment(JobModel item){
        YourSelectedJobItemFragment yourSelectedJobItemFragment = new YourSelectedJobItemFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bndl = new Bundle();

        bndl.putString("title", item.getJob_title());
        bndl.putString("payment", item.getJob_payment());
        bndl.putString("loc", item.getJob_loc());
        bndl.putString("type_dur", String.valueOf(mItemDurationTimeAvailabilityAutomation(item)));
        bndl.putString("posted_time", mGetTimeAgo(String.valueOf(item.getWork_order_place_time())));
        bndl.putString("addr", item.getJob_address());
        bndl.putString("job_id", item.getJob_id());
        yourSelectedJobItemFragment.setArguments(bndl);

        fragmentTransaction.replace(R.id.lin_frag_container_id, yourSelectedJobItemFragment);
        fragmentTransaction.addToBackStack(null);//TODO: new added1
        fragmentTransaction.commit();

    };

    public void mNavigateToDetailsFragment(JobModel item, Fragment frag){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bndl = new Bundle();

        bndl.putString("title", item.getJob_title());
        bndl.putString("desc", item.getJob_desc());
        bndl.putString("payment", item.getJob_payment()+" BDT");
        bndl.putString("loc", item.getJob_loc());
        bndl.putString("type", item.getJob_type());
        bndl.putString("type_dur", String.valueOf(mItemDurationTimeAvailabilityAutomation(item)));
        bndl.putString("posted_time", mGetTimeAgo(String.valueOf(item.getWork_order_place_time())));
        bndl.putString("addr", item.getJob_address());
        bndl.putString("job_id", item.getJob_id());
        bndl.putString("job_provider_id", item.getWork_provider_id());
        bndl.putDouble("lat", item.getJob_lat());
        bndl.putDouble("lng", item.getJob_lng());
        frag.setArguments(bndl);

        fragmentTransaction.replace(R.id.lin_frag_container_id, frag);
        fragmentTransaction.addToBackStack(null);//TODO: new added1
        fragmentTransaction.commit();

    };
    public void mNavigateToJobFilteredFragment(Fragment frag, List<JobModel> jobList, List<AppliedJobModel> appliedList, String UserId, Activity activity){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment myFragment = null;
        if (frag instanceof HourlyJobListFragment){
            myFragment = new HourlyJobListFragment(jobList, appliedList, UserId, activity);
        } else if (frag instanceof PartTimeJobListFragment) {
            myFragment = new PartTimeJobListFragment(jobList, appliedList, UserId, activity);
        }else if (frag instanceof ContractJobListFragment) {
            myFragment = new ContractJobListFragment(jobList, appliedList, UserId, activity);
        }

        fragmentTransaction.replace(R.id.lin_frag_container_id, myFragment);
        fragmentTransaction.addToBackStack(null);//TODO: new added1
        fragmentTransaction.commit();

    };
    private String mMakeAddrTextFormated(String jobLoc) {
        if (jobLoc.length() > 12){
            return jobLoc.substring(0, 13)+"...";
        }else {
            return  jobLoc;
        }
    }

    private String mItemDurationTimeAvailabilityAutomation( JobModel item) {
        String s_dur = String.valueOf(item.getJob_duration());
        String tmp = "";
        if (s_dur.equals("null")){
            tmp = item.getJob_type();
//            holder.conditonal_bulet_dot.setVisibility(View.GONE);
        }else {
            tmp = String.valueOf(item.getJob_duration());
            tmp = mGetCorrectFormattedTimeText(tmp);
//            holder.conditonal_bulet_dot.setVisibility(View.VISIBLE);
        }

        return tmp;
    }
    private String mGetCorrectFormattedTimeText(String time_str){

        String a = time_str.substring(6,11);//02:00
        String b = time_str.substring(12, 14);//PM
        String c = time_str.substring(19, 24);//08:45
        String d = time_str.substring(25,27);//PM

        String[] aa = a.split(":");
        String[] bb = c.split(":");
        int m = Integer.parseInt(aa[0]);
        int n = Integer.parseInt(aa[1]);
        int o = Integer.parseInt(bb[0]);
        int p = Integer.parseInt(bb[1]);

        String s = String.valueOf(m);
        s+=String.valueOf(n==0?"":":"+n);
        s+=b+"-";
        s+=String.valueOf(o);
        s+=String.valueOf(p==0?"":":"+p);
        s+=d;

        return s;
//        return time_str.substring(6,11)+time_str.substring(12, 14)+"-"+time_str.substring(19, 24)+time_str.substring(25,27);


    };
    private String mGetTimeAgo(String time){
        long secondsInMilli = 1000;
        int minutesInMinute = 60;
        int hoursInDay = 24;
        int daysInMonth = 30;
        int monthsInYear = 12;
        int secondsInMinute = 60;
        int minutesInHour = 60;
        int daysInYear = 365;


        long posted_time = Long.valueOf(time);

        long now = System.currentTimeMillis();

        long diff = now - posted_time;

        if (diff < secondsInMilli) {
            return "just now";
        }

        int seconds = (int) (diff / secondsInMilli);
        int minutes = seconds / secondsInMinute;
        int hours = minutes / minutesInHour;
        int days = hours / hoursInDay;
        int months = days / daysInMonth;
        int years = days / daysInYear;

        if (seconds < minutesInMinute) {
            return seconds + " seconds ago";
        } else if (minutes < minutesInHour) {
            return minutes + " minutes ago";
        } else if (hours < hoursInDay) {
            return hours + " hours ago";
        } else if (days < daysInMonth) {
            return days + " days ago";
        } else if (months < monthsInYear) {
            return months + " months ago";
        } else {
            return years + " years ago";
        }






    };
    private void mSetProfileData() {
        if (mAuth != null){
            String e = mAuth.getCurrentUser().getEmail();
            if (isRecruiterOrCandidate(e)){
                myAllFireBaseWork.setUser_type(RecriterAuthFragment.USER_TYPE);
                myAllFireBaseWork.mGetImageById(e.substring(0, 10),  MainActivity.this);
            }else{
                myAllFireBaseWork.setUser_type(CandidateAuthFragment.USER_TYPE);
                myAllFireBaseWork.mGetImageById(e.substring(0, 10), MainActivity.this);
            }
        }else {
            Log.d("step3", "mauth null");
        }
    }

    private boolean isRecruiterOrCandidate(String email){
        return (email.contains("recruiter"))?true:false;
    };
    public void mNavAddShowOrNot(boolean isShow){//TODO: on login condition it will vary,
        if (isShow){
            MenuItem item_add = findViewById(R.id.nav_add);
            item_add.setVisible(true);
        }
    }

    private void makeReadyFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lin_frag_container_id, frag);
        //fragmentTransaction.addToBackStack(null);//TODO: new added
        fragmentTransaction.commit();
    }

    private void makeReadyFragment(Fragment frag, int bootm_nav_pos) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lin_frag_container_id, frag);
        fragmentTransaction.addToBackStack(null);//TODO: new added
        fragmentTransaction.commit();

        MenuItem item_add;
        switch(bootm_nav_pos){
            case 1://candidate home
                item_add = findViewById(R.id.candidate_nav_home);
                item_add.setVisible(true);
                break;
            case 2://recruiter home
                item_add = findViewById(R.id.nav_home);
                item_add.setVisible(true);
                break;
            case 3://add job
                item_add = findViewById(R.id.nav_add);
                item_add.setVisible(true);
                break;
            case 4://account
                item_add = findViewById(R.id.nav_profile);
                item_add.setVisible(true);
                break;

        }
    }


    @Override
    public void sendData(Context context, Fragment to_fragment, Bundle data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        to_fragment.setArguments(data);
        fragmentTransaction.replace(R.id.lin_frag_container_id, to_fragment);
        fragmentTransaction.addToBackStack(null);//TODO: new added
        fragmentTransaction.commit();
    }


    @Override
    public void goToFragment(int accFragmentContainerId, Fragment goToFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(accFragmentContainerId, goToFragment);
        fragmentTransaction.addToBackStack(null);//TODO: new added
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() <= 1){


            Menu menu = bottom_nav.getMenu();
            if (mAuth !=null){
                String e = mAuth.getCurrentUser().getEmail();
                if ((e.contains("recruiter"))) {
                    //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
                    makeReadyFragment(new HomeFragment());
                    MenuItem selectedItem = menu.getItem(1);
                    selectedItem.setChecked(true);
                }else{
                    //TODO: Below 3 line is for: by default 3rd item (worker_home, home, add, account:: 0,1,2,3 index) account(3) button should select .
                    makeReadyFragment(new CandidateHomeFragment());
                    MenuItem selectedItem = menu.getItem(0);//home fragment is 2nd indexed, so item of it should be selected colored
                    selectedItem.setChecked(true);
                }
            }


        }else
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack(); // Go back to previous fragment
        } else {

            myAllFunction.mAlertrForMsg("Exit", "Do you want to exit from app?", 4);
//            super.onBackPressed(); // Exit the app if no more fragments in back stack
        }

//        myAllFunction.mAlertrForMsg("Exit", "Do you want to exit from app?", 4);
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    @Override
    public void mSetImage(Object user_obj, Activity activity) {
        if (activity != null){
            if (user_obj instanceof RecruiterModel){
                RecruiterModel user = (RecruiterModel) user_obj;
                tv_main_profile_name.setText(user.getR_name());
                tv_main_profile_phone.setText("0"+user.getR_phone());
                Glide.with(activity)
                        .load(user.getR_img_url())
                        .into(iv_profile);
            }else if (user_obj instanceof CandidateModel){
                CandidateModel user = (CandidateModel) user_obj;
                tv_main_profile_name.setText(user.getC_name());
                tv_main_profile_phone.setText("0"+user.getC_phone());
                Glide.with(activity)
                        .load(user.getC_img_url())
                        .into(iv_profile);
            }
        }else {
            Log.d("step3", "activity null");
        }
    }


    @Override
    public void mGotoHomeFragment() {
        makeReadyFragment(new HomeFragment());
        MenuItem item = bottom_nav.getMenu().getItem(1);
        item.setChecked(true);
    }

    @Override
    public void mSetMainActivityTitle(String title) {
        if (title != null){
            tv_main_profile_title.setText(title);
        }else{
            tv_main_profile_title.setText("");
        }
    }

    AlertDialog dialog;
    private void mNotice(String title,String msg, Drawable img){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


    public void mPerformApply(String[] data, Activity activity) {

        if (mAuth != null ){

            String e = mAuth.getCurrentUser().getEmail();
            String[] apply_data = {e.substring(0,10),data[0], data[1]};
            myAllFireBaseWork.mPerformApplyFunctionality(apply_data, activity);

        }

    }

    public void mPerformRecruit(String[] data, Activity activity) {

        if (mAuth != null ){



        }

    }

    @Override
    public void mNotifyJobApplied(Activity activity) {
        if (activity != null ){
            mNotice("Success", "You Have Applied Successfully!");

        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        //makeReadyFragment(new CandidateHomeFragment());




    }
}