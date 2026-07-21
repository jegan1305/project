package com.example.contextreminderapp.ui.navigation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.contextreminderapp.R;

public class BottomNavView {

    private final Context context;

    private Button dashboardTab;
    private Button remindersTab;
    private Button contextTab;
    private Button sensorsTab;
    private Button profileTab;

    public BottomNavView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout bottomNav = new LinearLayout(context);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setBackgroundColor(Color.rgb(12, 18, 34));
        bottomNav.setPadding(dp(4), 0, dp(4), 0);

        dashboardTab = new Button(context);
        dashboardTab.setText("Dashboard");
        dashboardTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_dashboard, 0, 0);
        dashboardTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(dashboardTab);

        remindersTab = new Button(context);
        remindersTab.setText("Reminder");
        remindersTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_reminder, 0, 0);
        remindersTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(remindersTab);

        contextTab = new Button(context);
        contextTab.setText("Context");
        contextTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_context, 0, 0);
        contextTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(contextTab);

        sensorsTab = new Button(context);
        sensorsTab.setText("Sensor");
        sensorsTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_sensor, 0, 0);
        sensorsTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(sensorsTab);

        profileTab = new Button(context);
        profileTab.setText("Profile");
        profileTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_nav_profile, 0, 0);
        profileTab.setCompoundDrawablePadding(dp(3));
        styleBottomNavButton(profileTab);

        bottomNav.addView(dashboardTab);
        bottomNav.addView(remindersTab);
        bottomNav.addView(contextTab);
        bottomNav.addView(sensorsTab);
        bottomNav.addView(profileTab);

        return bottomNav;
    }

    public Button getDashboardTab() {
        return dashboardTab;
    }

    public Button getRemindersTab() {
        return remindersTab;
    }

    public Button getContextTab() {
        return contextTab;
    }

    public Button getSensorsTab() {
        return sensorsTab;
    }

    public Button getProfileTab() {
        return profileTab;
    }

    public void updateSelectedTab(Button selectedButton) {
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

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}