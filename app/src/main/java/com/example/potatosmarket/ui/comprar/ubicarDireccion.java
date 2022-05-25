package com.example.potatosmarket.ui.comprar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Area;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ubicarDireccion extends Fragment implements GoogleMap.OnMapLongClickListener,GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    GoogleMap mMap;
    Button btnterr;
    private Marker marca1,marca2,marca3,marca4;
    ArrayList<LatLng> listaMarker;
    Double lat, lng;
    String direccion;
    Dialog dialog;
    LatLng Vendedor;
    TextView Direccion,Coordenadas;
    EditText descripcion;
    TextView gps1,gps2,gps3,gps4,txtarea,txtFecha;
    int markas = 0;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
            lat = Double.valueOf(sharedPreferences.getString("Latitud","0"));
            lng = Double.valueOf(sharedPreferences.getString("Longitud","0"));
            Vendedor = new LatLng(lat, lng);
            CameraUpdate myUbicacion = CameraUpdateFactory.newLatLngZoom(Vendedor, 16);
            mMap.addMarker(new MarkerOptions().position(Vendedor).draggable(false)
                    .title("Ubicación vendedor")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_vendedor));
            mMap.animateCamera(myUbicacion);
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) { }
                @Override
                public void onMarkerDrag(Marker marker) { }
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> list = geocoder.getFromLocation(
                                marker.getPosition().latitude,marker.getPosition().longitude, 1);
                        if (!list.isEmpty()) {
                            Address DirCalle = list.get(0);
                            direccion = DirCalle.getAddressLine(0);
                            lat=marker.getPosition().latitude;
                            lng=marker.getPosition().longitude;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Direccion.setText(direccion);
                    Coordenadas.setText("Area : "+ SphericalUtil.computeArea(listaMarker)
                            +"\nPerimetro : "+SphericalUtil.computeLength(listaMarker));
                }
            });
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    markas=markas+1;
                    switch(markas){
                        case 1:{
                            marca1 = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true)
                                    .title("Punto 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_1)));
                            break;
                        }
                        case 2:{
                            marca2 = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true)
                                    .title("Punto 2").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_2)));
                            break;
                        }
                        case 3:{
                            marca3 = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true)
                                    .title("Punto 3").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_3)));
                            break;
                        }
                        case 4:{
                            marca4 = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true)
                                    .title("Punto 4").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_4)));
                            break;
                        }
                        default:{
                            Toast.makeText(getContext(), "Solo se pueden integrar", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            });
            mapa();
        }
    };

    public void mapa(){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //actualizar(l);
        LatLng coordenadas = new LatLng(-12.067984798986537, -75.21003798750014);//new LatLng(l.getLatitude(),l.getLongitude());
        CameraUpdate myUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        //mimarca = mMap.addMarker(new MarkerOptions().position(coordenadas).draggable(true).title("Mi ubicación").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gm_casa)));

        mMap.animateCamera(myUbicacion);
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(
                    l.getLatitude(),l.getLongitude(), 1);
            if (!list.isEmpty()) {
                Address DirCalle = list.get(0);
                direccion = DirCalle.getAddressLine(0);
                lat=l.getLatitude();
                lng=l.getLongitude();
            }
            Direccion.setText(direccion);
            Coordenadas.setText(lat+" - "+lng);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubicar_direccion, container, false);
        Coordenadas=view.findViewById(R.id.txtCoordenadas);
        Direccion=view.findViewById(R.id.txtDirecion);
        btnterr=view.findViewById(R.id.btnmapa3);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_mapa);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false); //Optional
        descripcion = dialog.findViewById(R.id.edt_descripcion_mapa);
        gps1 = dialog.findViewById(R.id.txt_gps_1);
        gps2 = dialog.findViewById(R.id.txt_gps_2);
        gps3 = dialog.findViewById(R.id.txt_gps_3);
        gps4 = dialog.findViewById(R.id.txt_gps_4);
        txtarea = dialog.findViewById(R.id.txt_area);
        txtFecha = dialog.findViewById(R.id.txt_fecha);
        Button btnRegistrar = dialog.findViewById(R.id.btn_registrar_mapa);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regMapa();
                dialog.hide();
            }
        });
        btnterr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markas==4){
                    gps1.setText(marca1.getPosition().latitude+" , "+marca1.getPosition().longitude);
                    gps2.setText(marca1.getPosition().latitude+" , "+marca1.getPosition().longitude);
                    gps3.setText(marca1.getPosition().latitude+" , "+marca1.getPosition().longitude);
                    gps4.setText(marca1.getPosition().latitude+" , "+marca1.getPosition().longitude);
                    txtFecha.setText(getDate());
                    listaMarker = new ArrayList<LatLng>();
                    listaMarker.add(marca1.getPosition());
                    listaMarker.add(marca2.getPosition());
                    listaMarker.add(marca3.getPosition());
                    listaMarker.add(marca4.getPosition());
                    txtarea.setText(SphericalUtil.computeArea(listaMarker)+"");
                    dialog.show();
                }else {
                    Toast.makeText(getContext(), "Se requiere 4 markas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public  void regMapa(){
        DatabaseReference df;
        df= FirebaseDatabase.getInstance().getReference();
        df.child("RegistroMapa").child(new Random().nextInt((5000 - 0) + 1)+"").setValue(
                new Area(descripcion.getText().toString(),
                        getDate(),
                        SphericalUtil.computeArea(listaMarker),
                        marca1.getPosition(),
                        marca2.getPosition(),
                        marca3.getPosition(),
                        marca4.getPosition()
                        ))
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show();
                            marca1.remove();
                            marca2.remove();
                            marca3.remove();
                            marca4.remove();
                            markas=0;
                        }else{
                            Toast.makeText(getContext(), "No registrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.dismiss();
    }
    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public static double rad(double x) {
        return x * Math.PI / 180;
    }
    public  static double  getDistance(double p1lat, double p1long, double p2lat, double p2long) {
        long R = 6378137; // Earth’s mean radius in meter
        double dLat = rad(p2lat - p1lat);
        double dLong = rad(p2long - p1long);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(p1lat)) * Math.cos(rad(p2lat)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    };

    @Override
    public void onMapLongClick(LatLng latLng) {
    }
}