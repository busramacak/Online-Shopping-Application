package com.example.esatis.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esatis.Interface.ItemClickListener;
import com.example.esatis.R;

public class SepetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUrunAdi, txtUrunSayisi, txtUrunFiyati, arti, eksi;
    public ImageView cop;
    public LinearLayout l2;

    public ItemClickListener listener;

    public SepetViewHolder(@NonNull View itemView) {
        super(itemView);

        txtUrunAdi = itemView.findViewById(R.id.urunAdiSepet);
        txtUrunSayisi = itemView.findViewById(R.id.urunSayisiSepet);
        txtUrunFiyati = itemView.findViewById(R.id.urunFiyatiSepet);
        arti = itemView.findViewById(R.id.artiBtn);
        eksi = itemView.findViewById(R.id.eksiBtn);
        cop = itemView.findViewById(R.id.cop);
        l2 = itemView.findViewById(R.id.l2);
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.listener = itemClickListener;
    }
}
