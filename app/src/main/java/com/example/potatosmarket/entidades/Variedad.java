package com.example.potatosmarket.entidades;

public class Variedad {
    String idVariedad,Nombre,Precio;

    public Variedad(String idVariedad, String nombre, String precio) {
        this.idVariedad = idVariedad;
        Nombre = nombre;
        Precio = precio;
    }

    public String getIdVariedad() {
        return idVariedad;
    }

    public void setIdVariedad(String idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }
}
