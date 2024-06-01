package com.abu.microjob.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abu.microjob.R;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class MyTimePicker extends DialogFragment {


    public void setMyTimePickerListener(MyTimePickerListener listener){
        this.myTimePickerListener = listener;
    }
    public MyTimePicker() {
        // Required empty public constructor
    }

    TimePicker timePicker;
    FrameLayout btn_conf;
    private MyTimePickerListener myTimePickerListener;




//    @Override
//    public void onAttach(@NonNull Context context) {
//        myTimePickerListener = (MyTimePickerListener) getActivity();
//        super.onAttach(context);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.fragment_my_time_picker, null);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker_id);
        btn_conf = (FrameLayout) view.findViewById(R.id.time_picker_conf_btn_id);



        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySetTimeToReturnBack();
                dismiss();

            }
        });

        builder.setView(view)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    mySetTimeToReturnBack();
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();


    }
    public void mySetTimeToReturnBack(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hour = timePicker.getHour();
            int min = timePicker.getMinute();

            String amPm;
            if (hour >= 12) {
                amPm = "PM";
                hour = hour % 12; // Convert to 12-hour format for afternoon/evening
                if (hour == 0) {
                    hour = 12; // Special case for 12 PM
                }
            } else {
                amPm = "AM";
            }

            String h_hour = mMakeTwoPlaceNumber(hour);
            String m_min = mMakeTwoPlaceNumber(min);

            String selected_time_string = h_hour+":"+m_min+" "+amPm;
//            String selected_time_string = hour+":"+min+" "+amPm;
            if (myTimePickerListener != null){
                myTimePickerListener.myGetSelectedTime(selected_time_string);
            }else {
                Toast.makeText(getActivity(), "listener is null", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String mMakeTwoPlaceNumber(int hour) {
        String s = String.valueOf(hour);
        String t = "";
        if (s.length() == 1){
             t= "0"+s;
        }else{
            t = s;
        }
        return t;
    }


    public interface MyTimePickerListener {
        void myGetSelectedTime(String selected_time);
        //ampm: 1=>am, 0=>pm
    }






}