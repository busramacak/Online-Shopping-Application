package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.Model.Kullanicilar;
import com.example.esatis.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button anaGirisYap, anaUyeOl;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        anaGirisYap = findViewById(R.id.ana_giris_buton);
        anaUyeOl = findViewById(R.id.ana_uyeOl_buton);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        anaGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GirisActivity.class);
                startActivity(intent);
            }
        });

        anaUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KayitActivity.class);
                startActivity(intent);

            }
        });


        String kullaniciTelefonAnahtar = Paper.book().read(Mevcut.kullaniciTelefonAnahtar);
        String kullaniciSifreAnahtar = Paper.book().read(Mevcut.kullaniciSifreAnahtar);

        if(kullaniciTelefonAnahtar!= "" && kullaniciSifreAnahtar != ""){

            if(!TextUtils.isEmpty(kullaniciTelefonAnahtar) && !TextUtils.isEmpty(kullaniciSifreAnahtar)){

                ErismeIzni(kullaniciTelefonAnahtar,kullaniciSifreAnahtar);

                loadingBar.setTitle("Zaten Giriş Yapılmış.");
                loadingBar.setMessage("Lütfen bekleyin....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }

        }
    }

    private void ErismeIzni(String numara, String sifre) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Kullanicilar").child(numara).exists()){

                    Kullanicilar usersData = snapshot.child("Kullanicilar").child(numara).getValue(Kullanicilar.class);

                    if(usersData.getNumara().equals(numara)) {
                        if (usersData.getSifre().equals(sifre)) {

                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Mevcut.onlineKullanicilar = usersData;
                            startActivity(intent);
                        } else {

                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Parola hatalı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{

                    Toast.makeText(MainActivity.this, "Bu numara ile kayıtlı hesap bulunmuyor...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}