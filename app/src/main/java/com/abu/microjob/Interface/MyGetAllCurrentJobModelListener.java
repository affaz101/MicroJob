package com.abu.microjob.Interface;

import android.app.Activity;

import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.JobModel;

import java.util.List;

public interface MyGetAllCurrentJobModelListener {
    void mGetAllCurrentJob(List<JobModel> allCurrentJob, List<AppliedJobModel> allAppliedJobList, String userID, Activity activity);
}
