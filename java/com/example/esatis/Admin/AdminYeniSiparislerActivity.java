package com.example.esatis.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esatis.Model.AdminSiparisler;
import com.example.esatis.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminYeniSiparislerActivity extends AppCompatActivity {

    private RecyclerView recycler_menu1;

    private DatabaseReference sipRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_yeni_siparisler);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.sellerr));
        }

        sipRef = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Siparisler");

        recycler_menu1 = findViewById(R.id.recycler_menu1);
        recycler_menu1.setLayoutManager(new LinearLayoutManager(this));


    }

    public void onStart(){

        super.onStart();

        FirebaseRecyclerOptions<AdminSiparisler> secenek =
                new FirebaseRecyclerOptions.Builder<AdminSiparisler>()
                        .setQuery(sipRef, AdminSiparisler.class)
                        .build();


        FirebaseRecyclerAdapter<AdminSiparisler, AdminSiparisViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminSiparisler, AdminSiparisViewHolder>(secenek) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull AdminSiparisViewHolder adminSiparisViewHolder, int i, @NonNull AdminSiparisler adminSiparisler) {

                        adminSiparisViewHolder.sipKullaniciAdi.setText("Alıcı: " + adminSiparisler.getAdSoyad());
                        adminSiparisViewHolder.sipKullaniciNumara.setText(adminSiparisler.getTelNo());
                        adminSiparisViewHolder.sipAdres.setText(adminSiparisler.getTeslimatAdresi() + ", " + adminSiparisler.getIlce() + "/" + adminSiparisler.getIl());
                        adminSiparisViewHolder.sipTarihSaat.setText(adminSiparisler.getSaat() + "  "+ adminSiparisler.getTarih());
                        adminSiparisViewHolder.sipUrunFiyati.setText("Sipariş Tutarı: " + adminSiparisler.getToplamFiyat());

                        adminSiparisViewHolder.sipDetay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uid = getRef(i).getKey();
                                Intent intent = new Intent(AdminYeniSiparislerActivity.this, AdminKullaniciUrunActivity.class);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                            }
                        });


                        adminSiparisViewHolder.cop1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminYeniSiparislerActivity.this);
                                builder.setMessage(Html.fromHtml("<font color='#757474'>Bu sipariş ürünleri teslim edildi mi?</font>"));
                                builder.setPositiveButton(Html.fromHtml("<font color='#268E3C'>Evet</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {

                                        String uid = getRef(i).getKey();
                                        siparisSil(uid);

                                    }
                                });
                                builder.setNegativeButton(Html.fromHtml("<font color='#268E3C'>Hayır</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminSiparisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparisler_layout, parent, false);
                        AdminSiparisViewHolder holder = new AdminSiparisViewHolder(view);
                        return holder;
                    }
                };

        recycler_menu1.setAdapter(adapter);
        adapter.startListening();

    }

    private void siparisSil(String uid) {
        sipRef.child(uid).removeValue();

        DatabaseReference refref = FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Sepet Listesi").child("Admin Ekrani");
        refref.child(uid).removeValue();
    }

    public static class AdminSiparisViewHolder extends RecyclerView.ViewHolder{

        public TextView sipKullaniciAdi, sipKullaniciNumara, sipUrunFiyati, sipAdres, sipTarihSaat, sipDetay;
        public ImageView cop1;

        public AdminSiparisViewHolder(View itemView){
            super(itemView);


            sipKullaniciAdi = itemView.findViewById(R.id.sipKullaniciAdi);
            sipKullaniciNumara = itemView.findViewById(R.id.sipKullaniciNumara);
            sipUrunFiyati = itemView.findViewById(R.id.sipUrunFiyati);
            sipAdres = itemView.findViewById(R.id.sipAdres);
            sipTarihSaat = itemView.findViewById(R.id.sipTarihSaat);
            sipDetay = itemView.findViewById(R.id.sipDetay);
            cop1 = itemView.findViewById(R.id.cop1);
        }

    }
}