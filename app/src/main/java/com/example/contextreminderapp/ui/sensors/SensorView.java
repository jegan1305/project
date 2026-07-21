package com.example.contextreminderapp.ui.sensors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SensorView {

    private final Context context;

    private Button simulateWearableButton;
    private Button phoneSensorButton;

    public SensorView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout sensorsSection = new LinearLayout(context);
        sensorsSection.setOrientation(LinearLayout.VERTICAL);

        TextView sensorsHeading = new TextView(context);
        sensorsHeading.setText("Sensors");
        styleSectionHeading(sensorsHeading);

        TextView sensorsInfo = new TextView(context);
        sensorsInfo.setText(
                "Use wearable simulation or phone sensor detection to capture activity and connect it with the current place."
        );
        styleInfoCard(sensorsInfo);

        simulateWearableButton = new Button(context);
        simulateWearableButton.setText("Simulate Wearable Data");
        styleButton(simulateWearableButton);

        phoneSensorButton = new Button(context);
        phoneSensorButton.setText("Start Phone Sensor Detection");
        styleButton(phoneSensorButton);

        sensorsSection.addView(sensorsHeading);
        sensorsSection.addView(sensorsInfo);
        sensorsSection.addView(simulateWearableButton);
        sensorsSection.addView(phoneSensorButton);

        return sensorsSection;
    }

    public Button getSimulateWearableButton() {
        return simulateWearableButton;
    }

    public Button getPhoneSensorButton() {
        return phoneSensorButton;
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