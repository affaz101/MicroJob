package com.abu.microjob.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.Interface.MyFirebaseAuthProvider;
import com.abu.microjob.Interface.MySetTitleInMainActivity;
import com.abu.microjob.MainActivity;
import com.abu.microjob.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AddJobFragment extends Fragment implements View.OnClickListener, MyAllFireBaseWork.MyOnJobAddSuccessListener{


    public static final String USER_TYPE = "RECRUITER";
    private static final String LOCATION_IS_SELECTED_TEXT = "Location is Selected";
    private static final String JOD_DURATION_DEFAULT_TEXT = "From: 00:00 AM\nTo: 00:00 PM";
    private Context context;
    private TextInputEditText add_et_job_title, add_et_job_payment;
    private EditText add_et_job_desc,add_et_job_addr;
    private Spinner add_job_loc_spinner,add_job_type_spinner;
    private LinearLayout time_linear_layout, ll_job_add_progress;
    private FrameLayout fl_add_job_add_btn;
    private TextView tv_time_text;
    private static TextView tv_google_text;
    private ImageView iv_time_piker;
    private CheckBox checkBox;
    private ConstraintLayout map_btn;
    private MyAllFireBaseWork myAllFireBaseWork;
    private static FirebaseAuth firebaseAuth;
    private static Double[] map_selected_latLng = {0.0, 0.0};
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private final String[] loc_spn_options = {"Select","On Site", "Remote"};
    private final String[] type_spn_options = {"Select","Contract", "Hourly", "Part Time"};
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static MyOnGotoHomeFragmentListener gotoHomeFragmentListener;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
        myAllFireBaseWork = new MyAllFireBaseWork(context);//getactivity
        myAllFireBaseWork.MySetOnJobAddSuccess((MyAllFireBaseWork.MyOnJobAddSuccessListener) new AddJobFragment());

        ((MainActivity) getActivity()).mSetMainActivityTitle("Add Job Section");//this line for setting a title for this fragment.

        mGetReadyFirebaseAuth();
    }

    public AddJobFragment() {

    }

    private static View v;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_add_job, container, false);
        init();


        add_job_loc_spinner.setAdapter(mySpinnerListGen(1));
        add_job_type_spinner.setAdapter(mySpinnerListGen(2));
        add_job_type_spinner.setSelection(2);

        add_job_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2){
                    time_linear_layout.setVisibility(View.VISIBLE);
                }else {
                    time_linear_layout.setVisibility(View.GONE);
                }
                //TODO: if spin index 0 is selected: nothing selected, error
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        map_btn.setOnClickListener(this);
        fl_add_job_add_btn.setOnClickListener(this);
        iv_time_piker.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked){
                 checkBox.setTextColor(getActivity().getResources().getColor(R.color.selected));
                 fl_add_job_add_btn.setEnabled(true);
                 fl_add_job_add_btn.setAlpha(1.0f);
             }else {
                 checkBox.setTextColor(getActivity().getResources().getColor(R.color.black));
                 fl_add_job_add_btn.setEnabled(false);
                 fl_add_job_add_btn.setAlpha(0.5f);
             }
            }
        });

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        return  v;
    }

    private void init() {
        add_et_job_title = (TextInputEditText) v.findViewById(R.id.add_et_job_title_id);
        add_et_job_desc = (EditText)v.findViewById(R.id.add_et_job_desc_id);
        add_et_job_payment = (TextInputEditText) v.findViewById(R.id.add_et_job_payment_id);
        add_et_job_addr = (EditText)v.findViewById(R.id.add_et_job_addr_id);
        add_job_loc_spinner = (Spinner) v.findViewById(R.id.add_job_loc_spn_id);
        add_job_type_spinner = (Spinner) v.findViewById(R.id.add_job_type_spn_id);
        time_linear_layout = (LinearLayout)v.findViewById(R.id.add_ll_time_id);
        ll_job_add_progress = (LinearLayout)v.findViewById(R.id.ll_job_add_progress_id);
        fl_add_job_add_btn = (FrameLayout) v.findViewById(R.id.add_job_add_btn_id);
        iv_time_piker = (ImageView) v.findViewById(R.id.add_time_piker_btn_id);
        tv_time_text = (TextView) v.findViewById(R.id.add_tv_time_text_id);
        tv_google_text = (TextView) v.findViewById(R.id.add_tv_google_map_text_id);
        checkBox = (CheckBox) v.findViewById(R.id.add_check_box_id);
        map_btn = (ConstraintLayout) v.findViewById(R.id.add_map_btn_id);
        fl_add_job_add_btn.setEnabled(false);
    }

    public ArrayAdapter<String> mySpinnerListGen(int which_spin){
        /*TODO:
             which_spin:1 => job location spin
             which_spin:2 => job type spin
             which_spin:3 => etc spin*/
        ArrayAdapter<String> dr_adapter = null;
        if (which_spin == 1){

            dr_adapter = new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, loc_spn_options);
        }else if (which_spin == 2){

            dr_adapter = new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, type_spn_options);
        }
        return dr_adapter;
    };

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.add_time_piker_btn_id){
            mGetJobDurationTime();
        } else if (v.getId() == R.id.add_map_btn_id) {
            if (checkLocationPermission()){
                mGetMyMapLatLng();
            }
        } else if (v.getId() == R.id.add_job_add_btn_id) {
            mBeginAddJob();
//            Toast.makeText(getActivity(), "Job Added Kintu...", Toast.LENGTH_SHORT).show();
        }
    }

    private void mBeginAddJob() {
        if (mVerifyIsEverythingOK()){
            mGetAllInputStr();
        }else {
            mNotice("Aware!", "There is a problem, Maybe you missed some input.");
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

    private void mGetAllInputStr() {
        String title = add_et_job_title.getText().toString().trim();
        String desc = add_et_job_desc.getText().toString().trim();
        String payment = add_et_job_payment.getText().toString().trim();
        String loc = loc_spn_options[add_job_loc_spinner.getSelectedItemPosition()];
        String type = type_spn_options[add_job_type_spinner.getSelectedItemPosition()];
        String duration = mGetDurationInput();
        String addr = add_et_job_addr.getText().toString().trim();
        Double lat = map_selected_latLng[0];
        Double lng = map_selected_latLng[1];

        String recruiter_id = mGetUserId();
        long timestamp = System.currentTimeMillis();


        if (isInternetAvailable()){
            mShowProg();
            myAllFireBaseWork.mPassJobData(requireActivity(), title, desc, payment, loc, type, duration, addr, lat, lng, recruiter_id);

        }else{
            mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
        }
    }
    @Override
    public void mJobAdded(FragmentActivity fragmentActivity) {
        mHideProg();
        mGoToHome();
        mNotice("Success!", "Your Job Is Added Successfully!", fragmentActivity );
    }
    private void mGoToHome(){

        if (gotoHomeFragmentListener != null){
            gotoHomeFragmentListener.mGotoHomeFragment();
            Log.d("step1", "    :Main Activity");
        }else {
//            mainActivityListener = (MyOnGotoMainActivityListener) new MyAuth();
//            mainActivityListener.mGotoMainActivity();
            Log.d("step1", "    :Not got Main Activity");
        }
    };
    public interface MyOnGotoHomeFragmentListener{
        void mGotoHomeFragment();
    }
    public void MySetGotoHomeFragment(MyOnGotoHomeFragmentListener listener){

        this.gotoHomeFragmentListener = listener;

    }

    private void mShowProg(){

//        mNotice("hi", " by");
        ll_job_add_progress = (LinearLayout) v.findViewById(R.id.ll_job_add_progress_id);
        if (ll_job_add_progress.getVisibility() == View.GONE){
            ll_job_add_progress.setVisibility(View.VISIBLE);
        }
    }
    private void mHideProg(){

        ll_job_add_progress = (LinearLayout) v.findViewById(R.id.ll_job_add_progress_id);
        if (ll_job_add_progress.getVisibility() == View.VISIBLE){
            ll_job_add_progress.setVisibility(View.GONE);
        }
    }

    private String mGetDurationInput() {
        if (add_job_type_spinner.getSelectedItemPosition() == 2 && !(tv_time_text.getText().equals(JOD_DURATION_DEFAULT_TEXT))  ){
            return tv_time_text.getText().toString().trim();
        }else {
            return "null";
        }
    }
    private String mGetUserId(){
        if (firebaseAuth != null){
            String e = firebaseAuth.getCurrentUser().getEmail();
            return e.substring(0,10);
        }else {
            return "null";
        }
    }
    private static final long secondsInMilli = 1000;
    private static final int minutesInMinute = 60;
    private static final int hoursInDay = 24;
    private static final int daysInMonth = 30; // approximation for simplicity
    private static final int monthsInYear = 12;
    private static final int secondsInMinute = 60;
    private static final int minutesInHour = 60;
    private static final int daysInYear = 365;
    public static String getTimeAgo(long timestamp) {

        long now = System.currentTimeMillis();

        long diff = now - timestamp;

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
    }



    private boolean mVerifyIsEverythingOK() {
        boolean isOk = false;
        isOk = mIsEditTextEmpty(add_et_job_title);
        isOk = mIsEditTextEmpty(add_et_job_desc);
        isOk = mIsEditTextEmpty(add_et_job_payment);
        isOk = mIsEditTextEmpty(add_et_job_addr);

        if (isOk){
            isOk = mCheckIsSpinSelected(add_job_loc_spinner, 1);
            if (isOk){
                isOk = mCheckIsSpinSelected(add_job_type_spinner, 2);
                if (isOk){
                    isOk = mDurationTextValidation(add_job_type_spinner.getSelectedItemPosition());
                    if (isOk){
                        isOk = tv_google_text.getText().equals(LOCATION_IS_SELECTED_TEXT);
                        if (!isOk){
                            mNotice("Error", "Maybe you didn't selected 'Google Map Location' of your provided job.");
                        }

                    }
                }
            }
        }
        return isOk;


    }

    private boolean mDurationTextValidation(int type_spn_pos) {
        boolean b = false;
        if (type_spn_pos == 2  &&  tv_time_text.getText().equals(JOD_DURATION_DEFAULT_TEXT)  &&    (time_linear_layout.getVisibility() == View.VISIBLE)  ){//hourly
            b = false;
            mNotice("Aware", "You Maybe forgot to enter time. Please select Job Duration.");
        }else {
            //All is ok as it we wanted
            b = true;
        };
        Log.d("stpe9", tv_time_text.getText().toString());
        return b;
    }

    private boolean mCheckSpinJobType() {
        boolean isSelect = false;
        if (mCheckIsSpinSelected(add_job_type_spinner, 2)){
            if (add_job_type_spinner.getSelectedItemPosition() == 1){//contract
                isSelect = false;
            }else {
                isSelect = true;
            }
        }else {
            isSelect = false;
        }

        return isSelect;
    }

    private boolean mCheckIsSpinSelected(Spinner spn, int loc_or_type) {
        boolean isSelect = false;
        if (spn.getSelectedItemPosition() == 0){
            isSelect = false;
            String s = loc_or_type==1?"Location":"Type";
            mNotice("Error", "You didn\'t selected Job "+s+". Please, Select it first.");
        }else {
            isSelect = true;
        }

        return isSelect;
    }

    private boolean mIsEditTextEmpty(EditText et){
        if (TextUtils.isEmpty(et.getText().toString())){
            et.setError("Can't be empty");
            return false;
        }else {
            return true;
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


    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mGetMyMapLatLng();
            }else {
                Toast.makeText(getActivity(), "Location Permission Dite Hobe...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mGetMyMapLatLng(){

        MyMapFragmentDialog myMapFragmentDialog = new MyMapFragmentDialog();
        myMapFragmentDialog.setMyOnLocationSelectedListener(new MyMapFragmentDialog.MyOnLocationSelectedListener() {
            @Override
            public void onLocationSelected(double latitude, double longitude) {
                map_selected_latLng[0] = latitude;
                map_selected_latLng[1] = longitude;

                map_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.google_map_btn_bg));
                tv_google_text.setText(LOCATION_IS_SELECTED_TEXT);
//                Toast.makeText(getActivity(), "lat: "+latitude+", long: "+longitude, Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = new Bundle();

        bundle.putDouble("selected_map_lat", map_selected_latLng[0]);
        bundle.putDouble("selected_map_lng", map_selected_latLng[1]);

        myMapFragmentDialog.setArguments(bundle);
        myMapFragmentDialog.show(getChildFragmentManager(), "map");
    }
    public void mGetJobDurationTime(){
        MyCustomTimePikerFragment myCustomTimePikerFragment = new MyCustomTimePikerFragment();
        myCustomTimePikerFragment.setTextInputDialogListener(new MyCustomTimePikerFragment.TextInputDialogListener() {
            @Override
            public void onInputEntered(String input) {
                tv_time_text.setText(input);
            }
        });
//        String timeRange = "From 01:45 AM To 07:45 PM";
        String timeRange = tv_time_text.getText().toString();

        // Split the string using "From" and "To" as delimiters
        String[] parts = timeRange.split("From: |\\nTo: ");
        Log.d("date", parts.length+" :size");
        Log.d("date", parts[0]+" :1");
        Log.d("date", parts[1]+" :2");
        Log.d("date", parts[2]+" :3");
//        Log.d("date", parts[1]+" :second");
        Bundle timeBundle = new Bundle();
        timeBundle.putString("prev_from", parts[1]);
        timeBundle.putString("prev_to", parts[2]);
//        timeBundle.putString("prev_from", parts[0]);
//        timeBundle.putString("prev_to", parts[1]);
        myCustomTimePikerFragment.setArguments(timeBundle);

        myCustomTimePikerFragment.show(getChildFragmentManager(), "bey");
    }

    private void mGetReadyFirebaseAuth(){
        MyFirebaseAuthProvider provider = (MyFirebaseAuthProvider) getActivity();
        if (provider != null){
            this.firebaseAuth = provider.getFirebaseAuth();
        }else {
            Toast.makeText(getActivity(), "FirebaseAuthProvider is null", Toast.LENGTH_SHORT).show();
        }
    };



}