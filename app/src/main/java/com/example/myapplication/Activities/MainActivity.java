package com.example.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;

import com.example.myapplication.Activities.ShoppingLists.ShopListActivity;
import com.example.myapplication.Activities.Tasks.TasksActivity;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.R;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        Intent myIntent = new Intent(this, TasksActivity.class);
        startActivity(myIntent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODES.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            NaturalLanguageProcessing NLP = NaturalLanguageProcessing.getInstance();

            // TODO: DO SOMETHING
            spokenText = NLP.normalizeText(spokenText);
            log("SpokenText: " +  spokenText);
            if(NLP.isGoToTask(spokenText)) {
                Intent myIntent = new Intent(this, TasksActivity.class);
                startActivity(myIntent);
            }
            else if(NLP.isShoppingList(spokenText)) {
                Intent myIntent = new Intent(this, ShopListActivity.class);
                startActivity(myIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void log(String text) {
        System.out.println("-------------------------------------------------------------- " + text);
    }
}