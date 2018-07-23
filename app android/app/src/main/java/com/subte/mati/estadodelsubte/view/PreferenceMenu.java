package com.subte.mati.estadodelsubte.view;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.subte.mati.estadodelsubte.R;


public class PreferenceMenu extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configmenu);

        PreferenceScreen screen = this.getPreferenceScreen();
        final Context context = screen.getContext();

        PreferenceCategory notificationsCategory = new PreferenceCategory(context);
        notificationsCategory.setTitle(R.string.notificationsCategory);
        screen.addPreference(notificationsCategory);

        String[] subways = {"A", "B", "C", "D", "E", "H", "P"};

        for (int i = 0; i < subways.length; i++) {
            CheckBoxPreference checkbox = new CheckBoxPreference(context);
            checkbox.setChecked(false);
            checkbox.setDefaultValue(false);
            checkbox.setKey(subways[i]);
            checkbox.setTitle("Subte " + subways[i]);
            checkbox.setSummary("Recibe notificaciones del subte " + subways[i]);

            notificationsCategory.addPreference(checkbox);
        }

    }
}
