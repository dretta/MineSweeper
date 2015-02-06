package com.daniel.minesweeper;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Vibrator;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GridFragment extends Fragment {

    GridLayout gridLayout;
    public enum GameState{READY, PLAYING, WIN, LOSE}
    GameState gameState;
    long gameTime, milliseconds, savedTime;
    MainActivity mainActivity;
    int remainingMines, numOfMines, unOpenedButtons, flagsOnMines, numOfButtons, difficulty;
    float maxScrollX, maxScrollY;
    Vibrator v;

    static Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            milliseconds = System.currentTimeMillis() - gameTime;
            int seconds = (int) (milliseconds / 1000);
            if(seconds > 999)
                seconds = 999;
            TextView textView = mainActivity.gameTimer;


            mainActivity.setText(seconds, textView);
            timerHandler.postDelayed(this, 500);
        }
    };

    public GridFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mainActivity = (MainActivity)getActivity();
        gameState = GameState.READY;
        gameTime = 0;
        savedTime = 0;
        gridLayout = (GridLayout)view.findViewById(R.id.grid);
        difficultyValues(getArguments().getInt("difficulty"));
        //Log.d("(Buttons,Mines)","("+numOfButtons+","+numOfMines+")");
        MButton[] buttons = generateGrid(numOfButtons,numOfMines);

        unOpenedButtons = numOfButtons - numOfMines;
        v = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
        for(MButton mButton:buttons){
            gridLayout.addView(mButton);
        }
        return view;
    }

    public void difficultyValues(int d){
        difficulty = d;
        //Log.d("d",""+difficulty);
        switch(difficulty){
            case 1:{
                gridLayout.setColumnCount(16);
                gridLayout.setRowCount(16);
                numOfMines = 40;
                break;
            }
            case 2:{
                gridLayout.setColumnCount(16);
                gridLayout.setRowCount(30);
                numOfMines = 99;
                break;
            }
            default:{
                gridLayout.setColumnCount(9);
                gridLayout.setRowCount(9);
                numOfMines = 10;
            }
        }

        numOfButtons = gridLayout.getRowCount()*gridLayout.getColumnCount();
        remainingMines = numOfMines;
        mainActivity.setText(numOfMines,mainActivity.mineCount);
    }

    public MButton[] generateGrid(int gridSize, int numMines){
        //Log.d("gridSize",""+gridSize);
        //Log.d("numMines",""+numMines);
        ArrayList<Integer> array =  new ArrayList<Integer>();
        for (int i = 0; i < gridSize; i++) {
            array.add(new Integer(i));
        }
        //Log.d("unsorted",array.toString());

        Collections.shuffle(array,new Random(System.currentTimeMillis()));
        MButton[] mButtons = new MButton[gridSize];
        MButton mButton;

        String numList = array.toString();
        //Log.d("arraySize",""+array.size());
        //Log.d("array",numList);

        for (int i = 0; i < gridSize; i++) {
            int x = array.get(i);
            mButton = new MButton(getActivity(),i, numMines > x);
            mButtons[i] = mButton;
        }
        mButtons = generateMineCount(mButtons);
        for(MButton m :mButtons){
            m.setTextColor();
        }
        return mButtons;
    }

    private MButton[] generateMineCount(MButton[] mButtons){
        int rows = gridLayout.getRowCount();//10
        int columns = gridLayout.getColumnCount();//9
        //Log.d("(rows,columns)","("+rows+","+columns+")");
        for (int i = 0; i < mButtons.length; i++) {
            if((i/columns) > 0){if(mButtons[i-columns].isMine()){mButtons[i].addAdjacentMines();}}//North
            if((i/columns) < rows-1){if(mButtons[i+columns].isMine()){mButtons[i].addAdjacentMines();}}//South
            if((i%columns) > 0){if(mButtons[i-1].isMine()){mButtons[i].addAdjacentMines();}}//West
            if((i%columns) < columns-1){if(mButtons[i+1].isMine()){mButtons[i].addAdjacentMines();}}//East
            if( ((i/columns) > 0)&&((i%columns) > 0) ){if(mButtons[i-columns-1].isMine()){mButtons[i].addAdjacentMines();}}//NorthWest
            if( ((i/columns) < rows-1)&&((i%columns) > 0) ){if(mButtons[i+columns-1].isMine()){mButtons[i].addAdjacentMines();}}//SouthWest
            if( ((i/columns) > 0)&&((i%columns) < columns-1) ){if(mButtons[i-columns+1].isMine()){mButtons[i].addAdjacentMines();}}//NorthEast
            if( ((i/columns) < rows-1)&&((i%columns) < columns-1) ){if(mButtons[i+columns+1].isMine()){mButtons[i].addAdjacentMines();}}//SouthEast
            if(MainActivity.debug){if(!mButtons[i].isMine() && mButtons[i].getAdjacentMines() > 0){mButtons[i].displayMines();}}
        }
        return mButtons;
    }

    public void startGame(){
        gameState = GameState.PLAYING;
        gameTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        /*
        int textSize = (((MainActivity)getActivity()).findViewById(R.id.textViewLeft)).getWidth()/3;
        ViewGroup.LayoutParams params;
        TextView tLeft0 = (TextView)(((MainActivity) getActivity()).findViewById(R.id.topTextViewLeft0));
        TextView tLeft1 = (TextView)(((MainActivity) getActivity()).findViewById(R.id.topTextViewLeft1));
        TextView tLeft2 = (TextView)(((MainActivity) getActivity()).findViewById(R.id.topTextViewLeft2));
        params = tLeft0.getLayoutParams();
        params.width = textSize;
        tLeft0.setLayoutParams(params);
        params = tLeft1.getLayoutParams();
        params.width = textSize;
        tLeft1.setLayoutParams(params);
        params = tLeft2.getLayoutParams();
        params.width = textSize;
        tLeft2.setLayoutParams(params);
        */
    }


    public void checkGameWin(){
        //Log.d("(upOpened,mines)","("+unOpenedButtons+","+remainingMines+")");
        if(unOpenedButtons == 0 && remainingMines == 0){
            gameWon();
        }
    }

    public void gameWon(){
        gameState = GameState.WIN;
        mainActivity.startButton.setImageResource(R.drawable.smiley4);
        Database db = new Database(mainActivity);
        db.addSession(new Session(db.getSessionsCount(-1)+1,difficulty,true,milliseconds/1000.0f,1.0f));
        db.close();
        endGame();
        winAlert();
    }

    private void winAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Your Title");

        Database db = new Database(mainActivity);

        String diffLevel;
        switch(difficulty){
            case 1:
                diffLevel = "Intermediate";
            break;
            case 2:
                diffLevel = "Expert";
            break;
            default:
                diffLevel = "Beginner";
        }
        int wins = db.getAllSessionsCountByResult(true, difficulty);
        int plays = db.getSessionsCount(difficulty);
        // set dialog message
        alertDialogBuilder
                .setTitle("Game Won!")
                .setMessage("Time:  "+(db.getSession(plays)).getTime()+
                        "\nDifficulty:  "+diffLevel+
                        "\nBest Time:   "+db.getBestTime(difficulty)+
                        "\nAverage Time:    "+db.getAverageTime(difficulty)+
                        "\nWin percentage:  "+((float)wins/(float)plays)+
                        "\nExploration percentage:  "+Float.toString(db.getExplorationPercent(difficulty))+"%"+
                        "\nGames Won:  "+wins+
                        "\nGame Played:   "+plays)
                .setCancelable(false)
                .setPositiveButton("New",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        mainActivity.restartGame();
                    }
                })
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        db.close();
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void gameLost(){
        gameState = GameState.LOSE;
        mainActivity.startButton.setImageResource(R.drawable.smiley3);
        Database db = new Database(mainActivity);
        float explorationPercent = ((float)numOfButtons - (float)numOfMines - (float)unOpenedButtons + (float)flagsOnMines)/(float)numOfButtons;
        db.addSession(new Session(db.getSessionsCount(-1) + 1,difficulty, false, explorationPercent));
        db.close();
        endGame();
    }


    public void endGame(){
        timerHandler.removeCallbacks(timerRunnable);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            MButton mb = (MButton)gridLayout.getChildAt(i);
            mb.setOnTouchListener(null);
        }
        if(mainActivity.isVibrating) {
            //Log.d("vibrating","vibrating");
            v.vibrate(500);
        }
        maxScrollX = (float)gridLayout.getColumnCount()*150f-(float)mainActivity.findViewById(R.id.fragment_container).getWidth();
        maxScrollY = Math.max(0f,(float)gridLayout.getRowCount()*150f-(float)mainActivity.findViewById(R.id.fragment_container).getHeight());
        ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
        ScrollView vertical = (ScrollView)getActivity().findViewById(R.id.verticalScroll);
        HorizontalScrollView horizontal = (HorizontalScrollView)getActivity().findViewById(R.id.horizontalScroll);
        //Log.d("width",""+gridLayout.getWidth());
        float xScale = Math.max((float) getActivity().findViewById(R.id.fragment_container).getWidth() / gridLayout.getWidth(), params.width);
        float yScale = Math.max((float) getActivity().findViewById(R.id.fragment_container).getHeight() / gridLayout.getHeight(), params.height);
        //Log.d("xScale",""+xScale);
        //Log.d("yScale",""+yScale);
        gridLayout.setScaleX(xScale);
        gridLayout.setScaleY(yScale);
        //RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.fragment_container);
        //rl.setScaleY(yScale);
        //Log.d("(ScrollScaleX,GridPivotX)","("+horizontal.getScrollX()+","+gridLayout.getScaleX()+")");
        //Log.d("(ScrollScaleY,GridPivotY)","("+vertical.getScrollY()+","+gridLayout.getScaleY()+")");

        float yPivot = vertical.getScrollY();
        //Log.d("yPivot",""+yPivot);
        float rowNum = gridLayout.getRowCount();
        //Log.d("rowNum",""+rowNum);
        float totalPivotY = (yPivot/maxScrollY)*rowNum*150;
        //Log.d("totalPivotY",""+totalPivotY);
        //float xPivot = (2.5f/12.0f)*horizontal.getScrollX()*(float)gridLayout.getRowCount();
        //float yPivot = (18.0f/12.0f)*vertical.getScrollY()*(float)gridLayout.getColumnCount();
        float xPivot = horizontal.getScrollX();
        float columnNum = gridLayout.getColumnCount();
        float totalPivotX = (xPivot/maxScrollX)*columnNum*150;

        gridLayout.setPivotX(totalPivotX);

        if( gridLayout.getRowCount()*150 < getActivity().findViewById(R.id.fragment_container).getHeight() ) {
            //Log.d("length","low");
            params.height = getActivity().findViewById(R.id.fragment_container).getHeight();
            gridLayout.setPivotY(yPivot);
        }else{
            //Log.d("length","high");
            gridLayout.setPivotY(totalPivotY);
        }
        gridLayout.setLayoutParams(params);

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
    }

    public void pauseTimer(){
        if(gameState == GameState.PLAYING) {
            timerHandler.removeCallbacks(timerRunnable);
            savedTime = milliseconds;
        }
    }

    public void resumeTimer(){
        if(gameState == GameState.PLAYING) {
            gameTime = System.currentTimeMillis() - savedTime;
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }
}
