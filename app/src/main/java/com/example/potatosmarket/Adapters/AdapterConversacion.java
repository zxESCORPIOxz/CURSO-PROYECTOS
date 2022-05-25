package com.example.potatosmarket.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Conversacion;

import java.util.ArrayList;

public class AdapterConversacion extends RecyclerView.Adapter<AdapterConversacion.RecyclerHolder> implements View.OnClickListener{
    LayoutInflater inflater;
    ArrayList<Conversacion> model;
    View.OnClickListener listener;

    public AdapterConversacion(Context context, ArrayList<Conversacion> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public AdapterConversacion.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_conversacion, parent,false);
        v.setOnClickListener(this);
        return new AdapterConversacion.RecyclerHolder(v);
    }
    public  void setOnListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConversacion.RecyclerHolder holder, int position) {
        holder.txtNombre.setText(model.get(position).getNombre());
        holder.txtfecha.setText(model.get(position).getFecha());
        if(model.get(position).getMensajes()>0){
            holder.txtmensajes.setBackgroundResource(R.drawable.circulo);
        }
        holder.txtmensajes.setText(model.get(position).getMensajes()+"");
    }

    @Override
    public int getItemCount() {
        return model .size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre, txtmensajes, txtfecha;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre=itemView.findViewById(R.id.NombreSender);
            txtfecha=itemView.findViewById(R.id.Fechaitem);
            txtmensajes=itemView.findViewById(R.id.mesajesitem);
        }
    }
}
