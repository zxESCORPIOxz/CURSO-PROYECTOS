package com.example.potatosmarket.entidades;

import com.google.android.gms.maps.model.LatLng;

public class Area {
    String Descripcion,Fecha;
    Double Area;
    LatLng Coor1,Coor2,Coor3,Coor4;

    public Area(String descripcion, String fecha, Double area, LatLng coor1, LatLng coor2, LatLng coor3, LatLng coor4) {
        Descripcion = descripcion;
        Fecha = fecha;
        Area = area;
        Coor1 = coor1;
        Coor2 = coor2;
        Coor3 = coor3;
        Coor4 = coor4;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public Double getArea() {
        return Area;
    }

    public void setArea(Double area) {
        Area = area;
    }

    public LatLng getCoor1() {
        return Coor1;
    }

    public void setCoor1(LatLng coor1) {
        Coor1 = coor1;
    }

    public LatLng getCoor2() {
        return Coor2;
    }

    public void setCoor2(LatLng coor2) {
        Coor2 = coor2;
    }

    public LatLng getCoor3() {
        return Coor3;
    }

    public void setCoor3(LatLng coor3) {
        Coor3 = coor3;
    }

    public LatLng getCoor4() {
        return Coor4;
    }

    public void setCoor4(LatLng coor4) {
        Coor4 = coor4;
    }
}
