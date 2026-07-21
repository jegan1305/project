package com.example.contextreminderapp.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

public class PhoneSensorHelper {

    private final Activity activity;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    public interface SensorDetectionCallback {
        void onSensorUnavailable();

        void onDetectionStarted();

        void onDetectionCompleted(
                float x,
                float y,
                float z,
                double movementLevel,
                String detectedActivity
        );
    }

    public PhoneSensorHelper(Activity activity) {
        this.activity = activity;

        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public boolean isAccelerometerAvailable() {
        return accelerometerSensor != null;
    }

    public void startDetection(SensorDetectionCallback callback) {
        if (sensorManager == null || accelerometerSensor == null) {
            if (callback != null) {
                callback.onSensorUnavailable();
            }
            return;
        }

        if (callback != null) {
            callback.onDetectionStarted();
        }

        final float[] lastX = {0};
        final float[] lastY = {0};
        final float[] lastZ = {0};
        final double[] maxMovement = {0};

        final SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double magnitude = Math.sqrt((x * x) + (y * y) + (z * z));
                double movementLevel = Math.abs(magnitude - 9.8);

                if (movementLevel > maxMovement[0]) {
                    maxMovement[0] = movementLevel;
                    lastX[0] = x;
                    lastY[0] = y;
                    lastZ[0] = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(
                sensorEventListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sensorManager.unregisterListener(sensorEventListener);

                String detectedActivity;

                if (maxMovement[0] > 2.0) {
                    detectedActivity = "walking";
                } else if (maxMovement[0] > 0.4) {
                    detectedActivity = "slight_movement";
                } else {
                    detectedActivity = "idle_or_standing";
                }

                if (callback != null) {
                    callback.onDetectionCompleted(
                            lastX[0],
                            lastY[0],
                            lastZ[0],
                            maxMovement[0],
                            detectedActivity
                    );
                }
            }
        }, 5000);
    }
}