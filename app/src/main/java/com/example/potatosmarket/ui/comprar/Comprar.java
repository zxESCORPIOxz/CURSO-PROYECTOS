package com.example.potatosmarket.ui.comprar;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.Adapters.AdapterCompra;
import com.example.potatosmarket.Adapters.AdapterPublicacion;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Compra;
import com.example.potatosmarket.entidades.Publicacion;
import com.example.potatosmarket.entidades.Variedad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Comprar extends Fragment {
    RecyclerView myrc;
    RequestQueue request;
    ProgressDialog progreso;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<String> VariedadList;
    ArrayList<Compra> listaCompra;
    Spinner spnVariedad;
    String correo;
    AdapterCompra adapterCompra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comprar, container, false);

        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        request= Volley.newRequestQueue(getContext());
        spnVariedad = view.findViewById(R.id.spnVariedad_comprar);
        myrc=view.findViewById(R.id.lista_mis_productos_comprar);
        cargarVariedad();
        spnVariedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarPublicaciones();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void cargarPublicaciones() {
        myrc.removeAllViewsInLayout();
        String myURL = DATOS.IP_SERVER+"consultarPublicacionCompra.php?nCorreo="+correo+"&nVariedad="+spnVariedad.getSelectedItem().toString();
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray myjson = response.getJSONArray("Consulta");
                    Compra compra = null;
                    listaCompra = new ArrayList<Compra>();
                    for (int i = 0; i < myjson.length(); i++) {
                        JSONObject myjsonObject = myjson.getJSONObject(i);
                        compra = new Compra(
                                myjsonObject.getString("idPublicacionUsuario"),
                                myjsonObject.getString("idUsuario"),
                                myjsonObject.getString("NombreVariedad"),
                                myjsonObject.getString("Cantidad"),
                                myjsonObject.getString("Precio"),
                                myjsonObject.getString("Descripcion"),
                                myjsonObject.getString("foto"),
                                myjsonObject.getString("fechacaducidad"),
                                myjsonObject.getString("Nombre")
                        );
                        listaCompra.add(compra);
                    }
                    mostrarDatos();
                    progreso.hide();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                if (error.toString().equals("com.android.volley.ParseError: org.json.JSONException: Value [] of type org.json.JSONArray cannot be converted to JSONObject")) {
                    Toast.makeText(getContext(), "No existen publicaciones para esta variedad", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), R.string.txt_not_publi, Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarVariedad() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "consultarVariedades.php";
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray myjson = response.getJSONArray("variedades");
                    VariedadList = new ArrayList<String>();
                    VariedadList.add(getResources().getString(R.string.txt_Variedad_filtrar));
                    for (int i=0;i<myjson.length();i++){
                        JSONObject myjsonObject = myjson.getJSONObject(i);
                        VariedadList.add(myjsonObject.getString("1"));
                    }
                    ArrayAdapter<CharSequence> adaptadorvariedad= new ArrayAdapter(getContext(),R.layout.item_spineer,
                            VariedadList);
                    spnVariedad.setAdapter(adaptadorvariedad);
                    cargarPublicaciones();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }
    public void mostrarDatos(){
        try {
            myrc.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterCompra = new AdapterCompra(getContext(),listaCompra);
            myrc.setAdapter(adapterCompra);
            adapterCompra.setOnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idPublicacion", listaCompra.get(myrc.getChildAdapterPosition(v)).getIdPublicacion());
                    editor.putString("usuario", listaCompra.get(myrc.getChildAdapterPosition(v)).getUsuario());
                    editor.putString("variedad", listaCompra.get(myrc.getChildAdapterPosition(v)).getVariedad());
                    editor.putString("cantidad", listaCompra.get(myrc.getChildAdapterPosition(v)).getCantidad());
                    editor.putString("precio", listaCompra.get(myrc.getChildAdapterPosition(v)).getPrecio());
                    editor.putString("descripcion", listaCompra.get(myrc.getChildAdapterPosition(v)).getDescripcion());
                    editor.putString("foto", listaCompra.get(myrc.getChildAdapterPosition(v)).getFoto());
                    editor.putString("fechacaducidad", listaCompra.get(myrc.getChildAdapterPosition(v)).getFechacaducidad());
                    editor.putString("nombreVendedor", listaCompra.get(myrc.getChildAdapterPosition(v)).getNombre());
                    editor.apply();
                    Navigation.findNavController(v).navigate(R.id.nav_detalleCompra);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}