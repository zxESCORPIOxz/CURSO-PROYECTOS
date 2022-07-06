package com.example.potatosmarket.ui.comprar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.potatosmarket.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ubicarDireccion extends Fragment implements GoogleMap.OnMapLongClickListener,GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    GoogleMap mMap;
    Double lat, lng;
    LatLng Vendedor;
    ImageButton btnRuta;
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
                }
            });
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubicar_direccion, container, false);
        btnRuta = view.findViewById(R.id.btn_Ruta);
        btnRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+lat+","+lng+"&mode=l"));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
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

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

}