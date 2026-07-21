package com.example.contextreminderapp.data;

import com.example.contextreminderapp.models.Reminder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseRepository {

    private final FirebaseFirestore db;

    public FirebaseRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface ReminderListCallback {
        void onSuccess(ArrayList<Reminder> reminders);
        void onFailure(String error);
    }

    public interface SimpleCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public void saveReminder(Reminder reminder, SimpleCallback callback) {
        db.collection("reminders")
                .add(reminder)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess("Reminder saved successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to save reminder: " + e.getMessage());
                });
    }

    public void loadReminders(ReminderListCallback callback) {
        db.collection("reminders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Reminder> reminderList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Reminder reminder = document.toObject(Reminder.class);
                        reminder.setId(document.getId());
                        reminderList.add(reminder);
                    }

                    callback.onSuccess(reminderList);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to load reminders: " + e.getMessage());
                });
    }

    public void updateReminder(String reminderId, Reminder reminder, SimpleCallback callback) {
        db.collection("reminders")
                .document(reminderId)
                .set(reminder)
                .addOnSuccessListener(unused -> {
                    callback.onSuccess("Reminder updated successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to update reminder: " + e.getMessage());
                });
    }

    public void deleteReminder(String reminderId, SimpleCallback callback) {
        db.collection("reminders")
                .document(reminderId)
                .delete()
                .addOnSuccessListener(unused -> {
                    callback.onSuccess("Reminder deleted successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to delete reminder: " + e.getMessage());
                });
    }

    public void saveContextLog(Map<String, Object> contextData, SimpleCallback callback) {
        db.collection("context_logs")
                .add(contextData)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess("Context log saved");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to save context log: " + e.getMessage());
                });
    }

    public void saveWearableData(Map<String, Object> wearableData, SimpleCallback callback) {
        db.collection("wearable_data")
                .add(wearableData)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess("Wearable data saved");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to save wearable data: " + e.getMessage());
                });
    }

    public void savePhoneSensorData(Map<String, Object> phoneSensorData, SimpleCallback callback) {
        db.collection("phone_sensor_data")
                .add(phoneSensorData)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess("Phone sensor data saved");
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to save phone sensor data: " + e.getMessage());
                });
    }
}