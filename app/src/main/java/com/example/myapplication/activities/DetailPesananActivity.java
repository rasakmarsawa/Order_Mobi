package com.example.myapplication.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPesananActivity extends AppCompatActivity {
    TextView tv_no, tv_tanggal, tv_total_harga;
    RecyclerView rv_detail;
    CardView cv_batal;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        String data = getIntent().getStringExtra("pesanan");
        if (data == null){
            data = getIntent().getStringExtra("data");
        }
        try {
            JSONObject pesanan = new JSONObject(data);

            tv_no = (TextView) findViewById(R.id.tv_no);
            tv_tanggal = (TextView) findViewById(R.id.tv_tanggal);
            tv_total_harga = (TextView) findViewById(R.id.tv_total_harga);
            rv_detail = (RecyclerView) findViewById(R.id.rv_detail);
            cv_batal = (CardView) findViewById(R.id.cv_batal);

            tv_no.setText(pesanan.getString("no"));
            tv_tanggal.setText(pesanan.getString("tanggal"));
            tv_total_harga.setText("Rp. "+pesanan.getString("total_harga"));

            ServerAccess serverAccess = new ServerAccess(this, api.URL_GET_DETAIL_PESANAN_BY_PESANAN, "Loading");
            serverAccess.StartProcess(pesanan);

            Log.d("hello", "onCreate: "+pesanan.toString());

            if(pesanan.getInt("status")==1){
                cv_batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServerAccess sa = new ServerAccess(v.getContext(),api.URL_CANCEL,"Dibatalkan");
                        sa.StartProcess(pesanan);
                    }
                });
            }else{
                TextView tv_batal = (TextView) findViewById(R.id.tv_batal);

                tv_batal.setText(pesanan.getString("nama_status"));
                cv_batal.setCardBackgroundColor(getResources().getColor(R.color.light_green));
                cv_batal.setOutlineSpotShadowColor(getResources().getColor(R.color.light_green));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}