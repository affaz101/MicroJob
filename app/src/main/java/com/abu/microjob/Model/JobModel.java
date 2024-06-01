package com.abu.microjob.Model;

public class JobModel {
    private String job_id;
    private String job_title;
    private String job_desc;
    private String job_payment;
    private String job_loc;
    private String job_type;
    private String job_duration;
    private String job_address;
    private Double job_lat;
    private Double job_lng;
    private long work_order_place_time;
    private String work_provider_id;

    public JobModel() {
    }

    public JobModel(String job_id, String job_title, String job_desc, String job_payment, String job_loc, String job_type, String job_duration, String job_address, Double job_lat, Double job_lng, long work_order_place_time, String work_provider_id) {
        this.job_id = job_id;
        this.job_title = job_title;
        this.job_desc = job_desc;
        this.job_payment = job_payment;
        this.job_loc = job_loc;
        this.job_type = job_type;
        this.job_duration = job_duration;
        this.job_address = job_address;
        this.job_lat = job_lat;
        this.job_lng = job_lng;
        this.work_order_place_time = work_order_place_time;
        this.work_provider_id = work_provider_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getJob_payment() {
        return job_payment;
    }

    public void setJob_payment(String job_payment) {
        this.job_payment = job_payment;
    }

    public String getJob_loc() {
        return job_loc;
    }

    public void setJob_loc(String job_loc) {
        this.job_loc = job_loc;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getJob_duration() {
        return job_duration;
    }

    public void setJob_duration(String job_duration) {
        this.job_duration = job_duration;
    }

    public String getJob_address() {
        return job_address;
    }

    public void setJob_address(String job_address) {
        this.job_address = job_address;
    }

    public Double getJob_lat() {
        return job_lat;
    }

    public void setJob_lat(Double job_lat) {
        this.job_lat = job_lat;
    }

    public Double getJob_lng() {
        return job_lng;
    }

    public void setJob_lng(Double job_lng) {
        this.job_lng = job_lng;
    }

    public long getWork_order_place_time() {
        return work_order_place_time;
    }

    public void setWork_order_place_time(long work_order_place_time) {
        this.work_order_place_time = work_order_place_time;
    }

    public String getWork_provider_id() {
        return work_provider_id;
    }

    public void setWork_provider_id(String work_provider_id) {
        this.work_provider_id = work_provider_id;
    }
}
