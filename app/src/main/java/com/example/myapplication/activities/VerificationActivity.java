package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Button btn_verif = (Button) findViewById(R.id.btn_verif);
        EditText et_verif = (EditText) findViewById(R.id.et_verif);

        btn_verif.setOnClickListener(v -> {
            try {
                String request_key = et_verif.getText().toString().trim();
                JSONObject data = new JSONObject();
                data.put("request_key",request_key);

                ServerAccess serverAccess = new ServerAccess(
                        this,
                        api.URL_REQUEST,
                        "Verifikasi Akun"
                );

                serverAccess.StartProcess(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }
}