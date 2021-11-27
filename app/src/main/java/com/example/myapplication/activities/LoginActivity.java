package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.myapplication.activities.fragments.LoginFragment;
import com.example.myapplication.entities.User;
import com.example.myapplication.services.MyFirebaseMessagingService;
import com.example.myapplication.R;
import com.example.myapplication.activities.fragments.RegisterFragment;
import com.example.myapplication.services.ServerAccess;
import com.example.myapplication.services.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User user = new User(this);
        user.clearSession(this);

        //clearing token when this user open this activity
        try {
            MyFirebaseMessagingService messagingService = new MyFirebaseMessagingService();
            JSONObject data = new JSONObject();

            data.put("fcm_token", messagingService.getToken(this));

            ServerAccess serverAccess = new ServerAccess(this, api.URL_LOGOUT,"Loading");
            serverAccess.StartProcess(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ViewPager viewPager = findViewById(R.id.viewPager);

        LoginActivity.AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    static class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}