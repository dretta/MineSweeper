package com.daniel.minesweeper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends Activity {

    private boolean mInit = false;
    private Fragment gridFragment;
    public ImageButton startButton;
    public TextView gameTimer;
    public TextView mineCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        ViewGroup actionBarViews = (ViewGroup)actionBar.getCustomView();
        startButton = (ImageButton)(actionBarViews.findViewById(R.id.actionBarLogo));
        mineCount = (TextView)actionBarViews.findViewById(R.id.topTextViewLeft);
        gameTimer = (TextView)actionBarViews.findViewById(R.id.topTextViewRight);
        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startButton.setImageResource(R.drawable.smiley2);
                        break;
                    case MotionEvent.ACTION_UP:
                        restartGame();
                        break;
                }
                return false;
            }
        });

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/digital-7 (mono).ttf");
        TextView textView;
        int[] resources =
                {R.id.textViewLeft,R.id.topTextViewLeft,R.id.textViewRight,R.id.topTextViewRight};
        for(int r: resources) {
            textView = (TextView) findViewById(r);
            textView.setTypeface(myTypeface);
        }

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null) {
                return;
            }
        }
    }

    public void restartGame() {
        startButton.setImageResource(R.drawable.smiley);
        getFragmentManager().beginTransaction().remove(gridFragment).commit();
        setText(999, gameTimer);
        startGame();
    }

    private void startGame(){

        gridFragment = new GridFragment();

        gridFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction().add(R.id.fragment_container, gridFragment,"gridFragment").commit();

    }

    public void setText(int value, TextView textView){
        value = Math.min(999,value);
        value = Math.max(-99,value);
        textView.setText(String.format("%03d",value));
    }

    @Override
    protected void onStart() {
        if (!mInit) {
            mInit = true;
            startGame();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
