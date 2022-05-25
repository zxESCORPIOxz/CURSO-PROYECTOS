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
import com.example.potatosmarket.Adapters.AdapterPublicacion;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Publicacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class Vender extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView myrc;
    AdapterPublicacion adapterPublicacion;
    ArrayList<Publicacion> listaPublicacion;
    String correo;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    ProgressDialog progreso;
    JSONObject myresponse=new JSONObject();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vender, container, false);
        FloatingActionButton btn_agregar = view.findViewById(R.id.btn_agregar_publicacion);
        FloatingActionButton btn_refresh = view.findViewById(R.id.btn_refresh);
        request= Volley.newRequestQueue(getContext());
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        myrc=view.findViewById(R.id.lista_mis_productos);
        listaPublicacion=new ArrayList<>();
        cargarLista();
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_vender_to_nav_agregar_publicacion);
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarLista();
            }
        });
        return view;
    }

    private void cargarLista() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        myrc.removeAllViewsInLayout();
        String myURL = DATOS.IP_SERVER+ "consultarPublicacion.php?nCorreo="+correo;
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
            if(myresponse!=response) {
                JSONArray myjson = response.getJSONArray("Consulta");
                Publicacion publicacion = null;
                listaPublicacion = new ArrayList<Publicacion>();
                for (int i = 0; i < myjson.length(); i++) {
                    JSONObject myjsonObject = myjson.getJSONObject(i);
                    publicacion = new Publicacion(
                            myjsonObject.getString("idPublicacionUsuario"),
                            correo,
                            myjsonObject.getString("NombreVariedad"),
                            myjsonObject.getString("Cantidad"),
                            myjsonObject.getString("Precio"),
                            myjsonObject.getString("Descripcion"),
                            myjsonObject.getString("foto"),
                            myjsonObject.getString("fechacaducidad"),
                            Integer.parseInt(myjsonObject.getString("Mensajes"))
                    );
                    listaPublicacion.add(publicacion);
                }
                //myresponse = response;
                mostrarDatos();
                progreso.hide();
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void mostrarDatos(){
        try {
            myrc.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterPublicacion = new AdapterPublicacion(getContext(),listaPublicacion);
            myrc.setAdapter(adapterPublicacion);
            adapterPublicacion.setOnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idPublicacion", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getIdPublicacion());
                    editor.putString("usuario", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getUsuario());
                    editor.putString("variedad", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getVariedad());
                    editor.putString("cantidad", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getCantidad());
                    editor.putString("precio", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getPrecio());
                    editor.putString("descripcion", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getDescripcion());
                    editor.putString("foto", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getFoto());
                    editor.putString("fechacaducidad", listaPublicacion.get(myrc.getChildAdapterPosition(v)).getFechacaducidad());
                    editor.putString("mensajes", ""+listaPublicacion.get(myrc.getChildAdapterPosition(v)).getMensajes());
                    editor.apply();
                    Navigation.findNavController(v).navigate(R.id.nav_detallePublicacion);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}