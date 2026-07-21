package com.example.contextreminderapp;

import com.example.contextreminderapp.data.FirebaseRepository;
import com.example.contextreminderapp.models.Reminder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import java.util.Random;

public class MainActivity extends Activity {

    EditText titleInput, messageInput, placeInput, currentPlaceInput;

    Button saveButton, checkButton, backgroundCheckButton, simulateWearableButton, phoneSensorButton;
    Button dashboardTab, remindersTab, contextTab, sensorsTab, profileTab;
    Button aboutButton, termsButton;

    TextView resultText, dashboardSummaryText, profileDeviceStatusText;
    TextView aboutCard, termsCard;

    LinearLayout savedReminderContainer;
    LinearLayout dashboardSection, remindersSection, contextSection, sensorsSection, profileSection;

    ArrayList<Reminder> reminders = new ArrayList<>();

    FirebaseRepository repository;

    SensorManager sensorManager;
    Sensor accelerometerSensor;

    String latestDetectedActivity = "unknown";
    double latestMovementLevel = 0.0;

    static final String CHANNEL_ID = "context_reminder_channel";
    boolean isAppInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 8, 20));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 20));

        repository = new FirebaseRepository();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        createNotificationChannel();
        requestNotificationPermission();

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

        dashboardSection = new LinearLayout(this);
        dashboardSection.setOrientation(LinearLayout.VERTICAL);

        TextView dashboardHeading = new TextView(this);
        dashboardHeading.setText("Dashboard");
        styleSectionHeading(dashboardHeading);

        TextView dashboardIntro = new TextView(this);
        dashboardIntro.setText(
                "Welcome to SynqSense.\n\n" +
                        "A context-aware reminder prototype that connects reminders, places, wearable simulation, and phone sensor activity."
        );
        styleInfoCard(dashboardIntro);

        dashboardSummaryText = new TextView(this);
        styleInfoCard(dashboardSummaryText);

        dashboardSection.addView(dashboardHeading);
        dashboardSection.addView(dashboardIntro);
        dashboardSection.addView(dashboardSummaryText);

        remindersSection = new LinearLayout(this);
        remindersSection.setOrientation(LinearLayout.VERTICAL);

        TextView addReminderHeading = new TextView(this);
        addReminderHeading.setText("Add Reminder");
        styleSectionHeading(addReminderHeading);

        titleInput = new EditText(this);
        titleInput.setHint("Reminder Title");
        styleInput(titleInput);

        messageInput = new EditText(this);
        messageInput.setHint("Reminder Message");
        styleInput(messageInput);

        placeInput = new EditText(this);
        placeInput.setHint("Place Name");
        styleInput(placeInput);

        saveButton = new Button(this);
        saveButton.setText("Save Reminder");
        styleButton(saveButton, false);

        TextView savedHeading = new TextView(this);
        savedHeading.setText("Saved Reminders");
        styleSectionHeading(savedHeading);

        savedReminderContainer = new LinearLayout(this);
        savedReminderContainer.setOrientation(LinearLayout.VERTICAL);

        remindersSection.addView(addReminderHeading);
        remindersSection.addView(titleInput);
        remindersSection.addView(messageInput);
        remindersSection.addView(placeInput);
        remindersSection.addView(saveButton);
        remindersSection.addView(savedHeading);
        remindersSection.addView(savedReminderContainer);

        contextSection = new LinearLayout(this);
        contextSection.setOrientation(LinearLayout.VERTICAL);

        TextView contextHeading = new TextView(this);
        contextHeading.setText("Check Current Context");
        styleSectionHeading(contextHeading);

        TextView contextInfo = new TextView(this);
        contextInfo.setText(
                "Enter your current place and check whether any reminder matches that context."
        );
        styleInfoCard(contextInfo);

        currentPlaceInput = new EditText(this);
        currentPlaceInput.setHint("Enter Current Place");
        styleInput(currentPlaceInput);

        checkButton = new Button(this);
        checkButton.setText("Check Reminder Now");
        styleButton(checkButton, false);

        backgroundCheckButton = new Button(this);
        backgroundCheckButton.setText("Start Background Check");
        styleButton(backgroundCheckButton, false);

        contextSection.addView(contextHeading);
        contextSection.addView(contextInfo);
        contextSection.addView(currentPlaceInput);
        contextSection.addView(checkButton);
        contextSection.addView(backgroundCheckButton);

        sensorsSection = new LinearLayout(this);
        sensorsSection.setOrientation(LinearLayout.VERTICAL);

        TextView sensorsHeading = new TextView(this);
        sensorsHeading.setText("Sensors");
        styleSectionHeading(sensorsHeading);

        TextView sensorsInfo = new TextView(this);
        sensorsInfo.setText(
                "Use wearable simulation or phone sensor detection to capture activity and connect it with the current place."
        );
        styleInfoCard(sensorsInfo);

        simulateWearableButton = new Button(this);
        simulateWearableButton.setText("Simulate Wearable Data");
        styleButton(simulateWearableButton, false);

        phoneSensorButton = new Button(this);
        phoneSensorButton.setText("Start Phone Sensor Detection");
        styleButton(phoneSensorButton, false);

        sensorsSection.addView(sensorsHeading);
        sensorsSection.addView(sensorsInfo);
        sensorsSection.addView(simulateWearableButton);
        sensorsSection.addView(phoneSensorButton);

        profileSection = new LinearLayout(this);
        profileSection.setOrientation(LinearLayout.VERTICAL);

        TextView profileHeading = new TextView(this);
        profileHeading.setText("Profile");
        styleSectionHeading(profileHeading);

        TextView profileUserCard = new TextView(this);
        profileUserCard.setText(
                "Demo User\n" +
                        "Context-aware reminder user\n\n" +
                        "SynqSense Prototype Tester"
        );
        styleInfoCard(profileUserCard);

        TextView smartPreferencesCard = new TextView(this);
        smartPreferencesCard.setText(
                "Smart Preferences\n\n" +
                        "• Default Place: home\n" +
                        "• Reminder Mode: Context-based\n" +
                        "• Notifications: Enabled"
        );
        styleInfoCard(smartPreferencesCard);

        profileDeviceStatusText = new TextView(this);
        styleInfoCard(profileDeviceStatusText);

        TextView dataSyncCard = new TextView(this);
        dataSyncCard.setText(
                "Data Sync\n\n" +
                        "• Firebase: Connected\n" +
                        "• Reminders: Active\n" +
                        "• Context Logs: Active\n" +
                        "• Sensor Data: Active"
        );
        styleInfoCard(dataSyncCard);

        aboutButton = new Button(this);
        aboutButton.setText("About SynqSense");
        styleButton(aboutButton, false);

        aboutCard = new TextView(this);
        aboutCard.setText(
                "About SynqSense\n\n" +
                        "SynqSense is a context-aware reminder prototype that connects reminders with the user's place and activity.\n\n" +
                        "Main Purpose\n\n" +
                        "To help users receive reminders when they are actually relevant to their current situation, instead of depending only on time-based alerts.\n\n" +
                        "Current Version\n\n" +
                        "The app supports Firebase reminder storage, place-based reminder checking, context logs, phone sensor detection, and wearable data simulation.\n\n" +
                        "How It Works\n\n" +
                        "Users can create reminders with a title, message, and place. When the current place matches a saved reminder, the app can trigger the reminder.\n\n" +
                        "Prototype Note\n\n" +
                        "This is a prototype version. Some features are simulated for testing and demonstration. Phone sensor detection depends on device sensor availability.\n\n" +
                        "Project Goal\n\n" +
                        "The goal of SynqSense is to explore a smarter reminder system that gives reminders when they are more meaningful and useful."
        );
        styleInfoCard(aboutCard);
        aboutCard.setVisibility(View.GONE);

        termsButton = new Button(this);
        termsButton.setText("Terms and Conditions");
        styleButton(termsButton, false);

        termsCard = new TextView(this);
        termsCard.setText(
                "Terms and Conditions\n\n" +
                        "1. SynqSense is currently a prototype application created for project demonstration and testing purposes.\n\n" +
                        "2. The reminder result depends on the place entered by the user and the available device sensor data.\n\n" +
                        "3. Phone sensor detection may vary based on device model, sensor availability, and movement condition.\n\n" +
                        "4. Wearable data in the current version is simulated for testing the context-aware reminder workflow.\n\n" +
                        "5. Reminder, context log, wearable data, and phone sensor data may be stored in Firebase for project testing and demonstration.\n\n" +
                        "6. SynqSense should not be used as the only reminder source for emergency, medical-critical, or safety-critical tasks.\n\n" +
                        "7. This app is designed to explore smarter reminders using place and activity context."
        );
        styleInfoCard(termsCard);
        termsCard.setVisibility(View.GONE);

        profileSection.addView(profileHeading);
        profileSection.addView(profileUserCard);
        profileSection.addView(smartPreferencesCard);
        profileSection.addView(profileDeviceStatusText);
        profileSection.addView(dataSyncCard);
        profileSection.addView(aboutButton);
        profileSection.addView(aboutCard);
        profileSection.addView(termsButton);
        profileSection.addView(termsCard);

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

        LinearLayout bottomNav = new LinearLayout(this);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setBackgroundColor(Color.rgb(12, 18, 34));
        bottomNav.setPadding(dp(4), 0, dp(4), 0);

        dashboardTab = new Button(this);
        dashboardTab.setText("Dashboard");
        dashboardTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_dashboard, 0, 0);
        dashboardTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(dashboardTab);

        remindersTab = new Button(this);
        remindersTab.setText("Reminder");
        remindersTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_reminder, 0, 0);
        remindersTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(remindersTab);

        contextTab = new Button(this);
        contextTab.setText("Context");
        contextTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_context, 0, 0);
        contextTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(contextTab);

        sensorsTab = new Button(this);
        sensorsTab.setText("Sensor");
        sensorsTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_sensor, 0, 0);
        sensorsTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(sensorsTab);

        profileTab = new Button(this);
        profileTab.setText("Profile");
        profileTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_profile, 0, 0);
        profileTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(profileTab);

        bottomNav.addView(dashboardTab);
        bottomNav.addView(remindersTab);
        bottomNav.addView(contextTab);
        bottomNav.addView(sensorsTab);
        bottomNav.addView(profileTab);

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

    private void updateDashboardSummary() {
        if (dashboardSummaryText == null) {
            return;
        }

        String sensorStatus;
        if (accelerometerSensor == null) {
            sensorStatus = "Phone accelerometer: Not available";
        } else {
            sensorStatus = "Phone accelerometer: Available";
        }

        dashboardSummaryText.setText(
                "Project Status\n\n" +
                        "Total reminders: " + reminders.size() + "\n" +
                        "Latest activity: " + latestDetectedActivity + "\n" +
                        "Latest movement level: " + latestMovementLevel + "\n" +
                        sensorStatus + "\n\n" +
                        "Modules active:\n" +
                        "• Reminder management\n" +
                        "• Firebase Firestore\n" +
                        "• Context logs\n" +
                        "• Wearable simulation\n" +
                        "• Phone sensor detection"
        );
    }

    private void updateProfileStatus() {
        if (profileDeviceStatusText == null) {
            return;
        }

        String phoneSensorStatus;
        if (accelerometerSensor == null) {
            phoneSensorStatus = "Not available";
        } else {
            phoneSensorStatus = "Available";
        }

        profileDeviceStatusText.setText(
                "Device Status\n\n" +
                        "• Phone Sensor: " + phoneSensorStatus + "\n" +
                        "• Wearable Mode: Simulation\n" +
                        "• Last Activity: " + latestDetectedActivity + "\n" +
                        "• Movement Level: " + latestMovementLevel
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
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(10), dp(14), dp(10));
        card.setBackgroundColor(Color.rgb(10, 16, 32));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, dp(6), 0, dp(8));
        card.setLayoutParams(cardParams);

        TextView reminderText = new TextView(this);
        reminderText.setText(
                "Title: " + reminder.getTitle() +
                        "\nMessage: " + reminder.getMessage() +
                        "\nPlace: " + reminder.getPlace()
        );
        reminderText.setTextSize(15);
        reminderText.setTextColor(Color.rgb(230, 235, 245));
        reminderText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        reminderText.setPadding(0, 0, 0, dp(6));

        Button editButton = new Button(this);
        editButton.setText("Edit Reminder");
        styleButton(editButton, false);

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete Reminder");
        styleButton(deleteButton, true);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(reminder);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReminderFromFirebase(reminder.getId());
            }
        });

        card.addView(reminderText);
        card.addView(editButton);
        card.addView(deleteButton);

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
                showPhoneNotification("Reminder Triggered", matchedReminders.toString());
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
        Random random = new Random();

        String currentPlace = currentPlaceInput.getText().toString().trim();

        if (currentPlace.isEmpty()) {
            currentPlace = "unknown";
        }

        final String finalCurrentPlace = currentPlace;

        String[] activities = {"walking", "sitting", "standing", "idle"};
        String detectedActivity = activities[random.nextInt(activities.length)];

        double accelerometerX = -2 + (4 * random.nextDouble());
        double accelerometerY = -2 + (4 * random.nextDouble());
        double accelerometerZ = 8 + (3 * random.nextDouble());

        double gyroscopeX = -0.5 + random.nextDouble();
        double gyroscopeY = -0.5 + random.nextDouble();
        double gyroscopeZ = -0.5 + random.nextDouble();

        Map<String, Object> wearableData = new HashMap<>();

        wearableData.put("deviceId", "demo_watch_001");
        wearableData.put("accelerometerX", accelerometerX);
        wearableData.put("accelerometerY", accelerometerY);
        wearableData.put("accelerometerZ", accelerometerZ);
        wearableData.put("gyroscopeX", gyroscopeX);
        wearableData.put("gyroscopeY", gyroscopeY);
        wearableData.put("gyroscopeZ", gyroscopeZ);
        wearableData.put("detectedActivity", detectedActivity);
        wearableData.put("currentPlace", currentPlace);
        wearableData.put("source", "simulated_wearable");
        wearableData.put("timestamp", System.currentTimeMillis());

        repository.saveWearableData(wearableData, new FirebaseRepository.SimpleCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(MainActivity.this,
                        "Simulated wearable data saved",
                        Toast.LENGTH_SHORT).show();

                showResult(
                        "Result:\nWearable Data Simulated\n\n" +
                                "Device: demo_watch_001\n" +
                                "Activity: " + detectedActivity + "\n" +
                                "Place: " + finalCurrentPlace + "\n" +
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

    private void startPhoneSensorDetection() {
        if (accelerometerSensor == null) {
            Toast.makeText(MainActivity.this,
                    "Accelerometer sensor not available",
                    Toast.LENGTH_LONG).show();

            showResult(
                    "Result:\nPhone Sensor Detection\n\n" +
                            "Accelerometer sensor not available on this device.\n" +
                            "App is safe. No crash."
            );

            return;
        }

        Toast.makeText(MainActivity.this,
                "Phone sensor detection started for 5 seconds",
                Toast.LENGTH_SHORT).show();

        showResult(
                "Result:\nPhone sensor detection running...\n" +
                        "Move the phone slightly for testing."
        );

        final float[] lastX = {0};
        final float[] lastY = {0};
        final float[] lastZ = {0};
        final double[] maxMovement = {0};

        final SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double magnitude = Math.sqrt((x * x) + (y * y) + (z * z));
                double movementLevel = Math.abs(magnitude - 9.8);

                if (movementLevel > maxMovement[0]) {
                    maxMovement[0] = movementLevel;
                    lastX[0] = x;
                    lastY[0] = y;
                    lastZ[0] = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(
                sensorEventListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sensorManager.unregisterListener(sensorEventListener);

                String detectedActivity;

                if (maxMovement[0] > 2.0) {
                    detectedActivity = "walking";
                } else if (maxMovement[0] > 0.4) {
                    detectedActivity = "slight_movement";
                } else {
                    detectedActivity = "idle_or_standing";
                }

                latestDetectedActivity = detectedActivity;
                latestMovementLevel = maxMovement[0];

                updateDashboardSummary();
                updateProfileStatus();

                savePhoneSensorData(
                        lastX[0],
                        lastY[0],
                        lastZ[0],
                        maxMovement[0],
                        detectedActivity
                );
            }
        }, 5000);
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
                showPhoneNotification("Smart Context Reminder", matchedReminders.toString());
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

    private void styleButton(Button button, boolean isDeleteButton) {
        button.setAllCaps(false);
        button.setTextSize(13);
        button.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        button.setPadding(dp(12), 0, dp(12), 0);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setHeight(dp(40));
        button.setBackgroundColor(Color.rgb(18, 25, 45));

        if (isDeleteButton) {
            button.setTextColor(Color.rgb(255, 120, 180));
        } else {
            button.setTextColor(Color.rgb(0, 230, 255));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(4), 0, dp(4));
        button.setLayoutParams(params);
    }

    private void styleBottomNavButton(Button button) {
        button.setAllCaps(false);
        button.setTextSize(11);
        button.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        button.setTextColor(Color.rgb(145, 155, 175));
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setPadding(0, dp(5), 0, dp(5));

        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setIncludeFontPadding(false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                dp(68),
                1
        );

        params.setMargins(dp(2), dp(4), dp(2), dp(4));
        button.setLayoutParams(params);
    }

    private void updateSelectedTab(Button selectedButton) {
        setTabStyle(dashboardTab, selectedButton == dashboardTab);
        setTabStyle(remindersTab, selectedButton == remindersTab);
        setTabStyle(contextTab, selectedButton == contextTab);
        setTabStyle(sensorsTab, selectedButton == sensorsTab);
        setTabStyle(profileTab, selectedButton == profileTab);
    }

    private void setTabStyle(Button button, boolean isSelected) {
        if (isSelected) {
            int selectedColor = Color.rgb(0, 230, 255);

            button.setTextColor(selectedColor);
            button.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            button.setBackgroundColor(Color.rgb(18, 35, 55));
            tintButtonIcon(button, selectedColor);

        } else {
            int normalColor = Color.rgb(145, 155, 175);

            button.setTextColor(normalColor);
            button.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            button.setBackgroundColor(Color.TRANSPARENT);
            tintButtonIcon(button, normalColor);
        }
    }

    private void tintButtonIcon(Button button, int color) {
        Drawable[] drawables = button.getCompoundDrawables();

        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setTint(color);
            }
        }
    }

    private void styleInput(EditText input) {
        input.setTextSize(15);
        input.setTextColor(Color.rgb(230, 235, 245));
        input.setHintTextColor(Color.rgb(160, 170, 190));
        input.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        input.setSingleLine(true);
        input.setPadding(dp(10), 0, dp(10), 0);
        input.setMinHeight(0);
        input.setMinimumHeight(0);
        input.setHeight(dp(44));
        input.setBackgroundColor(Color.rgb(18, 25, 45));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(4), 0, dp(4));
        input.setLayoutParams(params);
    }

    private void styleSectionHeading(TextView textView) {
        textView.setTextSize(20);
        textView.setTextColor(Color.rgb(0, 230, 255));
        textView.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        textView.setPadding(0, dp(14), 0, dp(6));
    }

    private void styleInfoCard(TextView textView) {
        textView.setTextSize(14);
        textView.setTextColor(Color.rgb(220, 225, 238));
        textView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        textView.setPadding(dp(14), dp(12), dp(14), dp(12));
        textView.setBackgroundColor(Color.rgb(10, 16, 32));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(5), 0, dp(8));
        textView.setLayoutParams(params);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Context Reminder Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for context based reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void showPhoneNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText("You have a reminder for this place")
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}