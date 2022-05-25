package com.example.potatosmarket.ui.vender;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.Adapters.AdapterMensaje;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Mensaje;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ChatVendedor extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView myrc;
    AdapterMensaje adapterMensaje;
    ArrayList<Mensaje> listaMensaje;
    String correo,idConversacion,idUsuario,Nombre,idUsuarioS;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    EditText edtMesaje;
    ImageView btnSend;
    String cambio="123";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        request= Volley.newRequestQueue(getContext());
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        idConversacion=preferencias.getString("idConversacion","");
        idUsuario=preferencias.getString("receptor_id_id","");
        idUsuarioS=preferencias.getString("sender_id","");
        Nombre=preferencias.getString("nombre","");
        myrc=view.findViewById(R.id.lista_mis_chat);
        edtMesaje=view.findViewById(R.id.edtmensaje_chat);
        btnSend=view.findViewById(R.id.btn_Send);
        listaMensaje=new ArrayList<>();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMesaje.getText().length()>0){
                    regMensaje();
                }else {
                    Toast.makeText(getContext(), "Ingrese un mensaje", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iniciarChat();
        return view;
    }

    private void regMensaje() {
        String myURL = DATOS.IP_SERVER+ "registrarMensaje.php?"
                +"nContenido="+edtMesaje.getText().toString()
                +"&nidUsuario="+idUsuario
                +"&nidConversacion="+idConversacion
                +"&nfecha="+getDate();
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.optString("Consulta"), Toast.LENGTH_SHORT).show();
                edtMesaje.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
    private void iniciarChat() {
        final Thread miHilo=new Thread(){
            public void run(){
                try {
                    while (true){
                        Thread.sleep(1000);
                        cargarLista();
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }


        };
        miHilo.start();
    }

    private void cargarLista() {
        String myURL = DATOS.IP_SERVER+ "consultarMensaje.php?nidConversacion="+idConversacion+"&nidUsuario="+idUsuarioS;
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(listaMensaje.size()==0){
        }else {
            Toast.makeText(getContext(), R.string.txt_not_publi, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if(!cambio.equals(response.toString())){
                JSONArray myjson = response.getJSONArray("Consulta");
                Mensaje mensaje = null;
                listaMensaje = new ArrayList<Mensaje>();
                for (int i = 0; i < myjson.length(); i++) {
                    JSONObject myjsonObject = myjson.getJSONObject(i);
                    mensaje = new Mensaje(
                            myjsonObject.getString("idMensaje"),
                            myjsonObject.getString("Contenido"),
                            myjsonObject.getString("idUsuario"),
                            myjsonObject.getString("idConversacion"),
                            myjsonObject.getString("fecha"),
                            myjsonObject.getString("leido"),
                            Nombre
                    );
                    listaMensaje.add(mensaje);
                }
                mostrarDatos();
                cambio=response.toString();
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), "No existe mensajes", Toast.LENGTH_SHORT).show();
        }
    }
    public void mostrarDatos(){
        try {
            LinearLayoutManager muliner = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,true);
            myrc.setLayoutManager(muliner);
            adapterMensaje = new AdapterMensaje(getContext(),listaMensaje,idUsuario);
            myrc.setAdapter(adapterMensaje);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}