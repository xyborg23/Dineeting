package com.example.testapplication;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RestaurantActivity extends ActionBarActivity {

    ProgressBar myProgressBar;
    int myProgress = 3;
    ArrayList<String> rest = new ArrayList<String>();
    ArrayList<String> restop = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        myProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myProgressBar.setProgress(myProgress);

        ListView list = (ListView) findViewById(R.id.list_item);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ArrayList<Integer> pos = new ArrayList<Integer>();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {

                String restName = ((TextView) view).getText().toString();
                if(!pos.contains(position))
                {
                    parent.getChildAt(position).setBackgroundColor(Color.YELLOW);
                    pos.add(position);
                    restop.add(restName);
                }
                else
                {
                    parent.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                    pos.remove(position);
                    restop.remove(restName);
                }
            }
        });

        populateRestaurants();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant, menu);
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

    public void populateRestaurants()
    {
        rest.add("Ikenberry");
        rest.add("Busey-Evans");
        rest.add("FAR");
        rest.add("PAR");

        ListView list = (ListView) findViewById(R.id.list_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rest);
        list.setAdapter(adapter);
    }

    public void addRestaurant(View arg0)
    {
        Intent nextSettings = new Intent(this, ContactsActivity.class);
        nextSettings.putStringArrayListExtra("Restaurants", restop);
        startActivity(nextSettings);
    }
}
