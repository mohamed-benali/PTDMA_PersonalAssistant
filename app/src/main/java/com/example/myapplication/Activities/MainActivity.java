package com.example.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Activities.Events.EventsActivity;
import com.example.myapplication.Activities.ShoppingLists.ShopListActivity;
import com.example.myapplication.Activities.Tasks.TasksActivity;
import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.PermissionsManager;
import com.example.myapplication.R;
import com.example.myapplication.REQUEST_CODES.REQUEST_CODES;
import com.example.myapplication.TextToSpeech.TextToSpeech;
import com.example.myapplication.TextToSpeech.TextToSpeechImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextToSpeech speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Asistente: Home");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String title = "Comandos";
        String message = "-tareas \n-eventos \n-listas";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, title, message));

        PermissionsManager permissionsManager = new PermissionsManager(this);
        if(permissionsManager.calendarPermissionNotGranted()) permissionsManager.checkCalendarPermission();

        //Log.d("----------------------------------------TimeZone", TimeZone.getDefault().toString());
        speaker = new TextToSpeechImpl(this);

        String info = "Esta aplicación es una asistente de voz que trabaja con tareas, listas y eventos.\n\n";
        info += "La aplicacion funciona por voz casi siempre excepto para hablar (tocar micro), " +
                "ver comandos disponibles (tocar simbolo i)" +
                " y ir para atras (usar boton del propio telefono)";

        TextView textView = findViewById(R.id.info);
        textView.setText(info);

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
            else if(NLP.isGoToEvents(spokenText)) {
                Intent myIntent = new Intent(this, EventsActivity.class);
                startActivity(myIntent);
            }
            else speaker.didNotUnderstand();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void log(String text) {
        System.out.println("-------------------------------------------------------------- " + text);
    }
}