package com.example.contextreminderapp.ui.reminders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemindersView {

    private final Context context;

    private EditText titleInput;
    private EditText messageInput;
    private EditText placeInput;
    private Button saveButton;
    private LinearLayout savedReminderContainer;

    public RemindersView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout remindersSection = new LinearLayout(context);
        remindersSection.setOrientation(LinearLayout.VERTICAL);

        TextView addReminderHeading = new TextView(context);
        addReminderHeading.setText("Add Reminder");
        styleSectionHeading(addReminderHeading);

        titleInput = new EditText(context);
        titleInput.setHint("Reminder Title");
        styleInput(titleInput);

        messageInput = new EditText(context);
        messageInput.setHint("Reminder Message");
        styleInput(messageInput);

        placeInput = new EditText(context);
        placeInput.setHint("Place Name");
        styleInput(placeInput);

        saveButton = new Button(context);
        saveButton.setText("Save Reminder");
        styleButton(saveButton, false);

        TextView savedHeading = new TextView(context);
        savedHeading.setText("Saved Reminders");
        styleSectionHeading(savedHeading);

        savedReminderContainer = new LinearLayout(context);
        savedReminderContainer.setOrientation(LinearLayout.VERTICAL);

        remindersSection.addView(addReminderHeading);
        remindersSection.addView(titleInput);
        remindersSection.addView(messageInput);
        remindersSection.addView(placeInput);
        remindersSection.addView(saveButton);
        remindersSection.addView(savedHeading);
        remindersSection.addView(savedReminderContainer);

        return remindersSection;
    }

    public EditText getTitleInput() {
        return titleInput;
    }

    public EditText getMessageInput() {
        return messageInput;
    }

    public EditText getPlaceInput() {
        return placeInput;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public LinearLayout getSavedReminderContainer() {
        return savedReminderContainer;
    }

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
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
}