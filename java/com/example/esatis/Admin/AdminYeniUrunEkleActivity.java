package com.example.esatis.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esatis.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminYeniUrunEkleActivity extends AppCompatActivity {

    private String KategoriIsmi,cesit, fiyat, uAdi,tarihKayit, saatKayit ;
    private Button yeniUrunEkleBtn;
    private ImageView urunResmiSec;
    private EditText urunAdi, urunCesidi, urunFiyati;
    private static final int galerisec = 1;
    private TextView kategori_ismi;
    private Uri resimUri;
    private String urunRandomKey;
    private String downloadImgUrl;
    private StorageReference urunResimRef;
    private DatabaseReference urunRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_yeni_urun_ekle);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.sellerr));
        }

        KategoriIsmi = getIntent().getExtras().get("kategori").toString();
        urunResimRef = FirebaseStorage.getInstance().getReference().child("Ürün Resimleri");
        urunRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Urunler");

        kategori_ismi = findViewById(R.id.kategori_ismi);
        kategori_ismi.setText("Kategori ismi: " + KategoriIsmi);
        loadingBar = new ProgressDialog(this);
        yeniUrunEkleBtn = findViewById(R.id.yeniUrunEkleBtn);
        urunResmiSec = findViewById(R.id.urunResmiSec);
        urunAdi = findViewById(R.id.urunAdi);
        urunCesidi = findViewById(R.id.urunCesidi);
        urunFiyati = findViewById(R.id.urunFiyati);

        findViewById(R.id.r9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        urunResmiSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriAc();
            }
        });

        yeniUrunEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerileriDogrula();
            }
        });


    }

    private void VerileriDogrula() {

        uAdi = urunAdi.getText().toString();
        cesit = urunCesidi.getText().toString();
        fiyat = urunFiyati.getText().toString();
        
        if(resimUri == null){
            Toast.makeText(this, "ürün resmi zorunlu...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(uAdi)){
            Toast.makeText(this, "Lütfen ürün adını yazınız..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cesit)){
            Toast.makeText(this, "Lütfen ürün çeşidini yazınız..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fiyat)){
            Toast.makeText(this, "Lütfen ürün fiyatını yazınız..", Toast.LENGTH_SHORT).show();
        }
        else{
            UrunBilgileriniDepola();
        }

    }

    private void UrunBilgileriniDepola() {

        loadingBar.setTitle("Yeni Ürün Ekleniyor");
        loadingBar.setMessage("Yeni ürün ekleniyor.\nLütfen bekleyin.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simdikiTarih = new SimpleDateFormat("dd-MM-yyyy");
        tarihKayit = simdikiTarih.format(calendar.getTime());
        SimpleDateFormat simdikiZaman = new SimpleDateFormat("HH:mm:ss a");
        saatKayit = simdikiZaman.format(calendar.getTime());

        urunRandomKey = tarihKayit + " | "+saatKayit;

        StorageReference dosyaYolu = urunResimRef.child(resimUri.getLastPathSegment() + urunRandomKey + ".jpg");
        final UploadTask up = dosyaYolu.putFile(resimUri);

        up.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String mesaj = e.toString();
                Toast.makeText(AdminYeniUrunEkleActivity.this, "Error:" + mesaj, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün fotoğrafı yüklendi...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImgUrl = dosyaYolu.getDownloadUrl().toString();
                        return dosyaYolu.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            downloadImgUrl = task.getResult().toString();
                            Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün fotoğraf url'i veritabanına kaydedildi...", Toast.LENGTH_SHORT).show();

                            UrunBilgileriniVeritabaninaKaydet();
                        }
                    }
                });
            }
        });

    }

    private void UrunBilgileriniVeritabaninaKaydet() {

        HashMap<String, Object> urunMap = new HashMap<>();
        urunMap.put("pid", urunRandomKey);
        urunMap.put("date", tarihKayit);
        urunMap.put("time", saatKayit);
        urunMap.put("cesit", cesit);
        urunMap.put("fotograf", downloadImgUrl);
        urunMap.put("kategori", KategoriIsmi);
        urunMap.put("fiyat", fiyat);
        urunMap.put("urunAdi", uAdi);

        urunRef.child(urunRandomKey).updateChildren(urunMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Intent intent = new Intent(AdminYeniUrunEkleActivity.this, AdminKategoriActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün Ekleme Başarılı..", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadingBar.dismiss();
                            String msj = task.getException().toString();
                            Toast.makeText(AdminYeniUrunEkleActivity.this, "Error:" + msj, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void GaleriAc() {
        Intent galeriIntent = new Intent();
        galeriIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeriIntent.setType("image/*");
        startActivityForResult(galeriIntent, galerisec);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Ayarlar kaydedilmedi çıkış yapmak istiyor musunuz?");

        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                finish();
            }
        });

        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == galerisec && resultCode == RESULT_OK && data!=null){

            resimUri = data.getData();
            urunResmiSec.setImageURI(resimUri);
        }
    }
}