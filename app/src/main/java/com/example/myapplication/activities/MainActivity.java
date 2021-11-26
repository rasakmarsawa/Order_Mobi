 package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.entities.User;
import com.example.myapplication.services.LoadingDialogBar;
import com.example.myapplication.R;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

 public class MainActivity extends AppCompatActivity {
    TextView saldo;
    LoadingDialogBar dialog;
    User user;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saldo = (TextView) findViewById(R.id.tv_saldo);
        dialog = new LoadingDialogBar(this);
        user = new User(this);

        CardView cv_order = (CardView) findViewById(R.id.cv_order);
        CardView cv_list = (CardView) findViewById(R.id.cv_list);
        CardView cv_logout = (CardView) findViewById(R.id.cv_logout);

         saldo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 reloadData("Loading");
             }
         });

         cv_order.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ServerAccess serverAccess = new ServerAccess(
                         v.getContext(),
                         api.URL_GET_ANTRIAN_STATUS,
                         "Loading");
                 serverAccess.StartProcess(new JSONObject());
             }
         });

         cv_list.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(v.getContext(), ListActivity.class);
                 startActivity(intent);
             }
         });

         cv_logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.ShowLogout();
             }
         });
    }

     private boolean checkLogin() {
         if (user.getUsername()==null){
             //no login
             Intent intent = new Intent(this, LoginActivity.class);
             startActivity(intent);
             finish();

             return false;

         }else{
             return true;

         }
     }

     private void reloadData(String dialog_msg) {
         try {
             JSONObject data = new JSONObject();
             data.put("username",user.getUsername());

             ServerAccess serverAccess = new ServerAccess(
                     this,
                     api.URL_RELOAD_USER_DATA,
                     dialog_msg);
             serverAccess.StartProcess(data);

         } catch (JSONException e) {
             e.printStackTrace();
         }
     }

     @Override
     protected void onResume() {
         super.onResume();
         if (checkLogin()){
             reloadData("Loading");
         }
     }
 }