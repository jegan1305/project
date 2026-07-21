package com.example.contextreminderapp.ui.reminders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contextreminderapp.models.Reminder;

public class ReminderCardView {

    private final Context context;

    public interface ReminderActionListener {
        void onEdit(Reminder reminder);
        void onDelete(Reminder reminder);
    }

    public ReminderCardView(Context context) {
        this.context = context;
    }

    public LinearLayout createCard(Reminder reminder, ReminderActionListener listener) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(10), dp(14), dp(10));
        card.setBackgroundColor(Color.rgb(10, 16, 32));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, dp(6), 0, dp(8));
        card.setLayoutParams(cardParams);

        TextView reminderText = new TextView(context);
        reminderText.setText(
                "Title: " + reminder.getTitle() +
                        "\nMessage: " + reminder.getMessage() +
                        "\nPlace: " + reminder.getPlace()
        );
        reminderText.setTextSize(15);
        reminderText.setTextColor(Color.rgb(230, 235, 245));
        reminderText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        reminderText.setPadding(0, 0, 0, dp(6));

        Button editButton = new Button(context);
        editButton.setText("Edit Reminder");
        styleButton(editButton, false);

        Button deleteButton = new Button(context);
        deleteButton.setText("Delete Reminder");
        styleButton(deleteButton, true);

        editButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onEdit(reminder);
            }
        });

        deleteButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDelete(reminder);
            }
        });

        card.addView(reminderText);
        card.addView(editButton);
        card.addView(deleteButton);

        return card;
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

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}