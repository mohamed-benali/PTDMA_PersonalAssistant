package com.example.myapplication.Activities.Events;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Adapters.EventsAdapter;
import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.Persistence.DBHelper;
import com.example.myapplication.Persistence.DBHelperImpl;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;
import com.example.myapplication.RecognizerIntentManager.RecognizerIntentManager;
import com.example.myapplication.RecognizerIntentManager.RecognizerIntentManagerImpl;
import com.example.myapplication.TextToSpeech.TextToSpeech;
import com.example.myapplication.TextToSpeech.TextToSpeechImpl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;

import com.example.myapplication.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventsAdapter mAdapter;

    TextToSpeech speaker;

    private String ID_forDelete = null;
    private boolean askingForDeleteConfirm = false;

    private boolean askingForDeleteAllConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Eventos");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String message = "-crear [titulo] \n-ver [titulo] \n" +
                "-eliminar [titulo]";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, "Comandos", message));

        mRecyclerView = findViewById(R.id.ListRecycleViewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DBHelper dbHelper = new DBHelperImpl(this);
        mAdapter = new EventsAdapter(dbHelper.getEvents());

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        speaker = new TextToSpeechImpl(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        DBHelper dbHelper = new DBHelperImpl(this);
        if(requestCode == REQUEST_CODES.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // TODO: DO SOMETHING
            System.out.println("---------------------------------------------------------------SponekText: " +  spokenText);

            NaturalLanguageProcessing NLP = NaturalLanguageProcessing.getInstance();
            spokenText = NLP.normalizeText(spokenText);
            if(NLP.isCreate(spokenText)) {
                String id = NLP.getIdFromText(spokenText);
                Intent myIntent = new Intent(this, SpecificEventActivity.class);
                myIntent.putExtra("ID", id);
                myIntent.putExtra("isCreate", true);
                startActivity(myIntent);
            }
            else if(NLP.isUpdate(spokenText) || NLP.isSeeElement(spokenText)) {
                Intent myIntent = new Intent(this, SpecificEventActivity.class);
                String id = NLP.getIdFromText(spokenText);
                if(dbHelper.eventExists(id)) {
                    myIntent.putExtra("ID", id);
                    myIntent.putExtra("isCreate", false);
                    startActivity(myIntent);
                }
                else speaker.eventDontExist();
            }
            else if(NLP.isDelete(spokenText)) {
                String ID = NLP.getIdFromText(spokenText);
                if(dbHelper.eventExists(ID)) {
                    speaker.askConfirmDelete();
                    this.askingForDeleteConfirm = true;
                }
                else speaker.sayElementDontExist();
                ID_forDelete = ID;
            }/*
            else if(NLP.isDeleteAll(spokenText)) {
                speaker.askConfirmDelete();
                this.askingForDeleteAllConfirm = true;
            }*/
            else if(NLP.isConfirmation(spokenText)) {
                if(askingForDeleteConfirm) {
                    dbHelper.deleteEventById(ID_forDelete);
                    this.askingForDeleteConfirm = false;
                }
                else if(askingForDeleteAllConfirm) {
                    dbHelper.deleteAllEvents();
                    askingForDeleteAllConfirm = false;
                }
                restart();
            }
            else if(spokenText.equals("no")) {}
            else { // Didnt understand,
                // Dialog or something with the info
                askingForDeleteAllConfirm = false;
                this.askingForDeleteConfirm = false;
                speaker.didNotUnderstand();
            }
            if(askingForDeleteAllConfirm || askingForDeleteConfirm) {
                RecognizerIntentManager recognizerIntentManager = new RecognizerIntentManagerImpl(this);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recognizerIntentManager.startSpeechToTextIntent();
                    }
                }, 1400);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        this.speaker.destroy();
        super.onDestroy();
    }



    private void restart() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, EventsActivity.class));
        overridePendingTransition(0, 0);
    }
}