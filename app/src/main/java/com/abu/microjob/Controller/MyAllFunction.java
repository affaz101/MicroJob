package com.abu.microjob.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.abu.microjob.R;

public class MyAllFunction {
    private Context context;
    private Activity activity;
    private Dialog loading_dialog, alert_dialog_forMsg;
//    private AlertDialog alert_dialog_forMsg;
    private View v;
    private AlertDialog.Builder builder;
    public MyAllFunction(){

    }

    public MyAllFunction(Activity activity){
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.loading_dialog = new Dialog(activity);
//        mGoogleInit();

    }

//===================================AlertDialog Area Begin===================================================================================================
    public void mAlertrForMsg(String title, String msg, int msg_type){
    //TODO: Type 0:no, 1:success, 2:warning, 3:err, 4:exit

    builder.setTitle(title);
    builder.setMessage(msg);
    //builder.setIcon(R.drawable.google);
    builder.setCancelable(false);

    switch (msg_type){
        case 0:
            //builder dialog icon should be nothing
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            break;
        case 1://success
            builder.setIcon(R.drawable.done_icon);
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            break;
        case 2://warning
            builder.setIcon(R.drawable.warning_icon);
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            break;
        case 3://error
            builder.setIcon(R.drawable.error_icon);
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            break;
        case 4://exit
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            break;

    }

    this.alert_dialog_forMsg = this.builder.create();
    this.alert_dialog_forMsg.show();

//        this.alert_dialog_forMsg = this.builder.create();
//        this.alert_dialog_forMsg.show();

}


//===================================AlertDialog Area End=====================================================================================================
//==================================Loading Dialog Area Begin=================================================================================================

    /*
    TODO
     Below 3 method doc:
     mLoadingDialogInit(String msg)
     mLoadingBegin()
     mLoadingStop()
     These 3 method is consequence from init to start to stop the loading dialog

    */
    public void mLoadingDialogInit(String msg){

        v = LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.custom_loading_dialog, null, false);
        TextView tv_loadingMsg = v.findViewById(R.id.tv_loading_msg_id);
//        TextView tv_yes = v.findViewById(R.id.tv_loading_yes_id);
//        TextView tv_no = v.findViewById(R.id.tv_loading_no_id);
        //TODO: here if you want to change value in progress, use thread
        if (msg != null || !msg.equals("")){
            tv_loadingMsg.setText(msg);
        }
//        tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
////                loading_dialog.dismiss();
//            }
//        });
//        tv_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
//            }
//        });
        loading_dialog.setContentView(v);
        loading_dialog.setCancelable(false);
        mLoadingBegin();
    }
    public void mLoadingBegin(){
        this.loading_dialog.show();
    }
    public void mLoadingStop(){
        if (this.loading_dialog.isShowing()){
            this.loading_dialog.dismiss();
        }
    }

//==================================Loading Dialog Area End=======================================================================================================







//==================================Google Login Area Start=======================================================================================================
//    private GoogleSignInOptions gso;
//    private GoogleSignInClient mGSC;
//    private GoogleSignInAccount mGSAcc;
//
//    public void mGoogleInit(){
//        this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        this.mGSC = GoogleSignIn.getClient(activity, this.gso);
//        this.mGSAcc = GoogleSignIn.getLastSignedInAccount(activity);
//        if (this.mGSAcc != null){
//            //loged in
//            String userName = this.mGSAcc.getDisplayName();
//            String userEmail = this.mGSAcc.getEmail();
//            Uri userPhoto = this.mGSAcc.getPhotoUrl();
//        }
//    }
//    public void mySignUpUsignGoogleAccount() {
//        this.googleSignInIntent = this.mGSC.getSignInIntent();
//        activity.startActivityForResult(this.googleSignInIntent, 1000);
//    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (resultCode == 1000){
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            handleSignInData(task);
////        }
////
////    }
//
//
//
//
//    public void handleGoogleSignInData(Task<GoogleSignInAccount> task) {
//        try {
//            this.account = task.getResult(ApiException.class);
//            //signed in successfully...
//            if (this.account != null){
//                String userName = this.account.getDisplayName();
//                String userEmail = this.account.getEmail();
//                Uri userPhoto = this.account.getPhotoUrl();
//
//                Intent intent = new Intent(activity, MainActivity.class);
//                intent.putExtra("name", userName);
//                intent.putExtra("email", userEmail);
//                intent.putExtra("uri", userPhoto);
//                activity.startActivity(intent);
//            }
//        } catch (ApiException e) {
//            Toast.makeText(activity, "SignIn Failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void signedOutFromGoogle(){
//        this.mGSC.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Intent intent = new Intent(activity, SignInActivity.class);
//                activity.startActivity(intent);
//            }
//        });
//    }
//









//==================================Google Login Area End=======================================================================================================


























}
