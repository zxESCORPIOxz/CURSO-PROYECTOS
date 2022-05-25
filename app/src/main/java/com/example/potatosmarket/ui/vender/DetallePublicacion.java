package com.example.potatosmarket.ui.vender;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.LoadImagenes;
import com.example.potatosmarket.ui.vender.Vender;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DetallePublicacion extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    ImageView img;
    EditText edtCantidad, edtPrecio, edtDescripcion;
    RequestQueue request;
    Button btnactualizar;
    FloatingActionButton btneliminar, btnMensaje, btnVender;
    ProgressDialog progreso;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<String> VariedadList;
    Spinner spnVariedad;
    Bitmap mybm;
    Uri imagenUri;
    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;
    LoadImagenes ld;
    String correo;
    String idPublicacion;
    String usuario;
    String variedad;
    String cantidad;
    String precio;
    String descripcion;
    String foto;
    String fechacaducidad;
    int mensajes;
    Button Okay,Cancel;
    private Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_publicacion, container, false);
        request= Volley.newRequestQueue(getContext());
        spnVariedad = view.findViewById(R.id.spnVariedad_detalle_publicacion);
        edtCantidad = view.findViewById(R.id.edtcantidad_detalle_publicacion);
        edtDescripcion = view.findViewById(R.id.edtDescripcion_detalle_publicacion);
        edtPrecio = view.findViewById(R.id.edtprecio_detalle_publicacion);
        btnactualizar = view.findViewById(R.id.btn_actualizar_detalle_publicacion);
        btneliminar = view.findViewById(R.id.btnEliminar_detalle);
        btnMensaje = view.findViewById(R.id.btnmensaje_detalle);
        btnVender = view.findViewById(R.id.btnVender_detalle);
        img = view.findViewById(R.id.imgArchivo_detallePublicacion);
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", Context.MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        idPublicacion=preferencias.getString("idPublicacion","");
        usuario=preferencias.getString("usuario","");
        variedad=preferencias.getString("variedad","");
        cantidad=preferencias.getString("cantidad","");
        precio=preferencias.getString("precio","");
        descripcion=preferencias.getString("descripcion","");
        foto=preferencias.getString("foto","");
        fechacaducidad=preferencias.getString("fechacaducidad","");
        mensajes= Integer.parseInt(preferencias.getString("mensajes",""));
        edtCantidad.setText(cantidad);
        edtPrecio.setText(precio);
        edtDescripcion.setText(descripcion);
        ld = new LoadImagenes(img);
        ld.execute(foto);
        cargarVariedad();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_yes_no);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false); //Optional
        Okay = dialog.findViewById(R.id.btn_okay);
        Cancel = dialog.findViewById(R.id.btn_cancel);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirAccion();
            }
        });
        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCantidad.getText().length()!=0
                        && edtDescripcion.getText().length()!=0
                        && edtPrecio.getText().length()!=0
                        && spnVariedad.getSelectedItemId()!=0){
                    updatePublicacion();
                    Navigation.findNavController(view).navigate(R.id.nav_vender);
                }else {
                    Toast.makeText(getContext(), R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        if(mensajes>0){
            btnMensaje.setTitle("Mensajes("+mensajes+")");
        }
        btnMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_conversacion);
            }
        });
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_reg_vender);
            }
        });
        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCantidad.getText().length()!=0
                        && edtDescripcion.getText().length()!=0
                        && edtPrecio.getText().length()!=0
                        && spnVariedad.getSelectedItemId()!=0){
                    deletePublicacion();
                    Navigation.findNavController(view).navigate(R.id.nav_vender);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(), R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return view;
    }

    private int seleccionarVariedad() {
        for ( int i=0;i<VariedadList.size();i++ ){
            if(VariedadList.get(i).equals(variedad)){
                return i;
            }
        }
        return 0;
    }
    private void updatePublicacion() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        mybm = ((BitmapDrawable)img.getDrawable()).getBitmap();
        String myURL = DATOS.IP_SERVER+ "updatePublicacion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("nidPublicacionUsuario", idPublicacion);
                params.put("nCorreo", correo);
                params.put("nVariedad", spnVariedad.getSelectedItem().toString());
                params.put("nCantidad", edtCantidad.getText().toString());
                params.put("nPrecio", edtPrecio.getText().toString());
                params.put("nDescripcion", edtDescripcion.getText().toString());
                params.put("nFoto", getStringImagen(mybm));
                params.put("nfechacaducidad", getDate());
                return params;
            }
        };
        request.add(stringRequest);
    }

    private void deletePublicacion() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        mybm = ((BitmapDrawable)img.getDrawable()).getBitmap();
        String myURL = DATOS.IP_SERVER+ "deletePublicacion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("nidPublicacionUsuario", idPublicacion);
                return params;
            }
        };
        request.add(stringRequest);
    }
    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }
    private void cargarVariedad() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "consultarVariedades.php";
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, this, this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray myjson = response.getJSONArray("variedades");
            VariedadList = new ArrayList<String>();
            VariedadList.add(getResources().getString(R.string.txt_Variedad));
            for (int i=0;i<myjson.length();i++){
                JSONObject myjsonObject = myjson.getJSONObject(i);
                VariedadList.add(myjsonObject.getString("1"));
            }
            ArrayAdapter<CharSequence> adaptadorvariedad= new ArrayAdapter(getContext(),R.layout.item_spineer,
                    VariedadList);
            spnVariedad.setAdapter(adaptadorvariedad);
            spnVariedad.setSelection(seleccionarVariedad());
            progreso.hide();
        } catch (JSONException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void elegirAccion(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_camara_galeria);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false); //Optional
        Button camara = dialog.findViewById(R.id.btn_Camara);
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tomarFoto();
            }
        });
        Button galeria = dialog.findViewById(R.id.btn_Galeria);
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                seleccionarImagen();
            }
        });
        dialog.show();
    }
    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TOMAR_FOTO);
    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, SELEC_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
                imagenUri = data.getData();
                img.setImageURI(imagenUri);
                mybm = ((BitmapDrawable)img.getDrawable()).getBitmap();
            } else if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
                Bundle extras = data.getExtras();
                mybm = (Bitmap) extras.get("data");
                img.setImageBitmap(mybm);
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}