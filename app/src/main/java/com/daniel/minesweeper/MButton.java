package com.daniel.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Daniel on 11/13/2014.
 */
public class MButton extends Button {

    boolean pressed = false;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable

    static Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long milliseconds = System.currentTimeMillis() - startTime;

            if(milliseconds >= 1500) {
                pressTile();
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

    private void pressTile(){
        this.setBackgroundResource(R.drawable.tile3);
        pressed = true;
    }



    private void create(String text, int x, int y){
        this.setText(text);
        this.setBackgroundResource(R.drawable.tile);
        this.setHeight(50);
        this.setWidth(50);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                switch(me.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(!pressed) {
                            startTime = System.currentTimeMillis();
                            v.setBackgroundResource(R.drawable.tile2);
                            timerHandler.postDelayed(timerRunnable, 0);
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_UP:{
                        if(!pressed) {
                            v.setBackgroundResource(R.drawable.tile);
                            timerHandler.removeCallbacks(timerRunnable);
                        }
                        return true;
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
