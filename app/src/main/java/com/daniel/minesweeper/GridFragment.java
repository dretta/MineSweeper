package com.daniel.minesweeper;


import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GridFragment extends Fragment {

    GridLayout gridLayout;
    public enum GameState{READY, PLAYING, WIN, LOSE}
    GameState gameState;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gameState = GameState.READY;
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        gridLayout = (GridLayout)view.findViewById(R.id.grid);
        MButton[] buttons = generateGrid(144,25);
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
            mButton = new MButton(getActivity(),i, numMines > x);
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

    public void startGame(){
            gameState = GameState.PLAYING;
    }

    public void gameOver(){
        gameState = GameState.LOSE;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            MButton mb = (MButton)gridLayout.getChildAt(i);
            mb.setOnTouchListener(null);
        }

        ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
        ScrollView vertical = (ScrollView)getActivity().findViewById(R.id.verticalScroll);
        HorizontalScrollView horizontal = (HorizontalScrollView)getActivity().findViewById(R.id.horizontalScroll);
        float xScale = Math.max((float) getActivity().findViewById(R.id.fragment_container).getWidth() / gridLayout.getWidth(), params.width);
        float yScale = Math.max((float) getActivity().findViewById(R.id.fragment_container).getHeight() / gridLayout.getWidth(), params.height);
        gridLayout.setScaleX(xScale);
        gridLayout.setScaleY(yScale);
        Log.d("(ScrollScaleX,GridPivotX)","("+horizontal.getScrollX()+","+gridLayout.getScaleX()+")");
        Log.d("(ScrollScaleY,GridPivotY)","("+vertical.getScrollY()+","+gridLayout.getScaleY()+")");
        float xPivot = 2.5f*horizontal.getScrollX();
        float yPivot = 18.0f*vertical.getScrollY();
        Log.d("totalPivotX",""+xPivot);
        Log.d("totalPivotY",""+yPivot);
        gridLayout.setPivotX(xPivot);
        gridLayout.setPivotY(yPivot);
        vertical.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        horizontal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //((ScrollView)getActivity().findViewById(R.id.verticalScroll)).smoothScrollTo(0,0);
        //((HorizontalScrollView)getActivity().findViewById(R.id.horizontalScroll)).smoothScrollTo(0,0);
        //((ScrollView)getActivity().findViewById(R.id.verticalScroll)).
    }

}
