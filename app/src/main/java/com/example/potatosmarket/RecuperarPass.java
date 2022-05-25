package com.example.potatosmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPass extends AppCompatActivity {
    Button btnEnviarCorreo;
    ImageView btnvolver;
    EditText edtEmail;
    private FirebaseAuth mAuth;
    ProgressDialog progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);
        btnvolver=findViewById(R.id.btn_volver_recuperar);
        btnEnviarCorreo=findViewById(R.id.btn_enviar_correo_recuperar);
        edtEmail=findViewById(R.id.edtemailrecuperar);
        mAuth = FirebaseAuth.getInstance();
        progreso = new ProgressDialog(this);
        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_Pass();
            }
        });
    }

    private void reset_Pass() {
        try {
            if(edtEmail.getText().toString()!=null){
                progreso = new ProgressDialog(this);
                progreso.show();
                progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progreso.setContentView(R.layout.custom_progressdialog);
                progreso.setCanceledOnTouchOutside(false);
                mAuth.setLanguageCode("es");
                mAuth.sendPasswordResetEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RecuperarPass.this, R.string.txt_correo_enviado, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RecuperarPass.this, R.string.txt_correo_noenviado, Toast.LENGTH_SHORT).show();
                        }
                        progreso.hide();
                    }
                });
            }else {
                Toast.makeText(this, R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, " "+e, Toast.LENGTH_SHORT).show();
        }
    }
}