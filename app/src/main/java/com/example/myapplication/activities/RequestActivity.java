package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.activities.fragments.ForgotPasswordFormFragment;
import com.example.myapplication.activities.fragments.RequestForgotPasswordFragment;
import com.example.myapplication.activities.fragments.RequestKeyFragment;

public class RequestActivity extends AppCompatActivity {
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        layout = (ConstraintLayout) findViewById(R.id.layout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        String type = getIntent().getStringExtra("type");
        if (type==null){
            type = "request_forgot_password";
        }
        switch (type){
            case "request_key":
                //request_key
                RequestKeyFragment requestKeyFragment = new RequestKeyFragment();
                transaction.replace(R.id.layout, requestKeyFragment);
                transaction.commit();
                break;

            case "forgot_password_form":
                //forgot_password
                ForgotPasswordFormFragment formFragment = new ForgotPasswordFormFragment();
                transaction.replace(R.id.layout, formFragment);
                transaction.commit();
                break;
            default :
                //request_forgot_password
                RequestForgotPasswordFragment forgotPasswordFragment = new RequestForgotPasswordFragment();
                transaction.replace(R.id.layout, forgotPasswordFragment);
                transaction.commit();
                break;
        }
    }
}