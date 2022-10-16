package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.esatis.R.color.buton;

public class SiparisOnayActivity extends AppCompatActivity {

    private EditText aliciAdSoyad, aliciTelefon, teslimatAdresi, il, ilce;
    private TextView toplamFiyat;
    private Button adresOnayBtn;
    private String ToplamFiyat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_onay);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(buton));
        }

        ToplamFiyat = getIntent().getStringExtra("toplam");

        aliciAdSoyad = findViewById(R.id.aliciAdSoyad);
        aliciTelefon = findViewById(R.id.aliciTelefon);
        teslimatAdresi = findViewById(R.id.teslimatAdresi);
        toplamFiyat = findViewById(R.id.toplm);
        toplamFiyat.setText(ToplamFiyat);
        il = findViewById(R.id.il);
        ilce = findViewById(R.id.ilce);
        adresOnayBtn = findViewById(R.id.adresOnayBtn);



        findViewById(R.id.r12).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });


        adresOnayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kontrol();
            }
        });


    }

    private void Kontrol() {

        if(TextUtils.isEmpty(aliciAdSoyad.getText().toString())){

            Toast.makeText(this, "Ad Soyad boş bırakılamaz..", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(aliciTelefon.getText().toString())){

            Toast.makeText(this, "Telefon numarası boş bırakılamaz..", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(teslimatAdresi.getText().toString())){

            Toast.makeText(this, "Adres boş bırakılamaz..", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(il.getText().toString())){

            Toast.makeText(this, "İl boş bırakılamaz..", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(ilce.getText().toString())){

            Toast.makeText(this, "İlçe boş bırakılamaz..", Toast.LENGTH_SHORT).show();

        }else{
            SiparisOnay();
        }
    }

    private void SiparisOnay() {

        final String tarihText, saatText;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat tarih = new SimpleDateFormat("dd/MM/yyyy");
        tarihText = tarih.format(calForDate.getTime());

        SimpleDateFormat saat = new SimpleDateFormat("HH:mm:ss");
        saatText = saat.format(calForDate.getTime());

        final DatabaseReference siparisRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                .child("Siparisler")
                .child(Mevcut.onlineKullanicilar.getNumara());

        HashMap<String,Object> siparisMap = new HashMap<>();
        siparisMap.put("toplamFiyat", ToplamFiyat);
        siparisMap.put("adSoyad",aliciAdSoyad.getText().toString());
        siparisMap.put("telNo",aliciTelefon.getText().toString());
        siparisMap.put("teslimatAdresi",teslimatAdresi.getText().toString());
        siparisMap.put("il",il.getText().toString());
        siparisMap.put("ilce",ilce.getText().toString());
        siparisMap.put("tarih",tarihText);
        siparisMap.put("saat",saatText);
        siparisMap.put("durum","Sipariş Alındı");


        siparisRef.updateChildren(siparisMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                            .child("Sepet Listesi")
                            .child("Kullanici Ekrani")
                            .child(Mevcut.onlineKullanicilar.getNumara())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(SiparisOnayActivity.this, "Sipariş alındı", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SiparisOnayActivity.this, SiparisAlindiActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#757474'>Sipariş tamamlanmadı, çıkmak istediğinizden emin misiniz?</font>"));
        builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>Evet</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                Intent intent = new Intent(SiparisOnayActivity.this, SepetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#805BBC'>Hayır</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}