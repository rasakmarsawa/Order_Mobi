package com.example.myapplication.entities;

import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

public class FormCheck {
    private final JSONObject formData;
    private Boolean check;
    private String msg;

    public FormCheck(JSONObject formData) {
        this.formData = formData;
        this.check = true;
        this.msg = "";
    }

    public Boolean getCheck() {
        return check;
    }

    public String getMsg() {
        return msg;
    }

    public void LengthCheck(String key, Integer min, Integer max) throws JSONException {
        if (min != null && formData.getString(key).length() < min){
            this.check = false;
            this.msg = this.msg + key + " tidak boleh kurang dari " + min + " karakter.";
        }

        if (max != null && formData.getString(key).length() > max){
            this.check = false;
            this.msg = this.msg + key + " tidak boleh lebih dari " + max + " karakter.";
        }
    }

    public void EmailCheck(String key) throws JSONException {
        if (!Patterns.EMAIL_ADDRESS.matcher(formData.getString(key)).matches()){
            this.check = false;
            this.msg = this.msg + "Format email anda tidak sesuai.";
        }
    }
}
