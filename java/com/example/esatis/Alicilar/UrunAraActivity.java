package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.esatis.Model.Urunler;
import com.example.esatis.R;
import com.example.esatis.ViewHolder.UrunViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class UrunAraActivity extends AppCompatActivity {

    private Button araBtn;
    private EditText inputText;
    private RecyclerView aramaListesi;
    private String aramaInput="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ara);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        araBtn = findViewById(R.id.fab1);
        inputText = findViewById(R.id.ara);
        aramaListesi = findViewById(R.id.aramaListesi);
        aramaListesi.setLayoutManager(new LinearLayoutManager(UrunAraActivity.this));

        araBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aramaInput = inputText.getText().toString();

                onStart();
            }
        });

    }

    protected void onStart(){
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Urunler");

        FirebaseRecyclerOptions<Urunler> secenek  =
                new FirebaseRecyclerOptions.Builder<Urunler>()
                        .setQuery(ref.orderByChild("urunAdi").startAt(aramaInput), Urunler.class)
                        .build();

        FirebaseRecyclerAdapter<Urunler, UrunViewHolder> adapter = new FirebaseRecyclerAdapter<Urunler, UrunViewHolder>(secenek) {
            @Override
            protected void onBindViewHolder(@NonNull UrunViewHolder urunViewHolder, int i, @NonNull Urunler urunler) {

                urunViewHolder.txtUrunAdi.setText(urunler.getUrunAdi());
                urunViewHolder.txtUrunFiyati.setText("Fiyat = " + urunler.getFiyat() + "₺");
                urunViewHolder.txtUrunCesidi.setText(urunler.getCesit());
                Picasso.get().load(urunler.getFotograf()).into(urunViewHolder.imageView);

                urunViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(UrunAraActivity.this, UrunDetayActivity.class);
                        i.putExtra("pid",urunler.getPid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public UrunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urun_item_layout, parent, false);
                UrunViewHolder holder = new UrunViewHolder(view);
                return holder;

            }
        };
        aramaListesi.setAdapter(adapter);
        adapter.startListening();
    }

}