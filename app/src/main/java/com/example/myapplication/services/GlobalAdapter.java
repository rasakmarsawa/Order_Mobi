package com.example.myapplication.services;

import static com.example.myapplication.services.api.ROOT_IMAGES;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.DetailPesananActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalAdapter extends RecyclerView.Adapter {
    Context context;
    JSONArray data;
    Integer type;

    public GlobalAdapter(Context context, JSONArray data, Integer type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType){
            case 1: {
                view = layoutInflater.inflate(R.layout.confirmrow, parent, false);
                return new confirmrowHolder(view);
            }
            case 2: {
                view = layoutInflater.inflate(R.layout.pesananrow, parent, false);
                return new pesananrowHolder(view);
            }
            default: {
                view = layoutInflater.inflate(R.layout.barangrow, parent, false);
                return new barangrowHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            switch (type){
                case 1: //list konfirmasi pesanan
                    confirmrowHolder confirmrow = (confirmrowHolder) holder;
                    try {
                        int j = data.getJSONObject(position).getInt("jumlah_barang");

                        if (j!=0){
                            int harga = j*data.getJSONObject(position).getInt("harga");

                            confirmrow.tvk_jumlah.setText(String.valueOf(j));
                            confirmrow.tvk_nama_barang.setText(data.getJSONObject(position).getString("nama_barang"));
                            confirmrow.tvk_harga.setText("Rp. "+String.valueOf(harga));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://list pesanan
                    JSONObject pesanan = data.getJSONObject(position);

                    pesananrowHolder pesananrow = (pesananrowHolder) holder;
                    pesananrow.tv_id.setText(pesanan.getString("tanggal")+" / "+pesanan.get("no"));
                    pesananrow.tv_total_harga.setText("Rp. "+pesanan.getString("total_harga"));
                    if (pesanan.getInt("status")==5){
                        pesananrow.iv_pesanan.setImageResource(R.drawable.fail);
                    }else{
                        pesananrow.iv_pesanan.setImageResource(R.drawable.ic_order);
                    }

                    pesananrow.cv_pesanan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DetailPesananActivity.class);
                            intent.putExtra("pesanan",pesanan.toString());
                            context.startActivity(intent);
                        }
                    });
                    break;
                default: //list barang
                    JSONObject barang = data.getJSONObject(position);

                    barangrowHolder barangrow = (barangrowHolder) holder;
                    barangrow.tv_nama_barang.setText(barang.getString("nama_barang"));
                    barangrow.tv_harga.setText("Rp. "+barang.getString("harga"));
                    String x = ROOT_IMAGES
                            + barang.getString("id_barang")
                            + "."
                            + barang.getString("type");
                    Glide.with(context).load(x).into(barangrow.iv_barang);

                    barangrow.tv_jumlah.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            try {
                                barang.remove("jumlah_barang");
                                barang.put("jumlah_barang",Integer.valueOf(String.valueOf(s)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
            }
        }catch (JSONException e){

        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    private class barangrowHolder extends RecyclerView.ViewHolder {
        TextView tv_nama_barang, tv_harga, tv_jumlah;
        ImageView iv_barang, iv_plus, iv_minus;

        public barangrowHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama_barang = itemView.findViewById(R.id.tv_nama_barang);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_jumlah = itemView.findViewById(R.id.tv_jumlah);
            iv_barang = itemView.findViewById(R.id.iv_barang);
            iv_plus = itemView.findViewById(R.id.iv_plus);
            iv_minus = itemView.findViewById(R.id.iv_minus);

            iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = Integer.valueOf(String.valueOf(tv_jumlah.getText()))+1;
                    tv_jumlah.setText(String.valueOf(a));
                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = Integer.valueOf(String.valueOf(tv_jumlah.getText()))-1;
                    if (a>=0){
                        tv_jumlah.setText(String.valueOf(a));
                    }
                }
            });
        }
    }

    private class confirmrowHolder extends RecyclerView.ViewHolder {
        TextView tvk_nama_barang, tvk_jumlah, tvk_harga;

        public confirmrowHolder(@NonNull View itemView) {
            super(itemView);
            tvk_nama_barang = itemView.findViewById(R.id.tvk_nama_barang);
            tvk_jumlah = itemView.findViewById(R.id.tvk_jumlah);
            tvk_harga = itemView.findViewById(R.id.tvk_harga);
        }
    }

    private class pesananrowHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_total_harga;
        CardView cv_pesanan;
        ImageView iv_pesanan;

        public pesananrowHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_total_harga = (TextView) itemView.findViewById(R.id.tv_total_harga);
            cv_pesanan = (CardView) itemView.findViewById(R.id.cv_pesanan);
            iv_pesanan = (ImageView) itemView.findViewById(R.id.iv_pesanan);
        }
    }
}