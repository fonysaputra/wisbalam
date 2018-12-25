package com.wisbalam.server.koneksi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class KoneksiActivity {

    // Mendapatkan nilai bit yang diperlukan
    public String call(String url) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        // Membaca nilai bit menjadi nilai character
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        int charRead;
        String string = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                string += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return string;
    }

    // Membuka URL dan meminta respon dari inputStreamReader
    private InputStream OpenHttpConnection(String url) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url1 = new URL(url);
        URLConnection conn = url1.openConnection();
        if (!(conn instanceof HttpURLConnection)) throw new IOException("Not An Http Connection");
        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection) conn;
            httpurlconnection.setAllowUserInteraction(false);
            httpurlconnection.setInstanceFollowRedirects(true);
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.connect();
            response = httpurlconnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpurlconnection.getInputStream();
            }
        } catch (Exception e) {
            throw new IOException("Error Connecting");
        }
        return in;
    }}