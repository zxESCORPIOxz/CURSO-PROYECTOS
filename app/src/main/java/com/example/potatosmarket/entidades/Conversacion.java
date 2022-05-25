package com.example.potatosmarket.entidades;

public class Conversacion {
    String idConversacion,sender_id,receptor_id_id,idPublicacionUsuario,fecha,nombre;
    int mensajes;

    public Conversacion(String idConversacion, String sender_id,String receptor_id_id, String idPublicacionUsuario, String fecha, int mensajes, String nombre) {
        this.idConversacion = idConversacion;
        this.sender_id = sender_id;
        this.receptor_id_id = receptor_id_id;
        this.idPublicacionUsuario = idPublicacionUsuario;
        this.fecha = fecha;
        this.mensajes = mensajes;
        this.nombre = nombre;
    }

    public String getReceptor_id_id() {
        return receptor_id_id;
    }

    public void setReceptor_id_id(String receptor_id_id) {
        this.receptor_id_id = receptor_id_id;
    }

    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getIdPublicacionUsuario() {
        return idPublicacionUsuario;
    }

    public void setIdPublicacionUsuario(String idPublicacionUsuario) {
        this.idPublicacionUsuario = idPublicacionUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getMensajes() {
        return mensajes;
    }

    public void setMensajes(int mensajes) {
        this.mensajes = mensajes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
