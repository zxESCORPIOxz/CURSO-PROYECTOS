package com.example.potatosmarket.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.example.potatosmarket.DATOS;
import java.io.IOException;
import java.io.InputStream;

public class LoadImagenes extends AsyncTask<String,Void, Bitmap> {
    ImageView img;
    Bitmap bitmap;
    public LoadImagenes(ImageView img) {
        this.img = img;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bit = null;
        try {
            InputStream inputStream = new java.net.URL(DATOS.IP_SERVER+url).openStream();
            bit = BitmapFactory.decodeStream(inputStream);
            bitmap = bit;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bit;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    public Bitmap getBit() {
        return bitmap;
    }
}
