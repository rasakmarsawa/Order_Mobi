package com.example.myapplication.entities;

import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Iterator;

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
        if (this.check == true){
            if (min != null && formData.getString(key).length() < min){
                this.check = false;
                this.msg = this.msg + key + " tidak boleh kurang dari " + min + " karakter.";
            }

            if (max != null && formData.getString(key).length() > max){
                this.check = false;
                this.msg = this.msg + key + " tidak boleh lebih dari " + max + " karakter.";
            }
        }
    }

    public void EmailCheck(String key) throws JSONException {
        if (!Patterns.EMAIL_ADDRESS.matcher(formData.getString(key)).matches() && this.check == true){
            this.check = false;
            this.msg = this.msg + "Format email anda tidak sesuai.";
        }
    }

    public void EmptyCheck() throws JSONException {
        Iterator<String> keys = formData.keys();
        while(keys.hasNext() && this.check == true){
            String key = keys.next();
            if (formData.getString(key).equals("")){
                this.check = false;
                this.msg = "Data tidak boleh dikosongkan.";
            }
        }
    }
}
