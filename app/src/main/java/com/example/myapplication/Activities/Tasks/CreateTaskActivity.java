package com.example.myapplication.Activities.Tasks;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.Models.TaskModel;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.Persistence.DBHelper;
import com.example.myapplication.Persistence.DBHelperImpl;
import com.example.myapplication.R;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {

    EditText editText_title;
    EditText editText_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Crear tarea");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String message = "-asignar titulo [titulo] \n-asignar descripcion [descripcion] \n" +
                "-guardar";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, "Comandos", message));

        editText_title = findViewById(R.id.edit_title);
        editText_desc = findViewById(R.id.edit_desc);

        String taskID = this.getIntent().getStringExtra("taskID");
        editText_title.setText(taskID);

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

        if(NLP.isSetTitle(spokenText)) {
            String title = NLP.getTitleFromText(spokenText);
            editText_title.setText(title);
        }
        else if(NLP.isSetDescription(spokenText)) {
            String description = NLP.getDescriptionFromText(spokenText);
            editText_desc.setText(description);
        }
        else if(NLP.isSave(spokenText)) {
            DBHelper dbHelper = new DBHelperImpl(this);
            TaskModel taskModel = new TaskModel(editText_title.getText().toString().trim(), editText_desc.getText().toString().trim(), false);
            dbHelper.save(taskModel);
            startActivity(new Intent(this, TasksActivity.class)); // Go back to lists
        }
    }
}