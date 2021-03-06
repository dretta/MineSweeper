package com.daniel.minesweeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by Daniel on 11/13/2014.
 */

public class MButton extends Button {

    public enum State{NORMAL, OPENED, FLAGGED, UNKNOWN}
    State state;
    boolean longPress;
    long startTime = 0;
    int num;
    boolean mine;
    final float scale = getContext().getResources().getDisplayMetrics().density;
    int adjacentMines = 0;
    MainActivity mainActivity = (MainActivity)getContext();
    GridFragment gridFragment = (GridFragment)mainActivity.getFragmentManager().findFragmentByTag("gridFragment");
    ImageButton startButton = (ImageButton)(mainActivity.getActionBar()).getCustomView().findViewById(R.id.actionBarLogo);

    static Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long milliseconds = System.currentTimeMillis() - startTime;
            longPress = false;

            if(milliseconds >= 1000) {
                longPressTile();
                timerHandler.removeCallbacks(timerRunnable);
            }
            else
                timerHandler.postDelayed(this, 0);
        }
    };

    public MButton(Context context, int i, boolean m) {
        super(context);
        create(i, m);
    }

    public MButton(Context context, AttributeSet attrs, int i, boolean m) {
        super(context, attrs);
        create(i, m);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr, int i, boolean m) {
        super(context, attrs, defStyleAttr);
        create(i, m);
    }

    private void longPressTile(){
        longPress = true;
        setBackgroundResource(R.drawable.tile);
        startButton.setImageResource(R.drawable.smiley);
        if(state == State.FLAGGED){
            state = State.NORMAL;
            setText("");
            gridFragment.remainingMines++;
            if(isMine()){
                gridFragment.flagsOnMines--;
            }
            mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
        }
        else if(state == State.UNKNOWN){
            state = State.NORMAL;
            setText("");
        }
        else if(state == State.NORMAL){
            state = State.FLAGGED;
            setText("F");
            gridFragment.remainingMines--;
            if(isMine()){
                gridFragment.flagsOnMines++;
            }
            mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
        }
    }


    private boolean isInGrayArea(float xCoord, float yCoord){
        int xValue = (int) ((xCoord/scale)+0.5);
        int yValue = (int) ((yCoord/scale)+0.5);
        //Log.d("button"+num,"("+xValue+","+yValue+")");
        //Log.d("size","("+Integer.toString(getWidth())+","+Integer.toString(getHeight())+")");
        return (xValue >= 5)&&(xValue < 45)&&(yValue >= 5)&&(yValue < 45);
    }

    public void addAdjacentMines(){
        adjacentMines++;
    }

    public boolean isMine(){
        return mine;
    }

    public void displayMines(){
        if(mine)
            setText("M");
        else if(hasAdjacentMines())
            setText(""+adjacentMines);
        else
            setText("");
    }

    public int getAdjacentMines(){
        return adjacentMines;
    }

    public boolean hasAdjacentMines(){ return getAdjacentMines() > 0;}

    private void openAdjacentButtons(){

        GridLayout gridLayout = gridFragment.gridLayout;
        int rows = gridLayout.getRowCount();
        int columns = gridLayout.getColumnCount();
        if((num/columns) > 0){((MButton)gridLayout.getChildAt(num-columns)).revealButton();}
        if((num/columns) < rows-1){((MButton)gridLayout.getChildAt(num+columns)).revealButton();}
        if((num%rows) > 0){((MButton)gridLayout.getChildAt(num-1)).revealButton();}
        if((num%rows) < columns-1){((MButton)gridLayout.getChildAt(num+1)).revealButton();}
        if( ((num/columns) > 0)&&((num%rows) > 0) ){((MButton)gridLayout.getChildAt(num-columns-1)).revealButton();}
        if( ((num/columns) < rows-1)&&((num%rows) > 0) ){((MButton)gridLayout.getChildAt(num+columns-1)).revealButton();}
        if( ((num/columns) > 0)&&((num%rows) < columns-1) ){((MButton)gridLayout.getChildAt(num-columns+1)).revealButton();}
        if( ((num/columns) < rows-1)&&((num%rows) < columns-1) ){((MButton)gridLayout.getChildAt(num+columns+1)).revealButton();}

    }

    public void revealButton() {
        if (state != State.OPENED){
            state = State.OPENED;
            gridFragment.unOpenedButtons--;
            displayMines();
            if (!isMine() && !hasAdjacentMines())
                openAdjacentButtons();
            else if(isMine()){
                Log.d("","gameLost");
                gridFragment.gameLost();
            }
            setBackgroundResource(R.drawable.tile3);

        }
    }

    private void create(int i, boolean m){
        state = State.NORMAL;
        num = i;
        mine = m;
        setBackgroundResource(R.drawable.tile);
        setLayoutParams(new LinearLayout.LayoutParams(150,150));

        if(mine){
            setText("m");
        }


        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if(isInGrayArea(me.getX(), me.getY())){
                    switch (me.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            if (state != State.OPENED) {
                                if(gridFragment.gameState == GridFragment.GameState.READY)
                                    gridFragment.startGame();
                                startTime = System.currentTimeMillis();
                                v.setBackgroundResource(R.drawable.tile2);
                                startButton.setImageResource(R.drawable.smiley2);
                                timerHandler.postDelayed(timerRunnable, 0);
                            }
                            return true;
                        }
                        case MotionEvent.ACTION_UP: {
                            if (!longPress) {
                                startButton.setImageResource(R.drawable.smiley);
                                switch(state){
                                    case NORMAL:{
                                        //Log.d("","revealButton");
                                        revealButton();
                                        break;
                                    }
                                    case UNKNOWN:{
                                        state = State.FLAGGED;
                                        gridFragment.remainingMines--;
                                        if(isMine()){
                                            gridFragment.flagsOnMines++;
                                        }
                                        mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
                                        v.setBackgroundResource(R.drawable.tile);
                                        setText("F");
                                        break;
                                    }
                                    case FLAGGED:{
                                        state = State.UNKNOWN;
                                        gridFragment.remainingMines++;
                                        if(isMine()){
                                            gridFragment.flagsOnMines--;
                                        }
                                        mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
                                        v.setBackgroundResource(R.drawable.tile);
                                        setText("?");
                                    }
                                }
                            }
                            gridFragment.checkGameWin();
                            //Log.d("State",state.toString());
                            timerHandler.removeCallbacks(timerRunnable);
                            return true;
                        }
                    }
                }
                return false;
            }

        });
    }

}
