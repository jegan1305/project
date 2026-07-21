package com.example.contextreminderapp;

import com.example.contextreminderapp.data.FirebaseRepository;
import com.example.contextreminderapp.models.Reminder;
import com.example.contextreminderapp.ui.dashboard.DashboardView;
import com.example.contextreminderapp.ui.reminders.RemindersView;
import com.example.contextreminderapp.ui.reminders.ReminderCardView;
import com.example.contextreminderapp.ui.context.ContextView;
import com.example.contextreminderapp.ui.sensors.SensorView;
import com.example.contextreminderapp.ui.profile.ProfileView;
import com.example.contextreminderapp.ui.navigation.BottomNavView;
import com.example.contextreminderapp.utils.NotificationHelper;
import com.example.contextreminderapp.utils.PhoneSensorHelper;
import com.example.contextreminderapp.utils.WearableSimulationHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    EditText titleInput, messageInput, placeInput, currentPlaceInput;

    Button saveButton, checkButton, backgroundCheckButton, simulateWearableButton, phoneSensorButton;
    Button dashboardTab, remindersTab, contextTab, sensorsTab, profileTab;
    Button aboutButton, termsButton;

    TextView resultText;
    TextView aboutCard, termsCard;

    LinearLayout savedReminderContainer;
    LinearLayout dashboardSection, remindersSection, contextSection, sensorsSection, profileSection;

    ArrayList<Reminder> reminders = new ArrayList<>();

    FirebaseRepository repository;
    NotificationHelper notificationHelper;
    PhoneSensorHelper phoneSensorHelper;
    WearableSimulationHelper wearableSimulationHelper;

    DashboardView dashboardView;
    RemindersView remindersView;
    ReminderCardView reminderCardView;
    ContextView contextView;
    SensorView sensorView;
    ProfileView profileView;
    BottomNavView bottomNavView;

    String latestDetectedActivity = "unknown";
    double latestMovementLevel = 0.0;

    boolean isAppInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 8, 20));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 20));

        repository = new FirebaseRepository();

        notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel();
        notificationHelper.requestNotificationPermission();

        phoneSensorHelper = new PhoneSensorHelper(this);
        wearableSimulationHelper = new WearableSimulationHelper();

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.rgb(5, 8, 20));

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(dp(12), dp(18), dp(12), dp(8));
        mainLayout.setBackgroundColor(Color.rgb(5, 8, 20));

        TextView heading = new TextView(this);
        heading.setText("SynqSense");
        heading.setTextSize(28);
        heading.setTextColor(Color.rgb(0, 230, 255));
        heading.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        heading.setPadding(0, 0, 0, dp(2));

        TextView tagline = new TextView(this);
        tagline.setText("Dark Neon Context-Aware Reminder System");
        tagline.setTextSize(13);
        tagline.setTextColor(Color.rgb(180, 190, 210));
        tagline.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        tagline.setPadding(0, 0, 0, dp(10));

        dashboardView = new DashboardView(this);
        dashboardSection = dashboardView.createView();

        remindersView = new RemindersView(this);
        remindersSection = remindersView.createView();

        reminderCardView = new ReminderCardView(this);

        titleInput = remindersView.getTitleInput();
        messageInput = remindersView.getMessageInput();
        placeInput = remindersView.getPlaceInput();
        saveButton = remindersView.getSaveButton();
        savedReminderContainer = remindersView.getSavedReminderContainer();

        contextView = new ContextView(this);
        contextSection = contextView.createView();

        currentPlaceInput = contextView.getCurrentPlaceInput();
        checkButton = contextView.getCheckButton();
        backgroundCheckButton = contextView.getBackgroundCheckButton();

        sensorView = new SensorView(this);
        sensorsSection = sensorView.createView();

        simulateWearableButton = sensorView.getSimulateWearableButton();
        phoneSensorButton = sensorView.getPhoneSensorButton();

        profileView = new ProfileView(this);
        profileSection = profileView.createView();

        aboutButton = profileView.getAboutButton();
        termsButton = profileView.getTermsButton();
        aboutCard = profileView.getAboutCard();
        termsCard = profileView.getTermsCard();

        resultText = new TextView(this);
        styleResultText(resultText);
        resultText.setVisibility(View.GONE);

        mainLayout.addView(heading);
        mainLayout.addView(tagline);

        mainLayout.addView(dashboardSection);
        mainLayout.addView(remindersSection);
        mainLayout.addView(contextSection);
        mainLayout.addView(sensorsSection);
        mainLayout.addView(profileSection);

        mainLayout.addView(resultText);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.rgb(5, 8, 20));
        scrollView.addView(mainLayout);

        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        );
        scrollView.setLayoutParams(scrollParams);

        View bottomDivider = new View(this);
        bottomDivider.setBackgroundColor(Color.rgb(35, 45, 70));

        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(1)
        );
        bottomDivider.setLayoutParams(dividerParams);

        bottomNavView = new BottomNavView(this);
        LinearLayout bottomNav = bottomNavView.createView();

        dashboardTab = bottomNavView.getDashboardTab();
        remindersTab = bottomNavView.getRemindersTab();
        contextTab = bottomNavView.getContextTab();
        sensorsTab = bottomNavView.getSensorsTab();
        profileTab = bottomNavView.getProfileTab();

        rootLayout.addView(scrollView);
        rootLayout.addView(bottomDivider);
        rootLayout.addView(bottomNav);

        setContentView(rootLayout);

        showSection("dashboard");
        updateSelectedTab(dashboardTab);
        updateDashboardSummary();
        updateProfileStatus();

        loadRemindersFromFirebase();

        dashboardTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSection("dashboard");
                updateSelectedTab(dashboardTab);
                updateDashboardSummary();
                hideResult();
            }
        });

        remindersTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSection("reminders");
                updateSelectedTab(remindersTab);
                hideResult();
            }
        });

        contextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSection("context");
                updateSelectedTab(contextTab);
                hideResult();
            }
        });

        sensorsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSection("sensors");
                updateSelectedTab(sensorsTab);
                hideResult();
            }
        });

        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSection("profile");
                updateSelectedTab(profileTab);
                updateProfileStatus();
                hideResult();
                aboutCard.setVisibility(View.GONE);
                termsCard.setVisibility(View.GONE);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aboutCard.getVisibility() == View.VISIBLE) {
                    aboutCard.setVisibility(View.GONE);
                } else {
                    aboutCard.setVisibility(View.VISIBLE);
                    termsCard.setVisibility(View.GONE);
                }
            }
        });

        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termsCard.getVisibility() == View.VISIBLE) {
                    termsCard.setVisibility(View.GONE);
                } else {
                    termsCard.setVisibility(View.VISIBLE);
                    aboutCard.setVisibility(View.GONE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString().trim();
                String message = messageInput.getText().toString().trim();
                String place = placeInput.getText().toString().trim();

                if (title.isEmpty() || message.isEmpty() || place.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveReminderToFirebase(title, message, place);

                titleInput.setText("");
                messageInput.setText("");
                placeInput.setText("");
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkReminder(false);
            }
        });

        backgroundCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPlace = currentPlaceInput.getText().toString().trim();

                if (currentPlace.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter current place first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this,
                        "Background check started. Minimize the app now.",
                        Toast.LENGTH_LONG).show();

                showResult("Result:\nBackground check will run in 10 seconds...");

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkReminder(true);
                    }
                }, 10000);
            }
        });

        simulateWearableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSimulatedWearableData();
            }
        });

        phoneSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoneSensorDetection();
            }
        });
    }

    private void showSection(String sectionName) {
        dashboardSection.setVisibility(View.GONE);
        remindersSection.setVisibility(View.GONE);
        contextSection.setVisibility(View.GONE);
        sensorsSection.setVisibility(View.GONE);
        profileSection.setVisibility(View.GONE);

        if (sectionName.equals("dashboard")) {
            dashboardSection.setVisibility(View.VISIBLE);
        } else if (sectionName.equals("reminders")) {
            remindersSection.setVisibility(View.VISIBLE);
        } else if (sectionName.equals("context")) {
            contextSection.setVisibility(View.VISIBLE);
        } else if (sectionName.equals("sensors")) {
            sensorsSection.setVisibility(View.VISIBLE);
        } else if (sectionName.equals("profile")) {
            profileSection.setVisibility(View.VISIBLE);
        }
    }

    private void showResult(String message) {
        resultText.setText(message);
        resultText.setVisibility(View.VISIBLE);
    }

    private void hideResult() {
        resultText.setVisibility(View.GONE);
    }

    private void updateSelectedTab(Button selectedButton) {
        if (bottomNavView != null) {
            bottomNavView.updateSelectedTab(selectedButton);
        }
    }

    private void updateDashboardSummary() {
        if (dashboardView == null) {
            return;
        }

        boolean accelerometerAvailable = false;

        if (phoneSensorHelper != null) {
            accelerometerAvailable = phoneSensorHelper.isAccelerometerAvailable();
        }

        dashboardView.updateSummary(
                reminders.size(),
                latestDetectedActivity,
                latestMovementLevel,
                accelerometerAvailable
        );
    }

    private void updateProfileStatus() {
        if (profileView == null) {
            return;
        }

        boolean sensorAvailable = false;

        if (phoneSensorHelper != null) {
            sensorAvailable = phoneSensorHelper.isAccelerometerAvailable();
        }

        profileView.updateDeviceStatus(
                sensorAvailable,
                latestDetectedActivity,
                latestMovementLevel
        );
    }

    private void saveReminderToFirebase(String title, String message, String place) {
        Reminder reminder = new Reminder(title, message, place);

        repository.saveReminder(reminder, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(MainActivity.this,
                        "Reminder saved to Firebase",
                        Toast.LENGTH_SHORT).show();

                showResult(
                        "Result:\nReminder saved successfully.\n\n" +
                                "Title: " + title + "\n" +
                                "Place: " + place
                );

                loadRemindersFromFirebase();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this,
                        "Firebase save failed: " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadRemindersFromFirebase() {
        repository.loadReminders(new FirebaseRepository.ReminderListCallback() {
            @Override
            public void onSuccess(ArrayList<Reminder> reminderList) {
                reminders.clear();
                savedReminderContainer.removeAllViews();

                reminders.addAll(reminderList);

                for (Reminder reminder : reminders) {
                    addReminderView(reminder);
                }

                updateDashboardSummary();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this,
                        "Firebase load failed: " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addReminderView(Reminder reminder) {
        if (reminderCardView == null) {
            reminderCardView = new ReminderCardView(this);
        }

        LinearLayout card = reminderCardView.createCard(
                reminder,
                new ReminderCardView.ReminderActionListener() {
                    @Override
                    public void onEdit(Reminder selectedReminder) {
                        showEditDialog(selectedReminder);
                    }

                    @Override
                    public void onDelete(Reminder selectedReminder) {
                        deleteReminderFromFirebase(selectedReminder.getId());
                    }
                }
        );

        savedReminderContainer.addView(card);
    }

    private void showEditDialog(Reminder reminder) {
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(40, 20, 40, 20);

        EditText editTitle = new EditText(this);
        editTitle.setHint("Reminder Title");
        editTitle.setText(reminder.getTitle());

        EditText editMessage = new EditText(this);
        editMessage.setHint("Reminder Message");
        editMessage.setText(reminder.getMessage());

        EditText editPlace = new EditText(this);
        editPlace.setHint("Place Name");
        editPlace.setText(reminder.getPlace());

        dialogLayout.addView(editTitle);
        dialogLayout.addView(editMessage);
        dialogLayout.addView(editPlace);

        new AlertDialog.Builder(this)
                .setTitle("Edit Reminder")
                .setView(dialogLayout)
                .setPositiveButton("Update", (dialogInterface, i) -> {
                    String newTitle = editTitle.getText().toString().trim();
                    String newMessage = editMessage.getText().toString().trim();
                    String newPlace = editPlace.getText().toString().trim();

                    if (newTitle.isEmpty() || newMessage.isEmpty() || newPlace.isEmpty()) {
                        Toast.makeText(MainActivity.this,
                                "All fields are required",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updateReminderInFirebase(reminder.getId(), newTitle, newMessage, newPlace);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateReminderInFirebase(String reminderId, String title, String message, String place) {
        Reminder updatedReminder = new Reminder(title, message, place);

        repository.updateReminder(reminderId, updatedReminder, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(MainActivity.this,
                        "Reminder updated",
                        Toast.LENGTH_SHORT).show();

                showResult(
                        "Result:\nReminder updated successfully.\n\n" +
                                "Title: " + title + "\n" +
                                "Place: " + place
                );

                loadRemindersFromFirebase();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this,
                        "Update failed: " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteReminderFromFirebase(String reminderId) {
        repository.deleteReminder(reminderId, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(MainActivity.this,
                        "Reminder deleted",
                        Toast.LENGTH_SHORT).show();

                showResult("Result:\nReminder deleted successfully.");

                loadRemindersFromFirebase();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this,
                        "Delete failed: " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkReminder(boolean fromBackgroundCheck) {
        String currentPlace = currentPlaceInput.getText().toString().trim();

        if (currentPlace.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter current place", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder matchedReminders = new StringBuilder();
        boolean found = false;

        for (Reminder reminder : reminders) {
            if (reminder.getPlace().equalsIgnoreCase(currentPlace)) {
                found = true;
                matchedReminders.append("Title: ").append(reminder.getTitle())
                        .append("\nMessage: ").append(reminder.getMessage())
                        .append("\nPlace: ").append(reminder.getPlace())
                        .append("\n\n");
            }
        }

        String triggerType;
        if (fromBackgroundCheck) {
            triggerType = "background_simulation";
        } else {
            triggerType = "foreground_manual";
        }

        if (found) {
            saveContextLog(currentPlace, "matched", triggerType, matchedReminders.toString());

            showResult("Result:\nReminder Triggered!\n\n" + matchedReminders);

            if (isAppInForeground && !fromBackgroundCheck) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reminder Triggered")
                        .setMessage(matchedReminders.toString())
                        .setPositiveButton("OK", null)
                        .show();
            } else if (isAppInForeground) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Background Check Result")
                        .setMessage(matchedReminders.toString())
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                notificationHelper.showPhoneNotification("Reminder Triggered", matchedReminders.toString());
            }

        } else {
            saveContextLog(currentPlace, "not_matched", triggerType, "No reminder found");

            showResult("Result:\nNo reminder found for this place.");

            if (isAppInForeground) {
                Toast.makeText(MainActivity.this, "No reminder found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveContextLog(String currentPlace, String result, String triggerType, String matchedDetails) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("currentPlace", currentPlace);
        logData.put("result", result);
        logData.put("triggerType", triggerType);
        logData.put("matchedDetails", matchedDetails);
        logData.put("timestamp", System.currentTimeMillis());

        repository.saveContextLog(logData, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
            }

            @Override
            public void onFailure(String error) {
                if (isAppInForeground) {
                    Toast.makeText(MainActivity.this,
                            "Context log failed: " + error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveSimulatedWearableData() {
        if (wearableSimulationHelper == null) {
            wearableSimulationHelper = new WearableSimulationHelper();
        }

        String currentPlace = currentPlaceInput.getText().toString().trim();

        wearableSimulationHelper.generateWearableData(
                currentPlace,
                new WearableSimulationHelper.WearableSimulationCallback() {
                    @Override
                    public void onDataGenerated(
                            Map<String, Object> wearableData,
                            String deviceId,
                            String detectedActivity,
                            String generatedPlace
                    ) {
                        repository.saveWearableData(wearableData, new FirebaseRepository.SimpleCallback() {
                            @Override
                            public void onSuccess(String successMessage) {
                                Toast.makeText(MainActivity.this,
                                        "Simulated wearable data saved",
                                        Toast.LENGTH_SHORT).show();

                                showResult(
                                        "Result:\nWearable Data Simulated\n\n" +
                                                "Device: " + deviceId + "\n" +
                                                "Activity: " + detectedActivity + "\n" +
                                                "Place: " + generatedPlace + "\n" +
                                                "Saved to Firebase collection: wearable_data"
                                );
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(MainActivity.this,
                                        "Wearable data save failed: " + error,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
        );
    }

    private void startPhoneSensorDetection() {
        if (phoneSensorHelper == null) {
            phoneSensorHelper = new PhoneSensorHelper(this);
        }

        phoneSensorHelper.startDetection(new PhoneSensorHelper.SensorDetectionCallback() {
            @Override
            public void onSensorUnavailable() {
                Toast.makeText(MainActivity.this,
                        "Accelerometer sensor not available",
                        Toast.LENGTH_LONG).show();

                showResult(
                        "Result:\nPhone Sensor Detection\n\n" +
                                "Accelerometer sensor not available on this device.\n" +
                                "App is safe. No crash."
                );
            }

            @Override
            public void onDetectionStarted() {
                Toast.makeText(MainActivity.this,
                        "Phone sensor detection started for 5 seconds",
                        Toast.LENGTH_SHORT).show();

                showResult(
                        "Result:\nPhone sensor detection running...\n" +
                                "Move the phone slightly for testing."
                );
            }

            @Override
            public void onDetectionCompleted(
                    float x,
                    float y,
                    float z,
                    double movementLevel,
                    String detectedActivity
            ) {
                latestDetectedActivity = detectedActivity;
                latestMovementLevel = movementLevel;

                updateDashboardSummary();
                updateProfileStatus();

                savePhoneSensorData(
                        x,
                        y,
                        z,
                        movementLevel,
                        detectedActivity
                );
            }
        });
    }

    private void savePhoneSensorData(float x, float y, float z, double movementLevel, String detectedActivity) {
        String currentPlace = currentPlaceInput.getText().toString().trim();

        if (currentPlace.isEmpty()) {
            currentPlace = "unknown";
        }

        final String finalCurrentPlace = currentPlace;

        Map<String, Object> sensorData = new HashMap<>();

        sensorData.put("accelerometerX", x);
        sensorData.put("accelerometerY", y);
        sensorData.put("accelerometerZ", z);
        sensorData.put("movementLevel", movementLevel);
        sensorData.put("detectedActivity", detectedActivity);
        sensorData.put("currentPlace", finalCurrentPlace);
        sensorData.put("source", "phone_accelerometer");
        sensorData.put("contextType", "place_plus_activity");
        sensorData.put("timestamp", System.currentTimeMillis());

        repository.savePhoneSensorData(sensorData, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(MainActivity.this,
                        "Phone sensor data saved",
                        Toast.LENGTH_SHORT).show();

                showResult(
                        "Result:\nPhone Sensor Data Saved\n\n" +
                                "Activity: " + detectedActivity + "\n" +
                                "Place: " + finalCurrentPlace + "\n" +
                                "Movement Level: " + movementLevel + "\n" +
                                "X: " + x + "\n" +
                                "Y: " + y + "\n" +
                                "Z: " + z + "\n" +
                                "Saved to Firebase collection: phone_sensor_data"
                );

                checkSmartContextReminder(finalCurrentPlace, detectedActivity);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this,
                        "Phone sensor data save failed: " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkSmartContextReminder(String currentPlace, String detectedActivity) {
        if (currentPlace == null || currentPlace.trim().isEmpty()) {
            currentPlace = "unknown";
        }

        StringBuilder matchedReminders = new StringBuilder();
        boolean found = false;

        for (Reminder reminder : reminders) {
            if (reminder.getPlace().equalsIgnoreCase(currentPlace)) {
                found = true;

                matchedReminders.append("Title: ").append(reminder.getTitle())
                        .append("\nMessage: ").append(reminder.getMessage())
                        .append("\nPlace: ").append(reminder.getPlace())
                        .append("\nActivity: ").append(detectedActivity)
                        .append("\n\n");
            }
        }

        if (found) {
            saveContextLog(
                    currentPlace,
                    "smart_context_matched",
                    "place_plus_activity",
                    matchedReminders.toString()
            );

            showResult(
                    "Result:\nSmart Context Reminder Triggered!\n\n" +
                            matchedReminders
            );

            if (isAppInForeground) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Smart Context Reminder")
                        .setMessage(matchedReminders.toString())
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                notificationHelper.showPhoneNotification("Smart Context Reminder", matchedReminders.toString());
            }

        } else {
            saveContextLog(
                    currentPlace,
                    "smart_context_not_matched",
                    "place_plus_activity",
                    "No reminder found for place: " + currentPlace + " and activity: " + detectedActivity
            );

            showResult(
                    "Result:\nNo smart context reminder found.\n\n" +
                            "Place: " + currentPlace + "\n" +
                            "Activity: " + detectedActivity
            );
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void styleResultText(TextView textView) {
        textView.setTextSize(15);
        textView.setTextColor(Color.rgb(230, 235, 245));
        textView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        textView.setPadding(0, dp(8), 0, dp(8));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(6), 0, dp(4));
        textView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAppInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAppInForeground = false;
    }
}