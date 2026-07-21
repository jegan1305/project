package com.example.contextreminderapp.utils;

import com.example.contextreminderapp.models.Reminder;

import java.util.List;

public class ContextMatcherHelper {

    public static class MatchResult {

        private final boolean found;
        private final String matchedDetails;

        public MatchResult(boolean found, String matchedDetails) {
            this.found = found;
            this.matchedDetails = matchedDetails;
        }

        public boolean isFound() {
            return found;
        }

        public String getMatchedDetails() {
            return matchedDetails;
        }
    }

    public MatchResult checkPlaceMatch(List<Reminder> reminders, String currentPlace) {
        if (currentPlace == null) {
            currentPlace = "";
        }

        String cleanedPlace = currentPlace.trim();

        StringBuilder matchedReminders = new StringBuilder();
        boolean found = false;

        if (reminders == null) {
            return new MatchResult(false, "No reminder found");
        }

        for (Reminder reminder : reminders) {
            if (reminder.getPlace() != null &&
                    reminder.getPlace().equalsIgnoreCase(cleanedPlace)) {

                found = true;

                matchedReminders.append("Title: ").append(reminder.getTitle())
                        .append("\nMessage: ").append(reminder.getMessage())
                        .append("\nPlace: ").append(reminder.getPlace())
                        .append("\n\n");
            }
        }

        if (found) {
            return new MatchResult(true, matchedReminders.toString());
        } else {
            return new MatchResult(false, "No reminder found");
        }
    }

    public MatchResult checkSmartContextMatch(
            List<Reminder> reminders,
            String currentPlace,
            String detectedActivity
    ) {
        if (currentPlace == null || currentPlace.trim().isEmpty()) {
            currentPlace = "unknown";
        }

        if (detectedActivity == null || detectedActivity.trim().isEmpty()) {
            detectedActivity = "unknown";
        }

        String cleanedPlace = currentPlace.trim();

        StringBuilder matchedReminders = new StringBuilder();
        boolean found = false;

        if (reminders == null) {
            return new MatchResult(false, "No reminder found");
        }

        for (Reminder reminder : reminders) {
            if (reminder.getPlace() != null &&
                    reminder.getPlace().equalsIgnoreCase(cleanedPlace)) {

                found = true;

                matchedReminders.append("Title: ").append(reminder.getTitle())
                        .append("\nMessage: ").append(reminder.getMessage())
                        .append("\nPlace: ").append(reminder.getPlace())
                        .append("\nActivity: ").append(detectedActivity)
                        .append("\n\n");
            }
        }

        if (found) {
            return new MatchResult(true, matchedReminders.toString());
        } else {
            return new MatchResult(false, "No reminder found");
        }
    }
}