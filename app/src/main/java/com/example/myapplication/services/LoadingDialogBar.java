package com.example.myapplication.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadingDialogBar {
    Context context;
    Dialog dialog, notification, logout, confirmation;

    public LoadingDialogBar(Context context) {
        this.context = context;
    }

    public void ShowDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        dialog.create();
        dialog.show();
    }

    public void ShowNotification(String message, Boolean status){
        notification = new Dialog(context);
        notification.setContentView(R.layout.notification);

        notification.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_title = notification.findViewById(R.id.tv_title);
        tv_title.setText(message);

        ImageView iv_status = notification.findViewById(R.id.iv_status);
        if (status){
            iv_status.setImageResource(R.drawable.success);
        }else {
            iv_status.setImageResource(R.drawable.fail);
        }

        notification.create();
        notification.show();
    }

    public void ShowLogout(){
        logout = new Dialog(context);
        logout.setContentView(R.layout.logout);

        logout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_logout = (Button) logout.findViewById(R.id.btn_logout);
        Button btn_cancel_logout = (Button) logout.findViewById(R.id.btn_cancel_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
                ((Activity) context).finish();
            }
        });

        btn_cancel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.dismiss();
            }
        });

        logout.create();
        logout.show();
    }

    public void ShowConfirmation(JSONArray data){
        try{
            int i = 0, total=0, jumlah;
            JSONArray pesan = new JSONArray();
            Log.d("hello", "ShowConfirmation: "+data.toString());
            while(i<data.length()){
                JSONObject temp = data.getJSONObject(i);
                if (temp.getInt("jumlah_barang")!=0){
                    pesan.put(temp);
                    jumlah = temp.getInt("harga")*temp.getInt("jumlah_barang");
                    total = total + jumlah;
                }
                i = i+1;
            }

            if(pesan.length()==0){
                Toast.makeText(context,"Ups! Kamu belum memilih item apapun.",Toast.LENGTH_SHORT).show();
            }else{
                int ttl = total;
                String id_pelanggan;

                confirmation = new Dialog(context);
                confirmation.setContentView(R.layout.dialog_confirmation);
                confirmation.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                confirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView tv_total = confirmation.findViewById(R.id.tv_total);
                tv_total.setText("Rp. "+String.valueOf(total));

                User user = new User(context);

                RecyclerView rv = (RecyclerView) confirmation.findViewById(R.id.recyclerView);
                GlobalAdapter adapter = new GlobalAdapter(context,pesan,1);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(context));

                CardView cvk_konfirmasi = confirmation.findViewById(R.id.cvk_konfirmasi);
                cvk_konfirmasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("id_pelanggan",user.getUsername());
                            data.put("total_harga",String.valueOf(ttl));
                            data.put("item",pesan);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ServerAccess serverAccess = new ServerAccess(v.getContext(), api.URL_ADD_PESANAN,"Buat Pesanan");
                        serverAccess.StartProcess(data);
                    }
                });

                confirmation.create();
                confirmation.show();
            }
        }catch(JSONException e){

        }
    }

    public void HideDialog(){
        dialog.dismiss();
    }
}
