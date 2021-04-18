package com.example.myapplication.Activities.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;

import com.example.myapplication.Listeners.HelpOnButtonClickListener;
import com.example.myapplication.Listeners.MicrophoneOnButtonClickListener;
import com.example.myapplication.NaturalLanguageProcessing.NaturalLanguageProcessing;
import com.example.myapplication.Persistence.DBHelper;
import com.example.myapplication.Persistence.DBHelperImpl;
import com.example.myapplication.Models.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.Adapters.TaskListAdapter;
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

import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;

    private String taskID_forDelete = null;
    private boolean askingForDeleteConfirm = false;

    TextToSpeech speaker;
    private boolean askingForDeleteAllConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tareas");
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new MicrophoneOnButtonClickListener(this));

        String message = "-crear [titulo] \n-modificar [titulo] \n" +
                "-eliminar [titulo]\n-eliminar todo\n-marcar [titulo]\n-desmarcar [titulo]";
        findViewById(R.id.helpButton).setOnClickListener(new HelpOnButtonClickListener(this, "Comandos", message));

        mRecyclerView = findViewById(R.id.TasksRecycleViewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        List<TaskModel> myDataSet = this.getTasks();

        mAdapter = new TaskListAdapter(myDataSet);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        speaker = new TextToSpeechImpl(this);
    }

    private List<TaskModel> getTasks() {
        DBHelper dbHelper = new DBHelperImpl(this);
        return dbHelper.getTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        DBHelper dbHelper = new DBHelperImpl(this);
        if(requestCode == REQUEST_CODES.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // TODO: DO SOMETHING
            log("SponekText: " +  spokenText);

            NaturalLanguageProcessing NLP = NaturalLanguageProcessing.getInstance();
            spokenText = NLP.normalizeText(spokenText);
            if(NLP.isCreateTask(spokenText)) {
                Intent myIntent = new Intent(this, CreateTaskActivity.class);
                String id = NLP.getTaskIdFromText(spokenText);
                myIntent.putExtra("taskID", id);
                startActivity(myIntent);
            }
            else if(NLP.isUpdateTask(spokenText)) {
                Intent myIntent = new Intent(this, UpdateTaskActivity.class);
                String id = NLP.getTaskIdFromText(spokenText);
                if(dbHelper.taskExists(id)) {
                    myIntent.putExtra("taskID", id);
                    startActivity(myIntent);
                }
                else speaker.taskDontExist();
            }
            else if(NLP.isDeleteTask(spokenText)) {
                String taskID = NLP.getTaskIdFromText(spokenText);
                //TODO: Confirm task with id exists
                if(dbHelper.taskExists(taskID)) {
                    speaker.askConfirmDelete();
                    this.askingForDeleteConfirm = true;
                }
                else speaker.sayElementDontExist();
                taskID_forDelete = taskID;

            }
            else if(NLP.isDeleteAllTask(spokenText)) {
                speaker.askConfirmDelete();
                this.askingForDeleteAllConfirm = true;
            }
            else if(NLP.isConfirmation(spokenText)) {
                if(askingForDeleteConfirm) {
                    dbHelper.deleteTaskById(taskID_forDelete);
                    this.askingForDeleteConfirm = false;
                    restart();
                }
                else if(askingForDeleteAllConfirm) {
                    dbHelper.deleteAllTasks();
                    askingForDeleteAllConfirm = false;
                    restart();
                }

            }
            else if(spokenText.equals("no")) {}
            else if(NLP.isTaskDone(spokenText)) {
                String taskID = NLP.getTaskIdFromText(spokenText);
                TaskModel taskModel =dbHelper.getTask(taskID);
                if(taskModel != null) {
                    taskModel.setDone(true);
                    dbHelper.save(taskID, taskModel);
                    restart();
                }
                else speaker.taskDontExist();
            }
            else if(NLP.isTaskNotDone(spokenText)) {
                String taskID = NLP.getTaskIdFromText(spokenText);
                TaskModel taskModel =dbHelper.getTask(taskID);
                if(taskModel != null) {
                    taskModel.setDone(false);
                    dbHelper.save(taskID, taskModel);
                    restart();
                }
                else speaker.taskDontExist();
            }

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
        startActivity(new Intent(this, TasksActivity.class));
        overridePendingTransition(0, 0);
    }

    private void log(String text) {
        System.out.println("-------------------------------------------------------------- " + text);
    }
}