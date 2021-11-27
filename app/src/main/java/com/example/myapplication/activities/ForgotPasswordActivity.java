package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_email = (EditText) findViewById(R.id.et_email);
        Button btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(v -> {
            JSONObject data = new JSONObject();
            try {
                data.put("username", et_username.getText().toString().trim());
                data.put("email", et_email.getText().toString().trim());

                ServerAccess serverAccess = new ServerAccess(v.getContext(), api.URL_GET_FORGOT, "Loading");
                serverAccess.StartProcess(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}