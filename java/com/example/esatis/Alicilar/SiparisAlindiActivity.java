package com.example.esatis.Alicilar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.esatis.R;

import static com.example.esatis.R.color.buton;

public class SiparisAlindiActivity extends AppCompatActivity {

    private Button devam_etBtn, sipSyfBtn;
    private TextView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_alindi);


        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(buton));
        }

        devam_etBtn = findViewById(R.id.devam_etBtn);

        devam_etBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiparisAlindiActivity.this, HomeActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(SiparisAlindiActivity.this, HomeActivity.class));
    }

}