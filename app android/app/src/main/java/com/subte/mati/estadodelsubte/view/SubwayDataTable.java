package com.subte.mati.estadodelsubte.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.subte.mati.estadodelsubte.R;
import com.subte.mati.estadodelsubte.model.Subway;

import java.lang.reflect.Type;
import java.util.List;

public class SubwayDataTable {

    private Context context;
    private LinearLayout container;
    private TextView errorField;
    private TextView updatedField;

    public SubwayDataTable(Context context, LinearLayout container){
        this.context = context;
        this.container = container.findViewById(R.id.dataTable);
        this.errorField = container.findViewById(R.id.textError);
        this.updatedField = container.findViewById(R.id.textLastUpdated);
    }

    public void hide() {
        this.container.setVisibility(View.GONE);
        this.errorField.setVisibility(View.GONE);
        this.updatedField.setVisibility(View.GONE);
    }

    public void fillAndShow(String data, String error) {
        this.container.removeAllViews();
        if (!error.equals("")){
            this.setError(error);
            return;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Subway>>(){}.getType();
        List<Subway> subways = gson.fromJson(data, type);

        if (subways.get(0).error()){
            this.setError("No se pudo actualizar los datos");
        }

        for (Subway subway: subways){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate(R.layout.subway_state_data, this.container, false);

            TextView line = view.findViewById(R.id.line);
            line.setText("Subte " + subway.getLine());
            this.setIcon(line, subway.getLine());


            TextView state = view.findViewById(R.id.state);
            state.setText(subway.getState());
            state.setTextColor(subway.getColor());

            this.container.addView(view);
        }
        this.container.setVisibility(View.VISIBLE);
        this.updatedField.setText("Actualizado: " + subways.get(0).getUpdatedDate());
        this.updatedField.setVisibility(View.VISIBLE);
    }

    private void setIcon(TextView text, String line) {
        String name = "ic_subway_" + line.toLowerCase();
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        Drawable icon = AppCompatResources.getDrawable(context, id);
        text.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private void setError(String error){
        this.errorField.setText(error);
        this.errorField.setVisibility(View.VISIBLE);
    }
}
