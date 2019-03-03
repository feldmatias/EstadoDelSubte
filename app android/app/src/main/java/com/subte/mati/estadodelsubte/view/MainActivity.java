package com.subte.mati.estadodelsubte.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.subte.mati.estadodelsubte.DataRetriever;
import com.subte.mati.estadodelsubte.R;

public class MainActivity extends AppCompatActivity {

    private SubwayDataTable data;
    private View progressBar;
    private View updateButton;
    private DataRetriever dataRetriever = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic(this.getString(R.string.firebaseNotificatiosnTopic));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout dataTable = findViewById(R.id.data);
        this.data = new SubwayDataTable(this, dataTable);
        this.progressBar = findViewById(R.id.layoutUpdating);
        this.updateButton = findViewById(R.id.updateButton);
        this.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startDataRetriever(true);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.startDataRetriever(false);
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.stopDataRetriever();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDataRetriever(boolean refresh){
        if (this.dataRetriever == null || this.dataRetriever.getStatus() == AsyncTask.Status.FINISHED){
            this.dataRetriever = new DataRetriever(this, progressBar, data, updateButton, refresh);
            this.dataRetriever.execute();
        }
    }

    private void stopDataRetriever(){
        if (this.dataRetriever != null){
            this.dataRetriever.cancel(true);
            this.dataRetriever = null;
        }
    }
}
