package com.example.myapplication.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.activities.DetailPesananActivity;
import com.example.myapplication.activities.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("new token", "onNewToken: "+s);

        SharedPreferences token = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor =  token.edit();
        editor.putString("token", s);
        editor.apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String , String> data = remoteMessage.getData();
        Integer type = Integer.valueOf(data.get(new String("type")));
        switch (type){
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(data.get(new String("data")));
                    statusUpdateNotification(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                forceLogoutNotification();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case 3:
                antrianStatusNotification(data.get(new String("data")));
                break;
            default:
                Log.d("default", "onMessageReceived: Default");
                break;
        }
    }

    public String getToken(Context context){
        SharedPreferences token = context.getSharedPreferences("token", MODE_PRIVATE);
        return token.getString("token",null);
    }

    public void statusUpdateNotification(JSONObject pesanan) throws JSONException {
        Intent intent = new Intent(this, DetailPesananActivity.class);
        intent.putExtra("pesanan",pesanan.toString());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.app.Notification notification = new NotificationCompat.Builder(this, Notification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_fried_rice)
                .setContentTitle("Pesanan")
                .setContentText(getMessage(pesanan.getInt("status")))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1,notification);
    }

    public void forceLogoutNotification(){
        android.app.Notification notification = new NotificationCompat.Builder(this,Notification.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_fried_rice)
                .setContentTitle("Logout")
                .setContentText("Akun anda telah login di perangkat lain")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2,notification);
    }

    public void antrianStatusNotification(String status){
        String title, msg;
        if (status.equals("1")){
            title = "Restoran buka";
            msg = "Sudah bisa pesan makanan ya";
        }else{
            title = "Restoran tutup";
            msg = "Udah gak bisa mesan makanan lagi";
        }

        android.app.Notification notification = new NotificationCompat.Builder(this,Notification.CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_fried_rice)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(3,notification);
    }

    public String getMessage(Integer status){
        switch (status){
            case 2:{
                return "Pesananmu mulai disiapkan ya.";
            }
            case 3:{
                return "Pesananmu sudah siap, lagi nunggu dijemput.";
            }
            case 4:{
                return "Pesananmu sudah diambil.";
            }
            default:{
                return "Pesananmu dibatalkan.";
            }
        }
    }
}
