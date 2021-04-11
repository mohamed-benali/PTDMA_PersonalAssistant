package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.TaskModel;
import com.example.myapplication.R;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter {
    private List<TaskModel> mDataSet;

    public TaskListAdapter(List<TaskModel> myDataSet) {
        this.mDataSet = myDataSet;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title;
        public TextView textView_description;
        public TextView textView_done;

        public myViewHolder(View itemView) {
            super(itemView);
            textView_title          = itemView.findViewById(R.id.title);
            textView_description    = itemView.findViewById(R.id.description);
            textView_done           = itemView.findViewById(R.id.done);
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_task, parent, false);
        // Aquí podemos definir tamaños, márgenes, paddings, etc
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskModel taskModel = mDataSet.get(position);
        myViewHolder holder2 = (myViewHolder) holder;

        holder2.textView_title.setText(taskModel.getTitle());
        holder2.textView_description.setText(taskModel.getDescription());
        holder2.textView_done.setText(taskModel.getDone().toString());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
