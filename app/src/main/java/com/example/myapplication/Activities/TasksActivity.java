package com.example.myapplication.Activities;

import android.os.Bundle;

import com.example.myapplication.Models.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.Adapters.TaskListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.TasksRecycleViewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Asociamos un adapter (ver más adelante cómo definirlo)

        List<TaskModel> myDataSet = this.getTasks();

        mAdapter = new TaskListAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);


        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private List<TaskModel> getTasks() {

        List<TaskModel> t = Arrays.asList(  new TaskModel("T1", "D1", false),
                new TaskModel("T2", "D2", false),
                new TaskModel("T3", "D3", false),
                new TaskModel("T4", "D4", true),
                new TaskModel("T4", "D4", true),
                new TaskModel("T4", "D4", true),
                new TaskModel("T4", "D4", true),
                new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true),
        new TaskModel("T4", "D4", true));

        return t;
    }
}