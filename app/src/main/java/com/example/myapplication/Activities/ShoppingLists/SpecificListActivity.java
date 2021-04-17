package com.example.myapplication.Activities.ShoppingLists;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Adapters.ShopListAdapter;
import com.example.myapplication.Adapters.ShopListElementAdapter;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.Models.ListModel;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.Persistence.DBHelper;
import com.example.myapplication.Persistence.DBHelperImpl;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;
import com.example.myapplication.TextToSpeech.TextToSpeech;
import com.example.myapplication.TextToSpeech.TextToSpeechImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;

import java.util.List;

public class SpecificListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ShopListElementAdapter mAdapter;

    private String ID_forDelete = null;
    private boolean askingForDeleteConfirm = false;

    TextToSpeech speaker;
    private boolean askingForDeleteAllConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String ID = this.getIntent().getStringExtra("ID");
        toolbar.setTitle(ID);
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        mRecyclerView = findViewById(R.id.ShopListRecycleViewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DBHelper dbHelper = new DBHelperImpl(this);
        mAdapter = new ShopListElementAdapter(dbHelper.getShopListbyID(ID).getElements());

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
            System.out.println("---------------------------------------------------------------SponekText: " +  spokenText);

            NaturalLanguageProcessing NLP = NaturalLanguageProcessing.getInstance();
            spokenText = NLP.normalizeText(spokenText);
            if(NLP.isCreate(spokenText)) {
                String id = NLP.getIdFromText(spokenText);
                dbHelper.save(new ListModel(id));
                restart();
            }
            else if(NLP.isUpdate(spokenText)) {
                Intent myIntent = new Intent(this, SpecificListActivity.class);
                String id = NLP.getIdFromText(spokenText);
                if(dbHelper.taskExists(id)) {
                    myIntent.putExtra("ID", id);
                    startActivity(myIntent);
                }
                else speaker.listDontExist();
            }
            else if(NLP.isDelete(spokenText)) {
                String ID = NLP.getIdFromText(spokenText);
                //TODO: Confirm task with id exists
                if(dbHelper.listExists(ID)) speaker.askConfirmDelete();
                else speaker.sayElementDontExist();
                ID_forDelete = ID;
                this.askingForDeleteConfirm = true;
            }
            else if(NLP.isDeleteAll(spokenText)) {
                speaker.askConfirmDelete();
                this.askingForDeleteAllConfirm = true;
            }
            else if(NLP.isConfirmation(spokenText)) {
                if(askingForDeleteConfirm) {
                    dbHelper.deleteListById(ID_forDelete);
                    this.askingForDeleteConfirm = false;
                }
                else if(askingForDeleteAllConfirm) {
                    dbHelper.deleteAllLists();
                    askingForDeleteAllConfirm = false;
                }
                restart();
            }
            else { // Didnt understand,
                // Dialog or something with the info
                speaker.didNotUnderstand();
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
        startActivity(new Intent(this, ShopListActivity.class));
        overridePendingTransition(0, 0);
    }
}