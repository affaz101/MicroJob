package com.abu.microjob.Model;

public class RecruiterModel {
    private String r_id;
    private String r_name;
    private String r_phone;
    private String r_gender;
    private String r_img_url;
    private String r_bod_day;
    private String r_bod_month;
    private String r_bod_year;

    public RecruiterModel() {
    }

    public RecruiterModel(String r_id, String r_name, String r_phone, String r_gender, String r_img_url, String r_bod_day, String r_bod_month, String r_bod_year) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_phone = r_phone;
        this.r_gender = r_gender;
        this.r_img_url = r_img_url;
        this.r_bod_day = r_bod_day;
        this.r_bod_month = r_bod_month;
        this.r_bod_year = r_bod_year;
    }



    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_phone() {
        return r_phone;
    }

    public void setR_phone(String r_phone) {
        this.r_phone = r_phone;
    }

    public String getR_gender() {
        return r_gender;
    }

    public void setR_gender(String r_gender) {
        this.r_gender = r_gender;
    }

    public String getR_img_url() {
        return r_img_url;
    }

    public void setR_img_url(String r_img_url) {
        this.r_img_url = r_img_url;
    }

    public String getR_bod_day() {
        return r_bod_day;
    }

    public void setR_bod_day(String r_bod_day) {
        this.r_bod_day = r_bod_day;
    }

    public String getR_bod_month() {
        return r_bod_month;
    }

    public void setR_bod_month(String r_bod_month) {
        this.r_bod_month = r_bod_month;
    }

    public String getR_bod_year() {
        return r_bod_year;
    }

    public void setR_bod_year(String r_bod_year) {
        this.r_bod_year = r_bod_year;
    }
}


