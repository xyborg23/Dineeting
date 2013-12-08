package com.example.testapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MeetingTimeActivity extends ActionBarActivity {
    private final String dateFormat = "a hh:mm-MM/dd/yyyy";

    ProgressBar myProgressBar;
    int myProgress = 2;
    int choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_time);

        Intent intent = getIntent();
        choice = intent.getIntExtra(EventActivity.CHOICE_KEY, 1);

        myProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myProgressBar.setProgress(myProgress);

        // Set dateTime inputs to the current time
        EditText meetHour = (EditText) findViewById(R.id.meetHourInput);
        EditText meetMinute = (EditText) findViewById(R.id.meetMinuteInput);
        EditText meetYear = (EditText) findViewById(R.id.meetYearInput);
        EditText meetMonth = (EditText) findViewById(R.id.meetMonthInput);
        EditText meetDate = (EditText) findViewById(R.id.meetDateInput);
        ToggleButton meetDayToggle = (ToggleButton) findViewById(R.id.meetingDayToggle);

        EditText deadlineHour = (EditText) findViewById(R.id.deadlineHourInput);
        EditText deadlineMinute = (EditText) findViewById(R.id.deadlineMinuteInput);
        EditText deadlineYear = (EditText) findViewById(R.id.deadlineYearInput);
        EditText deadlineMonth = (EditText) findViewById(R.id.deadlineMonthInput);
        EditText deadlineDate = (EditText) findViewById(R.id.deadlineDateInput);
        ToggleButton deadlineDayToggle = (ToggleButton) findViewById(R.id.deadlineDayToggle);

        Date now = new Date();
        // Convert from military time
        if (now.getHours() > 12) {
            meetHour.setText(String.valueOf(now.getHours()-12));
            deadlineHour.setText(String.valueOf(now.getHours()-12));
            meetDayToggle.setChecked(true);
            deadlineDayToggle.setChecked(true);
        } else if (now.getHours() == 0) {
            meetHour.setText("12");
            deadlineHour.setText("12");
        } else {
            meetHour.setText(String.valueOf(now.getHours()));
            deadlineHour.setText(String.valueOf(now.getHours()));
        }

        meetMinute.setText(String.valueOf(now.getMinutes()));
        deadlineMinute.setText(String.valueOf(now.getMinutes()));

        meetYear.setText(String.valueOf(now.getYear()+1900));
        deadlineYear.setText(String.valueOf(now.getYear()+1900));

        meetMonth.setText(String.valueOf(now.getMonth()+1));
        deadlineMonth.setText(String.valueOf(now.getMonth()+1));

        meetDate.setText(String.valueOf(now.getDay()+1));
        deadlineDate.setText(String.valueOf(now.getDay()+1));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meeting_time, menu);
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

    public void completeTimeSettings(View v) {
        Date meetingDateTime = getMeetingTimeInfo();
        Date deadlineDateTime = getDeadlineInfo();

        Toast error = null;
        if (meetingDateTime == null) {
            error = Toast.makeText(getApplicationContext(), "Invalid meet time", Toast.LENGTH_SHORT);
        } else if (deadlineDateTime == null) {
            error = Toast.makeText(
                    getApplicationContext(),
                    "Invalid deadline time",
                    Toast.LENGTH_SHORT
            );
        } else if (deadlineDateTime.after(meetingDateTime)) {
            error = Toast.makeText(
                    getApplicationContext(),
                    "The voting deadline cannot be after the meeting time",
                    Toast.LENGTH_SHORT
            );
        }

        if (error != null) {
            error.setGravity(Gravity.CENTER, 0, 0);
            error.show();
            return;
        }

        if(choice == 1)
        {
            Intent nextSettings = new Intent(this, RestaurantActivity.class);
            nextSettings.putExtra("Meeting Time", meetingDateTime);
            nextSettings.putExtra("Deadline Time", deadlineDateTime);
            startActivity(nextSettings);
        }
        else
        {
            Intent nextSettings = new Intent(this, MainActivity.class);
            nextSettings.putExtra("Meeting Time", meetingDateTime);
            nextSettings.putExtra("Deadline Time", deadlineDateTime);
            startActivity(nextSettings);
        }

    }

    private Date getMeetingTimeInfo() {
        EditText hour = (EditText) findViewById(R.id.meetHourInput);
        EditText minute = (EditText) findViewById(R.id.meetMinuteInput);
        EditText year = (EditText) findViewById(R.id.meetYearInput);
        EditText month = (EditText) findViewById(R.id.meetMonthInput);
        EditText date = (EditText) findViewById(R.id.meetDateInput);
        ToggleButton amPM = (ToggleButton) findViewById(R.id.meetingDayToggle);

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        formatter.setLenient(false);

        String meetingDateStr = String.format("%02d:%02d-%02d/%02d/%04d",
                Integer.parseInt(hour.getText().toString()),
                Integer.parseInt(minute.getText().toString()),
                (Integer.parseInt(month.getText().toString())),
                (Integer.parseInt(date.getText().toString())),
                (Integer.parseInt(year.getText().toString()))
        );
        if (amPM.isChecked()) {
            meetingDateStr = "PM " + meetingDateStr;
        } else {
            meetingDateStr = "AM " + meetingDateStr;
        }

        Date meetingDate = null;
        try {
            meetingDate = formatter.parse(meetingDateStr);
        } catch (ParseException e) {
            // pass
        }
        return meetingDate;
    }

    private Date getDeadlineInfo() {
        EditText hour = (EditText) findViewById(R.id.deadlineHourInput);
        EditText minute = (EditText) findViewById(R.id.deadlineMinuteInput);
        EditText year = (EditText) findViewById(R.id.deadlineYearInput);
        EditText month = (EditText) findViewById(R.id.deadlineMonthInput);
        EditText date = (EditText) findViewById(R.id.deadlineDateInput);
        ToggleButton amPM = (ToggleButton) findViewById(R.id.deadlineDayToggle);

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        formatter.setLenient(false);


        String deadlineDateStr = String.format("%02d:%02d-%02d/%02d/%04d",
                Integer.parseInt(hour.getText().toString()),
                Integer.parseInt(minute.getText().toString()),
                (Integer.parseInt(month.getText().toString())),
                (Integer.parseInt(date.getText().toString())),
                (Integer.parseInt(year.getText().toString()))
        );

        if (amPM.isChecked()) {
            deadlineDateStr = "PM " + deadlineDateStr;
        } else {
            deadlineDateStr = "AM " + deadlineDateStr;
        }

        Date deadlineDate = null;
        try {
            deadlineDate = formatter.parse(deadlineDateStr);
        } catch (ParseException e) {
            // pass
        }
        return deadlineDate;
    }
}
