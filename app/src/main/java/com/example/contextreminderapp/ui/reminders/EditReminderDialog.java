package com.example.contextreminderapp.ui.reminders;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.contextreminderapp.models.Reminder;

public class EditReminderDialog {

    private final Activity activity;

    public interface EditReminderCallback {
        void onUpdate(Reminder reminder, String title, String message, String place);
    }

    public EditReminderDialog(Activity activity) {
        this.activity = activity;
    }

    public void show(Reminder reminder, EditReminderCallback callback) {
        LinearLayout dialogLayout = new LinearLayout(activity);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(40, 20, 40, 20);

        EditText editTitle = new EditText(activity);
        editTitle.setHint("Reminder Title");
        editTitle.setText(reminder.getTitle());

        EditText editMessage = new EditText(activity);
        editMessage.setHint("Reminder Message");
        editMessage.setText(reminder.getMessage());

        EditText editPlace = new EditText(activity);
        editPlace.setHint("Place Name");
        editPlace.setText(reminder.getPlace());

        dialogLayout.addView(editTitle);
        dialogLayout.addView(editMessage);
        dialogLayout.addView(editPlace);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Edit Reminder")
                .setView(dialogLayout)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String newTitle = editTitle.getText().toString().trim();
                String newMessage = editMessage.getText().toString().trim();
                String newPlace = editPlace.getText().toString().trim();

                if (newTitle.isEmpty() || newMessage.isEmpty() || newPlace.isEmpty()) {
                    Toast.makeText(activity, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (callback != null) {
                    callback.onUpdate(reminder, newTitle, newMessage, newPlace);
                }

                dialog.dismiss();
            });
        });

        dialog.show();
    }
}