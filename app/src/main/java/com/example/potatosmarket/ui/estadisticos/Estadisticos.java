package com.example.potatosmarket.ui.estadisticos;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.potatosmarket.DATOS;
import com.example.potatosmarket.R;
import com.example.potatosmarket.entidades.Publicacion;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Estadisticos extends Fragment {
    ProgressDialog progreso;
    PieChart pastel;
    HorizontalBarChart barras;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    int total=0;
    boolean val1 = true,val2 = true;
    ArrayList<PieEntry> listaVariedad = new ArrayList<PieEntry>();
    ArrayList<BarEntry> listaBar = new ArrayList<BarEntry>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticos, container, false);
        pastel=view.findViewById(R.id.estPastel);
        barras=view.findViewById(R.id.estBarras);
        request= Volley.newRequestQueue(getContext());
        cargarListaPastel();
        cargarBarras();
        return view;
    }

    private void cargarListaPastel() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "estadisticoVariedad.php";
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray myjson = response.getJSONArray("variedades");
                    PieEntry nEntry = null;
                    BarEntry barEntry = null;
                    for (int i = 0; i < myjson.length(); i++) {
                        JSONObject myjsonObject = myjson.getJSONObject(i);
                        nEntry = new PieEntry(
                                Integer.parseInt(myjsonObject.getString("CanProductores")),
                                myjsonObject.getString("NombreVariedad")
                        );
                        barEntry = new BarEntry(
                                Integer.parseInt(myjsonObject.getString("idVariedad")),
                                Integer.parseInt(myjsonObject.getString("CanProducto")),
                                myjsonObject.getString("NombreVariedad")
                        );
                        total+=nEntry.getValue();
                        if(nEntry.getValue()>0){
                            listaVariedad.add(nEntry);
                        }else {
                            if(val1){
                                listaVariedad.add(new PieEntry(0,"Otros"));
                                val1 = false;
                            }
                        }
                        if(barEntry.getY()>0){
                            listaBar.add(barEntry);
                        }
                    }
                    cargarBarras();
                    cargarPastel();
                } catch (JSONException e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarPastel() {
        Description des = new Description();
        des.setTextAlign(Paint.Align.CENTER);
        des.setText("Productores por variedad");
        des.setTextSize(15);
        pastel.setDescription(des);
        pastel.setEntryLabelColor(Color.BLACK);
        pastel.setDrawEntryLabels(false);
        pastel.setCenterTextColor(Color.BLACK);
        pastel.setCenterText("TOTAL\n"+total);
        pastel.setCenterTextSize(18);
        pastel.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        Legend legend = pastel.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        PieDataSet pieDataSet = new PieDataSet(listaVariedad,"Variedades");
        pieDataSet.setColors(MYSCOLORES);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setSelectionShift(20);
        PieData pieData = new PieData(pieDataSet);
        pastel.setBackgroundResource(R.drawable.content_est);
        pastel.setData(pieData);
        progreso.hide();
    }
    
    public void cargarBarras(){
        BarDataSet barDataSet = new BarDataSet(listaBar,"Barras");
        barDataSet.setColors(MYSCOLORES);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barras.setFitBars(true);
        barras.setActivated(true);
        barras.setBackgroundResource(R.drawable.content_est);
        barras.setData(barData);
        Description des = new Description();
        des.setTextAlign(Paint.Align.RIGHT);
        des.setText("Cantidad por variedad");
        des.setTextSize(15);
        barras.setDescription(des);
    }

    public static final int[] MYSCOLORES = {
            Color.parseColor("#800080"),
            Color.parseColor("#FF0000"),
            Color.parseColor("#00FF00"),
            Color.parseColor("#F4D03F"),
            Color.parseColor("#0000FF"),
            Color.parseColor("#3498DB"),
            Color.parseColor("#A6ACAF"),
            Color.parseColor("#AAFF00"),
            Color.parseColor("#FF00FE")
    };
}