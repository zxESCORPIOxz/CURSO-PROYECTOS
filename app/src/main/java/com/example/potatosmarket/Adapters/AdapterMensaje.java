package com.example.potatosmarket.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Mensaje;
import java.util.ArrayList;


public class AdapterMensaje extends RecyclerView.Adapter<AdapterMensaje.RecyclerHolder> implements View.OnClickListener{
    LayoutInflater inflater;
    ArrayList<Mensaje> model;
    View.OnClickListener listener;
    String idUsuario;
    Context context;
    public AdapterMensaje(Context context, ArrayList<Mensaje> model, String idUsuario) {
        this.inflater = LayoutInflater.from(context);
        this.context=context;
        this.model = model;
        this.idUsuario = idUsuario;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public AdapterMensaje.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_mensaje, parent,false);
        v.setOnClickListener(this);
        return new AdapterMensaje.RecyclerHolder(v);
    }
    public  void setOnListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMensaje.RecyclerHolder holder, int position) {
        holder.txtcontenido.setText(model.get(position).getContenido());
        holder.txtfecha.setText(model.get(position).getFecha());
        if(model.get(position).getLeido().equals("0")){
            holder.imgLeido.setBackgroundResource(R.drawable.circulo);
        }else{
            holder.imgLeido.setBackgroundResource(R.drawable.circulo2);
        }
        if(idUsuario.equals(model.get(position).getIdUsuario())){
            holder.txtnombre.setText("Tu");
            holder.txtnombre.setGravity(Gravity.RIGHT);
            holder.contenedor.setHorizontalGravity(Gravity.RIGHT);
            holder.contenedor.setGravity(Gravity.RIGHT);
            holder.txtnombre.setTextColor(ContextCompat.getColor(context, R.color.colorAmarillo));
        }else {
            holder.txtnombre.setText(model.get(position).getNombre());
            holder.txtnombre.setGravity(Gravity.LEFT);
            holder.contenedor.setHorizontalGravity(Gravity.LEFT);
            holder.contenedor.setGravity(Gravity.LEFT);
            holder.txtnombre.setTextColor(ContextCompat.getColor(context, R.color.colorVerde1));
        }
    }

    @Override
    public int getItemCount() {
        return model .size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView txtnombre, txtcontenido, txtfecha;
        private ImageView imgLeido;
        private LinearLayout contenedor;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            txtnombre=itemView.findViewById(R.id.Nombreitem);
            txtcontenido=itemView.findViewById(R.id.Contenidoitem);
            txtfecha=itemView.findViewById(R.id.Fechaitem);
            imgLeido=itemView.findViewById(R.id.leidoitem);
            contenedor=itemView.findViewById(R.id.contenedorMensaje);
        }
    }
}