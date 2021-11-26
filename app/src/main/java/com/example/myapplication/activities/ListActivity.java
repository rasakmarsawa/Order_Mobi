package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.entities.User;
import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity {
    LoadingDialogBar dialog;
    RecyclerView rv_pesanan;
    ImageView iv_history, iv_home;
    JSONObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dialog = new LoadingDialogBar(this);
        rv_pesanan = (RecyclerView) findViewById(R.id.rv_pesanan);
        iv_history = (ImageView) findViewById(R.id.iv_history);
        iv_home = (ImageView) findViewById(R.id.iv_home);

        User user = new User(this);
        data = new JSONObject();

        try {
            data.put("username",user.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        rv_pesanan.setAdapter(null);
        rv_pesanan.setLayoutManager(new LinearLayoutManager(this));

        ServerAccess serverAccess = new ServerAccess(this, api.URL_GET_ANTRIAN_BY_USER,"Loading");
        serverAccess.StartProcess(data);
    }
}