package com.example.testapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.testapplication.RequestParsers.EventUpdate;
import com.example.testapplication.RequestParsers.HomeUpdate;
import com.example.testapplication.RequestParsers.NotificationUpdate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private final static String LOGIN = "LOGIN";
    private final static String NOTIFICATION_UPDATE = "NOTIFICATION_UPDATE";
    private final static String INITIAL_UPDATE = "INITIAL_UPDATE";
    private final static String EVENT_UPDATE = "EVENT_UPDATE";

    private final String loginFormat = "login %s";
    private final String notificationPollFormat = "notificationupdate %s";
    private final String initialUpdateFormat = "homeupdate %s";
    private final String decisionFormat = "decision %s %s %s";
    private final String finalDecisionFormat = "finaldecision %s %s %s";
    private final String eventUpdateFormat = "eventupdate %s";

    private List<String> events = Collections.synchronizedList(new ArrayList<String>());
    private final Context context = this;
    private ArrayAdapter<String> eventsAdapter;
    private EditText edtResp;
    private String userName;

    public final static String NAME_KEY = "com.example.testapplication.NAME";
    private String url = "http://192.17.237.109:8080/DineServer/DoubleMeServlet";

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
                submitMessage(v);
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
        intent.putExtra("user name", userName);
        startActivity(intent);
    }

    /*public void submitMessage2(View view)
    {
        String name = "login Jobin";
        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{url, name};
        t.execute(params);
    }*/

    private void populateEvents()
    {
        ListView list = (ListView) findViewById(android.R.id.list);

        // Event update like in the server API
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Object update = EventUpdate.parse(
//                        // TODO find a way to get/put the event ID
//                        sendHttpRequest(url, String.format(eventUpdateFormat,))
//                );
//
//                if (update instanceof EventUpdate.FinishedEvent) {
//                    // TODO send to static info page? I don't know what we decided for this
//                } else if (update instanceof EventUpdate.OngoingEvent) {
//                    // TODO show voting/messages page
//                }
//            }
//        });

        eventsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, events);
        list.setAdapter(eventsAdapter);

    }


    private void login()
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
                userName = input.getText().toString().trim().toLowerCase().replace(' ', '_');

                String name = String.format(loginFormat, userName);
                SendHttpRequestTask t = new SendHttpRequestTask();

                String[] params = new String[]{url, name, LOGIN};
                t.execute(params);

                String initialUpdate = String.format(initialUpdateFormat, userName);
                t = new SendHttpRequestTask();

                params = new String[]{url, initialUpdate, INITIAL_UPDATE};
                t.execute(params);


                NotificationPoller poller = new NotificationPoller();
                poller.execute(events);
            }
        });

        alert.show();

        //	MenuItemCompat.setActionView(item, R.layout.progress);
        //			item.setActionView(R.layout.progress);
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

    private class NotificationPoller extends AsyncTask<List<String>, Void, String> {

        @Override
        protected String doInBackground(List<String>... params) {
            final List<String> events = params[0];
            // Essentially infinite, but it allows the return statement to work
            for (int i=0; i < 1000000000; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                // Synchronous request because we're running this in an infinite loop, ie we don't want infinite threads
                final NotificationUpdate update = NotificationUpdate.parse(
                        sendHttpRequest(url, String.format(notificationPollFormat, userName))
                );

                if (update != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    if (update.voteInProgress) {
                        builder.setTitle("New event");
                        builder.setPositiveButton("Vote", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendHttpRequest(
                                        url,
                                        String.format(decisionFormat,userName, update.eventID, "vote")
                                );
                                // TODO get more event info, this is just the ID
                                events.add(update.eventID);
                                eventsAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendHttpRequest(
                                        url,
                                        String.format(decisionFormat, userName, update.eventID, "decline")
                                );
                            }
                        });
                    } else {
                        builder.setTitle("Voting has ended");
                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendHttpRequest(
                                        url,
                                        String.format(finalDecisionFormat, userName, update.eventID, "accept")
                                );
                            }
                        });
                        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendHttpRequest(
                                        url,
                                        String.format(finalDecisionFormat, userName, update.eventID, "decline")
                                );
                            }
                        });
                    }

                    builder.setMessage(update.eventMessage);
                    builder.create().show();
                }
            }
            return "";
        }
    }

    // runs task in background
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        private String type;

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String name = params[1];
            type = params[2];

            String data = sendHttpRequest(url, name);
            System.out.println("Data ["+data+"]");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            if (type.equals(LOGIN)) {

            } else if (type.equals(INITIAL_UPDATE)) {
                List<HomeUpdate> updateList = HomeUpdate.parse(result);
                for (HomeUpdate update: updateList) {
                    events.add(
                            update.mealType +
                                    update.date.getMonth() + "/" +
                                    update.date.getDay() + "/" +
                                    update.date.getYear()
                    );
                }
                eventsAdapter.notifyDataSetChanged();
                // Store the id somewhere in a thread safe way
            } else if (type.equals(EVENT_UPDATE)) {

            }
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

            int bytesRead = 0;
            while ( (bytesRead = is.read(b)) != -1) {
                buffer.append(new String(Arrays.copyOf(b,bytesRead)));
            }

            con.disconnect();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return buffer.toString();
    }
}

