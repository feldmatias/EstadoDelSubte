package com.subte.mati.estadodelsubte;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.subte.mati.estadodelsubte.view.SubwayDataTable;

public class DataRetriever extends AsyncTask<Void, Void, String>{

    private Context context;
    private View progressBar;
    private View updateButton;
    private SubwayDataTable data;
    private String error;

    public DataRetriever(Context context, View progressBar, SubwayDataTable data, View updateButton){
        this.context = context;
        this.progressBar = progressBar;
        this.data = data;
        this.updateButton = updateButton;
        this.error = "";
    }

    @Override
    protected String doInBackground(Void... params){
        InternetAccess internet = InternetAccess.getInstance();
        String json = "";
        try {
            json = internet.getJsonFromUrl(context.getString(R.string.link), this.context);
        } catch (Exception e) {
            this.error = e.getMessage();
        }
        return json;
    }

    @Override
    protected void onPreExecute() {
        data.hide();
        progressBar.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
    }

    @Override
    protected void onPostExecute(String result){
        progressBar.setVisibility(View.GONE);
        data.fillAndShow(result, this.error);
        updateButton.setVisibility(View.VISIBLE);
    }
}
