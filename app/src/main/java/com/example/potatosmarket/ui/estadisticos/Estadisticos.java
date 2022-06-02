package com.example.potatosmarket.ui.estadisticos;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Estadisticos extends Fragment {
    ImageView btnLeft,btnRight;
    ProgressDialog progreso;
    Spinner spnMeses, spnVariedad;
    PieChart pastel;
    HorizontalBarChart barras;
    LineChart linea;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    boolean val=false;
    int total=0;
    boolean val1 = false,val2 = false;
    ArrayList<PieEntry> listaVariedad = new ArrayList<PieEntry>();
    ArrayList<BarEntry> listaBar = new ArrayList<BarEntry>();
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
    ArrayList<String> VariedadList = new ArrayList<String>();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticos, container, false);
        btnLeft=view.findViewById(R.id.btnLeft);
        btnRight=view.findViewById(R.id.btnRigth);
        spnMeses=view.findViewById(R.id.spnMeses);
        spnVariedad=view.findViewById(R.id.spnVariedad);
        pastel=view.findViewById(R.id.estPastel);
        barras=view.findViewById(R.id.estBarras);
        linea=view.findViewById(R.id.estLinea);
        request= Volley.newRequestQueue(getContext());
        spnMeses.setSelection(Integer.parseInt(getMes())-1);
        cargarVariedad();
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spnMeses.getSelectedItemPosition()!=0){
                    spnMeses.setSelection(spnMeses.getSelectedItemPosition()-1);
                }else {
                    spnMeses.setSelection(11);
                }
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spnMeses.getSelectedItemPosition()!=11){
                    spnMeses.setSelection(spnMeses.getSelectedItemPosition()+1);
                }else {
                    spnMeses.setSelection(0);
                }
            }
        });
        spnMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(val) {
                    vaciar();
                    cargarListaPastel();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnVariedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnVariedad.getSelectedItemId()==0){
                    vaciar();
                    cargarListaPastel();
                    val=true;
                }else {
                    iLineDataSets.clear();
                    cargarLineasVariedad(spnVariedad.getSelectedItemId() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    private void vaciar(){
        listaVariedad.clear();
        listaBar.clear();
        iLineDataSets.clear();
        spnVariedad.setSelection(0);
        total=0;
    }
    private String getMes(){
        DateFormat dfDate = new SimpleDateFormat("MM");
        String date=dfDate.format(Calendar.getInstance().getTime());
        return date;
    }
    private void cargarListaPastel() {
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
                                listaBar.size()+1,
                                Integer.parseInt(myjsonObject.getString("CanProducto")),
                                myjsonObject.getString("NombreVariedad")
                        );
                        total+=nEntry.getValue();
                        if(nEntry.getValue()>0){
                            cargarLineasVariedad(myjsonObject.getString("idVariedad"));
                            listaVariedad.add(nEntry);
                        }else {
                            val1=true;
                        }
                        if(barEntry.getY()>0){
                            listaBar.add(barEntry);
                        }else {
                            val2=true;
                        }
                    }
                    if(val1){
                        listaVariedad.add(new PieEntry(0.1f,"Otros"));
                        val1 = false;
                    }
                    if(val2){
                        listaBar.add(new BarEntry(listaBar.size()+1,0.1f));
                        val2 = false;
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

    private void cargarLineasVariedad(String idVariedad) {
        String mes;
        if(spnMeses.getSelectedItemId()+1>9){
            mes = ""+(spnMeses.getSelectedItemId()+1);
        }else {
            mes = "0"+(spnMeses.getSelectedItemId()+1);
        }
        String myURL = DATOS.IP_SERVER+ "estadisticoEstandar.php?nidVariedad="+idVariedad+"&nMes="+mes;
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(!response.toString().equals("[]")) {
                    try {
                        JSONArray myjson = response.getJSONArray("variedades");
                        ArrayList<Entry> listaLinea = new ArrayList<Entry>();
                        for (int i = 0; i < myjson.length(); i++) {
                            JSONObject myjsonObject = myjson.getJSONObject(i);
                            listaLinea.add(new Entry(Float.parseFloat(myjsonObject.getString("fecha")), Float.parseFloat(myjsonObject.getString("Promedio"))));

                        }
                        LineDataSet lineDataSet = new LineDataSet(listaLinea, "");
                        lineDataSet.setFillAlpha(110);
                        lineDataSet.setColor(MYSCOLORES[iLineDataSets.size()]);
                        lineDataSet.setLineWidth(3f);
                        lineDataSet.setValueTextSize(10f);
                        lineDataSet.setValueTextSize(10f);
                        lineDataSet.setDrawValues(false);
                        lineDataSet.setCircleColor(MYSCOLORES[iLineDataSets.size()]);
                        iLineDataSets.add(lineDataSet);
                        cargarLinea();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(spnVariedad.getSelectedItemId()!=0 && !error.toString().equals("com.android.volley.ParseError: org.json.JSONException: Value [] of type org.json.JSONArray cannot be converted to JSONObject")){
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    if(spnVariedad.getSelectedItemId()!=0) Toast.makeText(getContext(), "No existe registros de esta variedad", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarPastel() {
        Description des = new Description();
        des.setEnabled(false);
        pastel.getDescription().setEnabled(false);
        pastel.setDescription(des);
        pastel.setExtraOffsets(5, 5, 5, 5);
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
        legend.setXOffset(20);
        legend.setYOffset(20);
        legend.setDrawInside(false);
        PieDataSet pieDataSet = new PieDataSet(listaVariedad,"");
        pieDataSet.setColors(MYSCOLORES);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setSelectionShift(20);
        pieDataSet.setSliceSpace(2f);
        PieData pieData = new PieData(pieDataSet);
        pastel.setBackgroundResource(R.drawable.content_est);
        pastel.animateXY(1500,1500);
        pastel.setData(pieData);
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
        Description des = new Description();
        des.setEnabled(false);
        barras.setDescription(des);
        barras.animateXY(1500,1500);
        barras.setData(barData);
    }
    public void cargarLinea(){
        LineData data = new LineData(iLineDataSets);
        linea.setData(data);
        Description description = new Description();
        description.setEnabled(false);
        linea.setBackgroundResource(R.drawable.content_est);
        linea.setDescription(description);
        linea.animateXY(1500,1500);
        Legend legend = linea.getLegend();
        legend.setEnabled(false);
        progreso.hide();
    }

    private void cargarVariedad() {
        progreso = new ProgressDialog(getContext());
        progreso.show();
        progreso.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progreso.setContentView(R.layout.custom_progressdialog);
        String myURL = DATOS.IP_SERVER+ "consultarVariedades.php";
        myURL = myURL.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray myjson = response.getJSONArray("variedades");
                    VariedadList = new ArrayList<String>();
                    VariedadList.add(getResources().getString(R.string.txt_Variedad_filtrar));
                    for (int i=0;i<myjson.length();i++){
                        JSONObject myjsonObject = myjson.getJSONObject(i);
                        VariedadList.add(myjsonObject.getString("1"));
                    }
                    ArrayAdapter<CharSequence> adaptadorvariedad= new ArrayAdapter(getContext(),R.layout.item_spineer,
                            VariedadList);
                    spnVariedad.setAdapter(adaptadorvariedad);
                } catch (JSONException e) {
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
}