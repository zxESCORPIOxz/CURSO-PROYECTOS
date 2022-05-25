package com.example.potatosmarket;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button btningresar, btnregistrar, btnrecuperar;
    EditText edtEmail,edtContraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btningresar = findViewById(R.id.btn_ingresar_login);
        btnregistrar = findViewById(R.id.btn_registrar_login);
        btnrecuperar = findViewById(R.id.btn_recuperar_login);
        edtEmail = findViewById(R.id.edtemaillogin);
        edtContraseña = findViewById(R.id.edtpasslogin);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario();
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Login.this, RegistrarUsuario.class);
                startActivity(i);
            }
        });
        btnrecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Login.this, RecuperarPass.class);
                startActivity(i);
            }
        });
    }
    public void validarUsuario(){
        if (edtEmail.getText().length()!=0 && edtContraseña.getText().length()!=0) {
            ProgressDialog progreso = new ProgressDialog(this);
            progreso.show();
            progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progreso.setContentView(R.layout.custom_progressdialog);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    edtEmail.getText().toString(),
                    edtContraseña.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progreso.hide();
                        Intent i = new Intent( Login.this, MainActivity.class);
                        i.putExtra("correo",edtEmail.getText().toString());
                        startActivity(i);
                    }else{
                        progreso.hide();
                        Toast.makeText(Login.this, R.string.txt_not_user, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(Login.this, R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}