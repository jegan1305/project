package com.example.contextreminderapp.ui.context;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContextView {

    private final Context context;

    private EditText currentPlaceInput;
    private Button checkButton;
    private Button backgroundCheckButton;

    public ContextView(Context context) {
        this.context = context;
    }

    public LinearLayout createView() {
        LinearLayout contextSection = new LinearLayout(context);
        contextSection.setOrientation(LinearLayout.VERTICAL);

        TextView contextHeading = new TextView(context);
        contextHeading.setText("Check Current Context");
        styleSectionHeading(contextHeading);

        TextView contextInfo = new TextView(context);
        contextInfo.setText(
                "Enter your current place and check whether any reminder matches that context."
        );
        styleInfoCard(contextInfo);

        currentPlaceInput = new EditText(context);
        currentPlaceInput.setHint("Enter Current Place");
        styleInput(currentPlaceInput);

        checkButton = new Button(context);
        checkButton.setText("Check Reminder Now");
        styleButton(checkButton);

        backgroundCheckButton = new Button(context);
        backgroundCheckButton.setText("Start Background Check");
        styleButton(backgroundCheckButton);

        contextSection.addView(contextHeading);
        contextSection.addView(contextInfo);
        contextSection.addView(currentPlaceInput);
        contextSection.addView(checkButton);
        contextSection.addView(backgroundCheckButton);

        return contextSection;
    }

    public EditText getCurrentPlaceInput() {
        return currentPlaceInput;
    }

    public Button getCheckButton() {
        return checkButton;
    }

    public Button getBackgroundCheckButton() {
        return backgroundCheckButton;
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
}