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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class InfoActivity extends ActionBarActivity {
    private final String NON_VOTING_EVENT_FORMAT = "sendnonvotingeventinfo %s::%s::%s::%s::%d/%d/%d::%d/%d";
    private final String VOTING_EVENT_FORMAT = "sendvotingeventinfo %s::%s::%s::%s::%d/%d/%d::%d/%d::%d/%d/%d::%d/%d";

    ArrayList<String> friends;
    ProgressBar myProgressBar;
    int myProgress = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent2 = getIntent();
        friends = intent2.getStringArrayListExtra(ContactsActivity.INFO_KEY);

        myProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myProgressBar.setProgress(myProgress);

        outputFriends();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
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

    public void outputFriends(){

        // Get the reference of ListViewAnimals
        ListView friendList=(ListView)findViewById(R.id.listFriends);

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friends);
        // Set The Adapter
        friendList.setAdapter(arrayAdapter);
    }
}
