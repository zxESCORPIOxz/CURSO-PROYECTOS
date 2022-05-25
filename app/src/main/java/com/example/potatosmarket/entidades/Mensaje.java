package com.example.potatosmarket.entidades;

public class Mensaje {
    String idMensaje,Contenido,idUsuario,idConversacion,fecha,leido,nombre;

    public Mensaje(String idMensaje, String contenido, String idUsuario, String idConversacion, String fecha, String leido, String nombre) {
        this.idMensaje = idMensaje;
        Contenido = contenido;
        this.idUsuario = idUsuario;
        this.idConversacion = idConversacion;
        this.fecha = fecha;
        this.leido = leido;
        this.nombre = nombre;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
