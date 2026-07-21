package com.example.contextreminderapp.ui.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultView {

    private final Context context;
    private TextView resultText;

    public ResultView(Context context) {
        this.context = context;
    }

    public TextView createView() {
        resultText = new TextView(context);
        styleResultText(resultText);
        resultText.setVisibility(View.GONE);

        return resultText;
    }

    public void show(String message) {
        if (resultText == null) {
            return;
        }

        resultText.setText(message);
        resultText.setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (resultText == null) {
            return;
        }

        resultText.setVisibility(View.GONE);
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

    private int dp(int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}