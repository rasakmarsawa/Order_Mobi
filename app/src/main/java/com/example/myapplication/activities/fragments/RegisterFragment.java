package com.example.myapplication.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {

    View view;
    EditText et_username, et_password, et_nama_pelanggan, et_email, et_no_hp;
    LoadingDialogBar dialog;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
        et_nama_pelanggan = view.findViewById(R.id.et_nama_pelanggan);
        et_email = view.findViewById(R.id.et_email);
        et_no_hp = view.findViewById(R.id.et_no_hp);

        dialog = new LoadingDialogBar(getContext());

        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches()){
                    ServerAccess serverAccess = new ServerAccess(getContext(), api.URL_REGISTER,"Register");
                    serverAccess.StartProcess(getData());
                }else{
                    LoadingDialogBar dialog = new LoadingDialogBar(getContext());
                    dialog.ShowNotification("Format email tidak benar",false);
                }
            }
        });

        return view;
    }

    public JSONObject getData(){
        JSONObject data = new JSONObject();
        try {
            data.put("username",et_username.getText().toString().trim());
            data.put("password",et_password.getText().toString().trim());
            data.put("nama_pelanggan",et_nama_pelanggan.getText().toString().trim());
            data.put("email",et_email.getText().toString().trim());
            data.put("no_hp",et_no_hp.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}