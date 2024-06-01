package com.abu.microjob.Model;

public class MainTopProfileModel {
    String main_img_url;
    String main_name;
    String main_phone;

    public MainTopProfileModel() {
    }

    public String getMain_img_url() {
        return main_img_url;
    }

    public void setMain_img_url(String main_img_url) {
        this.main_img_url = main_img_url;
    }

    public String getMain_name() {
        return main_name;
    }

    public void setMain_name(String main_name) {
        this.main_name = main_name;
    }

    public String getMain_phone() {
        return main_phone;
    }

    public void setMain_phone(String main_phone) {
        this.main_phone = main_phone;
    }

    public MainTopProfileModel(String main_img_url, String main_name, String main_phone) {
        this.main_img_url = main_img_url;
        this.main_name = main_name;
        this.main_phone = main_phone;
    }
}
