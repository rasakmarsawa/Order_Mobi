package com.example.myapplication.activities.fragments;

import android.app.Activity;
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

public class ForgotPasswordFormFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText et_password = (EditText) view.findViewById(R.id.et_password);
        Button btn_submit = (Button) view.findViewById(R.id.btn_submit);

        Activity activity = (Activity) view.getContext();;
        try {
            JSONObject pelanggan = new JSONObject(activity.getIntent().getStringExtra("data"));

            btn_submit.setOnClickListener(v -> {
                JSONObject data = new JSONObject();
                try {
                    data.put("username",pelanggan.getString("username"));
                    data.put("password", et_password.getText().toString().trim());

                    FormCheck formCheck = new FormCheck(data);
                    formCheck.LengthCheck("password",6,null);

                    if (formCheck.getCheck()){
                        ServerAccess serverAccess = new ServerAccess(
                                v.getContext(),
                                api.URL_CHANGE_PASSWORD,
                                "Reset Password"
                        );
                        serverAccess.StartProcess(data);
                    }else{
                        LoadingDialogBar dialog = new LoadingDialogBar(getContext());
                        dialog.ShowNotification("Password minimal harus terdiri dari 6 karakter",false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}