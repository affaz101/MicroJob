package com.abu.microjob.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.Controller.MyAllFireBaseWork;
import com.abu.microjob.R;
import com.abu.microjob.View.MyAuth;
import com.google.android.gms.common.util.ArrayUtils;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class CandidateAuthFragment extends Fragment implements View.OnClickListener, MyAllFireBaseWork.MyOnAllThreeStepTaskIsCompletedListener, MyAllFireBaseWork.MyOnGotoLogInPageListener, MyAllFireBaseWork.MyOnGotoRegisterPageListener {

    public CandidateAuthFragment() {

    }
    public static final String USER_TYPE = "CANDIDATE";
    private static CircleImageView can_iv_reg_profile_img;
    private static ImageView can_iv_img_plus_btn, can_iv_reg_phone_reset_btn, can_iv_login_phone_reset_btn;
    private static EditText can_reg_et_profile_name,can_reg_et_profile_phone,can_reg_et_profile_day,can_reg_et_profile_year, can_login_et_phone;
    private static RadioGroup can_reg_radio_gender;
    private static Spinner can_reg_spin_month;
    private static TextView can_tv_reg_to_login, can_tv_login_to_signup;
    private AppCompatButton can_reg_registration_btn, can_reg_login_btn;
    private FrameLayout can_reg_fl_registration, can_reg_fl_login;
    private LinearLayout can_ll_reg_progress;

    private String[] can_monthNames = {"Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_STORAGE = 303; // Request code for storage permission
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_PICK = 200;

    public static final int UCROPPED_CODE = 301;

    private Uri can_final_img_uri;
    public static View v;
    private MyAllFireBaseWork myFirebase;
    private MyAuth myAuth;
    private static MyOnGotoMainActivityListener mainActivityListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myFirebase = new MyAllFireBaseWork(getActivity());
        myFirebase.MySetTaskCompletedListener((MyAllFireBaseWork.MyOnAllThreeStepTaskIsCompletedListener) new CandidateAuthFragment());
        myFirebase.MySetOnGotoLogInPage((MyAllFireBaseWork.MyOnGotoLogInPageListener) new CandidateAuthFragment());
        myFirebase.MySetOnGotoRegisterPage((MyAllFireBaseWork.MyOnGotoRegisterPageListener) new CandidateAuthFragment());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_candidate_auth, container, false);
        init(v);



        can_reg_registration_btn.setOnClickListener(this);
        can_reg_login_btn.setOnClickListener(this);
        can_tv_reg_to_login.setOnClickListener(this);
        can_tv_login_to_signup.setOnClickListener(this);
        can_iv_img_plus_btn.setOnClickListener(this);
        can_iv_reg_profile_img.setOnClickListener(this);
        can_iv_reg_phone_reset_btn.setOnClickListener(this);
        can_iv_login_phone_reset_btn.setOnClickListener(this);




        mMySpecialProgressShowOrHide(true);
        mRegistrationMode();
        mMySpecialProgressShowOrHide(false);

        mReadySpinner(can_reg_spin_month);



        return v;

    }
    private void mReadySpinner(Spinner spinnerMonth){

        // Create an adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, can_monthNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Handle spinner selection change (optional)
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = can_monthNames[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    };
    private void mRegistrationMode(){
        can_reg_et_profile_phone.setText(String.valueOf(can_login_et_phone.getText()));
        can_reg_fl_registration.setVisibility(View.VISIBLE);
        can_reg_fl_login.setVisibility(View.GONE);
    }
    private void mLoginMode(){
        can_login_et_phone.setText(String.valueOf(can_reg_et_profile_phone.getText()));
        can_reg_fl_login.setVisibility(View.VISIBLE);
        can_reg_fl_registration.setVisibility(View.GONE);
    }
    private void mMySpecialProgressShowOrHide(boolean isShow){
        if (isShow){
            can_ll_reg_progress.setVisibility(View.VISIBLE);
        }else {
            can_ll_reg_progress.setVisibility(View.GONE);
        }
    };

    private void init(View v) {
        can_iv_reg_profile_img = (CircleImageView) v.findViewById(R.id.candidate_civ_profile_img_id);
        can_iv_img_plus_btn = (ImageView) v.findViewById(R.id.iv_candidate_add_img_btn_id);
        can_iv_reg_phone_reset_btn = (ImageView) v.findViewById(R.id.iv_candidate_phone_reset_btn_id);
        can_iv_login_phone_reset_btn = (ImageView) v.findViewById(R.id.iv_candidate_login_phone_reset_btn_id);
        can_reg_et_profile_name = (EditText) v.findViewById(R.id.et_candidate_name_id);
        can_reg_et_profile_phone = (EditText) v.findViewById(R.id.et_candidate_phone_id);
        can_reg_et_profile_day = (EditText) v.findViewById(R.id.et_candidate_date_id);
        can_reg_et_profile_year = (EditText) v.findViewById(R.id.et_candidate_year_id);
        can_login_et_phone = (EditText) v.findViewById(R.id.et_candidate_login_phone_id);
        can_reg_radio_gender = (RadioGroup) v.findViewById(R.id.candidate_radioGroup_gender_id);
        can_tv_login_to_signup = (TextView) v.findViewById(R.id.tv_candidate_login_to_sign_up_id);
        can_tv_reg_to_login = (TextView) v.findViewById(R.id.tv_candidate_to_sign_in_id);
        can_reg_registration_btn = (AppCompatButton) v.findViewById(R.id.btn_candidate_register_btn_id);
        can_reg_login_btn = (AppCompatButton) v.findViewById(R.id.btn_candidate_login_log_in_id);
        can_reg_fl_registration = (FrameLayout) v.findViewById(R.id.fl_candidate_cotainer_id);
        can_reg_fl_login = (FrameLayout) v.findViewById(R.id.fl_candidate_login_cotainer_id);
        can_ll_reg_progress = (LinearLayout) v.findViewById(R.id.candidate_progress_id);
        can_reg_spin_month = (Spinner) v.findViewById(R.id.spin_candidate_month_id);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_candidate_to_sign_in_id){
            mLoginMode();
        }else if (v.getId() == R.id.tv_candidate_login_to_sign_up_id) {
            mRegistrationMode();
        } else if (v.getId() == R.id.btn_candidate_register_btn_id) {
//            mainActivityListener.mGotoMainActivity();
            if(mVerifyIsEverythingOK()){
                mRegBegin();
            }else {
                mNotice("Aware!", "Not ready to proceed, Maybe there are some error here. Please recheck again.");
            };
        } else if (v.getId() == R.id.iv_candidate_add_img_btn_id || v.getId() == R.id.candidate_civ_profile_img_id) {
            mGetImageWithCrop();
        } else if (v.getId() == R.id.btn_candidate_login_log_in_id) {
            if (mVerifyIsPhoneOK()){
                mLoginBegin();
            }
        } else if (v.getId() == R.id.iv_candidate_phone_reset_btn_id) {
            can_reg_et_profile_phone.setText("");
        } else if (v.getId() == R.id.iv_candidate_login_phone_reset_btn_id) {
            can_login_et_phone.setText("");
        }

    }
    private static final int REQUEST_CAMERA_PERMISSION = 111;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(getActivity(), "Storage permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied. Cannot capture image.", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with camera intent
                captureImage();
            } else {
                // Permission denied, handle the situation (e.g., inform user)
                Toast.makeText(getActivity(), "Storage permission is required to capture images.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void mLoginBegin() {

        if (isInternetAvailable()){
            mShowProg();
            can_login_et_phone = (EditText) v.findViewById(R.id.et_candidate_login_phone_id);
            myFirebase.mPassValue(can_login_et_phone.getText().toString().trim(), CandidateAuthFragment.USER_TYPE);
            myFirebase.mStartLoginProcess(requireActivity());
        }else{
            mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
        }


    }

    private boolean requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return true;
        } else {
            // Permission already granted
            return true;
        }
    }

    private void mGetImageWithCrop() {
        if ( requestStoragePermission()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Image Source");
            builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    switch (position) {
                        case 0:
                            mOpenCamera();
                            break;
                        case 1:
                            mOpenGallery();
                            break;
                    }
                }
            });
            builder.show();


//            startActivityForResult(galleryInten, PICK_IMAGE_CODE);
        }

    }

    private void mOpenCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            captureImage();
        } else {
            captureImage();
        }
    }



    private  static Uri sourceImageUri;
    private  static String img_path;
    private  static String timeStamp;
    private  static String imageFileName;
    private  static File storageDir;
    private Uri crop_des_uri;
    private String crop_imageFileName;
    private void captureImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            return;
        }


        //for main captured image
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = timeStamp + ".jpg";

        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Camera/");
//        Log.d("dekh", storageDir.toString()+"   :extra");
        img_path = storageDir+"/"+imageFileName;
        sourceImageUri = Uri.fromFile(new File(img_path));


        //for cropped image
        String crop_timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        crop_imageFileName = crop_timeStamp + ".jpg";
        File crop_storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Camera/");
        crop_des_uri = Uri.fromFile(new File(crop_storageDir, crop_imageFileName));


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, crop_des_uri);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImageUri);
        this.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);


    }

    private void mOpenGallery() {

        can_iv_reg_profile_img.setImageResource(R.drawable.icon_profile_w);//clear imageviews
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);

    }

    private boolean isCameraOrGallery = false;
    private void mCropImage(Uri img_uri){

        UCrop.Options options = new UCrop.Options();

        if (isCameraOrGallery){
            //calling from camera

            options.setCompressionQuality(80);
            UCrop.of(img_uri, crop_des_uri)
                    .withOptions(options)
                    .withAspectRatio(1,1)
                    .start(getActivity(), UCROPPED_CODE);
            Log.d("dekh", (img_uri == null)?"img uri is null": " camera  : ucrop" );
        }else {
            //calling from gallery

            Uri sourceUri = Uri.parse(img_uri.toString());
            Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped_image.jpg"));

            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(1,1)
                    .start(getActivity(), UCROPPED_CODE);

            Log.d("dekh", (img_uri == null)?"img uri is null": " gallery  : ucrop" );
        }




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK){
                File captured_img = new File(img_path);
                if (captured_img.exists()){
                    Uri uri = sourceImageUri;

                    isCameraOrGallery = true;
                    mCropImage(uri);

                    Log.d("dekh",uri.getPath().toString()+" :cam");
                }else {
                    Log.d("dekh","not exist   :cam");

                }
            }




        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();


                isCameraOrGallery = false;
                mCropImage(uri);
                Log.d("dekh",uri.toString()+"   :gal");
            }


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
//    if (isInternetAvailable()){
//
//    }else{
//        mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
//    }

    private void mRegBegin() {
        String name = can_reg_et_profile_name.getText().toString().trim();
        String phone = can_reg_et_profile_phone.getText().toString().trim();
        String month = (can_reg_spin_month.getSelectedItemPosition()+1)+"".toString().trim();
        String day = can_reg_et_profile_day.getText().toString().trim();
        String year = can_reg_et_profile_year.getText().toString().trim();
        String gender = "";
        if (can_reg_radio_gender.getCheckedRadioButtonId() == R.id.rb_candidate_male_id ){
            gender = "m";
        }else {
            gender = "f";
        }
        String img_url;

        if (isInternetAvailable()){
            mShowProg();
            myFirebase.mPassValue(USER_TYPE, mUploadImgGetUrl(phone), name, gender, phone, month, day, year);
            myFirebase.mStartRegistrationProcess(requireActivity());
        }else{
            mNotice("Ohho", "Maybe Internet is not available. Please check it again.");
        }




//        mSignOut();

//        myAllFireBaseWork.mFirebaseAuthRegistration(p, requireActivity());
//        mAlertDialogStop();
//        mNotice("Concern!", "Registration is success.");
//        if (myAllFireBaseWork.mCheckIsAlreadyRegistered(p, requireActivity())){
//            //this number is already registered
//            mAlertDialogStop();
//            mNotice("Concern!", "Your phone is already Registered. You may log in.");
//        }else {
//            //this number is new, can be registered
//            mAlertDialogStop();
////            mNotice("Welcome!", "You can register now.");
//            mUploadImgGetUrl(p);
////            FirebaseUser user = myAllFireBaseWork.mAuth.getCurrentUser();
//
////            Log.d("day1", (user == null?"user null":user.getEmail().toString())+"   :fire");
//        };


    }

    private void mShowProg(){

        can_ll_reg_progress = (LinearLayout) v.findViewById(R.id.candidate_progress_id);
        if (can_ll_reg_progress.getVisibility() == View.GONE){
            can_ll_reg_progress.setVisibility(View.VISIBLE);
        }
    }
    private void mHideProg(){

        can_ll_reg_progress = (LinearLayout) v.findViewById(R.id.candidate_progress_id);
        if (can_ll_reg_progress.getVisibility() == View.VISIBLE){
            can_ll_reg_progress.setVisibility(View.GONE);
        }
    }

    private byte[] mUploadImgGetUrl(String i_name) {
        String img_name = i_name+".jpg";
        can_iv_reg_profile_img.getDrawable().getCurrent().mutate();
        Bitmap bitmap = ((BitmapDrawable) can_iv_reg_profile_img.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] img_byte_data = byteArrayOutputStream.toByteArray();

        return img_byte_data;
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

    private void mNotice(String title, String msg, Activity activity){
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




    private boolean mVerifyIsEverythingOK() {
        boolean yesAllOk = false;
        yesAllOk = mIsEditTextEmpty(can_reg_et_profile_name);
        yesAllOk = mIsEditTextEmpty(can_reg_et_profile_phone);
        yesAllOk = mIsEditTextEmpty(can_reg_et_profile_day);
        yesAllOk = mIsEditTextEmpty(can_reg_et_profile_year);
        if (yesAllOk){
            yesAllOk = mPhoneEditTextVerify(can_reg_et_profile_phone);
            yesAllOk = mCheckDOB(can_reg_et_profile_day, can_reg_et_profile_year);
            if (yesAllOk){
                yesAllOk = mIsImageSelected(can_iv_reg_profile_img);
            }
        }
        return yesAllOk;


    }

    private boolean mVerifyIsPhoneOK() {
        boolean yesAllOk = false;
        yesAllOk = mIsEditTextEmpty(can_login_et_phone);
        if (yesAllOk){
            yesAllOk = mPhoneEditTextVerify(can_login_et_phone);

        }
        return yesAllOk;


    }

    private boolean mIsImageSelected(CircleImageView ivRegProfileImg) {
        if (ivRegProfileImg.getDrawable().getConstantState().equals(getActivity().getResources().getDrawable(R.drawable.icon_profile_w).getConstantState()) ){
            //profile img is not selected
            mNotice("Error", "Image is not selected");
            return false;
        }else {
            //profile img is selected
//            mNotice("Error", "Image is selected");
            return true;
        }
    }


    private boolean mCheckDOB(EditText et_day, EditText et_year){
        boolean tmp = false;
        try {
            if (et_year.getText().length() != 4){
                tmp = false;
                et_year.setError("Not Valid");
            }else {
                int[] m31 = {1,3,5,7,8,10,12};
                int d = Integer.parseInt(et_day.getText().toString().trim());
                int y = Integer.parseInt(et_year.getText().toString().trim());
                int spn_month = can_reg_spin_month.getSelectedItemPosition()+1 ;
                if ((y >= 1950 && y<= 2024)){
                    if (isLeapYear(y)) {
                        //this is leap year: feb 29 days
                        if (spn_month ==2 ){
                            if (!(d >= 1 && d <= 29)) {
                                tmp = false;
                                et_day.setError("Not Correct");
                            } else {
                                tmp = true;
                            }
                        }else if (ArrayUtils.contains(m31, can_reg_spin_month.getSelectedItemPosition()+1)){
                            if (!(d>=1 && d<=31)){
                                tmp = false;
                                et_day.setError("Not Correct");
                            }else { tmp = true;}

                        }else {
                            if (!(d>=1 && d<=30)){
                                tmp = false;
                                et_day.setError("Not Correct");
                            }else { tmp = true;}
                        }
                    }else {
                        //this is not leap year: feb 28 days
                        if (spn_month ==2 ){
                            if (!(d >= 1 && d <= 28)) {
                                tmp = false;
                                et_day.setError("Not Correct");
                            } else {
                                tmp = true;
                            }
                        }else if (ArrayUtils.contains(m31, can_reg_spin_month.getSelectedItemPosition()+1)){
                            if (!(d>=1 && d<=31)){
                                tmp = false;
                                et_day.setError("Not Correct");
                            }else { tmp = true;}

                        }else {
                            if (!(d>=1 && d<=30)){
                                tmp = false;
                                et_day.setError("Not Correct");
                            }else { tmp = true;}
                        }
                    }
                }else {
                    et_year.setError("Enter Valid Year");
                }

            }
        }catch (Exception e){

        }

        return tmp;

    }
    private static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0;
    }

    private boolean mPhoneEditTextVerify(EditText et){
        if (et.getText().toString().length() != 10){
            et.setError("Not Valid");
            return false;
        }else {
            return true;
        }
    }
    private boolean mIsEditTextEmpty(EditText et){
        if (TextUtils.isEmpty(et.getText().toString())){
            et.setError("Can't be empty");
            return false;
        }else {
            return true;
        }
    }
    public void mSetCroppedResult(Uri cropped_uri){

        can_iv_reg_profile_img = (CircleImageView) v.findViewById(R.id.candidate_civ_profile_img_id);
        can_iv_reg_profile_img.setImageURI(cropped_uri);

    }






    @Override
    public void mNotifyToTaskCompleted() {
        mHideProg();
        mGoToHome();

    }

    public interface MyOnGotoMainActivityListener{
        void mGotoMainActivity(String userType);
    }
    public void MySetGotoMainActivity(MyOnGotoMainActivityListener listener){

        this.mainActivityListener = listener;

    }


    private void mGoToHome(){

        if (mainActivityListener != null){
            mainActivityListener.mGotoMainActivity(USER_TYPE);
            Log.d("step1", "    :Main Activity");
        }else {
//            mainActivityListener = (MyOnGotoMainActivityListener) new MyAuth();
//            mainActivityListener.mGotoMainActivity();
            Log.d("step1", "    :Not got Main Activity");
        }
    };


    @Override
    public void mGotoLogInPage(String phone, FragmentActivity fragmentActivity) {
        can_reg_fl_registration = (FrameLayout) v.findViewById(R.id.fl_candidate_cotainer_id);
        can_reg_fl_login = (FrameLayout) v.findViewById(R.id.fl_candidate_login_cotainer_id);
        can_login_et_phone = (EditText) v.findViewById(R.id.et_candidate_login_phone_id);
        can_login_et_phone.setText(phone);
        mLoginMode();
        mHideProg();
        mNotice("Welcome Back!", "This Number is already Registered, Would you like to Log In!", fragmentActivity);
    }
    @Override
    public void mGotoRegisterPage(String phone, FragmentActivity fragmentActivity) {
        can_reg_fl_registration = (FrameLayout) v.findViewById(R.id.fl_candidate_cotainer_id);
        can_reg_fl_login = (FrameLayout) v.findViewById(R.id.fl_candidate_login_cotainer_id);
        can_reg_et_profile_phone = (EditText) v.findViewById(R.id.et_candidate_phone_id);
        can_reg_et_profile_phone.setText(phone);
        mRegistrationMode();
        mHideProg();
        mNotice("Welcome!", "This Number is not Registered, Would you like to Register!", fragmentActivity);
    }



}