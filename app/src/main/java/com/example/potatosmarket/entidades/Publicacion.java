package com.example.potatosmarket.entidades;

public class Publicacion {
    String idPublicacion, Usuario, Variedad, Cantidad, Precio, Descripcion, foto, fechacaducidad;
    int mensajes;

    public Publicacion(String idPublicacion, String usuario, String variedad, String cantidad, String precio, String descripcion, String foto, String fechacaducidad, int mensajes) {
        this.idPublicacion = idPublicacion;
        Usuario = usuario;
        Variedad = variedad;
        Cantidad = cantidad;
        Precio = precio;
        Descripcion = descripcion;
        this.foto = foto;
        this.fechacaducidad = fechacaducidad;
        this.mensajes = mensajes;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getVariedad() {
        return Variedad;
    }

    public void setVariedad(String variedad) {
        Variedad = variedad;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechacaducidad() {
        return fechacaducidad;
    }

    public void setFechacaducidad(String fechacaducidad) {
        this.fechacaducidad = fechacaducidad;
    }

    public int getMensajes() {
        return mensajes;
    }

    public void setMensajes(int mensajes) {
        this.mensajes = mensajes;
    }
}
