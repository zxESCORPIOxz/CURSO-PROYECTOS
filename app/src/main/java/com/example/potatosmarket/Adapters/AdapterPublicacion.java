package com.example.potatosmarket.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.LoadImagenes;
import com.example.potatosmarket.entidades.Publicacion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdapterPublicacion extends RecyclerView.Adapter<AdapterPublicacion.RecyclerHolder> implements View.OnClickListener{
    LayoutInflater inflater;
    ArrayList<Publicacion> model;
    View.OnClickListener listener;
    Context context;

    public AdapterPublicacion(Context context, ArrayList<Publicacion> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context= context;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_publicacion, parent,false);
        v.setOnClickListener(this);
        return new AdapterPublicacion.RecyclerHolder(v);
    }
    public  void setOnListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        String variedad=model.get(position).getVariedad();
        String cantidad=model.get(position).getCantidad();
        String precio=model.get(position).getPrecio();
        String descripcion=model.get(position).getDescripcion();
        String fecha=model.get(position).getFechacaducidad();
        String img=model.get(position).getFoto();
        int msj=model.get(position).getMensajes();
        holder.txtvariedad.setText(variedad);
        holder.txtcantidad.setText(cantidad+" Kg");
        holder.txtprecio.setText(precio+" S/.");
        holder.txtdescripcion.setText(descripcion);
        holder.txtfecha.setText(fecha);
        if(msj>0){
            //holder.txtmsj.setTextColor(Color.RED);
            holder.txtmsj.setBackgroundResource(R.drawable.circulo);
        }
        holder.txtmsj.setText(msj+"");
        Glide.with(context).load(DATOS.IP_SERVER+img).into(holder.imgitem);
//        LoadImagenes ld = new LoadImagenes(holder.imgitem);
//        ld.execute(img);
    }

    @Override
    public int getItemCount() {
        return model .size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView txtvariedad, txtcantidad, txtprecio, txtdescripcion, txtfecha, txtmsj;
        private ImageView imgitem;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            txtvariedad=itemView.findViewById(R.id.Variedaditem);
            txtcantidad=itemView.findViewById(R.id.Cantidaditem);
            txtprecio=itemView.findViewById(R.id.Precioitem);
            txtdescripcion=itemView.findViewById(R.id.Descripcionitem);
            txtfecha=itemView.findViewById(R.id.fechaitem);
            txtmsj=itemView.findViewById(R.id.Notificacionesitem);
            imgitem=itemView.findViewById(R.id.imagePublicacion);
        }
    }
}
