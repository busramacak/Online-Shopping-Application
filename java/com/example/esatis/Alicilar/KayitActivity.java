package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esatis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {

    private EditText kullaniciAdi, telefonNumarasiKayit, sifreKayit;
    private Button kayit;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        kullaniciAdi = findViewById(R.id.kayitKullaniciadi_input);
        telefonNumarasiKayit = findViewById(R.id.kayitTelefon_input);
        sifreKayit = findViewById(R.id.kayitSifre_input);
        kayit = findViewById(R.id.kayit_buton);
        loadingBar = new ProgressDialog(this);

        findViewById(R.id.r3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });


        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
            
        });

    }

    private void CreateAccount() {

        String kulanici = kullaniciAdi.getText().toString();
        String numara = telefonNumarasiKayit.getText().toString();
        String sifre = sifreKayit.getText().toString();

        if(TextUtils.isEmpty(kulanici)){

            Toast.makeText(this,"Lütfen kullanıcı Adınızı girin...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(numara)){

            Toast.makeText(this,"Lütfen numaranızı girin...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sifre)){

            Toast.makeText(this,"Lütfen şifrenizi girin...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Üyelik Oluşturuluyor");
            loadingBar.setMessage("Bilgileriniz kontrol ediliyor.\nLütfen bekleyin.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            TelefonNumarasiDogrulama(kulanici, numara, sifre);
        }

    }

    private void TelefonNumarasiDogrulama(final String kullanici, final String numara, final String sifre) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Kullanicilar").child(numara).exists())){

                    HashMap<String, Object> kullanicidataMap = new HashMap<>();
                    kullanicidataMap.put("numara", numara);
                    kullanicidataMap.put("sifre", sifre);
                    kullanicidataMap.put("kullanici", kullanici);

                    RootRef.child("Kullanicilar").child(numara).updateChildren(kullanicidataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(KayitActivity.this, "Hesabınız oluşturuldu.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(KayitActivity.this, GirisActivity.class);
                                        startActivity(intent);
                                    }
                                    else{

                                        loadingBar.dismiss();
                                        Toast.makeText(KayitActivity.this, "Ağ Hatası: Lütfen daha sonra tekrar deneyin...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{

                    Toast.makeText(KayitActivity.this, numara + "Numarası zaten kayıtlı..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(KayitActivity.this,"Lütfen başka bir telefon numarası ile tekrar deneyin.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(KayitActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}