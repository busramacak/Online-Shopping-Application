package com.example.esatis.Alicilar;

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
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AyarlarActivity extends AppCompatActivity {

    private CircleImageView settings_profil_fotisi;
    private TextView closeBtn, updateBtn, profil_img_degistir_btn;
    private EditText isimSoyisim, telefon, adres;
    private Button guvSorusuBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask task;
    private StorageReference profilPictureRef;
    private String kontrol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        profilPictureRef = FirebaseStorage.getInstance().getReference().child("Profil Fotograflari");

        closeBtn = findViewById(R.id.close_settings);
        updateBtn = findViewById(R.id.update_account_settings);
        guvSorusuBtn = findViewById(R.id.guvSorusuBtn);
        settings_profil_fotisi = findViewById(R.id.settings_profil_fotisi);
        profil_img_degistir_btn = findViewById(R.id.profil_img_degistir_btn);
        isimSoyisim = findViewById(R.id.settings_adSoyad);
        telefon = findViewById(R.id.settings_telNo);
        adres = findViewById(R.id.settings_adres);


        kullaniciBilgiEkrani(settings_profil_fotisi,isimSoyisim,telefon,adres);



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AyarlarActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("<font color='#757474'>Kaydetmeden çıkmak istiyor musunuz?</font>"));

                builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>EVET</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#805BBC'>HAYIR</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(kontrol.equals("clicked")){
                    KullaniciBilgiKayit();
                    
                }
                else{
                    KullaniciBilgiGüncelle();

                }
            }
        });

        settings_profil_fotisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kontrol="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(AyarlarActivity.this);
            }
        });

        guvSorusuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AyarlarActivity.this, SifremiUnuttumActivity.class);
                intent.putExtra("kontrol", "ayarlar");
                startActivity(intent);

            }
        });
    }

    private void KullaniciBilgiGüncelle() {

        DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Kullanicilar");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("kullanici", isimSoyisim.getText().toString());
        userMap.put("adres", adres.getText().toString());
        userMap.put("numara", telefon.getText().toString());
        ref.child(Mevcut.onlineKullanicilar.getNumara()).updateChildren(userMap);

        startActivity(new Intent(AyarlarActivity.this, HomeActivity.class));
        Toast.makeText(AyarlarActivity.this, "Profil başarıyla güncellendi", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settings_profil_fotisi.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error, Tekrar Deneyin.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AyarlarActivity.this, AyarlarActivity.class));
            finish();
        }
    }


    private void KullaniciBilgiKayit() {

        if(TextUtils.isEmpty(isimSoyisim.getText().toString())){
            Toast.makeText(this, "İsim Soyisim Alanı Zorunlu.", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(adres.getText().toString())){
            Toast.makeText(this, "Adres Alanı Zorunlu.", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(telefon.getText().toString())){
            Toast.makeText(this, "Telefon Numarası Alanı Zorunlu.", Toast.LENGTH_SHORT).show();
        }
        else if(kontrol.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Profil Güncelleniyor");
        dialog.setMessage("Profil güncellenirken lütfen bekleyin");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(imageUri!= null){
            final StorageReference fileRef = profilPictureRef
                    .child(Mevcut.onlineKullanicilar.getNumara() + ".jpg");

            task = fileRef.putFile(imageUri);
            task.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Kullanicilar");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("kullanici", isimSoyisim.getText().toString());
                        userMap.put("adres", adres.getText().toString());
                        userMap.put("digerNumara", telefon.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Mevcut.onlineKullanicilar.getNumara()).updateChildren(userMap);

                        dialog.dismiss();
                        startActivity(new Intent(AyarlarActivity.this, HomeActivity.class));
                        Toast.makeText(AyarlarActivity.this, "Profil başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(AyarlarActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Fotograf seçili değil", Toast.LENGTH_SHORT).show();
        }
    }

    private void kullaniciBilgiEkrani(CircleImageView settings_profil_fotisi, EditText isimSoyisim, EditText telefon, EditText adress) {

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Kullanicilar").child(Mevcut.onlineKullanicilar.getNumara());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if(snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String isim = snapshot.child("kullanici").getValue().toString();
                        String tel = snapshot.child("numara").getValue().toString();
                        String adres = snapshot.child("adres").getValue().toString();

                        Picasso.get().load(image).into(settings_profil_fotisi);
                        isimSoyisim.setText(isim);
                        telefon.setText(tel);
                        adress.setText(adres);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#757474'>Ayarlar kaydedilmedi, çıkmak istediğinizden emin misiniz?</font>"));
        builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>Evet</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                Intent intent = new Intent(AyarlarActivity.this, HomeActivity.class);
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