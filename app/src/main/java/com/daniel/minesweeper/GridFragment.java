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
        RelativeLayout grid = (RelativeLayout) view.findViewById(R.id.grid);
        //RelativeLayout.LayoutParams gParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().heightPixels/2);
        RelativeLayout main = (RelativeLayout)view.findViewById(R.id.fragment_container);
        Display display = (((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        Log.d("grid",""+height);
        //int m = main.getHeight();
        //Log.d("grid",""+m);
        RelativeLayout.LayoutParams gParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        grid.setLayoutParams(gParams);
        grid.setBackgroundColor(Color.RED);
        return view;
    }


}
