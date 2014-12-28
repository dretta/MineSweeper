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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GridFragment extends Fragment {

    GridLayout gridLayout;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        gridLayout = (GridLayout)view.findViewById(R.id.grid);
        MButton[] buttons = generateGrid(100,25);
        for(MButton mButton:buttons){
            gridLayout.addView(mButton);
        }
        return view;
    }

    public MButton[] generateGrid(int gridSize, int numMines){
        Log.d("gridSize",""+gridSize);
        Log.d("numMines",""+numMines);
        ArrayList<Integer> array =  new ArrayList<Integer>();
        for (int i = 0; i < gridSize; i++) {
            array.add(new Integer(i));
        }
        Log.d("unsorted",array.toString());

        Collections.shuffle(array,new Random(System.currentTimeMillis()));
        MButton[] mButtons = new MButton[gridSize];
        MButton mButton;

        String numList = array.toString();
        Log.d("arraySize",""+array.size());
        Log.d("array",numList);

        for (int i = 0; i < gridSize; i++) {
            int x = array.get(i);
            mButton = new MButton(getActivity(),""+i, numMines > x);
            mButtons[i] = mButton;
        }
        return generateMineCount(mButtons);
    }

    private MButton[] generateMineCount(MButton[] mButtons){
        int rows = gridLayout.getRowCount();
        int columns = gridLayout.getColumnCount();
        for (int i = 0; i < mButtons.length; i++) {
            if((i/columns) > 0){if(mButtons[i-columns].isMine()){mButtons[i].addAdjacentMines();}}
            if((i/columns) < rows-1){if(mButtons[i+columns].isMine()){mButtons[i].addAdjacentMines();}}
            if((i%rows) > 0){if(mButtons[i-1].isMine()){mButtons[i].addAdjacentMines();}}
            if((i%rows) < columns-1){if(mButtons[i+1].isMine()){mButtons[i].addAdjacentMines();}}
            if( ((i/columns) > 0)&&((i%rows) > 0) ){if(mButtons[i-columns-1].isMine()){mButtons[i].addAdjacentMines();}}
            if( ((i/columns) < rows-1)&&((i%rows) > 0) ){if(mButtons[i+columns-1].isMine()){mButtons[i].addAdjacentMines();}}
            if( ((i/columns) > 0)&&((i%rows) < columns-1) ){if(mButtons[i-columns+1].isMine()){mButtons[i].addAdjacentMines();}}
            if( ((i/columns) < rows-1)&&((i%rows) < columns-1) ){if(mButtons[i+columns+1].isMine()){mButtons[i].addAdjacentMines();}}
            if(!mButtons[i].isMine() && mButtons[i].getAdjacentMines() > 0){mButtons[i].displayMines();}
        }
        return mButtons;
    }
}
