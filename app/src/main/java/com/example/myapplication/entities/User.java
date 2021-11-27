package com.example.myapplication.entities;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private String username, nama_pelanggan, email, no_hp, saldo;

    public User(String username, String nama_pelanggan, String email, String no_hp, String saldo) {
        this.username = username;
        this.nama_pelanggan = nama_pelanggan;
        this.email = email;
        this.no_hp = no_hp;
        this.saldo = saldo;
    }

    public User(Context context) {
        SharedPreferences session = context.getSharedPreferences("session",Context.MODE_PRIVATE);

        this.username = session.getString("username",null);
        this.nama_pelanggan = session.getString("nama_pelanggan",null);
        this.no_hp = session.getString("no_hp",null);
        this.email = session.getString("email",null);
        this.saldo = session.getString("saldo",null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setSession(Context context) {
        SharedPreferences session = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  session.edit();

        editor.clear();

        editor.putString("username", this.username);
        editor.putString("nama_pelanggan", this.nama_pelanggan);
        editor.putString("no_hp", this.no_hp);
        editor.putString("email", this.email);
        editor.putString("saldo", String.valueOf(this.saldo));
        editor.apply();
    }

    public void clearSession(Context context) {
        SharedPreferences session = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  session.edit();

        editor.clear();
        editor.apply();
    }
}
