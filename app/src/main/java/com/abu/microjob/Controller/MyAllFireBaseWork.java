package com.abu.microjob.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.abu.microjob.Fragments.CandidateAuthFragment;
import com.abu.microjob.Fragments.RecriterAuthFragment;
import com.abu.microjob.Interface.MyGetAllCurrentJobModelListener;
import com.abu.microjob.Interface.MyGetAppliedJobCandidateDataListener;
import com.abu.microjob.Interface.MySetImageByIDFromFirebase;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.Model.RecruiterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MyAllFireBaseWork {
    private final Context context;
//    FragmentActivity fragmentActivity;
    private final FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private final boolean isRegistered = true;
    private String img_url;


    public FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean isRegistrationPossible;
    private MyOnAllThreeStepTaskIsCompletedListener listener;
    private MyOnJobAddSuccessListener jobAddSuccessListener;
    private MyOnGotoLogInPageListener logInPageListener;
    private MyOnGotoRegisterPageListener registerPageListener;
//    private MyGetUserAccountDataListener userAccountDataListener;
    private MyGetUserDataAsUserModelClassListener userAccountDataListener;
    private MySetImageByIDFromFirebase mySetImageByIDFromFirebase;
    private MyGetYourJobListListener myGetYourJobListListener;
    private MyGetAppliedJobCandidateDataListener myGetAppliedJobCandidateDataListener;
    private MyGetAllCurrentJobModelListener myGetAllCurrentJobModelListener;
    private FragmentActivity mFragActivity;
    private byte[] user_img_byte;
    private String user_name, user_gender, user_phone, user_month, user_day, user_year, user_img_url;
    private String user_type;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void mPassValue(String user_phone, String user_type) {
        this.user_phone = user_phone;
        this.user_type = user_type;
    }



    public void mPassValue(String user_type,byte[] user_img_byte, String user_name, String user_gender, String user_phone, String user_month, String user_day, String user_year) {
        this.user_type = user_type;
        this.user_img_byte = user_img_byte;
        this.user_name = user_name;
        this.user_gender = user_gender;
        this.user_phone = user_phone;
        this.user_month = user_month;
        this.user_day = user_day;
        this.user_year = user_year;
    }

    public MyAllFireBaseWork(Context context) {
        this.context = context;
        FirebaseApp.initializeApp(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();

    }

    public boolean mCheckIsAlreadyRegistered(String phone_num, FragmentActivity fragmentActivity){
        return mFirebaseAuthCheckSignIn(phone_num, fragmentActivity);



//        isRegistered = true;
//        databaseReference = firebaseDatabase.getReference("registered_phone");
//        databaseReference.child(phone_num).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    Log.d("fire", snapshot.getKey()+" already registered  :fire");
//                    isRegistered = true;
//                }else {
//                    Log.d("fire", snapshot.getKey()+" Not registered  :fire");
//                    isRegistered = false;
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return isRegistered;

    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String mSaveImageInFirebaseStorageGetUrl(String img_name, byte[] img_byte, FragmentActivity fragmentActivity){
         img_url = null;

//        storageReference = FirebaseStorage.getInstance().getReference().child("images/"+img_name);
        storageReference = FirebaseStorage.getInstance().getReference().child(mCreateImgPathAsUserType()+img_name);
        UploadTask uploadTask = storageReference.putBytes(img_byte);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fragmentActivity, "Image Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            img_url = downloadUri.toString();
                            mStoreUserData(img_url);
                            Log.d("step1", img_url+"    :img url gated");
                            //Toast.makeText(fragmentActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("step1", "no url found");
                            //Toast.makeText(fragmentActivity, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return img_url;
    }
    private String mCreateImgPathAsUserType(){
        String n = "";
        if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
            n = "images/recruiter/";
        } else if (user_type.equals(CandidateAuthFragment.USER_TYPE)) {
            n = "images/candidate/";
        }

        return n;
    }
    private String mCreateMailAsUserType(String ph){
        String m = "";
        if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
            m = ph+"@recruiter.com";
        } else if (user_type.equals(CandidateAuthFragment.USER_TYPE)) {
            m = ph+"@candidate.com";
        }


        return m;
    }

    public boolean mFirebaseAuthCheckSignIn(String phone, FragmentActivity fragmentActivity){
        isRegistrationPossible = false;
        String mail = mCreateMailAsUserType(phone);
        String pass = phone;

//        String mail = phone+"@abu.com";
//        String pass = phone;

        mSignOutUser();

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            isRegistrationPossible = false;//it means: this mail is already registered, can sign in, not rereg
                            user = mAuth.getCurrentUser();
                            setUser(user);
//                            mStartSignIn();
                            mGotoLogInAndSetPhoneNum(phone, fragmentActivity);
                        }else {
                            isRegistrationPossible = true;//it means: this mail is new to register
                            mFirebaseAuthRegistration(phone, fragmentActivity);

                        }
                    }
                });
        Log.d("day1", isRegistered+"    :isRegistered");
        return  isRegistrationPossible;
    }
    public boolean mFirebaseAuthSignIn(String phone, FragmentActivity fragmentActivity){
        isRegistrationPossible = false;
        String mail =  mCreateMailAsUserType(phone);
        String pass = phone;

        mSignOutUser();

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            isRegistrationPossible = false;//it means: this mail is already registered, can sign in, not rereg
                            user = mAuth.getCurrentUser();
                            setUser(user);

                            mNotifyAllTaskDone();
//                            mStartSignIn();
                        }else {

                            isRegistrationPossible = true;//it means: this mail is new to register
                            mGotoRegisterAndSetPhoneNum(phone, fragmentActivity);

//                            mFirebaseAuthRegistration(phone, fragmentActivity);

                        }
                    }
                });
        Log.d("day1", isRegistered+"    :isRegistered");
        return  isRegistrationPossible;
    }
    public FirebaseUser mFirebaseAuthRegistration(String phone, FragmentActivity fragmentActivity){
        String mail =  mCreateMailAsUserType(phone);
        String pass = phone;

        mAuth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(fragmentActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    user = mAuth.getCurrentUser();
                                    mSaveImageInFirebaseStorageGetUrl(phone, user_img_byte, fragmentActivity);
//                                    Toast.makeText(fragmentActivity, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(fragmentActivity, "User Creation Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        return user;

    }


    public void mStartRegistrationProcess( FragmentActivity fragmentActivity){
       mCheckIsAlreadyRegistered(user_phone, fragmentActivity);
    }

    public void mStartLoginProcess( FragmentActivity fragmentActivity){
       mFirebaseAuthSignIn(user_phone, fragmentActivity);
    }

    public void mRetrieveUserData(String id, FragmentActivity fragmentActivity){

        String USER_DB_NAME_IN_FIREBASE = "";
        if (user_type.equals(CandidateAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "worker_db";
        }else if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "recruiter_db";
        }

        Log.d("step8", USER_DB_NAME_IN_FIREBASE+"     : db name");

        databaseReference = FirebaseDatabase.getInstance().getReference(USER_DB_NAME_IN_FIREBASE).child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (user_type.equals(CandidateAuthFragment.USER_TYPE)){

                        CandidateModel candidateModel = snapshot.getValue(CandidateModel.class);
                        if (userAccountDataListener != null){
                            userAccountDataListener.mGetUserAccountData(candidateModel, fragmentActivity);
                        }else {
                            Toast.makeText(context, "User account data listener is null", Toast.LENGTH_SHORT).show();
                        }
                    } else if (user_type.equals(RecriterAuthFragment.USER_TYPE)) {

                        RecruiterModel recruiterModel = snapshot.getValue(RecruiterModel.class);
                        if (userAccountDataListener != null){
                            userAccountDataListener.mGetUserAccountData(recruiterModel, fragmentActivity);
                        }else {
                            Toast.makeText(context, "User account data listener is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void mStoreUserData(String img_url_str){
        this.user_img_url = img_url_str;

        String USER_DB_NAME_IN_FIREBASE = "";
        if (user_type.equals(CandidateAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "worker_db";
        }else if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "recruiter_db";
        }

        if (user_type.equals(CandidateAuthFragment.USER_TYPE)){
            CandidateModel candidateModel = new CandidateModel(user_phone,user_name,user_phone,user_gender,user_img_url,user_day, user_month, user_year);
            databaseReference = firebaseDatabase.getReference(USER_DB_NAME_IN_FIREBASE).child(user_phone) ;
            databaseReference.setValue(candidateModel);
        } else if (user_type.equals(RecriterAuthFragment.USER_TYPE)) {
            RecruiterModel recruiter_user_obj = new RecruiterModel(user_phone,user_name,user_phone,user_gender,user_img_url,user_day, user_month, user_year);
            databaseReference = firebaseDatabase.getReference(USER_DB_NAME_IN_FIREBASE).child(user_phone) ;
            databaseReference.setValue(recruiter_user_obj);
        }





        mNotifyAllTaskDone();

        Log.d("step1", "     :user store begin");
    }
    public void mSignOutUser(){
        if (mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
        Log.d("step1", "   : signed out");
    }
    public void mGotoLogInAndSetPhoneNum(String phn, FragmentActivity fragmentActivity){
        if (logInPageListener != null){
            logInPageListener.mGotoLogInPage(phn, fragmentActivity);
        }

        Log.d("step1", "   : Task Done");
    }
    public void mGotoRegisterAndSetPhoneNum(String phn, FragmentActivity fragmentActivity){
        if (registerPageListener != null){
            registerPageListener.mGotoRegisterPage(phn, fragmentActivity);
        }

        Log.d("step1", "   : Task Done");
    }

    public void mNotifyAllTaskDone(){
        if (listener != null){
            listener.mNotifyToTaskCompleted();
        }else {
            Log.d("step2", "listener is null   : Task Done");
        }
        Log.d("step1", "   : Task Done");
    }

    public void mPassJobData(FragmentActivity fragmentActivity, String title, String desc, String payment, String loc, String type, String duration, String addr, Double lat, Double lng, String recruiterId) {
        databaseReference = firebaseDatabase.getReference("current_job");
        String key_id = databaseReference.push().getKey();
        long current_time = System.currentTimeMillis();
        JobModel jobModel = new JobModel(key_id, title, desc, payment, loc, type, duration, addr, lat, lng, current_time, recruiterId);
        databaseReference = firebaseDatabase.getReference("current_job").child(recruiterId).child(key_id);
        databaseReference.setValue(jobModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            mNotifyJobAdded(fragmentActivity);
                        }else {
                            Toast.makeText(fragmentActivity, "Sorry, Job Addition is Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
    public void mNotifyJobAdded(FragmentActivity fragmentActivity){
        if (jobAddSuccessListener != null){
            jobAddSuccessListener.mJobAdded(fragmentActivity);
        }else {
            Log.d("step2", "listener is null   : Task Done");
        }
        Log.d("step1", "   : Task Done");
    }

    public void mGetImageById(String id, MainActivity mainActivity) {

        String USER_DB_NAME_IN_FIREBASE = "";
        if (user_type.equals(CandidateAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "worker_db";
        }else if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
            USER_DB_NAME_IN_FIREBASE = "recruiter_db";
        }

        databaseReference = firebaseDatabase.getReference(USER_DB_NAME_IN_FIREBASE).child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (user_type.equals(RecriterAuthFragment.USER_TYPE)){
                        RecruiterModel rm = snapshot.getValue(RecruiterModel.class);
                        if (mySetImageByIDFromFirebase != null){
                            mySetImageByIDFromFirebase.mSetImage(rm, mainActivity);
                        }
                    } else if (user_type.equals(CandidateAuthFragment.USER_TYPE))  {
                        CandidateModel cm = snapshot.getValue(CandidateModel.class);
                        if (mySetImageByIDFromFirebase != null){
                            mySetImageByIDFromFirebase.mSetImage(cm, mainActivity);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void MyOnSetImageByIDFromFirebase(MySetImageByIDFromFirebase listener){
        mySetImageByIDFromFirebase = listener;
    }

    public void mGetYourJobList(String id, FragmentActivity fragmentActivity) {
        List<JobModel> jobModelList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("current_job").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    jobModelList.clear();
                    for (DataSnapshot childSnap: snapshot.getChildren()){
//                        String key = childSnap.getKey();
                        jobModelList.add(childSnap.getValue(JobModel.class));
                    }

                    if (myGetYourJobListListener != null){
                        myGetYourJobListListener.mMyOnGetYourJobList(jobModelList, fragmentActivity);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void mGetCandidateData(String jobId, FragmentActivity fragmentActivity) {
        List<AppliedJobModel> appliedJobModelList = new ArrayList<>();
        List<CandidateModel> candidateList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("applied_job").child(jobId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    appliedJobModelList.clear();
                    for (DataSnapshot jobSnap: snapshot.getChildren()){
                        appliedJobModelList.add(jobSnap.getValue(AppliedJobModel.class));
//                        Log.d("step6", ""+jobSnap.getKey());
                    }

                    candidateList.clear();
                    for (AppliedJobModel model: appliedJobModelList){
//                        Log.d("step6", model.getWorker_id()+"    "+appliedJobModelList.size());


                        databaseReference = firebaseDatabase.getReference("worker_db").child(model.getWorker_id());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snp) {
                                if (snp.exists()){
//                                    RecruiterModel r = snp.getValue(RecruiterModel.class);
                                    CandidateModel cm = snp.getValue(CandidateModel.class);
                                    candidateList.add(cm);
//                                    Log.d("step6", r.getR_name());

                                }
                                if (candidateList.size() == appliedJobModelList.size()){


//                                    Log.d("step12", snp.getValue()+"    :bbb");

                                    if (myGetAppliedJobCandidateDataListener != null){
                                        myGetAppliedJobCandidateDataListener.mGetAppliedJobData(candidateList, appliedJobModelList,fragmentActivity);
                                        Log.d("step6", candidateList.size()+" size, and listener is not null");
                                    }else {
                                        Log.d("step6", candidateList.size()+" size, and listener is null");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });




                    }

                }else {

                    Log.d("step6", jobId+"  :snap not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void mExtractCandidateData(List<AppliedJobModel> appliedJobList, FragmentActivity fragmentActivity){
        //TODO: currently we are trying recruiter db, but it will be from worker db

        List<RecruiterModel> recruiterUserList = new ArrayList<>();


        for (int i = 0; i < appliedJobList.size(); i++) {
            databaseReference = firebaseDatabase.getReference("recruiter_db").child(appliedJobList.get(i).getRecruiter_id());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){
                        RecruiterModel single_user = snapshot.getValue(RecruiterModel.class);
                        recruiterUserList.add(single_user);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

        }


    }


    public void MyOnSetAppliedJobCandidateList(MyGetAppliedJobCandidateDataListener listener){
        this.myGetAppliedJobCandidateDataListener = listener;
    }

    long idLoopCount = 0;
    long keyLoopCount = 0;
    long keyCount = 0;

    public void mGetBackAllMicroJobs(FragmentActivity fragmentActivity, String userId) {

        idLoopCount = 0;
        keyLoopCount = 0;

        List<AppliedJobModel> appliedJobList = new ArrayList<>();
        List<JobModel> jobModelList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("current_job");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snp: snapshot.getChildren()){
                        idLoopCount++;
                        appliedJobList.clear();
                        databaseReference = firebaseDatabase.getReference("current_job").child(snp.getKey());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot sssnp) {
                                if (snapshot.exists()){
                                    keyCount += sssnp.getChildrenCount();
                                    keyLoopCount = 0;
                                    for (DataSnapshot dsn: sssnp.getChildren()){
                                        keyLoopCount++;
                                        jobModelList.add(dsn.getValue(JobModel.class));

                                        if( idLoopCount == snapshot.getChildrenCount()   &&   (jobModelList.size() == keyCount )){
                                            databaseReference = firebaseDatabase.getReference("applied_job");
                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snApplied) {
                                                    if (snApplied.exists()){
                                                        for (DataSnapshot ds: snApplied.getChildren()){
//                                                            for (AppliedJobModel ap: ds.getValue(AppliedJobModel.class))
//                                                            Log.d("step10", ds.getValue()+"    :1");
                                                            databaseReference = firebaseDatabase.getReference("applied_job").child(ds.getKey());
                                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot job_sn) {
                                                                    if (job_sn.exists()){
                                                                        for (DataSnapshot j_item: job_sn.getChildren()){
                                                                            appliedJobList.add(j_item.getValue(AppliedJobModel.class));
                                                                        }
                                                                        if (!appliedJobList.isEmpty()){
//                                                                            Log.d("step10", appliedJobList.get(0).getJob_id()+"    :1");

                //                                                            Log.d("step10","    :2");
                                                                            mSendBackAllCurrentJobModel(jobModelList, appliedJobList, userId, fragmentActivity);
                //                                                            mSendBackAllCurrentJobModel(jobModelList, appliedJobList, fragmentActivity);
                                                                        }else{
                //                                                            Log.d("step10","appliedjoblist is empty    :3");
                                                                            mSendBackAllCurrentJobModel(jobModelList, null, userId, fragmentActivity);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
//                                                            appliedJobList.add(ds.getValue(AppliedJobModel.class));

                                                        }

//                                                        if (!appliedJobList.isEmpty()){
////                                                            Log.d("step10", appliedJobList.get(0).getJob_id()+"    :1");
//
////                                                            Log.d("step10","    :2");
//                                                            mSendBackAllCurrentJobModel(jobModelList, appliedJobList, userId, fragmentActivity);
////                                                            mSendBackAllCurrentJobModel(jobModelList, appliedJobList, fragmentActivity);
//                                                        }else{
////                                                            Log.d("step10","appliedjoblist is empty    :3");
//                                                            mSendBackAllCurrentJobModel(jobModelList, null, userId, fragmentActivity);
//                                                        }
                                                    }else {
//                                                            Log.d("step10","    :4");

                                                        mSendBackAllCurrentJobModel(jobModelList, null, userId, fragmentActivity);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

//                                            mSendBackAllCurrentJobModel(jobModelList, fragmentActivity);
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
//    private void mSendBackAllCurrentJobModel(List<JobModel> currentJobs, FragmentActivity fragmentActivity){
    private void mSendBackAllCurrentJobModel(List<JobModel> currentJobs, List<AppliedJobModel> appliedJobList, String userID, FragmentActivity fragmentActivity){
        if (myGetAllCurrentJobModelListener != null){
            if (appliedJobList != null){
                myGetAllCurrentJobModelListener.mGetAllCurrentJob(currentJobs,appliedJobList,userID, fragmentActivity);
            }else {
                myGetAllCurrentJobModelListener.mGetAllCurrentJob(currentJobs,null, userID, fragmentActivity);
            }
        }

    };
    public void MySetOnGetAllCurrentData(MyGetAllCurrentJobModelListener listener){
        this.myGetAllCurrentJobModelListener = listener;
    };

    public void mPerformApplyFunctionality(String[] applyData, Activity activity) {
        String worker_id = applyData[0];
        String job_id = applyData[1];
        String job_provider_id = applyData[2];
        boolean isRecruited = false;
        boolean isApplied = true;

        AppliedJobModel worker = new AppliedJobModel(worker_id,job_provider_id, job_id, isApplied, isRecruited);

        databaseReference = firebaseDatabase.getReference("applied_job").child(job_id).child(worker_id);
        databaseReference.setValue(worker)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ((MainActivity) activity).mNotifyJobApplied(activity);
                        }
                    }
                });



    }

    public void mRecruiterFunctionalityBegin(String cId, String jobId, FragmentActivity activity) {
        databaseReference = firebaseDatabase.getReference("applied_job").child(jobId).child(cId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AppliedJobModel appliedJobModel = snapshot.getValue(AppliedJobModel.class);
                    Log.d("step12", appliedJobModel.isIs_recruited()+"    :aaa");
                    appliedJobModel.setIs_recruited(true);
                    databaseReference.setValue(appliedJobModel);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface MyGetYourJobListListener{
        void mMyOnGetYourJobList(List<JobModel> jobModelList, Activity activity);
    }
//    public interface MyGetYourJobListListener{
//        void mMyOnGetYourJobList(List<JobModel> jobModelList, long applyed_count, Activity activity);
//    }
    public void MyOnSetGetYourJobList(MyGetYourJobListListener listener){
        this.myGetYourJobListListener = listener;
    }


    public interface MyOnJobAddSuccessListener{
        void mJobAdded(FragmentActivity fragmentActivity);
    };

    public void MySetOnJobAddSuccess(MyOnJobAddSuccessListener listener){
        this.jobAddSuccessListener = listener;
    };

    public interface MyOnAllThreeStepTaskIsCompletedListener{
        void mNotifyToTaskCompleted();
    }

    public void MySetTaskCompletedListener(MyOnAllThreeStepTaskIsCompletedListener listener){
        this.listener = listener;
    }


    public interface MyOnGotoLogInPageListener{
        void mGotoLogInPage(String phone, FragmentActivity fragmentActivity);
    }
    public void MySetOnGotoLogInPage(MyOnGotoLogInPageListener listener){
        this.logInPageListener = listener;
    }


    public interface MyOnGotoRegisterPageListener{
        void mGotoRegisterPage(String phone, FragmentActivity fragmentActivity);
    }
    public void MySetOnGotoRegisterPage(MyOnGotoRegisterPageListener listener){
        this.registerPageListener = listener;
    }


//    public interface MyGetUserAccountDataListener{
//        void mGetUserAccountData(CandidateModel user_model, Activity activity);
//    }
    public interface MyGetUserDataAsUserModelClassListener<T>{
        void mGetUserAccountData(T user_model, Activity activity);
    }

    public void MySetUserAccountDataListener(MyGetUserDataAsUserModelClassListener listener){
        this.userAccountDataListener = listener;
    }


/*
    String userId = "user1_id"; // Replace with actual user ID

    DatabaseReference reference = database.getReference("users").child(userId);
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                UserData userData = snapshot.getValue(UserData.class);
                // Access user data using userData object (name, email, age, etc.)
            } else {
                // Handle case where user data doesn't exist
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Handle database errors appropriately
        }
    });

*/















//    AlertDialog dialog;
//    ProgressBar progressBar;
//    TextView tv_prog_msg;
//
//    public void mAlertDialogMessage(){
//        View view = LayoutInflater.from(context).inflate(R.layout.loading_prog_design, null);
////        progressBar = view.findViewById(R.id.loading_prog_id);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(view);
//        builder.setCancelable(false);
//
//        dialog = builder.create();
//    }
//
//    public void mAlertDialogMessage(String msg){
//        View view = LayoutInflater.from(context).inflate(R.layout.loading_prog_design, null);
//        progressBar = view.findViewById(R.id.loading_prog_id);
//        tv_prog_msg = view.findViewById(R.id.loading_msg_id);
//        tv_prog_msg.setText(msg);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(view);
//        builder.setCancelable(false);
//
//        dialog = builder.create();
//
//    }
//
//    public void mAlertDialogStart(){
//        dialog.show();
//    }
//    public void mAlertDialogStop(){
//        if (dialog.isShowing()){
//            dialog.dismiss();
//        }
//    }
//
//







}
