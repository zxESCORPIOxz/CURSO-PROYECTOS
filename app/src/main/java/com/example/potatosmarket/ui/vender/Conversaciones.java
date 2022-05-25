package com.example.potatosmarket.ui.vender;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.Adapters.AdapterConversacion;
import com.example.potatosmarket.Adapters.AdapterPublicacion;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Conversacion;
import com.example.potatosmarket.entidades.Publicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Conversaciones extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView myrc;
    AdapterConversacion adapterConversacion;
    ArrayList<Conversacion> listaConversaciones;
    String correo,idPublicacionUsuario;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    ProgressDialog progreso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversaciones, container, false);
        request= Volley.newRequestQueue(getContext());
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        idPublicacionUsuario=preferencias.getString("idPublicacion","");
        myrc=view.findViewById(R.id.lista_mis_conversaciones);
        listaConversaciones=new ArrayList<>();
        cargarLista();
        return view;
    }

    private void cargarLista() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        myrc.removeAllViewsInLayout();
        String myURL = DATOS.IP_SERVER+ "consultarConversaciones.php?nCorreo="+correo+"&nidPublicacionUsuario="+idPublicacionUsuario;
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), R.string.txt_not_publi, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray myjson = response.getJSONArray("Consulta");
            Conversacion conversaciones = null;
            listaConversaciones = new ArrayList<Conversacion>();
            for (int i = 0; i < myjson.length(); i++) {
                JSONObject myjsonObject = myjson.getJSONObject(i);
                conversaciones = new Conversacion(
                        myjsonObject.getString("idConversacion"),
                        myjsonObject.getString("sender_id"),
                        myjsonObject.getString("receptor_id"),
                        myjsonObject.getString("idPublicacionUsuario"),
                        myjsonObject.getString("fecha"),
                        Integer.parseInt(myjsonObject.getString("mensajes")),
                        myjsonObject.getString("usuario")
                );
                listaConversaciones.add(conversaciones);
            }
            mostrarDatos();
            progreso.hide();
        } catch (JSONException e) {
            Toast.makeText(getContext(), "No existe insteresados", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }
    public void mostrarDatos(){
        try {
            myrc.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterConversacion = new AdapterConversacion(getContext(),listaConversaciones);
            myrc.setAdapter(adapterConversacion);
            adapterConversacion.setOnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idConversacion", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getIdConversacion());
                    editor.putString("sender_id", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getSender_id());
                    editor.putString("receptor_id_id", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getReceptor_id_id());
                    editor.putString("idPublicacionUsuario", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getIdPublicacionUsuario());
                    editor.putString("fecha", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getFecha());
                    editor.putString("mensajes", ""+listaConversaciones.get(myrc.getChildAdapterPosition(v)).getMensajes());
                    editor.putString("nombre", listaConversaciones.get(myrc.getChildAdapterPosition(v)).getNombre());
                    editor.apply();
                    Navigation.findNavController(v).navigate(R.id.nav_chat_vender);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}