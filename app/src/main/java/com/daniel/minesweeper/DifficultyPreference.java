package com.daniel.minesweeper;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Daniel on 1/18/2015.
 */
public class DifficultyPreference extends ListPreference {

    public DifficultyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DifficultyPreference(Context context) {
        super(context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == 3){
            Log.d("Custom", "click");
        }
        else
            super.onClick(dialog, which);
    }
}
