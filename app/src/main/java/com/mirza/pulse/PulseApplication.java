package com.mirza.pulse;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

/**
 * The {@code PulseApplication} class is the main application class for the Pulse application.
 * It extends {@link Application} and is responsible for initializing Firebase when the application starts.
 */
public class PulseApplication extends Application {

    private static final String TAG = "PulseApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "Firebase initialized successfully.");

        } catch (IllegalStateException e) {
            // Handle the case where Firebase is already initialized. This can happen during testing or hot reload
            Log.w(TAG, "Firebase already initialized: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
        }
    }
}