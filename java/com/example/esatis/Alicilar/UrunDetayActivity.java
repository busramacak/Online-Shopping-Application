package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.Model.Urunler;
import com.example.esatis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class UrunDetayActivity extends AppCompatActivity {

    private Button sepeteEkleBtn;
    private ImageView urun_img_detay;
    private ElegantNumberButton numberButton;
    private TextView urunFiyatiDetay, urunCesidiDetay, urunAdiDetay;
    private String urunID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detay);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }


        urunID = getIntent().getStringExtra("pid");
        sepeteEkleBtn = findViewById(R.id.urunSepeteEkleBtn);
        urun_img_detay = findViewById(R.id.urun_img_detay);
        numberButton = findViewById(R.id.numaraButon);
        urunFiyatiDetay = findViewById(R.id.urunFiyatiDetay);
        urunAdiDetay = findViewById(R.id.urunAdiDetay);
        urunCesidiDetay = findViewById(R.id.urunCesidiDetay);



        getUrunDetaylari(urunID);

        sepeteEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SepetListesineEkle();
            }
        });


    }


    private void SepetListesineEkle() {

        String tarihKaydet, zamanKaydet;
        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        tarihKaydet = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        zamanKaydet = currentTime.format(callForDate.getTime());

        DatabaseReference sepetListesiRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Sepet Listesi");

        final HashMap<String, Object> sepetMap  = new HashMap<>();
        sepetMap.put("pid", urunID);
        sepetMap.put("urunAdi", urunAdiDetay.getText().toString());
        sepetMap.put("fiyat", urunFiyatiDetay.getText().toString());
        sepetMap.put("tarih", tarihKaydet);
        sepetMap.put("saat", zamanKaydet);
        sepetMap.put("miktar", numberButton.getNumber());
        sepetMap.put("indirim", "");


        sepetListesiRef.child("Kullanici Ekrani").child(Mevcut.onlineKullanicilar.getNumara())
                .child("Urunler").child(urunID)
                .updateChildren(sepetMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            sepetListesiRef.child("Admin Ekrani").child(Mevcut.onlineKullanicilar.getNumara())
                                    .child("Urunler").child(urunID)
                                    .updateChildren(sepetMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            
                                            if(task.isSuccessful()){

                                                Toast.makeText(UrunDetayActivity.this, "Sepete Eklendi.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UrunDetayActivity.this, SepetActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getUrunDetaylari(String urunID) {
        DatabaseReference urunRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Urunler");
        urunRef.child(urunID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Urunler urunler = snapshot.getValue(Urunler.class);

                    urunAdiDetay.setText(urunler.getUrunAdi());
                    urunFiyatiDetay.setText(urunler.getFiyat());
                    urunCesidiDetay.setText(urunler.getCesit());
                    Picasso.get().load(urunler.getFotograf()).into(urun_img_detay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}