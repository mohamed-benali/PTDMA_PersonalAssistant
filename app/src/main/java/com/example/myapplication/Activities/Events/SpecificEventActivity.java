package com.example.myapplication.Activities.Events;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Activities.Events.EventsActivity;
import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.Models.EventModel;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.Persistence.DBHelper;
import com.example.myapplication.Persistence.DBHelperImpl;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpecificEventActivity extends AppCompatActivity {

    EditText editText_title;
    EditText editText_desc;
    EditText editText_year;
    EditText editText_month;
    EditText editText_day;

    Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String ID = this.getIntent().getStringExtra("ID");
        toolbar.setTitle("Evento");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String message = "-asignar titulo [titulo] \n-asignar descripcion [descripcion] \n" +
                         "-asignar año [año] \n-asignar mes [mes] \n-asignar dia [dia]\n-guardar";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, "Comandos", message));

        editText_title = findViewById(R.id.edit_title);
        editText_desc = findViewById(R.id.edit_desc);
        editText_year = findViewById(R.id.year);
        editText_month = findViewById(R.id.month);
        editText_day = findViewById(R.id.day);

        editText_title.setText(ID);
        eventId = null;
        DBHelper dbHelper = new DBHelperImpl(this);
        if(!getIntent().getBooleanExtra("isCreate", false)) { // Es update o ver evento
            EventModel eventModel = dbHelper.getEventbyID(ID);
            editText_title.setText(eventModel.getTitle());
            editText_desc.setText(eventModel.getDescription());
            editText_year.setText(eventModel.getYear());
            editText_month.setText(eventModel.getMonth());
            editText_day.setText(eventModel.getDay());
            eventId = eventModel.getEventID();
        } else { // Es creacion  (quiza poner default en la fecha)
            Date currentTime = Calendar.getInstance().getTime();
            editText_year.setText(String.valueOf(currentTime.getYear()+1900));
            editText_month.setText(String.valueOf(currentTime.getMonth()+1));
            editText_day.setText(String.valueOf(currentTime.getDate()));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODES.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            this.execute_SpeechLogic(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void execute_SpeechLogic(String spokenText) {
        NaturalLanguageProcessing NLP = NaturalLanguageProcessing.getInstance();
        System.out.println("-------------------------------------------------------------------------- "+spokenText);
        spokenText=NLP.normalizeText(spokenText);

        if(NLP.isSetTitle(spokenText)) {
            String title = NLP.getTitleFromText(spokenText);
            editText_title.setText(title);
        }
        else if(NLP.isSetDescription(spokenText)) {
            String description = NLP.getDescriptionFromText(spokenText);
            editText_desc.setText(description);
        }
        else if(NLP.isSetYear(spokenText)) {
            String token = NLP.getDateElementFromText(spokenText);
            editText_year.setText(token);
        }
        else if(NLP.isSetMonth(spokenText)) {
            String token = NLP.getDateElementFromText(spokenText);
            editText_month.setText(token);
        }
        else if(NLP.isSetDay(spokenText)) {
            String token = NLP.getDateElementFromText(spokenText);
            editText_day.setText(token);
        }
        else if(NLP.isSave(spokenText)) {
            // TODO: Control de campos llenos (todos lo campos estan rellenados (no hay nulos)
            DBHelper dbHelper = new DBHelperImpl(this);
            EventModel model = new EventModel(  editText_title.getText().toString().trim(),
                                                editText_desc.getText().toString().trim(),
                                                editText_year.getText().toString().trim(),
                                                editText_month.getText().toString().trim(),
                                                editText_day.getText().toString().trim(), eventId);
            dbHelper.save(model);
            startActivity(new Intent(this, EventsActivity.class)); // Go back
        }
    }
}