package com.example.myapplication.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;

import com.example.myapplication.PermissionsManager;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;
import com.example.myapplication.RecognizerIntentManager.RecognizerIntentManager;
import com.example.myapplication.RecognizerIntentManager.RecognizerIntentManagerImpl;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MicrophoneOnButtonClickListener implements View.OnClickListener {

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public MicrophoneOnButtonClickListener(Activity context) {
        this.setActivity(context);
    }

    @Override
    public void onClick(View view) {
        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/

        PermissionsManager permissionsManager = new PermissionsManager(this.getActivity());
        if(permissionsManager.audioPermissionNotGranted()) permissionsManager.checkAudioPermission();

        RecognizerIntentManager recognizerIntentManager = new RecognizerIntentManagerImpl(this.getActivity());
        recognizerIntentManager.startSpeechToTextIntent();
    }


}
