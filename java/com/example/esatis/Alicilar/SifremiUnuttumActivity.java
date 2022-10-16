package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SifremiUnuttumActivity extends AppCompatActivity {

    private String kontrol ="";
    private TextView baslik, guvSorusuText;
    private EditText sifremiUnuttum_telNo, soru1, soru2;
    private Button sbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }


        kontrol = getIntent().getStringExtra("kontrol");


        baslik = findViewById(R.id.baslik);
        guvSorusuText = findViewById(R.id.guvSorusuText);
        sifremiUnuttum_telNo = findViewById(R.id.sifremiUnuttum_telNo);
        soru1 = findViewById(R.id.soru1);
        soru2 = findViewById(R.id.soru2);
        sbtn = findViewById(R.id.sbtn);



        findViewById(R.id.r10).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        findViewById(R.id.r11).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        sifremiUnuttum_telNo.setVisibility(View.GONE);

        if(kontrol.equals("ayarlar")){

            baslik.setText("Güvenlik Sorularını Düzenle");
            guvSorusuText.setText("Lütfen aşağıdaki güvenlik sorularını düzenleyiniz.");
            sbtn.setText("Değiştir");

            eskiCevaplariGoruntule();

            sbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    guvenlikSorulariniDuzenle();

                }
            });

        }
        else if(kontrol.equals("giris")){

            sifremiUnuttum_telNo.setVisibility(View.VISIBLE);

            sbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    kullaniciOnayla();
                }
            });

        }
    }

    private void kullaniciOnayla() {

        String tel = sifremiUnuttum_telNo.getText().toString();
        String cevap1 = soru1.getText().toString().toLowerCase();
        String cevap2 = soru2.getText().toString().toLowerCase();


        if(!tel.equals("") && !cevap1.equals("") && !cevap2.equals("")){

            DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                    .child("Kullanicilar")
                    .child(tel);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        
                        if(snapshot.hasChild("Guvenlik Sorulari")){

                            String cvp1 = snapshot.child("Guvenlik Sorulari").child("cevap1").getValue().toString();
                            String cvp2 = snapshot.child("Guvenlik Sorulari").child("cevap2").getValue().toString();

                            if(!cvp1.equals(cevap1)){
                                Toast.makeText(SifremiUnuttumActivity.this, "1. cevabınız yanlış..", Toast.LENGTH_SHORT).show();
                            }
                            else if(!cvp2.equals(cevap2)){
                                Toast.makeText(SifremiUnuttumActivity.this, "2. cevabınız yanlış..", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                final EditText yeniSif = new EditText(SifremiUnuttumActivity.this);
                                yeniSif.setHint("Yeni şifrenizi girin.");
                                yeniSif.setGravity(Gravity.CENTER);

                                AlertDialog.Builder builder = new AlertDialog.Builder(SifremiUnuttumActivity.this);
                                builder.setTitle(Html.fromHtml("<font color='#757474'>Yeni Şifre Oluştur</font>"));
                                builder.setView(yeniSif);

                                builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>Değiştir</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {

                                        if(!yeniSif.getText().toString().equals("")){

                                            ref.child("sifre").setValue(yeniSif.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Toast.makeText(SifremiUnuttumActivity.this, "Şifre başarıyla değiştirildi", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(SifremiUnuttumActivity.this, GirisActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton(Html.fromHtml("<font color='#805BBC'>Vazgeç</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }
                        else{
                            Toast.makeText(SifremiUnuttumActivity.this, "Güvenlik soruları eklenmemiş..", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SifremiUnuttumActivity.this, "Bu numaraya ait bir kullanıcı mevcut değil..", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void guvenlikSorulariniDuzenle() {

        String cevap1 = soru1.getText().toString().toLowerCase();
        String cevap2 = soru2.getText().toString().toLowerCase();

        if(soru1.equals("") && soru2.equals("")){

            Toast.makeText(SifremiUnuttumActivity.this, "Lütfen her iki soruyu da cevaplayın..", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                    .child("Kullanicilar")
                    .child(Mevcut.onlineKullanicilar.getNumara());

            HashMap<String, Object> kullanicidataMap = new HashMap<>();
            kullanicidataMap.put("cevap1", cevap1);
            kullanicidataMap.put("cevap2", cevap2);

            ref.child("Guvenlik Sorulari").updateChildren(kullanicidataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SifremiUnuttumActivity.this, "Güvenlik soruları başarıyla değiştirildi..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SifremiUnuttumActivity.this, HomeActivity.class));
                            }
                        }
                    });

        }
    }

    private void eskiCevaplariGoruntule(){

        DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("Kullanicilar")
                .child(Mevcut.onlineKullanicilar.getNumara());

        ref.child("Guvenlik Sorulari").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String cvp1 = snapshot.child("cevap1").getValue().toString();
                    String cvp2 = snapshot.child("cevap2").getValue().toString();

                    soru1.setText(cvp1);
                    soru2.setText(cvp2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}