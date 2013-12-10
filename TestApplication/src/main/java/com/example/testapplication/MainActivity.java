package com.example.testapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    ArrayList<String> events = new ArrayList<String>();
    final Context context = this;
    EditText edtResp;
    String thisname;

    public final static String NAME_KEY = "com.example.testapplication.NAME";
    private String url = "http://172.16.134.172:8080/DineServer/DoubleMeServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        events.add("Lunch today");
        events.add("Dinner today");
        events.add("Lunch tomorrow");

        login();

        final EditText edtTxt = (EditText) findViewById(R.id.editText1);

        Button btnSend = (Button) findViewById(R.id.submit);
        edtResp = (EditText) findViewById(R.id.edtResp);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = edtTxt.getText().toString();
                //	MenuItemCompat.setActionView(item, R.layout.progress);
                //			item.setActionView(R.layout.progress);
                SendHttpRequestTask t = new SendHttpRequestTask();

                String[] params = new String[]{url, name};
                t.execute(params);
            }
        });

        populateEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

/*    @Override
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
*/
    /*
     * Submit Button
     */
    public void submitMessage(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    /*public void submitMessage2(View view)
    {
        String name = "login Jobin";
        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{url, name};
        t.execute(params);
    }*/

    public void populateEvents()
    {
        ListView list = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, events);
        list.setAdapter(adapter);
    }

    public void login()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setNeutralButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                thisname = input.getText().toString();
            }
        });

        alert.show();

        String name = "login ".concat(thisname);
        //	MenuItemCompat.setActionView(item, R.layout.progress);
        //			item.setActionView(R.layout.progress);
        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{url, name};
        t.execute(params);
    }

    public void helpMessage(View arg0)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Help");

        // set dialog message
        alertDialogBuilder
                .setMessage("Enter the help message here")
                .setCancelable(false)
                .setNeutralButton("Close", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    // runs task in background
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String name = params[1];

            String data = sendHttpRequest(url, name);
            System.out.println("Data ["+data+"]");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            edtResp.setText(result);
            //	item.setActionView(null);

        }

    }

    // establishes connection to server
    // sends message with name variable to server at url
    //returns string response from server
    private String sendHttpRequest(String url, String name) {
        StringBuffer buffer = new StringBuffer();
        try {
            System.out.println("URL ["+url+"] - Name ["+name+"]");

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write( (name).getBytes());

            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];

            while ( is.read(b) != -1)
                buffer.append(new String(b));

            con.disconnect();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return buffer.toString();
    }
}

