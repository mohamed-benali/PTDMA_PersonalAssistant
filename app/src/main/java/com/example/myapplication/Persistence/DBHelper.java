package com.example.myapplication.Persistence;

import android.content.Context;

import com.example.myapplication.Models.TaskModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper {
    Context context;

    String tasksPath = "llista_tasks.obj";

    public DBHelper(Context context) {
        this.context = context;
    }

    public List<TaskModel> getTasks() {
        List<TaskModel> mDades = null;

        FileInputStream fis;
        try {
            boolean fileExist = this.fileExists(context, tasksPath);
            if(!fileExist) this.createFile(tasksPath);


            fis = context.openFileInput(tasksPath);
            ObjectInputStream is = new ObjectInputStream(fis);
            mDades = (List<TaskModel>) is.readObject();
            is.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mDades;
    }

    private void createFile(String tasksPath) {

        /*List<TaskModel> l = new ArrayList<>(Arrays.asList(new TaskModel("T1", "D1", false),
                new TaskModel("T2", "D2", false),
                new TaskModel("T3", "D3", false),
                new TaskModel("T5", "D5", true),
                new TaskModel("T6", "D6", true),
                new TaskModel("T7", "D7", true),
                new TaskModel("T8", "D8", true)));*/

        List<TaskModel> l = new ArrayList<>();

        setTasks(l);
    }

    private boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    public void setTasks(List<TaskModel> mDades) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(tasksPath, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mDades);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
