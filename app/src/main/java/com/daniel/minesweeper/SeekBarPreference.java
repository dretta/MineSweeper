package com.daniel.minesweeper;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Daniel on 1/18/2015.
 */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {

    /*TODO: Figure out how get the value from the SeekBar*/

    RelativeLayout relativeLayout;
    //SeekBar sb;
    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarPreference(Context context) {
        super(context);
    }



    @Override
    protected View onCreateView( ViewGroup parent ){
        relativeLayout = ((RelativeLayout)(((MainActivity) getContext()).findViewById(R.id.seekBar_frame)));
        //sb = (SeekBar)(((RelativeLayout)(((MainActivity) getContext()).findViewById(R.id.seekBar_frame))).getChildAt(1));
        LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return li.inflate( R.layout.seekbar_preference, parent, false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        ((TextView)relativeLayout.getChildAt(2)).setText(seekBar.getScrollX() + "ms");

        persistInt(progress);
        callChangeListener( progress );
        seekBar.setProgress( progress );
        (((TextView)relativeLayout.getChildAt(2))).setText(progress + "ms");
        //updatePreference( progress );
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        ((TextView)relativeLayout.getChildAt(2)).setText(seekBar.getScrollX()+"ms");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        ((TextView)relativeLayout.getChildAt(2)).setText(seekBar.getScrollX()+"ms");

    }

}
