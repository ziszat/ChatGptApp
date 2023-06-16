package com.example.chatgptapp;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class AppPermission {

    private static final int PERMISSION_REQUEST_CODE = 1;

    public static void requestPermissions(Activity activity) {
        if (!arePermissionsGranted(activity)) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    public static boolean arePermissionsGranted(Activity activity) {
        int recordAudioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        int internetPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        return recordAudioPermission == PackageManager.PERMISSION_GRANTED &&
                internetPermission == PackageManager.PERMISSION_GRANTED;
    }
}
