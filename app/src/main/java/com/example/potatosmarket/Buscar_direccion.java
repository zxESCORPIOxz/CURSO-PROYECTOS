package com.example.potatosmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Buscar_direccion extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    Double lat, lng;
    String direccion;
    private GoogleMap mMap;
    private Marker mimarca;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_direccion);
        Button btn_guardar = findViewById(R.id.btn_guardar_ubicacion);
        ImageView btnVolver = findViewById(R.id.btn_volver_mapa);
        titulo = findViewById(R.id.txtUbicacion);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("direccion",direccion);
                setResult(Activity.RESULT_OK,returnIntent);
                SharedPreferences sharedPreferences = getSharedPreferences("ARCHIVOREG", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("latitud", lat.toString());
                editor.putString("longitud", lng.toString());
                editor.apply();
                finish();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerDragListener(this);
        miUBICACION();
    }


    private void miUBICACION() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //actualizar(l);
            LatLng coordenadas = new LatLng(l.getLatitude(),l.getLongitude());
            CameraUpdate myUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
            mimarca = mMap.addMarker(new MarkerOptions().position(coordenadas).draggable(true)
                    .title("Mi ubicación").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gm_casa)));
            mMap.animateCamera(myUbicacion);
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        l.getLatitude(),l.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = DirCalle.getAddressLine(0);
                    lat=l.getLatitude();
                    lng=l.getLongitude();
                }
            } catch (IOException e) {
                e.printStackTrace();
        }
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
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
        titulo.setText(direccion);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}