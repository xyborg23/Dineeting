package com.example.testapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ProgressBar;

public class EventActivity extends ActionBarActivity {

    ProgressBar myProgressBar;
    int myProgress = 1;
    public final static String CHOICE_KEY = "com.example.testapplication.CHOICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        myProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myProgressBar.setProgress(myProgress);

        Button chooseRest = (Button)findViewById(R.id.button1);
        Button pollFriends = (Button) findViewById(R.id.button2);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseRestaurant(View v)
    {
        Intent intent = new Intent(this, MeetingTimeActivity.class);
        intent.putExtra(CHOICE_KEY, 1);
        startActivity(intent);
    }

    public void pollingFriends(View v)
    {
        Intent intent = new Intent(this, MeetingTimeActivity.class);
        intent.putExtra(CHOICE_KEY, 2);
        startActivity(intent);
    }
}
