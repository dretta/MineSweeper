package com.daniel.minesweeper;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


//some comment
public class MainActivity extends Activity {

    private boolean mInit = false;
    /*
    TextView timerTextView;
    long startTime = System.currentTimeMillis();

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            timerTextView.setText(Integer.toString(seconds));

            timerHandler.postDelayed(this, 500);
        }
    };
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*
        TableLayout layout = new TableLayout(this);
        layout.setLayoutParams(new TableLayout.LayoutParams(1, 2));
        layout.setPadding(0, 0, 0, 0);

        TableRow tr = new TableRow(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,50));
        TextView tv = new TextView(this);
        tv.setText("Area");
        ll.addView(tv);
        //tr.addView(ll);
        //layout.addView(tr);


        for(int i = 0;i<2;i++) {
            tr = new TableRow(this);
            for(int j = 1;j<=2;j++) {
                MButton button = new MButton(this, "" + (2*i+j), 0, 0);
                tr.addView(button);
            }
            layout.addView(tr);
        }

        tr = new TableRow(this);
        timerTextView = new TextView(this);
        tr.addView(timerTextView);
        layout.addView(tr);

        timerHandler.postDelayed(timerRunnable, 0);

        setContentView(layout);
        */

        /*
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.activity_main, null);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
            ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
            new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        */

        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        TextView myTextView = (TextView)findViewById(R.id.textView1);
        TextView myTopTextView = (TextView)findViewById(R.id.topTextView1);
        myTextView.setTypeface(myTypeface);
        myTopTextView.setTypeface(myTypeface);


        //Log.d("actionBarSize",""+actionBar.));


        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null) {
                return;
            }
        }
    }

    private void initial(){
        GridFragment gFragment = new GridFragment();

        gFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction().add(R.id.fragment_container, gFragment).commit();
    }

    @Override
    protected void onStart() {
        if (!mInit) {
            mInit = true;
            initial();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*
        TextView m = new TextView(this);
        m.setText("Mines");
        m.setTextColor(Color.WHITE);
        m.setPadding(5, 0, 5, 0);
        m.setTypeface(null, Typeface.BOLD);
        m.setTextSize(14);
        menu.add(1, 1, 1, "text").setActionView(m).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        TextView t = new TextView(this);
        t.setText("Time");
        t.setTextColor(Color.WHITE);
        t.setPadding(5, 0, 5, 0);
        t.setTypeface(null, Typeface.BOLD);
        t.setTextSize(14);
        menu.add(0, 0, 0, "text").setActionView(t).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        */
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
