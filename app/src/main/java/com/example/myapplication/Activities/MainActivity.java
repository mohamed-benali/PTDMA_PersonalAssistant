package com.example.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.example.myapplication.R;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1;
    private static final int SPEECH_REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) checkPermission();

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);



        startActivityForResult(speechRecognizerIntent, SPEECH_REQUEST_CODE);
        */

        Intent myIntent = new Intent(this, TasksActivity.class);
        startActivity(myIntent);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // TODO: DO SOMETHING
            log("SponekText: " +  spokenText);

            if(spokenText.equals("task") || spokenText.equals("tasks") ||  spokenText.equals("ask") || spokenText.equals("as")) {
                Intent myIntent = new Intent(this, TasksActivity.class);
                startActivity(myIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void log(String text) {
        System.out.println("-------------------------------------------------------------- " + text);
    }
}