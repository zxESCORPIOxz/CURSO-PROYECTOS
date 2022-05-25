package com.example.potatosmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONObject;


public class RegistrarUsuario extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    ImageView btnVolver,btngps;
    String lat,lng;
    ProgressDialog progreso;
    Button btnregistrar;
    EditText edtdni, edtapellido, edtnombre, edttelefono, edtemail, edtpass;
    TextView edtDireccion;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    accesoDataBase mydb;
    Button Okay;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        btnVolver=findViewById(R.id.btn_volver);
        btngps=findViewById(R.id.btn_gps);
        btnregistrar=findViewById(R.id.btn_registrar_usuario);
        edtdni=findViewById(R.id.edt_dni_usuario);
        edtapellido=findViewById(R.id.edt_apellidos_usuario);
        edtnombre=findViewById(R.id.edt_nombres_usuario);
        edttelefono=findViewById(R.id.edt_telefono_usuario);
        edtemail=findViewById(R.id.edt_email_usuario);
        edtpass=findViewById(R.id.edt_pass_usuario);
        edtDireccion=findViewById(R.id.edt_direccion_usuario);
        request= Volley.newRequestQueue(this);
        mydb = new accesoDataBase();

        dialog = new Dialog(RegistrarUsuario.this);
        dialog.setContentView(R.layout.custom_dialog_bienvenidad);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false); //Optional
        Okay = dialog.findViewById(R.id.btn_ok);
        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                dialog.dismiss();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrarUsuario.this, Buscar_direccion.class);
                startActivityForResult(i, 1);
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regUsuario();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("direccion");
                SharedPreferences preferencias = getSharedPreferences("ARCHIVOREG", Context.MODE_PRIVATE);
                lat=preferencias.getString("latitud","");
                lng=preferencias.getString("longitud","");
                edtDireccion.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private void regUsuario() {
        String myURL=null;
        if( edtemail.getText().toString()!=null &&
            edtpass.getText().toString()!=null &&
            edtdni.getText().toString()!=null &&
            edtapellido.getText().toString()!=null &&
            edtnombre.getText().toString()!=null &&
            edtDireccion.getText().toString()!=null &&
            lat!=null &&
            lng!=null &&
            edttelefono.getText().toString()!=null
        ) {
            if(edtdni.getText().toString().length()==8){
                if(edtpass.getText().toString().length()>=6){
                    try {
                        progreso = new ProgressDialog(this);
                        progreso.show();
                        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        progreso.setContentView(R.layout.custom_progressdialog);
                        myURL = DATOS.IP_SERVER+ "registrar.php?"+
                                "nCorreo=" + edtemail.getText().toString() + "&" +
                                "nPass=" + edtpass.getText().toString() + "&" +
                                "nDni=" + edtdni.getText().toString() + "&" +
                                "nApellido=" + edtapellido.getText().toString() + "&" +
                                "nNombre=" + edtnombre.getText().toString() + "&" +
                                "nDirecion=" + edtDireccion.getText().toString() + "&" +
                                "nLatitud=" + lat + "&" +
                                "nLongitud=" + lng + "&" +
                                "nTelefono=" + edttelefono.getText().toString();
                        myURL = myURL.replace(" ", "%20");
                        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, this, this);
                        request.add(jsonObjectRequest);
                    } catch (Exception e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        progreso.hide();
                    }
                }else{
                    Toast.makeText(this, R.string.txt_pass_corto, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, R.string.txt_dni_corto, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        if(error.toString().equals("com.android.volley.ParseError: org.json.JSONException: Value <br of type java.lang.String cannot be converted to JSONObject")){
            Toast.makeText(RegistrarUsuario.this, "El correo ya esta en uso", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(RegistrarUsuario.this,R.string.txt_regitro_incorrecto+ error.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.optString("usuario").equals("Registrado")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edtemail.getText().toString(),
                    edtpass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        dialog.show();
                    }else{
                        Toast.makeText(RegistrarUsuario.this, R.string.txt_regitro_incorrecto, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(RegistrarUsuario.this, response.optString("usuario"), Toast.LENGTH_SHORT).show();
        }
        progreso.hide();
    }
}