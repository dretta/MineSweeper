package com.daniel.minesweeper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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

import java.util.Map;


public class MainActivity extends Activity {

    public static final boolean debug = false;
    private ActionBar actionBar;
    private boolean mInit = false;
    private boolean showIcon = true;
    private Menu m;
    private GridFragment gridFragment;
    private int difficulty;
    public SettingsFragment settingsFragment;
    public ImageButton startButton;
    public TextView gameTimer;
    public TextView mineCount;
    public boolean isVibrating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        settingsFragment = new SettingsFragment();
        updateSettings();
        actionBar = getActionBar();
        actionBar.setTitle("Settings");
        actionBar.setCustomView(R.layout.actionbar);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
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
        gridFragment.timerHandler.removeCallbacks(gridFragment.timerRunnable);
        getFragmentManager().beginTransaction().remove(gridFragment).commit();
        gridFragment = null;
        setText(999, gameTimer);
        startGame();
    }

    private void startGame(){
        Bundle b = new Bundle(1);
        /*
        Log.d("difficulty",""+difficulty);
        switch(difficulty){
            case 1:{
                b.putInt("rows",16);
                b.putInt("columns",16);
                b.putInt("mines",40);
                break;
            }
            case 2:{
                b.putInt("rows",30);
                b.putInt("columns",16);
                b.putInt("mines",99);
                break;
            }
            default:{
                b.putInt("rows",10);
                b.putInt("columns",9);
                b.putInt("mines",10);
            }
        }
        */

        gridFragment = new GridFragment();
        b.putInt("difficulty",difficulty);
        gridFragment.setArguments(b);

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
            Database db = new Database(this);
            db.deleteAllSessions();
            //this.deleteDatabase(db.getDatabaseName());
            db.close();
            startGame();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        m = menu;
        return true;
    }

    private void openSettings(){
        showIcon = false;
        gridFragment.pauseTimer();
        onPrepareOptionsMenu(m);
        actionBar.setDisplayShowCustomEnabled(false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.hide(gridFragment);
        ft.add(android.R.id.content, settingsFragment).commit();
        //ft.replace(android.R.id.content,settingsFragment);
    }

    private void updateSettings(){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedPrefs = getPreferences(MODE_PRIVATE);

        Map<String, ?> map = sharedPrefs.getAll();
        /*
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
        */
        //isVibrating = (Boolean)map.get("vibration");
        difficulty = Integer.parseInt((String)map.get("difficulty_"));
        //difficulty = sharedPrefs.getInt("difficulty_",0);
        //Log.d("Difficulty",""+difficulty);

    }

    private void closeSettings(){
        showIcon = true;
        onPrepareOptionsMenu(m);
        actionBar.setDisplayShowCustomEnabled(true);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.show(gridFragment);
        ft.remove(settingsFragment).commit();
        //ft.replace(android.R.id.content,gridFragment);
        gridFragment.resumeTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }
        else if(id == R.id.backButton){
            updateSettings();
            closeSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(showIcon);
        item = menu.findItem(R.id.backButton);
        item.setVisible(!showIcon);
        return super.onPrepareOptionsMenu(menu);
    }
}
