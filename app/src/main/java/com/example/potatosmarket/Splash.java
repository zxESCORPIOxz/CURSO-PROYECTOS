package com.example.potatosmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView logo;
    TextView logotexto,txtbienvenida;
    Button btn_omitir;
    Animation animation1,animation2,animation3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.imgLogoSplahs);
        logotexto = findViewById(R.id.txtLogoTextoSplahs);
        txtbienvenida = findViewById(R.id.txtBienvenida);
        btn_omitir = findViewById(R.id.btn_omitir);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.animacion1);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.animacion2);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.animation);
        logo.startAnimation(animation1);
        logotexto.startAnimation(animation2);
        txtbienvenida.startAnimation(animation3);
        final Timer timer = new Timer("MY_TIMER");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent( Splash.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        },3000);
        btn_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer.purge();
                Intent i = new Intent( Splash.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
}