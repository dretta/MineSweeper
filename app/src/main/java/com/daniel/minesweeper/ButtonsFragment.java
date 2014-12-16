package com.daniel.minesweeper;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class ButtonsFragment extends Fragment {


    public ButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        RelativeLayout buttons = (RelativeLayout) view.findViewById(R.id.buttons);
        //RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().heightPixels/2);
        RelativeLayout main = (RelativeLayout)getActivity().findViewById(R.id.fragment_container);
        Display display = (((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        Log.d("button",""+height);
        //int h = ((Menu)getActivity().findViewById(R.menu.menu_main));
        int m = (height-48)/2;//(height - h)/2;
        Log.d("buttons", "" + m);

        RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        buttons.setLayoutParams(bParams);
        buttons.setBackgroundColor(Color.BLUE);

        return view;
    }


}
