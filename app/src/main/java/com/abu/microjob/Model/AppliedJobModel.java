package com.abu.microjob.Model;

public class AppliedJobModel {
    private String worker_id;
    private String recruiter_id;
    private String job_id;
    private boolean is_applied;
    private boolean is_recruited;


    public AppliedJobModel(String worker_id, String recruiter_id, String job_id, boolean is_applied, boolean is_recruited) {
        this.worker_id = worker_id;
        this.recruiter_id = recruiter_id;
        this.job_id = job_id;
        this.is_applied = is_applied;
        this.is_recruited = is_recruited;
    }

    public AppliedJobModel() {
    }


    public AppliedJobModel(String worker_id, String recruiter_id, String job_id) {
        this.worker_id = worker_id;
        this.recruiter_id = recruiter_id;
        this.job_id = job_id;
    }

    public boolean isIs_applied() {
        return is_applied;
    }

    public void setIs_applied(boolean is_applied) {
        this.is_applied = is_applied;
    }

    public boolean isIs_recruited() {
        return is_recruited;
    }

    public void setIs_recruited(boolean is_recruited) {
        this.is_recruited = is_recruited;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getRecruiter_id() {
        return recruiter_id;
    }

    public void setRecruiter_id(String recruiter_id) {
        this.recruiter_id = recruiter_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }



}
