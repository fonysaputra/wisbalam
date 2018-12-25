package com.wisbalam.server.list;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wisbalam.server.R;
import com.wisbalam.server.adapter.adapter_semua_wisata;
import com.wisbalam.server.helper.PermissionHelper;
import com.wisbalam.server.koneksi.AppController;
import com.wisbalam.server.koneksi.DbActivity;
import com.wisbalam.server.koneksi.Server;
import com.wisbalam.server.recycler.recycler_semua_wisata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class wisata_baru extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    DbActivity dbActivity = new DbActivity();

    JSONArray arraydata;
    EditText edlongitude , edlatitude, ednamawisata , edalamat, edbiaya , edfasilitas, eddeskripsi;
    String lat1,long1,id_wisata,id_kecamatan;
    ImageView image_photo,imgviewfoto;

    private Location mLastLocation;
    public LocationManager mLocationManager;
    private ProgressDialog dialog;
    Spinner spkecamatan,spjeniswisata;
    Intent intent;
    Uri fileUri;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    final ArrayList<String> list_namakecamatan = new ArrayList<String>();
    final ArrayList<String> list_id_kecamatan = new ArrayList<String>();
    final ArrayList<String> list_id_tipe_wisata = new ArrayList<String>();
    final ArrayList<String> list_nama_tipe_wisata = new ArrayList<String>();
    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;
    PermissionHelper permissionHelper;
    TextView tv_tgl;
    Button btn_upload,btn_reset,tambah_foto;
    private String url = Server.URL + "image.php";

    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size2 = 60; // range 1 - 100

    private static final String TAG = wisata_baru.class.getSimpleName();



    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_NAME_WISATA = "wisata";

    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wisata_baru);


        permissionHelper = new PermissionHelper(this);

        checkAndRequestPermissions();
        tv_tgl=(TextView) findViewById(R.id.tv_tgl);
        edlatitude = (EditText) findViewById(R.id.edlatitude);
        edlongitude = (EditText) findViewById(R.id.edlongitude);
        ednamawisata = (EditText) findViewById(R.id.ednamawisata);
        edalamat = (EditText) findViewById(R.id.edalamat);
        edbiaya = (EditText)findViewById(R.id.edbiaya);
        edfasilitas = (EditText) findViewById(R.id.edfasilitas);
        eddeskripsi = (EditText) findViewById(R.id.eddeskripsi);
        image_photo = (ImageView) findViewById(R.id.imagephoto);
        imgviewfoto = (ImageView) findViewById(R.id.imgviewphoto);
        spkecamatan = (Spinner) findViewById(R.id.spkeamatan);
        spjeniswisata = (Spinner) findViewById(R.id.sptipewisata);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        tambah_foto = (Button) findViewById(R.id.btn_tambahfoto);

        edlongitude.setText("Mencari Posisi");
        edlatitude.setText("Mencari Posisi");

        ednamawisata.setFocusable(true);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Mencari posisi...");
        int LOCATION_REFRESH_TIME = 10;
        int LOCATION_REFRESH_DISTANCE = 1;


        //save wisata baru


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);

        image_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        kecamatan();
        wisata();
        tv_tgl.setText(new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, list_namakecamatan);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, list_nama_tipe_wisata);
        spkecamatan.setAdapter(adapter);
        spjeniswisata.setAdapter(adapter2);

        spjeniswisata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
               // Toast.makeText(wisata_baru.this, "Selected "+ list_id_tipe_wisata.get(i), Toast.LENGTH_SHORT).show();
            id_wisata = list_id_tipe_wisata.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spkecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                // Toast.makeText(wisata_baru.this, "Selected "+ list_id_tipe_wisata.get(i), Toast.LENGTH_SHORT).show();
                id_kecamatan = list_id_kecamatan.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tambah_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edlongitude.equals("Mencari Posisi")&&edlatitude.equals("Mencari Posisi")){
                    Toast.makeText(wisata_baru.this, "Lokasi Belum DItemukan Tunggu Beberapa Saat", Toast.LENGTH_SHORT).show();
                }
                else if (ednamawisata.getText().toString().trim().equals("")) {
                    Toast.makeText(wisata_baru.this, "Masukan Nama", Toast.LENGTH_SHORT).show();
                }
                else if (edalamat.getText().toString().trim().equals("")) {
                    Toast.makeText(wisata_baru.this, "Masukan Alamat", Toast.LENGTH_SHORT).show();
                }
                else if (edbiaya.getText().toString().trim().equals("")) {
                    Toast.makeText(wisata_baru.this, "Masukan Biaya", Toast.LENGTH_SHORT).show();
                }
                else if (edfasilitas.getText().toString().trim().equals("")) {
                    Toast.makeText(wisata_baru.this, "Masukan Fasilitas", Toast.LENGTH_SHORT).show();
                }
                else if (eddeskripsi.getText().toString().trim().equals("")) {
                    Toast.makeText(wisata_baru.this, "Masukan Deskripsi", Toast.LENGTH_SHORT).show();
                }
              else{

                save_wisata(ednamawisata.getText().toString().trim(), edalamat.getText().toString().trim(), lat1, long1, id_kecamatan, id_wisata
                        , edbiaya.getText().toString().trim(), edfasilitas.getText().toString().trim(), ednamawisata.getText().toString(), "d",eddeskripsi.getText().toString());
                bersih();
            }}

        });
        btn_reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                  bersih();
                  kosong();
            }
        });

         }

    public void save_wisata(String nama_wisata, String alamat, String latitude, String longitude, String id_kecamatan, String
            id_tipe_wisata, String biaya_masuk, String fasilitas_wisata, String foto_wisata, String tgl_survei,String deskripsi){
        try {

            arraydata = new JSONArray(dbActivity.save_wisata_baru(nama_wisata,alamat,latitude,longitude,id_kecamatan,id_tipe_wisata
            ,biaya_masuk,fasilitas_wisata,foto_wisata,tgl_survei,deskripsi));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void bersih(){
        ednamawisata.setText("");
        edalamat.setText("");
        eddeskripsi.setText("");
        edbiaya.setText("");
        edfasilitas.setText("");
    }
    public void kecamatan(){
        try {
            // Mengubah dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.kecamatan());
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);
                String idkecamatan = jsonChildNode.optString("id_kecamatan");
                String namakecamatan= jsonChildNode.optString("nama_kecamatan");
                list_namakecamatan.add(namakecamatan);
                list_id_kecamatan.add(idkecamatan);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void wisata(){
        try {
            // Mengubah dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.tipe_wisata());
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);
                String idtipewisata = jsonChildNode.optString("id_tipe_wisata");
                String namatipewisata= jsonChildNode.optString("nama_tipe");
                list_nama_tipe_wisata.add(namatipewisata);
                list_id_tipe_wisata.add(idtipewisata);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void kosong() {
        imgviewfoto.setImageResource(0);
       // ednamawisata.setText(null);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(wisata_baru.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();


                            } else {
                                Toast.makeText(wisata_baru.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(wisata_baru.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, ednamawisata.getText().toString().trim());
                params.put(KEY_NAME_WISATA, ednamawisata.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //code
            Log.d("mLastLocation", String.valueOf(mLastLocation));
            mLastLocation = location;
            edlatitude.setText(String.valueOf(location.getLatitude()));
            edlongitude.setText(String.valueOf(location.getLongitude()));

            lat1 = String.valueOf(location.getLatitude());
            long1 = String.valueOf(location.getLongitude());
            hidepDialog();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("onStatusChanged", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("onProviderDisabled", "onProviderDisabled");
            //turns off gps services
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


    private void selectImage() {
        imgviewfoto.setImageResource(0);
        final CharSequence[] items = {"Kamera", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(wisata_baru.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Kamera")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(wisata_baru.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imgviewfoto.setImageBitmap(decoded);
    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DeKa");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_DeKa_" + timeStamp + ".jpg");

        return mediaFile;
    }
    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {

            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }
}
