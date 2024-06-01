package com.abu.microjob.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.abu.microjob.R;
public class ProfileAccountFragment extends Fragment {


    public ProfileAccountFragment() {
        // Required empty public constructor
    }

    ImageView profile_img;
    TextView tv_profile_type,tv_profile_id,tv_profile_name,tv_profile_phone,tv_profile_dob;
    FrameLayout fl_profile_logout_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_account, container, false);
        profile_img = (ImageView) v.findViewById(R.id.profile_iv_img_id);
        tv_profile_type = (TextView) v.findViewById(R.id.profile_tv_user_type_id);
        tv_profile_id = (TextView) v.findViewById(R.id.profile_tv_user_id_id);
        tv_profile_name = (TextView) v.findViewById(R.id.profile_tv_user_name_id);
        tv_profile_phone = (TextView) v.findViewById(R.id.profile_tv_user_phone_id);
        tv_profile_dob = (TextView) v.findViewById(R.id.profile_tv_user_dob_id);





        return v;
    }
}