package com.example.contextreminderapp.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WearableSimulationHelper {

    private final Random random;

    public interface WearableSimulationCallback {
        void onDataGenerated(
                Map<String, Object> wearableData,
                String deviceId,
                String detectedActivity,
                String currentPlace
        );
    }

    public WearableSimulationHelper() {
        random = new Random();
    }

    public void generateWearableData(String currentPlace, WearableSimulationCallback callback) {
        if (currentPlace == null || currentPlace.trim().isEmpty()) {
            currentPlace = "unknown";
        }

        String finalCurrentPlace = currentPlace.trim();

        String deviceId = "demo_watch_001";

        String[] activities = {"walking", "sitting", "standing", "idle"};
        String detectedActivity = activities[random.nextInt(activities.length)];

        double accelerometerX = -2 + (4 * random.nextDouble());
        double accelerometerY = -2 + (4 * random.nextDouble());
        double accelerometerZ = 8 + (3 * random.nextDouble());

        double gyroscopeX = -0.5 + random.nextDouble();
        double gyroscopeY = -0.5 + random.nextDouble();
        double gyroscopeZ = -0.5 + random.nextDouble();

        Map<String, Object> wearableData = new HashMap<>();

        wearableData.put("deviceId", deviceId);
        wearableData.put("accelerometerX", accelerometerX);
        wearableData.put("accelerometerY", accelerometerY);
        wearableData.put("accelerometerZ", accelerometerZ);
        wearableData.put("gyroscopeX", gyroscopeX);
        wearableData.put("gyroscopeY", gyroscopeY);
        wearableData.put("gyroscopeZ", gyroscopeZ);
        wearableData.put("detectedActivity", detectedActivity);
        wearableData.put("currentPlace", finalCurrentPlace);
        wearableData.put("source", "simulated_wearable");
        wearableData.put("timestamp", System.currentTimeMillis());

        if (callback != null) {
            callback.onDataGenerated(
                    wearableData,
                    deviceId,
                    detectedActivity,
                    finalCurrentPlace
            );
        }
    }
}