package com.example.myapplication.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.R;
import com.example.myapplication.services.MyFirebaseMessagingService;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    EditText et_username, et_password;
    View view;
    LoadingDialogBar dialog;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);

        dialog = new LoadingDialogBar(getContext());

        btn_login.setOnClickListener(v -> {
            ServerAccess serverAccess = new ServerAccess(getContext(), api.URL_LOGIN,"Login");
            serverAccess.StartProcess(getData());
        });

        return view;
    }

    public JSONObject getData(){
        JSONObject data = new JSONObject();
        try {
            data.put("username",et_username.getText().toString().trim());
            data.put("password",et_password.getText().toString().trim());
            data.put("fcm_token",new MyFirebaseMessagingService().getToken(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}