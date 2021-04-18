package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {

    public static final Integer RecordAudioRequestCode = 1;
    public static final Integer CalendarRequestCode = 2;

    private Activity activity;

    public PermissionsManager(Activity activity) {
        this.setActivity(activity);
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    public boolean audioPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }


    public boolean calendarPermissionNotGranted() {
        return !(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                &&  ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED);

    }

    public void checkCalendarPermission() {
        ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR }, CalendarRequestCode);
    }
}
