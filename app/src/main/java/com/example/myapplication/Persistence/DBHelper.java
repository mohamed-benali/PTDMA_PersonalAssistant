package com.example.myapplication.Persistence;

import com.example.myapplication.Models.EventModel;
import com.example.myapplication.Models.ListModel;
import com.example.myapplication.Models.TaskModel;

import java.util.List;

public interface DBHelper {
    List<TaskModel> getTasks();
    void setTasks(List<TaskModel> mDades);
    TaskModel getTask(String taskID);
    boolean taskExists(String taskID);

    void deleteTaskById(String taskID);
    void deleteAllTasks();

    void save(TaskModel taskModel);
    void save(String taskID, TaskModel taskModel);

    List<ListModel> getShopLists();


    void deleteListById(String id_forDelete);
    void deleteAllLists();

    boolean listExists(String id);

    ListModel getShopListbyID(String id);

    void save(ListModel listModel);
    void save(String taskID, ListModel model);

    boolean listElementExists(String id, String elementId);

    void deleteListElementById(String ID, String id_forDelete);

    List<EventModel> getEvents();

    boolean eventExists(String id);

    void deleteEventById(String id_forDelete);

    void deleteAllEvents();

    void save(EventModel model);

    EventModel getEventbyID(String id);
}
