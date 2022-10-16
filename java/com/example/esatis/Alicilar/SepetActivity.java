package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.Model.Sepet;
import com.example.esatis.R;
import com.example.esatis.ViewHolder.SepetViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.esatis.R.color.*;

public class SepetActivity extends AppCompatActivity {


    private RecyclerView liste;
    private RecyclerView.LayoutManager layoutManager;
    private TextView toplam;
    private Button devam;
    public int topla = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepet);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(buton));
        }

        liste = findViewById(R.id.liste);
        liste.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        liste.setLayoutManager(layoutManager);

        toplam = findViewById(R.id.toplam);
        devam = findViewById(R.id.sepetOnayBtn);

        devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SepetActivity.this, SiparisOnayActivity.class);
                intent.putExtra("toplam", toplam.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference sepetListRef = FirebaseDatabase
                .getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference()
                .child("Sepet Listesi");

        final HashMap<String, Object> sepetMap  = new HashMap<>();

        FirebaseRecyclerOptions<Sepet> secenek =
                new FirebaseRecyclerOptions.Builder<Sepet>()
                .setQuery(sepetListRef.child("Kullanici Ekrani")
                        .child(Mevcut.onlineKullanicilar.getNumara())
                        .child("Urunler"), Sepet.class)
                        .build();

        FirebaseRecyclerAdapter<Sepet, SepetViewHolder> adapter =
                new FirebaseRecyclerAdapter<Sepet, SepetViewHolder>(secenek) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull SepetViewHolder sepetViewHolder, int i, @NonNull Sepet sepet) {


                        sepetViewHolder.txtUrunAdi.setText(sepet.getUrunAdi());
                        sepetViewHolder.txtUrunSayisi.setText(sepet.getMiktar());
                        sepetViewHolder.txtUrunFiyati.setText(String.valueOf(Integer.valueOf(sepet.getFiyat()) * Integer.valueOf(sepet.getMiktar())));



                        topla += ((Integer.valueOf(sepet.getFiyat())) * Integer.valueOf(sepet.getMiktar()));
                        toplam.setText(String.valueOf(topla));


                        sepetViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SepetActivity.this, UrunDetayActivity.class);
                                intent.putExtra("pid", sepet.getPid());
                                startActivity(intent);
                                topla =0;

                            }
                        });


                        sepetViewHolder.arti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Integer.valueOf(sepetViewHolder.txtUrunSayisi.getText().toString())== 10){
                                    Toast.makeText(SepetActivity.this, "Maksimum sayıya ulaştınız..", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    topla -= ((Integer.valueOf(sepet.getFiyat())) * Integer.valueOf(sepet.getMiktar()));

                                    sepetViewHolder.txtUrunSayisi.setText(String.valueOf(Integer.valueOf(String.valueOf(sepetViewHolder.txtUrunSayisi.getText()))+1));
                                    sepetMap.put("miktar",String.valueOf(sepetViewHolder.txtUrunSayisi.getText()));

                                    sepetListRef.child("Kullanici Ekrani")
                                            .child(Mevcut.onlineKullanicilar.getNumara())
                                            .child("Urunler")
                                            .child(sepet.getPid())
                                            .updateChildren(sepetMap);

                                    sepetViewHolder.txtUrunFiyati.setText(String.valueOf(Integer.valueOf(sepet.getFiyat())
                                            * Integer.valueOf(sepet.getMiktar())));

                                }
                            }
                        });


                        sepetViewHolder.eksi.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(View v) {

                                if(Integer.valueOf(sepetViewHolder.txtUrunSayisi.getText().toString())== 1){
                                    Toast.makeText(SepetActivity.this, "Minimum sayıya ulaştınız..", Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    sepetViewHolder.txtUrunSayisi.setText(String.valueOf(Integer.valueOf(String.valueOf(sepetViewHolder.txtUrunSayisi.getText()))-1));

                                    sepetMap.put("miktar",String.valueOf(sepetViewHolder.txtUrunSayisi.getText()));
                                    sepetListRef.child("Kullanici Ekrani")
                                            .child(Mevcut.onlineKullanicilar.getNumara())
                                            .child("Urunler")
                                            .child(sepet.getPid())
                                            .updateChildren(sepetMap);

                                    sepetViewHolder.txtUrunFiyati.setText(String.valueOf(Integer.valueOf(sepet.getFiyat())
                                            * Integer.valueOf(sepet.getMiktar())));

                                    topla -= (Integer.valueOf(sepet.getFiyat())
                                            * Integer.valueOf(sepet.getMiktar()));

                                }
                            }
                        });


                        sepetViewHolder.cop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(SepetActivity.this);
                                builder.setCancelable(false);
                                builder.setMessage("Ürünü silmek istediğinize emin misiniz?");

                                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        topla-= (Integer.valueOf(String.valueOf(sepetViewHolder.txtUrunFiyati.getText())));
                                        toplam.setText(String.valueOf(topla));

                                        sepetListRef.child("Kullanici Ekrani")
                                                .child(Mevcut.onlineKullanicilar.getNumara())
                                                .child("Urunler")
                                                .child(sepet.getPid())
                                                .removeValue();

                                        dialog.cancel();

                                    }
                                });

                                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SepetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sepet_item_layout, parent, false);
                        SepetViewHolder holder = new SepetViewHolder(view);
                        return holder;
                    }


                };

        liste.setAdapter(adapter);
        adapter.startListening();
    }
}