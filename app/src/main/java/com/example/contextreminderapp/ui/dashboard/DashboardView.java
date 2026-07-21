package com.example.contextreminderapp.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DashboardView {

    private final Context context;
    private TextView dashboardSummaryText;

    public DashboardView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout dashboardSection = new LinearLayout(context);
        dashboardSection.setOrientation(LinearLayout.VERTICAL);

        TextView dashboardHeading = new TextView(context);
        dashboardHeading.setText("Dashboard");
        styleSectionHeading(dashboardHeading);

        TextView dashboardIntro = new TextView(context);
        dashboardIntro.setText(
                "Welcome to SynqSense.\n\n" +
                        "A context-aware reminder prototype that connects reminders, places, wearable simulation, and phone sensor activity."
        );
        styleInfoCard(dashboardIntro);

        dashboardSummaryText = new TextView(context);
        styleInfoCard(dashboardSummaryText);

        dashboardSection.addView(dashboardHeading);
        dashboardSection.addView(dashboardIntro);
        dashboardSection.addView(dashboardSummaryText);

        return dashboardSection;
    }

    public void updateSummary(int reminderCount, String latestDetectedActivity, double latestMovementLevel, boolean accelerometerAvailable) {
        if (dashboardSummaryText == null) {
            return;
        }

        String sensorStatus;

        if (accelerometerAvailable) {
            sensorStatus = "Phone accelerometer: Available";
        } else {
            sensorStatus = "Phone accelerometer: Not available";
        }

        dashboardSummaryText.setText(
                "Project Status\n\n" +
                        "Total reminders: " + reminderCount + "\n" +
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

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
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