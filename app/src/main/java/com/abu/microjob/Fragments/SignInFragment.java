package com.abu.microjob.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.abu.microjob.Controller.MyAllFunction;
import com.abu.microjob.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignInFragment extends Fragment implements View.OnClickListener{
    TextInputEditText et_signin_email, et_signin_pass;
    TextView tv_signin_singup;
    Button btn_signin;
    boolean isAllOkForSignIn = false;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    MyAllFunction myAllFunction;
    Activity activity;

    public SignInFragment() {
    }
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);


//        et_signin_email = (TextInputEditText) v.findViewById(R.id.et_signin_email_id);
//        et_signin_pass = (TextInputEditText) v.findViewById(R.id.et_signin_pass_id);
//        tv_signin_singup = (TextView)v.findViewById(R.id.tv_reg_to_sign_in_id);
//        btn_signin = (Button)v.findViewById(R.id.btn_signin_id);
//
//
//        myAllFunction = new MyAllFunction(getParentFragment().getActivity());
//        mAuth = FirebaseAuth.getInstance();
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            et_signin_pass.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
//        }
//        init_et();


//
//
//        tv_signin_singup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SignUpActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("email", et_signin_email.getText().toString().trim());
//                bundle.putString("pass", et_signin_pass.getText().toString().trim());
////                intent.putExtra("email", et_signin_email.getText().toString().trim());
////                intent.putExtra("pass", et_signin_pass.getText().toString().trim());
//
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        btn_signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myAllFunction.mLoadingDialogInit("Wait Please...");
//                myAllFunction.mLoadingBegin();
//
//                mySignInVerify(et_signin_email.getText().toString().trim(), et_signin_pass.getText().toString().trim());
//            }
//        });


        return v;
    }
//
//    private void init_et() {
////        Intent intent = getIntent();
////        String e = intent.getStringExtra("email");
////        String p = intent.getStringExtra("pass");
//
//
//        String e = getArguments().getString("email");
//        String p = getArguments().getString("pass");
//
//
//        et_signin_email.setText(e);
//        et_signin_pass.setText(p);
//    }
//
//    private void isAnyUserSignedIn(){
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (mUser != null){
//            //some one is signed in
//            String uName = mUser.getDisplayName();
//            String uEmail =  mUser.getEmail();
//            String uId = mUser.getUid();
//
//
//
//        }else {
//            //no one is signed in
//
//
//        }
//    }
//
//    private void mySignInVerify(String mail, String pass) {
//        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
//            isAllOkForSignIn = false;
//            if ((TextUtils.isEmpty(mail))){et_signin_email.setError("Field should not be empty.");}
//            if ((TextUtils.isEmpty(pass))){et_signin_pass.setError("Field should not be empty.");}
//            myAllFunction.mLoadingStop();
//        }else {
//            //here all et field is filled, not empty, now password validation
//            if (pass.length() < 8){
//                et_signin_pass.setError("Password should be at least 8 character");
//                myAllFunction.mLoadingStop();
//            }else{
//                //TODO: REGULAR EXPRESSION Implement HERE, where i can check realtime password validation in length format char sequence etc
//
//                //TODO: Below is go for signin with firebase
//                mySignInUserUsingEmailPassInFirebase(mail, pass);
//            }
//        }
//    }
//    private void mySignInUserUsingEmailPassInFirebase(String em, String ps){
//        mAuth.signInWithEmailAndPassword(em, ps).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    mUser = mAuth.getCurrentUser();
//                    isAllOkForSignIn = true;
//
//
//
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                    myAllFunction.mLoadingStop();
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                isAllOkForSignIn = false;
//                if (e instanceof FirebaseAuthEmailException){
////                    Log.d("signInStatus", "Email is not exist");
////                    Toast.makeText(SignInActivity.this, "Email is not exist", Toast.LENGTH_SHORT).show();
//                    myAllFunction.mLoadingStop();
//                    myAllFunction.mAlertrForMsg("Worning!", "Email is not exist", 2);
//                }else{
////                    Log.d("signInStatus", "signed in failed. report is: "+e.getMessage().toString());
////                    Toast.makeText(SignInActivity.this, "signed in failed. report is: "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    myAllFunction.mLoadingStop();
//                    myAllFunction.mAlertrForMsg("Worning!", "signed in failed. report is: "+e.getMessage().toString(), 3);
//                }
//            }
//        });
//
//
//
//
//    };

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.tv_reg_to_sign_in_id){
//            Intent intent = new Intent(getActivity(), SignUpActivity.class);
//            intent.putExtra("email", et_signin_email.getText().toString().trim());
//            intent.putExtra("pass", et_signin_pass.getText().toString().trim());
//            startActivity(intent);
//        } else if (v.getId() == R.id.btn_signin_id){
//            myAllFunction.mLoadingDialogInit("Wait Please...");
//            myAllFunction.mLoadingBegin();
//
//            mySignInVerify(et_signin_email.getText().toString().trim(), et_signin_pass.getText().toString().trim());
//
//        }


    }
}