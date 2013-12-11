package com.example.testapplication.RequestParsers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * example: [breakfast|lunch|dinner] [MM/DD/YYYY] [Event id(Integer)]::[breakfast|lunch|dinner] [MM/DD/YYYY] [Event id(Integer)]::[breakfast|lunch|dinner] [MM/DD/YYYY] [Event id(Integer)]
 */
public class HomeUpdate {
    public String mealType;
    public CustomDate date;
    public String eventID;

    public HomeUpdate(String mealType, CustomDate date, String eventID) {
        this.mealType = mealType;
        this.date = date;
        this.eventID = eventID;
    }

    public static List<HomeUpdate> parse(String responseText) {
        if (responseText.equals("none")) {
            return new ArrayList<HomeUpdate>();
        }

        String[] events = responseText.split("::");

        Log.d("home update response text", responseText);
        Log.d("alskdfj\n\n\n\n\n\n", "stuff");
        List<HomeUpdate> result = new ArrayList<HomeUpdate>(events.length);
        for (String ev: events) {
            String[] evTokens = ev.split(" ");
            String[] dateTokens = evTokens[1].split("/");
            result.add(new HomeUpdate(
                    evTokens[0],
                    new CustomDate(
                            Integer.parseInt(dateTokens[2]),
                            Integer.parseInt(dateTokens[0]),
                            Integer.parseInt(dateTokens[1]),
                            -1,
                            -1
                    ),
                    evTokens[2]
            ));
        }

        return result;


    }

}
