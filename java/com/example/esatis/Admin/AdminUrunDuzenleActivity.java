package com.example.esatis.Admin;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.esatis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminUrunDuzenleActivity extends AppCompatActivity {

    private ImageView urun_resmi_duzenle;
    private EditText urun_adi_duzenle, urun_fiyati_duzenle, urun_cesidi_duzenle;
    private Button degisikOnayBtn, urunuSilBtn;
    private DatabaseReference urunRef;
    private String urunID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_urun_duzenle);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.sellerr));
        }

        urunID = getIntent().getStringExtra("pid");
        urunRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Urunler").child(urunID);


        urun_resmi_duzenle = findViewById(R.id.urun_resmi_duzenle);
        urun_adi_duzenle = findViewById(R.id.urun_adi_duzenle);
        urun_fiyati_duzenle = findViewById(R.id.urun_fiyati_duzenle);
        urun_cesidi_duzenle = findViewById(R.id.urun_cesidi_duzenle);
        degisikOnayBtn = findViewById(R.id.degisikOnayBtn);
        urunuSilBtn = findViewById(R.id.urunuSilBtn);


        urunBigileriniGoster();

        degisikOnayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                degisiklikleriOnayla();
            }
        });

        urunuSilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUrunDuzenleActivity.this);
                builder.setMessage(Html.fromHtml("<font color='#757474'>Ürünü silmek istediğinizden emin misiniz?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#0091EA'>ÜRÜNÜ SİL</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        urunuSil();
                    }
                });
                builder.setNegativeButton(Html.fromHtml("<font color='#0091EA'>VAZGEÇ</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


    }

    private void urunuSil() {

        urunRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminUrunDuzenleActivity.this, AdminKategoriActivity.class);
                startActivity(intent);
                Toast.makeText(AdminUrunDuzenleActivity.this, "Ürün Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private void degisiklikleriOnayla() {

        String uAdi = urun_adi_duzenle.getText().toString();
        String uFiyat = urun_fiyati_duzenle.getText().toString();
        String uCesit = urun_cesidi_duzenle.getText().toString();
        
        if(uAdi.equals("")){
            Toast.makeText(this, "Ürün adını yazın.", Toast.LENGTH_SHORT).show();
        }
        else if(uFiyat.equals("")){
            Toast.makeText(this, "Ürün fiyatını yazın.", Toast.LENGTH_SHORT).show();
        }
        else if(uCesit.equals("")){
            Toast.makeText(this, "Ürün çeşidini yazın.", Toast.LENGTH_SHORT).show();
        }
        else{

            HashMap<String, Object> urunMap = new HashMap<>();
            urunMap.put("pid", urunID);
            urunMap.put("cesit", uCesit);
            urunMap.put("fiyat", uFiyat);
            urunMap.put("urunAdi", uAdi);

            urunRef.updateChildren(urunMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Intent intent = new Intent(AdminUrunDuzenleActivity.this, AdminKategoriActivity.class);
                        startActivity(intent);
                        Toast.makeText(AdminUrunDuzenleActivity.this, "Ürün başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });

        }
    }

    private void urunBigileriniGoster() {

        urunRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String uAdi = snapshot.child("urunAdi").getValue().toString();
                    String uFiyat = snapshot.child("fiyat").getValue().toString();
                    String uCesit = snapshot.child("cesit").getValue().toString();
                    String uFotograf = snapshot.child("fotograf").getValue().toString();

                    urun_adi_duzenle.setText(uAdi);
                    urun_fiyati_duzenle.setText(uFiyat);
                    urun_cesidi_duzenle.setText(uCesit);
                    Picasso.get().load(uFotograf).into(urun_resmi_duzenle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}