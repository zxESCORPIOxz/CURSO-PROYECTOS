package com.example.potatosmarket.entidades;

public class UsuarioToken {
    String correo,token;

    public UsuarioToken(String correo, String token) {
        this.correo = correo;
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
