package com.wisbalam.server.maps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.wisbalam.server.koneksi.DbActivity;
import com.wisbalam.server.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMaps extends AppCompatActivity {

    private AlertDialog.Builder builder1;
    private TextView namaRs;
    private Button lihatPeta,hubungi;

    DbActivity dbActivity = new DbActivity();

    private String latlang, latlang2, kategori, namaJalan,rs, nama,lat1,lang1,alamat ,tlp;

    JSONArray arraydata;

    ArrayAdapter<String> adapter1;
    final ArrayList<String> list_toko = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_maps);


        lihatPeta = (Button)findViewById(R.id.btn_lihatPeta);
        namaRs = (TextView)findViewById(R.id.tvNm_rs);
        hubungi= (Button) findViewById(R.id.btn_hubungi);

        Intent intent = getIntent();
        latlang = intent.getExtras().getString("latlang");
        latlang2 = intent.getExtras().getString("latlang2");
        namaJalan = intent.getExtras().getString("jalan");
        nama = intent.getExtras().getString("nama");
        lat1 = intent.getExtras().getString("lat");
        lang1 = intent.getExtras().getString("lang");

        rs = intent.getExtras().getString("nama");
        alamat = intent.getExtras().getString("alamat");
        kategori = intent.getExtras().getString("kategori");

        namaRs.setText(nama);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try {
            // Mengubah list_oleholeh dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.view_toko_kategori(kategori,rs));
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);

                String nama_toko= jsonChildNode.optString("nama");
                String jam= jsonChildNode.optString("jam");
                String no_telp= jsonChildNode.optString("no_telp");
                String alamat= jsonChildNode.optString("alamat");
                list_toko.add("Nama Toko : "+ nama_toko);
                list_toko.add("Jam Buka : "+jam);
                list_toko.add("No Telpon : "+no_telp);
                list_toko.add("Alamat : "+alamat);
                tlp=no_telp;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        init2();


        builder1 = new AlertDialog.Builder(this);


        lihatPeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailMaps.this, LihatPeta.class);
                i.putExtra("nama",nama);
                i.putExtra("lat",lat1);
                i.putExtra("lang",lang1);
                i.putExtra("lat1",latlang);
                i.putExtra("lang1",latlang2);

                i.putExtra("alamat",alamat);
                startActivity(i);
            }
        });
        hubungi.setOnClickListener(new Button.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tlp));
                startActivity(intent);
            }
        });
    }
    private void init2() {

        ListView ls = (ListView) findViewById(R.id.list);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_toko);
        ls.setAdapter(adapter1);
    }

}
