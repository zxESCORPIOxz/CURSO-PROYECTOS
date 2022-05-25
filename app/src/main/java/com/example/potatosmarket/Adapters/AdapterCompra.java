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
import com.example.potatosmarket.entidades.Compra;
import com.example.potatosmarket.entidades.LoadImagenes;

import java.util.ArrayList;

public class AdapterCompra  extends RecyclerView.Adapter<AdapterCompra.RecyclerHolder> implements View.OnClickListener{
    LayoutInflater inflater;
    ArrayList<Compra> model;
    View.OnClickListener listener;

    public AdapterCompra(Context context, ArrayList<Compra> model) {
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
    public AdapterCompra.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_compra, parent,false);
        v.setOnClickListener(this);
        return new AdapterCompra.RecyclerHolder(v);
    }
    public  void setOnListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompra.RecyclerHolder holder, int position) {
        String variedad=model.get(position).getVariedad();
        String cantidad=model.get(position).getCantidad();
        String precio=model.get(position).getPrecio();
        String nombre=model.get(position).getNombre();
        String img=model.get(position).getFoto();
        holder.txtvariedad.setText(variedad);
        holder.txtnombre.setText(nombre);
        holder.txtcantidad.setText(cantidad+" Kg");
        holder.txtprecio.setText(precio+" S/.");
        LoadImagenes ld = new LoadImagenes(holder.imgitem);
        ld.execute(img);
    }

    @Override
    public int getItemCount() {
        return model .size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView txtvariedad, txtcantidad, txtprecio, txtnombre;
        private ImageView imgitem;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            txtnombre=itemView.findViewById(R.id.Nombreitem);
            txtvariedad=itemView.findViewById(R.id.Variedaditem);
            txtcantidad=itemView.findViewById(R.id.Cantidaditem);
            txtprecio=itemView.findViewById(R.id.Precioitem);
            imgitem=itemView.findViewById(R.id.imagePublicacion);
        }
    }
}
