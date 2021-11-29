package com.example.myapplication.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.entities.FormCheck;
import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestForgotPasswordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText et_email = (EditText) view.findViewById(R.id.et_email);
        EditText et_username = (EditText) view.findViewById(R.id.et_username);
        Button btn_submit = (Button) view.findViewById(R.id.btn_submit);
        LoadingDialogBar dialogBar = new LoadingDialogBar(getContext());

        btn_submit.setOnClickListener(v -> {


            JSONObject data = new JSONObject();
            try {
                data.put("email",et_email.getText().toString().trim());
                data.put("username",et_username.getText().toString().trim());

                FormCheck formCheck = new FormCheck(data);
                formCheck.EmailCheck("email");

                if (formCheck.getCheck()){
                    ServerAccess serverAccess = new ServerAccess(
                            v.getContext(),
                            api.URL_REQUEST_FORGOT,
                            "Loading"
                    );
                    serverAccess.StartProcess(data);
                }else{
                    dialogBar.ShowNotification(formCheck.getMsg(), false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }
}