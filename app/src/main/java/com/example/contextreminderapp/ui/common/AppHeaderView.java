package com.example.contextreminderapp.ui.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppHeaderView {

    private final Context context;

    public AppHeaderView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout headerLayout = new LinearLayout(context);
        headerLayout.setOrientation(LinearLayout.VERTICAL);

        TextView heading = new TextView(context);
        heading.setText("SynqSense");
        heading.setTextSize(28);
        heading.setTextColor(Color.rgb(0, 230, 255));
        heading.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        heading.setPadding(0, 0, 0, dp(2));

        TextView tagline = new TextView(context);
        tagline.setText("Dark Neon Context-Aware Reminder System");
        tagline.setTextSize(13);
        tagline.setTextColor(Color.rgb(180, 190, 210));
        tagline.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        tagline.setPadding(0, 0, 0, dp(10));

        headerLayout.addView(heading);
        headerLayout.addView(tagline);

        return headerLayout;
    }

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}