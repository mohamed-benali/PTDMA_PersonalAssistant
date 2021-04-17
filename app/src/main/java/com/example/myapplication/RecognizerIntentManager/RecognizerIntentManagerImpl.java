package com.example.myapplication.RecognizerIntentManager;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;

import java.util.Locale;

public class RecognizerIntentManagerImpl implements RecognizerIntentManager {

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public RecognizerIntentManagerImpl(Activity context) {
        this.setActivity(context);
    }

    @Override
    public void startSpeechToTextIntent() {
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);

        this.getActivity().startActivityForResult(speechRecognizerIntent, REQUEST_CODES.SPEECH_REQUEST_CODE);
    }


}
