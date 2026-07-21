package com.example.contextreminderapp.ui.profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileView {

    private final Context context;

    private TextView profileDeviceStatusText;
    private TextView aboutCard;
    private TextView termsCard;

    private Button aboutButton;
    private Button termsButton;

    public ProfileView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout profileSection = new LinearLayout(context);
        profileSection.setOrientation(LinearLayout.VERTICAL);

        TextView profileHeading = new TextView(context);
        profileHeading.setText("Profile");
        styleSectionHeading(profileHeading);

        TextView profileUserCard = new TextView(context);
        profileUserCard.setText(
                "Demo User\n" +
                        "Context-aware reminder user\n\n" +
                        "SynqSense Prototype Tester"
        );
        styleInfoCard(profileUserCard);

        TextView smartPreferencesCard = new TextView(context);
        smartPreferencesCard.setText(
                "Smart Preferences\n\n" +
                        "• Default Place: home\n" +
                        "• Reminder Mode: Context-based\n" +
                        "• Notifications: Enabled"
        );
        styleInfoCard(smartPreferencesCard);

        profileDeviceStatusText = new TextView(context);
        styleInfoCard(profileDeviceStatusText);

        TextView dataSyncCard = new TextView(context);
        dataSyncCard.setText(
                "Data Sync\n\n" +
                        "• Firebase: Connected\n" +
                        "• Reminders: Active\n" +
                        "• Context Logs: Active\n" +
                        "• Sensor Data: Active"
        );
        styleInfoCard(dataSyncCard);

        aboutButton = new Button(context);
        aboutButton.setText("About SynqSense");
        styleButton(aboutButton);

        aboutCard = new TextView(context);
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

        termsButton = new Button(context);
        termsButton.setText("Terms and Conditions");
        styleButton(termsButton);

        termsCard = new TextView(context);
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

        return profileSection;
    }

    public Button getAboutButton() {
        return aboutButton;
    }

    public Button getTermsButton() {
        return termsButton;
    }

    public TextView getAboutCard() {
        return aboutCard;
    }

    public TextView getTermsCard() {
        return termsCard;
    }

    public void updateDeviceStatus(boolean sensorAvailable, String latestDetectedActivity, double latestMovementLevel) {
        if (profileDeviceStatusText == null) {
            return;
        }

        String phoneSensorStatus;

        if (sensorAvailable) {
            phoneSensorStatus = "Available";
        } else {
            phoneSensorStatus = "Not available";
        }

        profileDeviceStatusText.setText(
                "Device Status\n\n" +
                        "• Phone Sensor: " + phoneSensorStatus + "\n" +
                        "• Wearable Mode: Simulation\n" +
                        "• Last Activity: " + latestDetectedActivity + "\n" +
                        "• Movement Level: " + latestMovementLevel
        );
    }

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void styleButton(Button button) {
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
        button.setTextColor(Color.rgb(0, 230, 255));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(4), 0, dp(4));
        button.setLayoutParams(params);
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
}