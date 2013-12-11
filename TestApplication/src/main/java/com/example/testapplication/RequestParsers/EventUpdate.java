package com.example.testapplication.RequestParsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * example: ongoing::[restaurant-(numofvotes),restaurant-(numofvotes),restaurant-(numofvotes),..]::(Voting deadline)[MM/DD/YYYY]::[HH/MM]
 * example: complete::[name of restaurant]::(Meeting time)[MM/DD/YYYY]::[HH/MM]::(friends who accepted)[friend,friend,friend,...]::(friends who declined)[friend,friend,friend,...]::(friends who haven't responded)[friend,friend,friend,...]
 */
public class EventUpdate {

    public static Object parse(String responseText) {
        String[] tokens = responseText.split("::");

        String[] dateTokens = tokens[2].split("/");
        String[] timeTokens = tokens[3].split("/");

        CustomDate deadline = new CustomDate(
                Integer.parseInt(dateTokens[2]),
                Integer.parseInt(dateTokens[0]),
                Integer.parseInt(dateTokens[1]),
                Integer.parseInt(timeTokens[0]),
                Integer.parseInt(timeTokens[1])
        );

        if (tokens[0].equals("ongoing")) {
            Map<String, Integer> restaurantVotes = new HashMap<String, Integer>();
            String[] restaurantVoteTokens = tokens[1].split(",");
            for (String resVoteToken: restaurantVoteTokens) {
                // Put res-#votes into map
                String[] resVote = resVoteToken.split("-");
                restaurantVotes.put(resVote[0], Integer.parseInt(resVote[1]));
            }

            return new OngoingEvent(restaurantVotes, deadline);
        } else {
            String restaurantName = tokens[1].replace("_", " ");

            String[] acceptedFriendsStrings = tokens[4].split(",");
            List<String> acceptedFriends = new ArrayList<String>(acceptedFriendsStrings.length);
            for (String accFr: acceptedFriendsStrings) {
                acceptedFriends.add(accFr.replace("_", " "));
            }

            // TODO this needs to uppercase the first letter of each word
            String[] declinedFriendsStrings = tokens[5].split(",");
            List<String> declinedFriends = new ArrayList<String>(declinedFriendsStrings.length);
            for (String decFr: declinedFriendsStrings) {
                declinedFriends.add(decFr.replace("_", " "));
            }

            return new FinishedEvent(restaurantName, deadline,acceptedFriends, declinedFriends);
        }

    }

    public static class OngoingEvent {
        public Map<String, Integer> restaurantVotes;
        public CustomDate votingDeadline;

        public OngoingEvent(Map<String, Integer> restaurantVotes, CustomDate votingDeadline) {
            this.restaurantVotes = restaurantVotes;
            this.votingDeadline = votingDeadline;
        }
    }

    public static class FinishedEvent {
        public String restaurantName;
        public CustomDate meetingTime;
        public List<String> acceptedFriends;
        public List<String> declinedFriends;

        public FinishedEvent(
                String restaurantName,
                CustomDate meetingTime,
                List<String> acceptedFriends,
                List<String> declinedFriends
        ) {
            this.restaurantName = restaurantName;
            this.meetingTime = meetingTime;
            this.acceptedFriends = acceptedFriends;
            this.declinedFriends = declinedFriends;
        }
    }
}
