package com.daniel.minesweeper;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class GridFragment extends Fragment {


    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        GridLayout gridLayout = (GridLayout)view.findViewById(R.id.grid);
        MButton[] buttons = new MButton[36];
        int i = 0;
        for(MButton button: buttons){
            button = new MButton(getActivity(),""+i,0,0);
            i++;
            gridLayout.addView(button);
        }



        return view;
    }


}
