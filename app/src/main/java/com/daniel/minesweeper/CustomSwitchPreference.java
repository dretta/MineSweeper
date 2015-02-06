package com.daniel.minesweeper;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

/**
 * Created by Daniel on 1/19/2015.
 */
public class CustomSwitchPreference extends SwitchPreference {

    /*TODO: Figure out how to save the value of the Switch*/

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateView( ViewGroup parent )
    {
        //return super.onCreateView(parent);

        LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return li.inflate( R.layout.customswitch_preference, parent, false);
    }

    @Override
    protected void onBindView(View view) {
        MainActivity mainActivity = (MainActivity)getContext();
        RelativeLayout relativeLayout = (RelativeLayout)mainActivity.findViewById(R.id.switch_frame);
        Switch s = (Switch)relativeLayout.getChildAt(1);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                persistBoolean(isChecked);
            }
        });
        super.onBindView(view);
    }
}
