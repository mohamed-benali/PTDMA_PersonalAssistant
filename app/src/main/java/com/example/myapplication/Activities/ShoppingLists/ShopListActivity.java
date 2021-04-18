package com.example.myapplication.Activities.ShoppingLists;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Activities.Tasks.CreateTaskActivity;
import com.example.myapplication.Activities.Tasks.UpdateTaskActivity;
import com.example.myapplication.Adapters.ShopListAdapter;
import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.Models.ListModel;
import com.example.myapplication.Models.TaskModel;
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

public class ShopListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ShopListAdapter mAdapter;

    private String ID_forDelete = null;
    private boolean askingForDeleteConfirm = false;

    TextToSpeech speaker;
    private boolean askingForDeleteAllConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de comparar");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String message = "-crear [titulo] \n-ver [titulo] \n" +
                "-eliminar [titulo] \n-eliminar todo";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, "Comandos", message));


        mRecyclerView = findViewById(R.id.ShopListRecycleViewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DBHelper dbHelper = new DBHelperImpl(this);
        mAdapter = new ShopListAdapter(dbHelper.getShopLists());

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
                dbHelper.save(new ListModel(id));
                restart();
            }
            else if(NLP.isUpdate(spokenText) || NLP.isSeeElement(spokenText)) {
                Intent myIntent = new Intent(this, SpecificListActivity.class);
                String id = NLP.getIdFromText(spokenText);
                if(dbHelper.listExists(id)) {
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
                    restart();
                }
                else if(askingForDeleteAllConfirm) {
                    dbHelper.deleteAllLists();
                    askingForDeleteAllConfirm = false;
                    restart();
                }
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
        startActivity(new Intent(this, ShopListActivity.class));
        overridePendingTransition(0, 0);
    }
}