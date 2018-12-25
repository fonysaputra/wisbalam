package com.wisbalam.server.maps;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.wisbalam.server.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

import com.wisbalam.server.djikstra.Mapdjikstra;

public class LihatPeta extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    double latitude, longitude;
    String lat, lang, lat1, lang1,nama, latlang,alamat;
    private Mapdjikstra md;
    private TextView jarak, durasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_peta);

        Intent intent = getIntent();
        nama = intent.getExtras().getString("nama");
        lat = intent.getExtras().getString("lat");
        lang = intent.getExtras().getString("lang");
        lat1 = intent.getExtras().getString("lat1");
        lang1 = intent.getExtras().getString("lang1");

        alamat = intent.getExtras().getString("alamat");

        jarak = (TextView) findViewById(R.id.tv_jarak);
        durasi = (TextView) findViewById(R.id.tv_durasi);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        md = new Mapdjikstra();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.peta1);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlang_RS = new LatLng(Double.valueOf(lat1), Double.valueOf(lang1));
        LatLng posisi = new LatLng(Double.valueOf(lat), Double.valueOf(lang));

        Marker apotik = mMap.addMarker(new MarkerOptions().position(latlang_RS).title(nama).snippet(alamat));
        apotik.showInfoWindow();

        Marker posisi_saya = mMap.addMarker(new MarkerOptions().position(posisi).title("Posisi Saya").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        posisi_saya.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(posisi));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//
        getDirectionMap(posisi, latlang_RS);
    }

    private void getDirectionMap(LatLng from, LatLng to) {
        LatLng fromto[] = {from, to};
        new LongOperation().execute(fromto);
    }

    private class LongOperation extends AsyncTask<LatLng, Void, Document> {

        @Override
        protected Document doInBackground(LatLng... params) {
            Document doc = md.getDocument(params[0], params[1],
                    Mapdjikstra.MODE_DRIVING);
            return doc;
        }

        @Override
        protected void onPostExecute(Document result) {
            setResult(result);

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void setResult(Document doc) {
//memanggil djikstra
        Log.d("Result", String.valueOf(doc));

//        int duration = md.getDurationValue(doc);
//        String distance = md.getDistanceText(doc);
//        String start_address = md.getStartAddress(doc);
//        String copy_right = md.getCopyRights(doc);

        ArrayList<String> duras = md.getDurasi(doc);
        int nilai=0;
        double durat = 0;
        for(int i=0;i<duras.size();i++){
            nilai+=Integer.parseInt(duras.get(i));
        }

        ArrayList<String> dis = md.getDistan(doc);

        for(int i=0;i<dis.size();i++){
            double dur=Double.parseDouble(dis.get(i));
            durat = durat + dur;
        }

        jarak.setText(String.valueOf(durat/1000)+ " km");
        durasi.setText(String.valueOf(nilai/60)+" menit");

//        Toast.makeText(this,start_address,Toast.LENGTH_LONG).show();

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(10).color(
                Color.DKGRAY);//mengganti style map

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
            System.out.println("Nilai Titik"+rectLine);
          //  Marker posisi_graph = mMap.addMarker(new MarkerOptions().title("Node").position(directionPoint.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
           // posisi_graph.showInfoWindow();
        }

        mMap.addPolyline(rectLine);
    }

}