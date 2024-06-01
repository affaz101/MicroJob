package com.abu.microjob.Interface;


import android.app.Activity;

import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.Model.RecruiterModel;

import java.util.List;

public interface MyGetAppliedJobCandidateDataListener {
    void mGetAppliedJobData(List<CandidateModel> userModel,List<AppliedJobModel> allAppliedJobList, Activity activity);
}
