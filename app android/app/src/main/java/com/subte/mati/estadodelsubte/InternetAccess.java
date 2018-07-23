package com.subte.mati.estadodelsubte;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class InternetAccess {

    private static InternetAccess instance = null;

    private InternetAccess(){}

    public static InternetAccess getInstance(){
        if (instance == null){
            instance = new InternetAccess();
        }
        return instance;
    }

    public boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public String getJsonFromUrl(String link, Context context) throws Exception{
        HttpURLConnection urlConnection = null;
        StringBuffer response = new StringBuffer();
        if (!this.isConnected(context)){
            throw new Exception("No está conectado a internet");
        }
        try {
            URL url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();

            if(statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error en la conexión con el servidor");
            }
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            String inputLine;

            while ((inputLine = inputStream.readLine()) != null){
                response.append(inputLine);
            }
            inputStream.close();
        } catch (IOException e){
            throw new Exception("Error inesperado");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response.toString();
    }

}
