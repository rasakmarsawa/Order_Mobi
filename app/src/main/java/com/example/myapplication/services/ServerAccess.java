package com.example.myapplication.services;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.PesanActivity;
import com.example.myapplication.entities.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerAccess {
    Context context;
    LoadingDialogBar dialog;
    String url, dialog_msg;
    JSONArray data_return, old = null;
    Boolean canLoadMore = true;

    public ServerAccess(Context context, String url, String dialog_msg) {
        this.context = context;
        this.url = url;
        this.dialog_msg = dialog_msg;
        dialog = new LoadingDialogBar(context);
    }

    public JSONArray getDataReturn() {
        return data_return;
    }

    public void StartProcess(JSONObject data){
        class SendPostReqAsyncTask extends AsyncTask<JSONObject, Void, String> {
            JSONObject response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!dialog_msg.equals("null")){
                    dialog.ShowDialog(dialog_msg);
                }
            }

            @Override
            protected String doInBackground(JSONObject... params) {
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("data", params[0].toString()));

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

                } catch (IOException | JSONException e){

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                GlobalAdapter adapter;
                Activity activity;

                if (!dialog_msg.equals("null")){
                    dialog.HideDialog();
                }

                try{
                    String error;
                    switch (url){
                        case api.URL_REGISTER:
                            error = response.getString("error");
                            if (error.equals("E10")){
                                dialog.ShowNotification("Register berhasil, silahkan login.",true);
                            }else{
                                if (error.equals("E11")){
                                    dialog.ShowNotification("Ups! Username sudah digunakan.",false);
                                }else{
                                    dialog.ShowNotification("Proses gagal, isi data dengan lengkap",false);
                                }
                            }
                            break;
                        case api.URL_LOGIN:
                            error = response.getString("error");
                            if (error.equals("E20")){
                                JSONObject data = response.getJSONObject("data");

                                User user = new User(
                                    data.getString("username"),
                                    null,
                                    null,
                                    null,
                                    null
                                );
                                user.setSession(context);

                                Intent intent = new Intent(context, MainActivity.class);
                                activity = (Activity) context;
                                context.startActivity(intent);
                                activity.finish();
                            }else{
                                if (error.equals("E21")){
                                    dialog.ShowNotification("Ups! Username dan password tidak sesuai.",false);
                                }else{
                                    dialog.ShowNotification("Proses gagal. Isi data dengan lengkap",false);
                                }
                            }
                            break;
                        case api.URL_RELOAD_USER_DATA:
                            try {
                                error = response.getString("error");
                                Log.d("reload", "onPostExecute: "+response.toString());
                                if (error.equals("E00")){
                                    JSONObject data = response.getJSONObject("data");

                                    MyFirebaseMessagingService messagingService = new MyFirebaseMessagingService();
                                    if (messagingService.getToken(context).equals(data.getString("fcm_token"))){
                                        User user = new User(
                                                data.getString("username"),
                                                data.getString("nama_pelanggan"),
                                                data.getString("email"),
                                                data.getString("no_hp"),
                                                data.getString("saldo")
                                        );
                                        user.setSession(context);

                                        activity = (Activity) context;
                                        TextView welcome = (TextView) activity.findViewById(R.id.tv_welcome);
                                        TextView saldo = (TextView) activity.findViewById(R.id.tv_saldo);

                                        welcome.setText("Selamat Datang,\n"+user.getNama_pelanggan());
                                        saldo.setText("Saldo Saat Ini:\nRp. "+user.getSaldo());
                                    }else{
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        activity = (Activity) context;
                                        activity.startActivity(intent);
                                        activity.finish();
                                    }
                                }else{
                                    if (error.equals("E31")){
                                        dialog.ShowNotification("Ups! Username tidak ditemukan.",false);
                                    }else{
                                        dialog.ShowNotification("Proses gagal. Isi data dengan lengkap",false);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case api.URL_GET_ANTRIAN_STATUS:
                            try {
                                Integer status = response.getJSONObject("data").getInt("status");

                                if(status==1){
                                    Intent intent = new Intent(context, PesanActivity.class);
                                    context.startActivity(intent);
                                }else{
                                    dialog.ShowNotification("Ups! Antrian belum buka.\nCoba lagi nanti ya!",false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case api.URL_GET_BARANG:
                            data_return = new JSONArray();
                            try {
                                data_return = response.getJSONArray("data");
                                int i = 0;
                                while (i<data_return.length()){
                                    data_return.getJSONObject(i).put("jumlah_barang",0);
                                    i=i+1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            activity = (Activity) context;
                            RecyclerView rv_barang = (RecyclerView) activity.findViewById(R.id.rv_barang);

                            adapter = new GlobalAdapter(context,data_return,0);
                            rv_barang.setAdapter(adapter);
                            rv_barang.setLayoutManager(new LinearLayoutManager(context));
                            break;
                        case api.URL_ADD_PESANAN:
                            error = response.getString("error");
                            if (error.equals("E50")){
                                dialog.ShowNotification("Pesananmu sudah masuk.",true);
                                dialog.notification.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        context = ((ContextWrapper) context).getBaseContext();
                                        Activity act = (Activity) context;
                                        act.finish();
                                    }
                                });
                            }else{
                                dialog.ShowNotification("Ups! Saldomu tidak cukup",false);
                            }
                            break;
                        case api.URL_LOGOUT:
                            break;
                        case api.URL_GET_ANTRIAN_BY_USER:
                            error = response.getString("error");
                            try {
                                if (error.equals("E60")) {
                                    adapter = new GlobalAdapter(context, response.getJSONArray("data"),2);
                                    activity = (Activity) context;

                                    RecyclerView rv_pesanan = (RecyclerView) activity.findViewById(R.id.rv_pesanan);
                                    rv_pesanan.setAdapter(adapter);
                                    rv_pesanan.setLayoutManager(new LinearLayoutManager(context));
                                }else{
                                    dialog.ShowNotification("Kamu belum memesan apapun",false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case api.URL_GET_DETAIL_PESANAN_BY_PESANAN:
                            adapter = new GlobalAdapter(context,response.getJSONArray("data"),1);
                            activity = (Activity) context;

                            RecyclerView rv_detail = (RecyclerView) activity.findViewById(R.id.rv_detail);
                            rv_detail.setAdapter(adapter);
                            rv_detail.setLayoutManager(new LinearLayoutManager(context));
                            break;
                        case api.URL_GET_HISTORY:
                            error = response.getString("error");
                            try {
                                if (error.equals("E80")) {
                                    data_return = response.getJSONArray("data");

                                    if (old != null){
                                        int i = 0;
                                        while (i<data_return.length()){
                                            old.put(data_return.getJSONObject(i));
                                            i = i + 1;
                                        }
                                        data_return = old;
                                    }

                                    adapter = new GlobalAdapter(context, data_return,2);
                                    activity = (Activity) context;

                                    RecyclerView rv_pesanan = (RecyclerView) activity.findViewById(R.id.rv_pesanan);
                                    rv_pesanan.setAdapter(adapter);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    rv_pesanan.setLayoutManager(layoutManager);
                                    final Boolean[] isLoading = {false};

                                    if(canLoadMore){
                                        rv_pesanan.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                            @Override
                                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                super.onScrolled(recyclerView, dx, dy);

                                                if (layoutManager!=null && layoutManager.findLastCompletelyVisibleItemPosition()==data_return.length()-1){
                                                    if (!isLoading[0]){
                                                        isLoading[0] = true;
                                                        LoadMore(data_return, adapter, 10,
                                                                isLoading[0], new User(context).getUsername());
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    if (old != null){
                                        dialog.ShowNotification("Semua pesananmu sudah di tampilkan",false);
                                        canLoadMore = false;
                                    }else{
                                        dialog.ShowNotification("Kamu belum memesan apapun",false);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case api.URL_CANCEL:
                            error = response.getString("error");
                            if (error.equals("E90")){
                                activity = (Activity) context;
                                activity.finish();
                            }else{
                                dialog.ShowNotification("Pesananmu udah disiapin dan gak bisa dibatalkan nih.",false);
                            }
                            break;
                    }

                }catch (JSONException e){

                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(data);
    }

    private void LoadMore(JSONArray dataset, GlobalAdapter adapter, Integer lenght, Boolean status, String username) {
        try {
            this.old = new JSONArray(dataset.toString());
            //GET DATA

            JSONObject data = new JSONObject();
            try {
                data.put("username",username);
                data.put("start",old.length()+1);
                data.put("length",10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.StartProcess(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
