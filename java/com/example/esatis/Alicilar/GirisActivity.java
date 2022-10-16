package com.example.esatis.Alicilar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
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

import com.example.esatis.Admin.AdminKategoriActivity;
import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.Model.Kullanicilar;
import com.example.esatis.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class GirisActivity extends AppCompatActivity {

    public static  EditText telefonNumarasiGiris, sifreGiris;
    private Button giris;
    private ImageView giris_applogo;
    private ProgressDialog loadingBar;
    private TextView adminPanel, notAdmin;
    private TextView sifremiUnuttum;
    public static String no;
    private String parentDbName = "Kullanicilar";
    private CheckBox beniHatirla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        telefonNumarasiGiris = findViewById(R.id.girisTelefon_input);
        sifreGiris = findViewById(R.id.girisSifre_input);
        giris = findViewById(R.id.giris_buton);
        giris_applogo = findViewById(R.id.giris_applogo);
        adminPanel = findViewById(R.id.adminPanel);
        notAdmin = findViewById(R.id.notAdmin);
        sifremiUnuttum = findViewById(R.id.sifremiUnuttum);

        loadingBar = new ProgressDialog(this);
        beniHatirla = findViewById(R.id.hatirla);

        Paper.init(this);


        findViewById(R.id.r2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        beniHatirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        adminPanel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                window.setStatusBarColor(getResources().getColor(R.color.sellerr));
                Resources res = getResources();

                giris.setText("Admin Girişi");
                giris.setBackground(res.getDrawable(R.drawable.sellerbtn));
                telefonNumarasiGiris.setBackground(res.getDrawable(R.drawable.sellerinpt));
                telefonNumarasiGiris.setHintTextColor(getResources().getColor(R.color.sellerr));
                telefonNumarasiGiris.setTextColor(getResources().getColor(R.color.sellerr));
                sifreGiris.setBackground(res.getDrawable(R.drawable.sellerinpt));
                sifreGiris.setHintTextColor(getResources().getColor(R.color.sellerr));
                sifreGiris.setTextColor(getResources().getColor(R.color.sellerr));

                adminPanel.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                beniHatirla.setVisibility(View.INVISIBLE);
                sifremiUnuttum.setVisibility(View.INVISIBLE);


                giris_applogo.setImageDrawable(res.getDrawable(R.drawable.sellericon));
                parentDbName = "Adminler";
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.setStatusBarColor(getResources().getColor(R.color.sellerr));
                Resources res = getResources();

                giris.setText("Giriş Yap");
                giris.setBackground(res.getDrawable(R.drawable.buton));
                telefonNumarasiGiris.setBackground(res.getDrawable(R.drawable.input));
                telefonNumarasiGiris.setHintTextColor(getResources().getColor(R.color.buton));
                telefonNumarasiGiris.setTextColor(getResources().getColor(R.color.buton));
                sifreGiris.setBackground(res.getDrawable(R.drawable.input));
                sifreGiris.setHintTextColor(getResources().getColor(R.color.buton));
                sifreGiris.setTextColor(getResources().getColor(R.color.buton));

                giris_applogo.setImageDrawable(res.getDrawable(R.drawable.dd));
                adminPanel.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                beniHatirla.setVisibility(View.VISIBLE);
                sifremiUnuttum.setVisibility(View.VISIBLE);
                parentDbName = "Kullanicilar";
            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                KullaniciGirisi();
            }
        });

        sifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, SifremiUnuttumActivity.class);
                intent.putExtra("kontrol", "giris");
                startActivity(intent);
            }
        });
    }


    public void KullaniciGirisi() {

        String numara = telefonNumarasiGiris.getText().toString();
        String sifre = sifreGiris.getText().toString();

        if(TextUtils.isEmpty(numara)){

            Toast.makeText(this,"Lütfen numaranızı girin...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sifre)){

            Toast.makeText(this,"Lütfen sifrenizi girin...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Giriş Yapılıyor");
            loadingBar.setMessage("Bilgileriniz kontrol ediliyor.\nLütfen bekleyin.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            GirisDogrulama(numara, sifre);

        }

    }

    private void GirisDogrulama(String numara, String sifre) {

        if(beniHatirla.isChecked()){

            Paper.book().write(Mevcut.kullaniciTelefonAnahtar,numara);
            Paper.book().write(Mevcut.kullaniciSifreAnahtar, sifre);

        }
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase
                .getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(numara).exists()){

                    Kullanicilar usersData = snapshot.child(parentDbName).child(numara).getValue(Kullanicilar.class);

                    if(usersData.getNumara().equals(numara)){
                        if(usersData.getSifre().equals(sifre)){
                            if(parentDbName.equals("Adminler")){

                                Toast.makeText(GirisActivity.this, "Admin Girişi başarılı...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(GirisActivity.this, AdminKategoriActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Kullanicilar")){

                                Toast.makeText(GirisActivity.this, "Giriş başarılı...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(GirisActivity.this,HomeActivity.class);
                                Mevcut.onlineKullanicilar= usersData;
                                startActivity(intent);
                            }
                        }
                        else{

                            loadingBar.dismiss();
                            Toast.makeText(GirisActivity.this, "Parola hatalı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{

                        loadingBar.dismiss();
                        Toast.makeText(GirisActivity.this, "Telefon Numarası hatalı.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                    Toast.makeText(GirisActivity.this, "Bu numara ile kayıtlı hesap bulunmuyor...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}