package com.example.potatosmarket.ui.vender;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class registrarVentas extends Fragment {
    EditText edtCorreo,edtCantidad,edtPrecio,edtDescripcion;
    Button btnRegistrar;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String correo,idPublicacion;
    int cantidad;
    ProgressDialog progreso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_ventas, container, false);
        request= Volley.newRequestQueue(getContext());
        edtCorreo = view.findViewById(R.id.edtCorreo_regVenta);
        edtCantidad = view.findViewById(R.id.edtcantidad_regVenta);
        edtPrecio = view.findViewById(R.id.edtprecio_regVenta);
        edtDescripcion = view.findViewById(R.id.edtDescripcion_regVenta);
        btnRegistrar = view.findViewById(R.id.btn_regVenta);
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", Context.MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        idPublicacion=preferencias.getString("idPublicacion","");
        cantidad= Integer.parseInt(preferencias.getString("cantidad",""));
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDescripcion.getText().length()!=0 &&
                edtPrecio.getText().length()!=0 &&
                edtCantidad.getText().length()!=0 &&
                edtCorreo.getText().length()!=0){
                    if (cantidad>=Integer.parseInt(edtCantidad.getText().toString())){
                        regVenta();
                        Navigation.findNavController(view).navigate(R.id.nav_vender);
                    }else {
                        Toast.makeText(getContext(),"No se cuenta con stock suficiente", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }

    private void regVenta() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "registrarVenta.php?"
                +"ncorreovendedor="+correo
                +"&ncorreocomprador="+edtCorreo.getText().toString()
                +"&nidPublicacionUsuario="+idPublicacion
                +"&ncantidad="+edtCantidad.getText().toString()
                +"&nprecioAcordado="+edtPrecio.getText().toString()
                +"&ndescripcion="+edtDescripcion.getText().toString()
                +"&nfecha="+getDate();
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.optString("Consulta"), Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        });
        request.add(jsonObjectRequest);
    }
}