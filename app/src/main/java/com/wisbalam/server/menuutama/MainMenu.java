package com.wisbalam.server.menuutama;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.wisbalam.server.Bantuan;
import com.wisbalam.server.R;
import com.wisbalam.server.Tentang;
import com.wisbalam.server.helper.PermissionHelper;
import com.wisbalam.server.list.semua_wisata;
import com.wisbalam.server.list.wisata_baru;
import com.wisbalam.server.list.wisata_belanja;
import com.wisbalam.server.list.wisata_cagaralam;
import com.wisbalam.server.list.wisata_kuliner;
import com.wisbalam.server.list.wisata_sejarah;
import com.wisbalam.server.maps.MapsAllWisata;
import com.wisbalam.server.splash;

import com.wisbalam.server.helper.PermissionHelper;
public class MainMenu extends AppCompatActivity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.foto1, R.drawable.foto2, R.drawable.foto3, R.drawable.foto4};
    ImageView lihatpeta,tentang,wisatabaru,bantuan,keluar,sejarah,alam,laut,kuliner,belanja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        lihatpeta=(ImageView) findViewById(R.id.lihatPeta);
        tentang=(ImageView) findViewById(R.id.tentang);
        wisatabaru=(ImageView) findViewById(R.id.wisatabaru);
        bantuan=(ImageView) findViewById(R.id.bantuan);
        keluar=(ImageView) findViewById(R.id.keluar);
        sejarah=(ImageView) findViewById(R.id.sejarah);
        alam=(ImageView) findViewById(R.id.alam);
        laut=(ImageView) findViewById(R.id.bahari);
        kuliner=(ImageView) findViewById(R.id.kuliner);
        belanja=(ImageView) findViewById(R.id.belanja);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        lihatpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MapsAllWisata.class);
                // in.putExtra("id_user", id_user);
                startActivity(in);
            }
        });

        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Tentang.class);
                // in.putExtra("id_user", id_user);
                startActivity(in);
            }
        });
        wisatabaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), wisata_baru.class);
                // in.putExtra("id_user", id_user);
                startActivity(i);
            }
        });
        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), Bantuan.class);
                // in.putExtra("id_user", id_user);
                startActivity(n);
            }
        });

        sejarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(getApplicationContext(), wisata_sejarah.class);
                startActivity(sw);
            }
        });

        alam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(getApplicationContext(), wisata_cagaralam.class);
                startActivity(sw);
            }
        });

        laut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(getApplicationContext(), semua_wisata.class);
                startActivity(sw);
            }
        });

        kuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(getApplicationContext(), wisata_kuliner.class);
                startActivity(sw);
            }
        });

        belanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(getApplicationContext(), wisata_belanja.class);
                startActivity(sw);
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Apakah Anda Ingin Keluar Dari Aplikasi Ini?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



}