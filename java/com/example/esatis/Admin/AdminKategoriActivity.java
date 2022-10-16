package com.example.esatis.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.esatis.Alicilar.HomeActivity;
import com.example.esatis.Alicilar.MainActivity;
import com.example.esatis.R;

public class AdminKategoriActivity extends AppCompatActivity {

    private ImageView tisort, spor, elbise, dis_giyim;
    private ImageView gozluk, canta, sapka, ayakkabi;
    private ImageView kulaklik, bilgisayar, saat, telefon;
    private Button siparisKontrol, urunDuzenleBtn, cikisYap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kategori);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.sellerr));
        }

        tisort = findViewById(R.id.tisort);
        spor = findViewById(R.id.spor);
        elbise = findViewById(R.id.elbise);
        dis_giyim = findViewById(R.id.dis_giyim);

        gozluk = findViewById(R.id.gozluk);
        canta = findViewById(R.id.canta);
        sapka = findViewById(R.id.sapka);
        ayakkabi = findViewById(R.id.ayakkabi);

        kulaklik = findViewById(R.id.kulaklik);
        bilgisayar = findViewById(R.id.bilgisayar);
        saat = findViewById(R.id.saat);
        telefon = findViewById(R.id.telefon);

        siparisKontrol = findViewById(R.id.sipKontroluBtn);
        urunDuzenleBtn = findViewById(R.id.urunDuzenleBtn);
        cikisYap = findViewById(R.id.adminCikisBtn);

        tisort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","tisort");
                startActivity(intent);
            }
        });

        spor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","spor");
                startActivity(intent);
            }
        });

        elbise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","elbise");
                startActivity(intent);
            }
        });

        dis_giyim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","dis_giyim");
                startActivity(intent);
            }
        });

        gozluk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","gozluk");
                startActivity(intent);
            }
        });

        canta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","canta");
                startActivity(intent);
            }
        });

        sapka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","sapka");
                startActivity(intent);
            }
        });

        ayakkabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","ayakkabi");
                startActivity(intent);
            }
        });

        kulaklik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","kulaklik");
                startActivity(intent);
            }
        });

        bilgisayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","bilgisayar");
                startActivity(intent);
            }
        });

        saat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","saat");
                startActivity(intent);
            }
        });

        telefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","telefon");
                startActivity(intent);
            }
        });

        siparisKontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminKategoriActivity.this, AdminYeniSiparislerActivity.class);
                startActivity(intent);

            }
        });

        urunDuzenleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminKategoriActivity.this, HomeActivity.class);
                intent .putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });

        cikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminKategoriActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#757474'>BlueBerry</font>"));
                builder.setMessage(Html.fromHtml("<font color='#757474'>Çıkış yapmak istediğinizden emin misiniz?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>ÇIKIŞ YAP</font>"), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int arg1) {

                        Intent intent = new Intent(AdminKategoriActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#805BBC'>VAZGEÇ</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });



    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}