package com.example.esatis.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.esatis.Interface.ItemClickListener;
import com.example.esatis.R;


public class UrunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUrunAdi, txtUrunCesidi, txtUrunFiyati;

    public ImageView imageView;
    public ItemClickListener listener;

    public UrunViewHolder(View itemView) {

        super(itemView);

        imageView =  itemView.findViewById(R.id.urun_resmi);
        txtUrunAdi = itemView.findViewById(R.id.urun_adi);
        txtUrunCesidi = itemView.findViewById(R.id.urun_cesidi);
        txtUrunFiyati = itemView.findViewById(R.id.urun_fiyati);
    }


    public void setItemClickListener(ItemClickListener listener){

        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);

    }
}
