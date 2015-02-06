package com.daniel.minesweeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import android.widget.TextView;

/**
 * Created by Daniel on 11/13/2014.
 */

public class MButton extends Button {

    public static boolean debug = false;
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
            setText("");
            setBackgroundResource(R.drawable.flag);
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
        if(mine){
            setText("");
            setBackgroundResource(R.drawable.mine);
        }
        else if(hasAdjacentMines())
            setText(""+adjacentMines);
        else
            setText("");
    }

    public int getAdjacentMines(){
        return adjacentMines;
    }

    public boolean hasAdjacentMines(){ return getAdjacentMines() > 0;}

    private void openAdjacentButtons()throws Exception{

        GridLayout gridLayout = gridFragment.gridLayout;
        int rows = gridLayout.getRowCount();
        int columns = gridLayout.getColumnCount();
        MButton m;
        if((num/columns) > 0){
            m = ((MButton)gridLayout.getChildAt(num-columns));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num-columns)+" is null, coming from "+ num);
        }
        if((num/columns) < rows-1){
            m = ((MButton)gridLayout.getChildAt(num+columns));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num+columns)+" is null, coming from "+ num);
        }
        if((num%columns) > 0){
            m = ((MButton)gridLayout.getChildAt(num-1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num-1)+" is null, coming from "+ num);
        }
        if((num%columns) < columns-1){
            m = ((MButton)gridLayout.getChildAt(num+1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num+1)+" is null, coming from "+ num);
        }
        if( ((num/columns) > 0)&&((num%columns) > 0) ){
            m = ((MButton)gridLayout.getChildAt(num-columns-1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num-columns-1)+" is null, coming from "+ num);
        }
        if( ((num/columns) < rows-1)&&((num%columns) > 0) ){
            m = ((MButton)gridLayout.getChildAt(num+columns-1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num+columns-1)+" is null, coming from "+ num);
        }
        if( ((num/columns) > 0)&&((num%columns) < columns-1) ){
            m = ((MButton)gridLayout.getChildAt(num-columns+1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num-columns+1)+" is null, coming from "+ num);
        }
        if( ((num/columns) < rows-1)&&((num%columns) < columns-1) ){
            m = ((MButton)gridLayout.getChildAt(num+columns+1));
            if(m!=null)m.revealButton();
            else throw new Exception("Button at "+(num+columns+1)+" is null, coming from "+ num);
        }

    }

    public void revealButton() throws Exception {
        if (state != State.OPENED){
            state = State.OPENED;
            gridFragment.unOpenedButtons--;
            setBackgroundResource(R.drawable.tile3);
            displayMines();
            if (!isMine() && !hasAdjacentMines())
                openAdjacentButtons();
            else if(isMine()){
                //Log.d("","gameLost");
                gridFragment.gameLost();
            }


        }
    }

    public void setTextColor() {
        switch(adjacentMines) {
            case 1:
                super.setTextColor(Color.BLUE);
                break;
            case 2:
                super.setTextColor(Color.GREEN);
                break;
            case 3:
                super.setTextColor(Color.RED);
                break;
            case 4:
                super.setTextColor(Color.rgb(0,0,128));
                break;
            case 5:
                super.setTextColor(Color.rgb(128,0,0));
                break;
            case 6:
                super.setTextColor(Color.rgb(64,224,208));
                break;
            case 7:
                super.setTextColor(Color.BLACK);
                break;
            case 8:
                super.setTextColor(Color.LTGRAY);
                break;
            default:
                super.setTextColor(Color.WHITE);
                break;
        }
    }

    private void create(int i, boolean m){
        state = State.NORMAL;
        num = i;
        mine = m;
        setBackgroundResource(R.drawable.tile);
        setLayoutParams(new LinearLayout.LayoutParams(150,150));
        setTypeface(Typeface.createFromAsset(mainActivity.getAssets(), "fonts/Comic-Sans.ttf"));
        setTextColor();

        if(MainActivity.debug) {
            if (mine) {
                setText("m");
            }
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
                                        try {
                                            revealButton();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                    case UNKNOWN:{
                                        state = State.FLAGGED;
                                        gridFragment.remainingMines--;
                                        if(isMine()){
                                            gridFragment.flagsOnMines++;
                                        }
                                        mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
                                        setText("");
                                        v.setBackgroundResource(R.drawable.flag);
                                        break;
                                    }
                                    case FLAGGED:{
                                        state = State.UNKNOWN;
                                        gridFragment.remainingMines++;
                                        if(isMine()){
                                            gridFragment.flagsOnMines--;
                                        }
                                        mainActivity.setText(gridFragment.remainingMines,mainActivity.mineCount);
                                        setText("");
                                        v.setBackgroundResource(R.drawable.question_mark);
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
