package com.wisbalam.server.koneksi;

/**
 * Created by Anonymous on 09/11/2017.
 */

public class DbActivity extends KoneksiActivity {
    // Source Code untuk URL -> URL menggunakan IP Address yang didapat dari komputer / laptop
    String URL = "http://simanjaonline.com/api.php";
    String url = "";
    String response = "";


    public String view_wisata(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=view_wisata&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String semua_wisata(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=semua_wisata&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String detail_image(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=image&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }


    public String wisata_cagaralam(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=wisata_cagaralam&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String wisata_sejarah(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=wisata_sejarah&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String wisata_kuliner(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=wisata_kuliner&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String wisata_belanja(String search) {

        search = search.replace(" ","%20");
        try {
            url = URL + "?operasi=wisata_belanja&data="+ search;
            System.out.println("URL Tampil Wisata : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String kecamatan() {
        try {
            url = URL + "?operasi=kecamatan";
            System.out.println("URL Tampil kecamatan : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String detail_wisata(String id) {
        id = id.replace(" ","%20");
        try {
            url = URL + "?operasi=detail_wisata&id="+ id;

            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String tipe_wisata() {
        try {
            url = URL + "?operasi=tipe_wisata";

            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }


    public String save_wisata_baru (String nama_wisata, String alamat, String latitude, String longitude, String id_kecamatan, String
            id_tipe_wisata, String biaya_masuk, String fasilitas_wisata, String foto_wisata, String tgl_survei, String deskripsi) {
        nama_wisata = nama_wisata.replace(" ","%20");
        alamat = alamat.replace(" ","%20");
        latitude = latitude.replace(" ","%20");
        longitude = longitude.replace(" ","%20");
        id_kecamatan = id_kecamatan.replace(" ","%20");
        id_tipe_wisata = id_tipe_wisata.replace(" ","%20");
        biaya_masuk = biaya_masuk.replace(" ","%20");
        fasilitas_wisata = fasilitas_wisata.replace(" ","%20");
        foto_wisata = foto_wisata.replace(" ","%20");
        tgl_survei = tgl_survei.replace(" ","%20");
        deskripsi = deskripsi.replace(" ","%20");
        try {
            url = URL + "?operasi=save_wisata_baru&nama_wisata=" + nama_wisata + "&alamat=" + alamat + "&latitude="+ latitude + "&longitude="
            +longitude+"&id_kecamatan="+id_kecamatan+"&id_tipe_wisata="+id_tipe_wisata+"&biaya_masuk="+biaya_masuk+"&fasilitas_wisata="
            +fasilitas_wisata+"&foto_wisata="+foto_wisata+"&tgl_survei="+tgl_survei+"&deskripsi="+deskripsi ;
            System.out.println("URL view : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }
    public String view_toko (String toko) {
        toko = toko.replace(" ","%20");
        // jk = jk.replace(" ","%20");
        try {
            url = URL + "?operasi=view_toko&toko=" + toko ;
            System.out.println("URL view_toko : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }
    public String view_barang (String id_toko, String nama_barang) {
       // lokasi_terdekat_oleholeh = lokasi_terdekat_oleholeh.replace(" ","%20");
       id_toko = id_toko.replace(" ","%20");
        nama_barang = nama_barang.replace(" ","%20");
        try {
            url = URL + "?operasi=view_barang&id_toko=" +id_toko+"&nama_barang=" + nama_barang;
            System.out.println("URL view barang : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String save_t_penjualan (String id_barang, String jumlah, String id_user, String id_toko) {

        id_toko = id_toko.replace(" ","%20");
        id_user = id_user.replace(" ","%20");
        jumlah = jumlah.replace(" ","%20");
        id_barang = id_barang.replace(" ","%20");
        try {
            url = URL + "?operasi=save_t_penjualan&id_toko=" +id_toko+"&id_user=" + id_user+"&jumlah=" + jumlah+"&id_barang=" + id_barang;

            System.out.println("URL view save barang penjualan : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }

    public String view_map_list (String id) {
        // lokasi_terdekat_oleholeh = lokasi_terdekat_oleholeh.replace(" ","%20");
        id = id.replace(" ","%20");
        try {
            url = URL + "?operasi=view_map_list&id=" +id ;
            System.out.println("URL view_map_dr_list : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }
    public String view_toko_kategori (String kategori, String toko) {
        kategori = kategori.replace(" ","%20");
        toko = toko.replace(" ","%20");
        try {
            url = URL + "?operasi=view_toko_kategori&kategori=" +kategori+ "&toko=" + toko;
            System.out.println("URL view_rs_dr : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }
    public String search_view_spesialis (String  nama) {
        nama = nama.replace(" ","%20");
        try {
            url = URL + "?operasi=search_view_spesialis&nama=" +nama;
            System.out.println("URL view_rs_dr : " + url);
            response = call(url);
        }
        catch (Exception e) {
        }
        return response;
    }



}