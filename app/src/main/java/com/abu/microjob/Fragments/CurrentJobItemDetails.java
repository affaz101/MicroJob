package com.abu.microjob.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.MainActivity;
import com.abu.microjob.R;
import com.google.android.gms.maps.model.LatLng;

public class CurrentJobItemDetails extends Fragment implements View.OnClickListener{


    public CurrentJobItemDetails() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).mSetMainActivityTitle("Job Details");
    }

    private  static  String title, desc, payment, loc,type, type_dur, posted_time, addr, JOB_ID, JOB_PROVIDER_ID;
    private TextView tv_job_dtls_time_ago, tv_job_dtls_title, tv_job_dtls_top_addr, tv_job_dtls_top_payment, tv_job_dtls_loc, tv_job_dtls_type, tv_job_dtls_desc, tv_job_dtls_bottom_payment, tv_job_dtls_duration, tv_job_dtls_bottom_addr ;
    private LinearLayout ll_apply_btn;
    private ConstraintLayout cl_google_map_btn;
    private static Double[] map_selected_latLng = {0.0, 0.0};
    private LatLng jobLatLng;
//    private MyApplyFunctionalityListener applyFunctionalityListener;


    private static View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_current_job_item_details, container, false);
        init();
        Bundle bundle = getArguments();
        if (bundle != null){
            title = bundle.getString("title");
            desc = bundle.getString("desc");
            payment = bundle.getString("payment");
            loc = bundle.getString("loc");
            type = bundle.getString("type");
            type_dur = bundle.getString("type_dur");
            posted_time = bundle.getString("posted_time");
            addr = bundle.getString("addr");
            JOB_PROVIDER_ID = bundle.getString("job_provider_id");
            JOB_ID = bundle.getString("job_id");
            Double lat = bundle.getDouble("lat");
            Double lng = bundle.getDouble("lng");
            jobLatLng = new LatLng(lat, lng);

            mSetDataToDetails(title, payment, loc, type_dur, posted_time, addr);
        }


        ll_apply_btn.setOnClickListener(this);
        cl_google_map_btn.setOnClickListener(this);
        return v;
    }

    private void init() {
        tv_job_dtls_time_ago = (TextView) v.findViewById(R.id.job_dtls_time_ago_tv_id);
        tv_job_dtls_title = (TextView) v.findViewById(R.id.job_dtls_title_tv_id);
        tv_job_dtls_top_addr = (TextView) v.findViewById(R.id.job_dtls_addr_tv_id);
        tv_job_dtls_top_payment = (TextView) v.findViewById(R.id.job_dtls_top_payment_tv_id);
        tv_job_dtls_loc = (TextView) v.findViewById(R.id.job_dtls_loc_tv_id);
        tv_job_dtls_type = (TextView) v.findViewById(R.id.job_dtls_top_type_tv_id);
        tv_job_dtls_desc = (TextView) v.findViewById(R.id.job_dtls_top_desc_tv_id);
        tv_job_dtls_bottom_payment = (TextView) v.findViewById(R.id.job_dtls_bottom_payment_tv_id);
        tv_job_dtls_duration = (TextView) v.findViewById(R.id.job_dtls_duration_tv_id);
        tv_job_dtls_bottom_addr = (TextView) v.findViewById(R.id.job_dtls_bottom_addr_tv_id);

        ll_apply_btn = (LinearLayout) v.findViewById(R.id.job_dtls_apply_btn_ll_id);
        cl_google_map_btn = (ConstraintLayout) v.findViewById(R.id.job_dtls_google_map_cl_id);


    }

    private void mSetDataToDetails(String title, String payment, String loc, String typeDur, String postedTime, String addr) {
        tv_job_dtls_time_ago.setText(postedTime);
        tv_job_dtls_title.setText(title);
        tv_job_dtls_top_addr.setText((addr.length()>15?addr.substring(0,13)+"...":addr));
        tv_job_dtls_top_payment.setText(payment);
        tv_job_dtls_loc.setText(loc);
        tv_job_dtls_type.setText(type);
        tv_job_dtls_desc.setText(desc);
        tv_job_dtls_bottom_payment.setText(payment);
        tv_job_dtls_duration.setText(typeDur);
        tv_job_dtls_bottom_addr.setText(addr);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.job_dtls_apply_btn_ll_id){
            mPerformApplyFunctionality();
        } else if (v.getId() == R.id.job_dtls_google_map_cl_id) {

            if (checkLocationPermission()){
                mGoToJobLocation();
            }
        }

    }

    private void mPerformApplyFunctionality() {
        String[] data = {JOB_ID, JOB_PROVIDER_ID};
        ((MainActivity) getActivity()).mPerformApply(data, getActivity());
    }

    private void mGoToJobLocation() {
        MyJobLocationMapFragmentDialog fragmentDialog = new MyJobLocationMapFragmentDialog();

        Bundle b = new Bundle();
        b.putDouble("job_lat", jobLatLng.latitude);
        b.putDouble("job_lng", jobLatLng.longitude);
        fragmentDialog.setArguments(b);
        fragmentDialog.show(getChildFragmentManager(),"map");

    }


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1005;


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
                mGoToJobLocation();
            }else {
                Toast.makeText(getActivity(), "Location Permission Dite Hobe...", Toast.LENGTH_SHORT).show();
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




}