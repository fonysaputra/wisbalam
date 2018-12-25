package com.wisbalam.server.adapter;


public class adapter_semua_wisata {

    String namawisata;

    private String imageUrl;

    public adapter_semua_wisata(String namawisata, String imageUrl) {
        this.namawisata = namawisata;
        this.imageUrl = imageUrl;
    }

    public String getNamawisata() {
        return namawisata;
    }

    public void setNamawisata(String namawisata) {
        this.namawisata = namawisata;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
