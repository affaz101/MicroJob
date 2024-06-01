package com.abu.microjob.Model;

public class CandidateModel {

    private String c_id;
    private String c_name;
    private String c_phone;
    private String c_gender;
    private String c_img_url;
    private String c_bod_day;
    private String c_bod_month;
    private String c_bod_year;


    public CandidateModel() {
    }


    public CandidateModel(String c_id, String c_name, String c_phone, String c_gender, String c_img_url, String c_bod_day, String c_bod_month, String c_bod_year) {
        this.c_id = c_id;
        this.c_name = c_name;
        this.c_phone = c_phone;
        this.c_gender = c_gender;
        this.c_img_url = c_img_url;
        this.c_bod_day = c_bod_day;
        this.c_bod_month = c_bod_month;
        this.c_bod_year = c_bod_year;
    }


    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_phone() {
        return c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    }

    public String getC_gender() {
        return c_gender;
    }

    public void setC_gender(String c_gender) {
        this.c_gender = c_gender;
    }

    public String getC_img_url() {
        return c_img_url;
    }

    public void setC_img_url(String c_img_url) {
        this.c_img_url = c_img_url;
    }

    public String getC_bod_day() {
        return c_bod_day;
    }

    public void setC_bod_day(String c_bod_day) {
        this.c_bod_day = c_bod_day;
    }

    public String getC_bod_month() {
        return c_bod_month;
    }

    public void setC_bod_month(String c_bod_month) {
        this.c_bod_month = c_bod_month;
    }

    public String getC_bod_year() {
        return c_bod_year;
    }

    public void setC_bod_year(String c_bod_year) {
        this.c_bod_year = c_bod_year;
    }
}

