package com.example.potatosmarket;

import com.example.potatosmarket.entidades.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class accesoDataBase {
    private DatabaseReference myDatabase;

    public accesoDataBase() {
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        myDatabase = fd.getReference(Usuario.class.getSimpleName());
    }
    public Task<Void> agregarUsuario(Usuario usuario){
        return myDatabase.push().setValue(usuario);
    }
}
