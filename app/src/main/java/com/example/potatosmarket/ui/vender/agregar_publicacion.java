package com.example.potatosmarket.ui.vender;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
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
import com.example.potatosmarket.entidades.Variedad;
import com.example.potatosmarket.ui.vender.Vender;
import com.github.barteksc.pdfviewer.util.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class agregar_publicacion extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    ImageView img;
    EditText edtCantidad, edtPrecio, edtDescripcion;
    Button btnregistrar;
    RequestQueue request;
    ProgressDialog progreso;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<String> VariedadList;
    Spinner spnVariedad;
    Bitmap mybm;
    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;
    private String correo;
    boolean cimg=false;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_publicacion, container, false);
        request= Volley.newRequestQueue(getContext());
        spnVariedad = view.findViewById(R.id.spnVariedad_add_publicacion);
        edtCantidad = view.findViewById(R.id.edtcantidad_add_publicacion);
        edtDescripcion = view.findViewById(R.id.edtDescripcion_add_publicacion);
        edtPrecio = view.findViewById(R.id.edtprecio_add_publicacion);
        btnregistrar = view.findViewById(R.id.btn_registrar_publicacion);
        img = view.findViewById(R.id.imgArchivo_agregarPublicacion);
        SharedPreferences preferencias = getContext().getSharedPreferences("ARCHIVOREG", Context.MODE_PRIVATE);
        correo=preferencias.getString("correo","");
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this.getContext(),
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirAccion();
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCantidad.getText().length()!=0
                && edtDescripcion.getText().length()!=0
                && edtPrecio.getText().length()!=0
                && spnVariedad.getSelectedItemId()!=0 && cimg){
                    registrarPublicacion();
                    //recargarVender();
                    Navigation.findNavController(view).navigate(R.id.nav_vender);
                }else {
                    Toast.makeText(getContext(), R.string.txt_no_datos, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cargarVariedad();
        return view;
    }
    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }
    private void registrarPublicacion() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "registrarPublicacion.php";
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
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, TOMAR_FOTO);
    }

    public void seleccionarImagen() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, SELEC_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK){
                if(requestCode == SELEC_IMAGEN){
                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setBorderCornerColor(Color.BLACK)
                            .start(getContext(), this);
                }
                else if(requestCode == TOMAR_FOTO){
                    CropImage.activity(imageUri)
                            .setAspectRatio(1, 1)
                            .setBorderCornerColor(Color.BLACK)
                            .start(getContext(), this);
                }
                else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                    //Croped image received
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK){
                        Uri resultUri = result.getUri();
                        imageUri = resultUri;
                        img.setImageURI(resultUri);
                        Bitmap bm = ((BitmapDrawable)img.getDrawable()).getBitmap();
                        mybm=Bitmap.createScaledBitmap(bm, 100, 100, true);
                        cimg=true;
                    }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                        Exception error = result.getError();
                        System.out.println(error);
                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
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