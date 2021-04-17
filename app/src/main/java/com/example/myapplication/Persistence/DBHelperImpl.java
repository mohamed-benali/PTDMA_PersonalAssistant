package com.example.myapplication.Persistence;

import android.content.Context;

import com.example.myapplication.Models.ListModel;
import com.example.myapplication.Models.TaskModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelperImpl implements DBHelper {
    Context context;

    String tasksPath = "llista_tasks2.obj";
    String listsPath = "llista_shop_lists2.obj";

    public DBHelperImpl(Context context) {
        this.context = context;
    }

    @Override
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

    @Override
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

    @Override
    public void save(TaskModel taskModel) {
        List<TaskModel> tasks = this.getTasks();
        tasks.add(taskModel);
        this.setTasks(tasks);
    }

    private TaskModel findByID(String id) {
        List<TaskModel> tasks = this.getTasks();
        TaskModel taskModel = null;
        for(TaskModel task : tasks) {
            if(task.getTitle().equals(id)) taskModel = task;
        }
        return taskModel;
    }

    @Override
    public TaskModel getTask(String taskID) {
        return findByID(taskID);
    }

    @Override
    public boolean taskExists(String taskID) {
        List<TaskModel> tasks = this.getTasks();
        for(TaskModel task : tasks) {
            if(task.getTitle().equals(taskID)) return true;
        }
        return false;
    }

    @Override
    public void deleteTaskById(String taskID) {
        List<TaskModel> tasks = this.getTasks();
        int index = -1;
        for(int i = 0; i < tasks.size(); ++i) {
            TaskModel task = tasks.get(i);
            if(task.getTitle().equals(taskID)) index = i;
        }
        if(index>=0) tasks.remove(index);
        this.setTasks(tasks);
    }

    @Override
    public void deleteAllTasks() {
        List<TaskModel> tasks = new ArrayList<>();
        this.setTasks(tasks);
    }

    /**
     * Modifica la tarea (elimina la antiga y añade la nueva)
     * @param taskID
     * @param taskModel
     */
    @Override
    public void save(String taskID, TaskModel taskModel) {
        List<TaskModel> tasks = this.getTasks();
        int index = -1;
        for(int i = 0; i < tasks.size(); ++i) {
            TaskModel task = tasks.get(i);
            if(task.getTitle().equals(taskID)) index = i;
        }
        if(index >= 0) {
            tasks.remove(index);
            tasks.add(taskModel);
        }
        this.setTasks(tasks);
    }




    private void createFileList(String path) {
        List<ListModel> mDades = new ArrayList<>();
        setShopLists(mDades);
    }

    @Override
    public List<ListModel> getShopLists() {
        List<ListModel> mDades = null;
        FileInputStream fis;
        try {
            boolean fileExist = this.fileExists(context, listsPath);
            if(!fileExist) this.createFileList(listsPath);

            fis = context.openFileInput(listsPath);
            ObjectInputStream is = new ObjectInputStream(fis);
            mDades = (List<ListModel>) is.readObject();
            is.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mDades;
    }

    @Override
    public void deleteListById(String id_forDelete) {
        List<ListModel> elementList = this.getShopLists();
        int index = -1;
        for(int i = 0; i < elementList.size(); ++i) {
            ListModel element = elementList.get(i);
            if(element.getTitle().equals(id_forDelete)) index = i;
        }
        if(index>=0) elementList.remove(index);
        this.setShopLists(elementList);
    }

    @Override
    public void deleteAllLists() {
        List<ListModel> tasks = new ArrayList<>();
        this.setShopLists(tasks);
    }

    @Override
    public boolean listExists(String id) {
        List<ListModel> elementsList = this.getShopLists();
        for(ListModel element : elementsList) {
            if(element.getTitle().equals(id)) return true;
        }
        return false;
    }

    @Override
    public ListModel getShopListbyID(String id) {
        List<ListModel> list = this.getShopLists();
        ListModel model = null;
        for(ListModel element : list) {
            if(element.getTitle().equals(id)) model = element;
        }
        return model;
    }

    @Override
    public void save(ListModel listModel) {
        List<ListModel> list = this.getShopLists();
        list.add(listModel);
        this.setShopLists(list);
    }

    @Override
    public void save(String taskID, ListModel model) {
        List<ListModel> list = this.getShopLists();
        int index = -1;
        for(int i = 0; i < list.size(); ++i) {
            ListModel element = list.get(i);
            if(element.getTitle().equals(taskID)) index = i;
        }
        if(index >= 0) {
            list.remove(index);
            list.add(model);
        }
        this.setShopLists(list);
    }

    @Override
    public boolean listElementExists(String id, String elementId) {
        ListModel model = this.getShopListbyID(id);
        return model.contains(elementId);
    }

    @Override
    public void deleteListElementById(String ID, String id_forDelete) {
        ListModel model = this.getShopListbyID(ID);
        model.remove(id_forDelete);
        this.save(ID, model);
    }

    private void setShopLists(List<ListModel> mDades) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(this.listsPath, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mDades);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
