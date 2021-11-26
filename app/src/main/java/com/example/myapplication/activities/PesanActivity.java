package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONObject;

public class PesanActivity extends AppCompatActivity {
    LoadingDialogBar dialog;
    RecyclerView rv_barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        dialog = new LoadingDialogBar(this);
        rv_barang = (RecyclerView) findViewById(R.id.rv_barang);

        ServerAccess serverAccess = new ServerAccess(this, api.URL_GET_BARANG,"Loading");
        serverAccess.StartProcess(new JSONObject());

        findViewById(R.id.cv_pesan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.ShowConfirmation(serverAccess.getDataReturn());
            }
        });

    }
}