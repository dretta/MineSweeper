package com.daniel.minesweeper;

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
    String num;
    final float scale = getContext().getResources().getDisplayMetrics().density;

    //runs without a timer by reposting this handler at the end of the runnable

    static Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long milliseconds = System.currentTimeMillis() - startTime;
            longPress = false;
            if(milliseconds >= 1500) {
                longPressTile();
                timerHandler.removeCallbacks(timerRunnable);
            }
            else
                timerHandler.postDelayed(this, 0);
        }
    };

    public MButton(Context context,String t, int x, int y) {
        super(context);
        create(t, x, y);
    }

    public MButton(Context context, AttributeSet attrs, String t, int x, int y) {
        super(context, attrs);
        create(t, x, y);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr, String t, int x, int y) {
        super(context, attrs, defStyleAttr);
        create(t, x, y);
    }

    private void longPressTile(){
        longPress = true;
        setBackgroundResource(R.drawable.tile);
        if(state == State.FLAGGED || state == State.UNKNOWN){
            state = State.NORMAL;
            this.setText(num);
        }
        else if(state == State.NORMAL){
            state = State.FLAGGED;
            this.setText("F");
        }
    }


    private boolean isInGrayArea(float xCoord, float yCoord){
        int xValue = (int) ((xCoord/scale)+0.5);
        int yValue = (int) ((yCoord/scale)+0.5);
        Log.d("button"+num,"("+xValue+","+yValue+")");
        Log.d("size","("+Integer.toString(getWidth())+","+Integer.toString(getHeight())+")");
        return (xValue >= 5)&&(xValue < 45)&&(yValue >= 5)&&(yValue < 45);
    }

    private void create(String text, int x, int y){
        state = State.NORMAL;
        num = text;
        setText(num);
        setBackgroundResource(R.drawable.tile);
        setLayoutParams(new LinearLayout.LayoutParams(150,150));
        //setMinimumHeight(5);
        //setMinimumWidth(5);
        //setPadding(10,10,10,10);
        //Log.d("Height",Integer.toString(getHeight()));
        //Log.d("Width",Integer.toString(getWidth()));

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if(isInGrayArea(me.getX(), me.getY())){
                    switch (me.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            if (state != State.OPENED) {
                                startTime = System.currentTimeMillis();
                                v.setBackgroundResource(R.drawable.tile2);
                                timerHandler.postDelayed(timerRunnable, 0);
                            }
                            return true;
                        }
                        case MotionEvent.ACTION_UP: {
                            if (!longPress) {
                                if (state == State.NORMAL) {
                                    state = State.OPENED;
                                    setText(num);
                                    v.setBackgroundResource(R.drawable.tile3);
                                } else if (state == State.FLAGGED) {
                                    state = State.UNKNOWN;
                                    v.setBackgroundResource(R.drawable.tile);
                                    setText("?");
                                } else if (state == State.UNKNOWN) {
                                    state = State.FLAGGED;
                                    v.setBackgroundResource(R.drawable.tile);
                                    setText("F");
                                }
                            }
                            timerHandler.removeCallbacks(timerRunnable);
                            return true;
                        }
                    }
                }
                return false;
            }

        });



        /*
        this.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                v.setBackgroundResource(R.drawable.tile3);
                return true;
            }
        });
        */
    }




    /*
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 10, 50, 10, new Paint(Color.BLACK));
        canvas.drawLine(0, 0, 0, 500, new Paint(Color.BLACK));
        canvas.drawLine(0, 0, 350, 350, new Paint(Color.BLACK));
    }
    */
}
