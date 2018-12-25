package com.wisbalam.server.list;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.wisbalam.server.R;
import com.wisbalam.server.adapter.adapter_semua_wisata;
import com.wisbalam.server.koneksi.DbActivity;
import com.wisbalam.server.koneksi.Server;
import com.wisbalam.server.maps.MapsAllWisata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class detail_wisata extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener  {

    DbActivity dbActivity = new DbActivity();
    private GoogleMap mMap;
    double lat1,latitude;
    double  lang1,longitude;
    private int PROXIMITY_RADIUS = 100;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    final ArrayList<Double> lat = new ArrayList<Double>();
    final ArrayList<Double> lng = new ArrayList<Double>();
    final ArrayList<String> nama_wisata = new ArrayList<>();
    JSONArray arraydata;
    TextView txt_nama,txt_alamat,txt_kecamatan,txt_tipe_wisata,txt_biaya_masuk,txt_fasilitas,txt_deskripsi;
    String id,uriString,sharefoto ;
    Button fb , ig;
    ImageView imgwisata;
    private String url = Server.URL ;
    double lats,longs;
    String title,namawisata2;
    CarouselView carouselView;

    ViewPager viewPager  ;
    String[] imageUrls = new String[26];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_wisata);

        txt_nama = (TextView) findViewById(R.id.txt_nama);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_kecamatan = (TextView) findViewById(R.id.txt_kecamatan);
        txt_tipe_wisata = (TextView) findViewById(R.id.txt_tipe_wisata);
        txt_biaya_masuk = (TextView) findViewById(R.id.txt_biaya_masuk);
        txt_fasilitas = (TextView) findViewById(R.id.txt_fasilitas);
        txt_deskripsi = (TextView) findViewById(R.id.txt_deskripsi);
       // imgwisata = (ImageView) findViewById(R.id.img_wisata);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);



      //  CirclePageIndicator indicator = (CirclePageIndicator)
        //        findViewById(R.id.indicator);
    //    indicator.setViewPager(viewPager);
     //   final float density = getResources().getDisplayMetrics().density;

      //  indicator.setRadius(5 * density);




        fb=(Button) findViewById(R.id.fb);
        ig=(Button) findViewById(R.id.ig);
        final Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                uriString ="http://maps.google.com/maps?q=" + lats + "," + longs + "&iwloc="+title;

                sharingIntent.putExtra(Intent.EXTRA_TEXT,uriString);
                sharingIntent.setPackage("com.facebook.katana");
                startActivity(sharingIntent);
            }
        });
        ig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
               // uriString ="https://www.google.com/maps/@"+lats+","+longs+",15z";
                uriString ="http://maps.google.com/maps?q=" + lats + "," + longs + "&iwloc="+title;
                sharingIntent.putExtra(Intent.EXTRA_TEXT,uriString);
                sharingIntent.setPackage("com.whatsapp");
                startActivity(sharingIntent);
            }
        });



        viewdata(id);
        detail_image(namawisata2);

        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");

            Toast.makeText(detail_wisata.this,"Silahkan update Google Play Service",Toast.LENGTH_LONG).show();

            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public void detail_image(String id){
        int a=1;

        try {
            // Mengubah dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.detail_image(id));
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);

                String foto_wisata= jsonChildNode.optString("name");

                imageUrls[i]=url+"/images/"+foto_wisata;

                a++;

               // Toast.makeText(this, "angka = " + foto_wisata, Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void viewdata(String id){
        try {
            // Mengubah dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.detail_wisata(id));
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);
                String idwisata = jsonChildNode.optString("id_wisata");
                String namawisata= jsonChildNode.optString("nama_wisata");
                String alamat= jsonChildNode.optString("alamat");
                String latit= jsonChildNode.optString("latitude");
                String longit= jsonChildNode.optString("longitude");
                String biaya_masuk= jsonChildNode.optString("biaya_masuk");
                String nama_kecamatan= jsonChildNode.optString("nama_kecamatan");
                String nama_tipe= jsonChildNode.optString("nama_tipe");
                String fasilitas_wisata= jsonChildNode.optString("fasilitas_wisata");
                String foto_wisata= jsonChildNode.optString("name");
                String id_image= jsonChildNode.optString("id");
                String deskripsi= jsonChildNode.optString("deskripsi");
                title = namawisata ;
                sharefoto = foto_wisata ;
                txt_nama.setText(namawisata);
                txt_alamat.setText(alamat);
                txt_biaya_masuk.setText("Rp. "+biaya_masuk);
                txt_kecamatan.setText(nama_kecamatan);
                txt_tipe_wisata.setText(nama_tipe);
                txt_deskripsi.setText(deskripsi);
                txt_fasilitas.setText(fasilitas_wisata);
                Double convertlat= Double.parseDouble(latit);
                Double convertlang= Double.parseDouble(longit);
                lats= Double.parseDouble(latit);
                longs= Double.parseDouble(longit);
                lat.add(convertlat);
                lng.add(convertlang);
                nama_wisata.add(namawisata);
                namawisata2=namawisata;



            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lat1 = location.getLatitude();
        lang1 = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        // Toast.makeText(MapsAllWisata.this,"Your Current Location", Toast.LENGTH_LONG).show();
        Toast.makeText(detail_wisata.this,lat1 +" "+lang1,Toast.LENGTH_LONG).show();
        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(this);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
        nearby();}

    public void nearby(){



        for(int s = 0; s < lat.size();s++) {
            LatLng a = new LatLng(lat.get(s), lng.get(s));
            mMap.addMarker(new MarkerOptions().position(a).title(nama_wisata.get(s)));

        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }}

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


}
