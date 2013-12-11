package com.example.testapplication.RequestParsers;

import android.util.Log;

public class NotificationUpdate {
    public boolean voteInProgress;
    public String eventID;
    public String eventMessage;

    private NotificationUpdate(boolean voteInProgress, String eventID, String eventMessage) {
        this.voteInProgress = voteInProgress;
        this.eventID = eventID;
        this.eventMessage = eventMessage;
    }

    public static NotificationUpdate parse(String responseText) {
        if (responseText.equals("none")) {
            return null;
        }

        String[] tokens = responseText.split(" ");

        boolean currentlyVoting = true;
        if (tokens[0].equals("votingover")) {
            currentlyVoting = false;
        }

        return new NotificationUpdate(currentlyVoting, tokens[1], tokens[2]);
    }
}
