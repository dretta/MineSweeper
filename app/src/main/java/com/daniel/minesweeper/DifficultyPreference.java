package com.daniel.minesweeper;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Daniel on 1/18/2015.
 */
public class DifficultyPreference extends ListPreference implements Preference.OnPreferenceChangeListener{

    public DifficultyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DifficultyPreference(Context context) {
        super(context);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value)
    {
        String textValue = value.toString();

        DifficultyPreference listPreference = (DifficultyPreference)findPreferenceInHierarchy("difficulty_");
        int index = listPreference.findIndexOfValue(textValue);

        CharSequence[] entries = listPreference.getEntries();

        //Log.d("Seq",entries.toString());
        //Log.d("index",""+index);

        return true;
    }


}
