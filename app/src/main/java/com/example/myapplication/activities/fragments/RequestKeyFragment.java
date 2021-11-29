package com.example.myapplication.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestKeyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_request_key, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_submit = (Button) view.findViewById(R.id.btn_submit);
        EditText et_kode = (EditText) view.findViewById(R.id.et_kode);

        btn_submit.setOnClickListener(v -> {
            try {
                String request_key = et_kode.getText().toString().trim();
                JSONObject data = new JSONObject();
                data.put("request_key",request_key);

                ServerAccess serverAccess = new ServerAccess(
                        getContext(),
                        api.URL_REQUEST,
                        "Verifikasi kode"
                );

                serverAccess.StartProcess(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}