package com.wisbalam.server.list;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.wisbalam.server.R;
import com.wisbalam.server.adapter.adapter_semua_wisata;
import com.wisbalam.server.koneksi.DbActivity;
import com.wisbalam.server.recycler.recycler_semua_wisata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class wisata_cagaralam extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DbActivity dbActivity = new DbActivity();

    JSONArray arraydata;

    final ArrayList<String> list_idwisata = new ArrayList<String>();
    final ArrayList<String> list_namawisata = new ArrayList<String>();
    SearchView search;
    RecyclerView recyclerView;
    ArrayList<adapter_semua_wisata> dataItem = new ArrayList<>();
    recycler_semua_wisata adapter ;
    AlertDialog.Builder dialog;
    View dialogView;
    LayoutInflater inflater;
    TextView txtnama;
    EditText edqty;
    String cvnama,cvqty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_semua_wisata);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);

        // Jika SDK Android diatas API Ver.9
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        viewdata("");
        txtnama = (TextView) findViewById(R.id.tvnama);
        txtnama.setText("Wisata Cagar Alam/ Wisata Alam Adalah Taman Wisata Atau Hutan Wisata Yang Memiliki Keindahan Alam Baik Keindahan Flora, Fauna, Maupun Alama " +
                "Itu Sendiri Yang Mempunyai Corak Khas Yang Dimanfaatkan Untuk Kepentingan Rekreasi Dan Kebudayaan");

        recyclerView.setHasFixedSize(true);

        Configuration orientation = new Configuration();
        if(this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else if (this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        //layoutManager = new GridLayoutManager(this, 2);
        adapter=new recycler_semua_wisata(dataItem,wisata_cagaralam.this);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new recycler_semua_wisata.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onItemClick(int position) {
            //   Toast.makeText(semua_wisata.this, list_namabarang.get(position) + " id_toko " + id_toko +" id user "
             //          + kiduser,Toast.LENGTH_SHORT).show();
             //   Toast.makeText(semua_wisata.this, list_idwisata.get(position), Toast.LENGTH_SHORT).show();
               Intent godata = new Intent(wisata_cagaralam.this, detail_wisata.class);
               godata.putExtra("id",list_idwisata.get(position));
               startActivity(godata);
            }

        });



    }

    public void RemoveItem(int position){


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchmenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchmenuItem.getActionView();
        searchView.setQueryHint("Cari Wisata");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public void viewdata(String search){
        try {
            // Mengubah dari DbActivity yang berupa String menjadi array
            arraydata = new JSONArray(dbActivity.wisata_cagaralam(search));
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject jsonChildNode = arraydata.getJSONObject(i);
                String idwisata = jsonChildNode.optString("id_wisata");
                String namawisata= jsonChildNode.optString("nama_wisata");
                String foto= jsonChildNode.optString("name");

                list_idwisata.add(idwisata);
                list_namawisata.add(namawisata);
                dataItem.add(new adapter_semua_wisata(namawisata,foto));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        dataItem.clear();
        adapter.notifyDataSetChanged();
         // viewdata.clear();
        list_idwisata.clear();
        viewdata(s);
        return false;

    }
}