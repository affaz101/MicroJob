package com.abu.microjob.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.R;

public class MyCustomTimePikerFragment extends DialogFragment implements View.OnClickListener {



    private TextInputDialogListener listener;

    public MyCustomTimePikerFragment() {

    }
    public void setTextInputDialogListener(TextInputDialogListener listener) {
        this.listener = listener;
    }
    View from_rst_btn;
    ImageView from_time_reset,to_time_reset;
    TextView from_time_text_tv,to_time_text_tv, reflect_from_top, reflect_from_bot, reflect_to_top, reflect_to_bot;
    LinearLayout from_lin_time_container, to_lin_time_container;
    FrameLayout time_duration_conf_btn;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();



        View view = inflater.inflate(R.layout.time_piker_dialog_design, null);
        from_rst_btn = (View) view.findViewById(R.id.from_time_reset_btn_id);

        from_time_text_tv= (TextView)view.findViewById(R.id.from_time_text_id);
        to_time_text_tv= (TextView)view.findViewById(R.id.to_time_text_id);
        from_time_reset= (ImageView)view.findViewById(R.id.from_time_reset_id);
        to_time_reset= (ImageView)view.findViewById(R.id.to_time_reset_id);
        from_lin_time_container = (LinearLayout) view.findViewById(R.id.from_time_piker_linerar_container_id);
        to_lin_time_container = (LinearLayout) view.findViewById(R.id.to_time_piker_linerar_container_id);
        time_duration_conf_btn = (FrameLayout) view.findViewById(R.id.time_duration_conf_btn_id);
        reflect_from_top = (TextView) view.findViewById(R.id.from_time_reflect_top_id);
        reflect_from_bot = (TextView) view.findViewById(R.id.from_time_reflect_bottom_id);
        reflect_to_top = (TextView) view.findViewById(R.id.to_time_reflect_top_id);
        reflect_to_bot = (TextView) view.findViewById(R.id.to_time_reflect_bottom_id);

        Bundle prevTimeBundle = getArguments();
        if (prevTimeBundle != null){
            String prev_from = prevTimeBundle.getString("prev_from");
            String prev_to = prevTimeBundle.getString("prev_to");
            from_time_text_tv.setText(prev_from);
            mFromReflect();
            to_time_text_tv.setText(prev_to);
            mToReflect();
        }

        from_time_reset.setOnClickListener(this);
        to_time_reset.setOnClickListener(this);
        from_time_text_tv.setOnClickListener(this);
        to_time_text_tv.setOnClickListener(this);
        from_lin_time_container.setOnClickListener(this);
        to_lin_time_container.setOnClickListener(this);
        time_duration_conf_btn.setOnClickListener(this);


        builder.setView(view)
                .setPositiveButton("Confirmed", (dialog, which) -> {
                    sendBackTimeText();
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();

    }

    public interface TextInputDialogListener {
        void onInputEntered(String input);
    }
    public void mFromReflect(){
        String t1 = from_time_text_tv.getText().toString();
        reflect_from_top.setText(t1);
        reflect_from_bot.setText(t1);
    }
    public void mToReflect(){
        String t2 = to_time_text_tv.getText().toString();
        reflect_to_top.setText(t2);
        reflect_to_bot.setText(t2);
    }
    public void sendBackTimeText(){
        if (listener != null) {
            String str_ren = "From: "+from_time_text_tv.getText().toString()+"\nTo: "+to_time_text_tv.getText().toString();
            listener.onInputEntered(str_ren);
            //Toast.makeText(getActivity(), str_ren, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.from_time_reset_btn_id || v.getId() == R.id.from_time_reset_id){
            //reset
            from_time_text_tv.setText("00:00 AM");
            mFromReflect();
        }else if ( v.getId() == R.id.to_time_reset_btn_id || v.getId() == R.id.to_time_reset_id){
            //reset
            to_time_text_tv.setText("00:00 AM");
            mToReflect();
        } else if (v.getId() == R.id.from_time_text_id || v.getId() == R.id.from_time_piker_linerar_container_id ) {
            myGetTime(1);
            //TODO: indicator:1 => means set text to view of:  from_time_text_tv
        } else if (v.getId() == R.id.to_time_text_id || v.getId() == R.id.to_time_piker_linerar_container_id ) {
            myGetTime(2);
            //TODO: indicator:2 => means set text to view of:  to_time_text_tv
        } else if (v.getId() == R.id.time_duration_conf_btn_id) {
            sendBackTimeText();
            dismiss();
        }
    }
    public void myGetTime(int indicator){
        MyTimePicker myTimePicker = new MyTimePicker();
        myTimePicker.setMyTimePickerListener(new MyTimePicker.MyTimePickerListener() {
            @Override
            public void myGetSelectedTime(String selected_time) {
                switch (indicator){
                    case 1:
                        from_time_text_tv.setText(selected_time);

                        mFromReflect();
                        break;
                    case 2:
                        to_time_text_tv.setText(selected_time);

                        mToReflect();
                        break;
                }
            }
        });

        myTimePicker.show(getActivity().getSupportFragmentManager(), "time");

    }













}