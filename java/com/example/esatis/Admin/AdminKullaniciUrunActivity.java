package com.example.esatis.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.esatis.Model.Sepet;
import com.example.esatis.R;
import com.example.esatis.ViewHolder.SepetViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminKullaniciUrunActivity extends AppCompatActivity {

    private RecyclerView recycler_menu2;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference sepetListRef;


    private  String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kullanici_urun);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.sellerr));
        }

        uid = getIntent().getStringExtra("uid");

        recycler_menu2 = findViewById(R.id.recycler_menu2);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu2.setLayoutManager(layoutManager);


        sepetListRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                .child("Sepet Listesi").child("Admin Ekrani").child(uid).child("Urunler");


    }

    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Sepet> secenek =
                new FirebaseRecyclerOptions.Builder<Sepet>()
                        .setQuery(sepetListRef, Sepet.class)
                        .build();

        Resources res = getResources();

        FirebaseRecyclerAdapter<Sepet, SepetViewHolder> adapter = new FirebaseRecyclerAdapter<Sepet, SepetViewHolder>(secenek) {

            @Override
            protected void onBindViewHolder(@NonNull SepetViewHolder sepetViewHolder, int i, @NonNull Sepet sepet) {

                sepetViewHolder.txtUrunAdi.setText(sepet.getUrunAdi());
                sepetViewHolder.txtUrunSayisi.setText("Adet: " + sepet.getMiktar());
                sepetViewHolder.txtUrunFiyati.setText(String.valueOf(Integer.valueOf(sepet.getFiyat()) * Integer.valueOf(sepet.getMiktar())));
                sepetViewHolder.eksi.setVisibility(View.INVISIBLE);
                sepetViewHolder.arti.setVisibility(View.INVISIBLE);
                sepetViewHolder.l2.setBackground(res.getDrawable(R.drawable.sellerinpt));
                sepetViewHolder.cop.setVisibility(View.INVISIBLE);


            }

            @NonNull
            @Override
            public SepetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sepet_item_layout, parent, false);
                SepetViewHolder holder = new SepetViewHolder(view);
                return holder;
            }
        };

        recycler_menu2.setAdapter(adapter);
        adapter.startListening();
    }
}