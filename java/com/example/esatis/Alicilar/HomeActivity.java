package com.example.esatis.Alicilar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.esatis.Admin.AdminKategoriActivity;
import com.example.esatis.Admin.AdminUrunDuzenleActivity;
import com.example.esatis.Mevcut.Mevcut;
import com.example.esatis.Model.Urunler;
import com.example.esatis.R;
import com.example.esatis.ViewHolder.UrunViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference urunRef;
    private RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    private String tip ="";

    @SuppressLint({"ResourceType", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Paper.init(this);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.buton));
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!= null){
            tip = getIntent().getExtras().get("Admin").toString();
        }

        urunRef= FirebaseDatabase.getInstance("https://esatis-6ee67-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Urunler");


        FloatingActionButton fab = findViewById(R.id.fab);

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!tip.equals("Admin")){

                    Intent i = new Intent(HomeActivity.this, SepetActivity.class);
                    startActivity(i);
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.kullanici_profil_adi);
        CircleImageView profileImageView = headerView.findViewById(R.id.kullanici_profile_image);

        if(!tip.equals("Admin")){

            userNameTextView.setText(Mevcut.onlineKullanicilar.getKullanici());
            Picasso.get().load(Mevcut.onlineKullanicilar.getImage()).into(profileImageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Urunler> secenek = new FirebaseRecyclerOptions.Builder<Urunler>()
                .setQuery(urunRef, Urunler.class).build();

        FirebaseRecyclerAdapter<Urunler, UrunViewHolder > adapter =
                new FirebaseRecyclerAdapter<Urunler, UrunViewHolder>(secenek ) {
            @Override
            protected void onBindViewHolder(@NonNull UrunViewHolder urunViewHolder, int i, @NonNull Urunler urunler) {
                urunViewHolder.txtUrunAdi.setText(urunler.getUrunAdi());
                urunViewHolder.txtUrunFiyati.setText("Fiyat = " + urunler.getFiyat() + "₺");
                urunViewHolder.txtUrunCesidi.setText(urunler.getCesit());
                Picasso.get().load(urunler.getFotograf()).into(urunViewHolder.imageView);


                urunViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(tip.equals("Admin")){
                            Intent i = new Intent(HomeActivity.this, AdminUrunDuzenleActivity.class);
                            i.putExtra("pid",urunler.getPid());
                            startActivity(i);
                        }
                        else{

                            Intent i = new Intent(HomeActivity.this, UrunDetayActivity.class);
                            i.putExtra("pid",urunler.getPid());
                            startActivity(i);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public UrunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urun_item_layout, parent, false);
                UrunViewHolder holder = new UrunViewHolder(view);
                return holder;
            }
        };

        recycler_menu.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed(){

        if(!tip.equals("Admin")){
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(Gravity.LEFT);
        }
        else{
            startActivity(new Intent(HomeActivity.this, AdminKategoriActivity.class));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        int id = item.getItemId();

//        if(id == R.id.action_settings){
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.nav_sepet){

            if(!tip.equals("Admin")){
                Intent i = new Intent(HomeActivity.this, SepetActivity.class);
                startActivity(i);
            }
        }
        else if(id == R.id.nav_ara){

            if(!tip.equals("Admin")){
                Intent intent = new Intent(HomeActivity.this, UrunAraActivity.class);
                startActivity(intent);
            }
        }

        else if(id == R.id.nav_settings){

            if(!tip.equals("Admin")){
                Intent intent = new Intent(HomeActivity.this, AyarlarActivity.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.nav_cikis){

            if(!tip.equals("Admin")){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#757474'>BlueBerry</font>"));
                builder.setMessage(Html.fromHtml("<font color='#757474'>Çıkış yapmak istediğinizden emin misiniz?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#805BBC'>ÇIKIŞ YAP</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        Paper.book().destroy();

                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}