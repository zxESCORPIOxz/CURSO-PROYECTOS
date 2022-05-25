package com.example.potatosmarket.ui.comprar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Conversacion;
import com.example.potatosmarket.entidades.LoadImagenes;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class DetalleCompra extends Fragment {
    TextView txtVariedad,txtNombre,txtDescripcion,txtCantidad,txtPrecio,txtFecha;
    ImageView imgPapa;
    FloatingActionButton btnLLamar, btnMensaje, btnMapa;
    String idUsuarioVendedor,correo,idpublicacion;
    String idConversacion,sender_id,receptor_id_id,idPublicacionUsuario,fecha,nombre;
    int mensajes;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    String telefonoV,Lng,Lat;
    Conversacion con;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_compra, container, false);
        request= Volley.newRequestQueue(getContext());
        imgPapa = view.findViewById(R.id.imagePapa);
        txtVariedad = view.findViewById(R.id.txtNombreVariedad);
        txtNombre = view.findViewById(R.id.txtNombreVendedorCompra);
        txtDescripcion = view.findViewById(R.id.txtDescripcionDetalleCompra);
        txtCantidad = view.findViewById(R.id.txtCantidadDetalleCompra);
        txtPrecio = view.findViewById(R.id.txtPrecioDetalleCompra);
        txtFecha = view.findViewById(R.id.txtFechaDetalleCompra);
        btnLLamar = view.findViewById(R.id.btnLlamardetalle);
        btnMensaje = view.findViewById(R.id.btnMensajesdetalle);
        btnMapa = view.findViewById(R.id.btnUbicacióndetalle);
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
        txtVariedad.setText(preferencias.getString("variedad",""));
        nombre=preferencias.getString("nombreVendedor","");
        txtNombre.setText(nombre);
        txtDescripcion.setText(preferencias.getString("descripcion",""));
        txtCantidad.setText(preferencias.getString("cantidad","")+" Kg");
        txtPrecio.setText(preferencias.getString("precio","")+" S/.");
        txtFecha.setText(preferencias.getString("fechacaducidad",""));
        idUsuarioVendedor=preferencias.getString("usuario","");
        correo=preferencias.getString("correo","");
        idpublicacion=preferencias.getString("idPublicacion","");
        LoadImagenes ld = new LoadImagenes(imgPapa);
        ld.execute(preferencias.getString("foto",""));
        obtenerDatosVendedor();
        obtenerConversacion();
        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+51"+telefonoV));
                startActivity(i);
            }
        });
        btnMensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idConversacion", idConversacion);
                    editor.putString("sender_id", receptor_id_id);
                    editor.putString("receptor_id_id", sender_id);
                    editor.putString("idPublicacionUsuario", idPublicacionUsuario);
                    editor.putString("fecha", fecha);
                    editor.putString("mensajes", ""+mensajes);
                    editor.putString("nombre", nombre);
                    editor.apply();
                    Navigation.findNavController(v).navigate(R.id.nav_chat_vender);
                }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Latitud", Lat);
                editor.putString("Longitud", Lng);
                editor.apply();
                Navigation.findNavController(v).navigate(R.id.nav_ubicarDireccion);
            }
        });
        return view;
    }

    private void obtenerDatosVendedor() {
        String myURL = DATOS.IP_SERVER+ "consultarUsuario.php?idUsuario="+idUsuarioVendedor;
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject myjson = response.optJSONObject("usuario");
                    telefonoV = myjson.getString("telefono");
                    Lng = myjson.getString("longitud");
                    Lat = myjson.getString("latitud");
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }
    private void obtenerConversacion() {
        String myURL = DATOS.IP_SERVER+ "consultaConversacionCompuesta.php?nCorreo="+correo+"&nidPublicacionUsuario="+idpublicacion+"&nReceptor="+idUsuarioVendedor;
        myURL = myURL.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject myjson = response.optJSONObject("Consulta");
                    idConversacion=myjson.getString("idConversacion");
                    sender_id=myjson.getString("sender_id");
                    receptor_id_id=myjson.getString("receptor_id");
                    idPublicacionUsuario=myjson.getString("idPublicacionUsuario");
                    fecha=myjson.getString("fecha");
                    mensajes=Integer.parseInt(myjson.getString("mensajes"));
                    if(mensajes>0){
                        btnMensaje.setTitle("Mensajes("+mensajes+")");
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.toString().equals("com.android.volley.ParseError: org.json.JSONException: Value [] of type org.json.JSONArray cannot be converted to JSONObject")){
                    regConversacion();
                }
            }
        });
        request.add(jsonObjectRequest);
    }
    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }

    private void regConversacion() {
        String myURL = DATOS.IP_SERVER+ "registrarConversacion.php?nCorreo="+correo+"&nidPublicacionUsuario="+idpublicacion+"&nReceptor="+idUsuarioVendedor+"&nFecha="+getDate();
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                obtenerConversacion();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }
}