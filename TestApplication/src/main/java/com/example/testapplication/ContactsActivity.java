package com.example.testapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
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
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends ActionBarActivity {

    private static int save = -1;
    ArrayList<String> friends = new ArrayList<String>();
    ProgressBar myProgressBar;
    int myProgress = 4;
    public final static String INFO_KEY = "com.example.testapplication.INFO";
    ArrayList<String> friends2 = new ArrayList<String>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        myProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myProgressBar.setProgress(myProgress);
        fetchContacts2();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
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

    public void fetchContacts2()
    {
        ListView list = (ListView) findViewById(android.R.id.list);
        friends2.add("Anne");
        friends2.add("Alyssa");
        friends2.add("Daniel");
        friends2.add("Jobin");
        friends2.add("Jonathan");
        friends2.add("Tyler");
        friends2.add("Vaidehi");
        friends2.add("William");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends2);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ArrayList<Integer> pos = new ArrayList<Integer>();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                if(!pos.contains(position))
                {
                    parent.getChildAt(position).setBackgroundColor(Color.BLUE);
                    pos.add(position);
                }
                else
                {
                    parent.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                    pos.remove(position);
                }
            }
        });
    }

//    public void fetchContacts() {
//
//        List<String> items = new ArrayList<String>();
//
//        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//        String _ID = ContactsContract.Contacts._ID;
//        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//
//        ContentResolver contentResolver = getContentResolver();
//
//        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
//        ListView list = (ListView) findViewById(android.R.id.list);
//
//        // Loop for every contact in the phone
//        if (cursor.getCount() > 0) {
//
//            while (cursor.moveToNext()) {
//
//                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
//                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
//                items.add(name);
//            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//
//            list.setAdapter(adapter);
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                ArrayList<Integer> pos = new ArrayList<Integer>();
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
//
//                    String contactname = ((TextView) view).getText().toString();
//                    if(!pos.contains(position))
//                    {
//                        parent.getChildAt(position).setBackgroundColor(Color.YELLOW);
//                        pos.add(position);
//                        friends.add(contactname);
//                    }
//                    else
//                    {
//                        parent.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
//                        pos.remove(position);
//                        friends.remove(contactname);
//                    }
//                }
//            });
//        }
//    }
    public void submitInfo(View view) {

        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtras(getIntent());
        intent.putStringArrayListExtra(INFO_KEY, friends);
        startActivity(intent);
    }
}
