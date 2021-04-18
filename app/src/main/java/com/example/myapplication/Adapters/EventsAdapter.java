package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.EventModel;
import com.example.myapplication.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter {
    private List<EventModel> mDataSet;

    public EventsAdapter(List<EventModel> myDataSet) {
        this.mDataSet = myDataSet;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title;
        public TextView textView_description;
        public TextView textView_date;

        public myViewHolder(View itemView) {
            super(itemView);
            textView_title          = itemView.findViewById(R.id.title);
            textView_description    = itemView.findViewById(R.id.description);
            textView_date           = itemView.findViewById(R.id.date);
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_event, parent, false);
        // Aquí podemos definir tamaños, márgenes, paddings, etc
        return new EventsAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventModel model = mDataSet.get(position);
        EventsAdapter.myViewHolder holder2 = (EventsAdapter.myViewHolder) holder;

        holder2.textView_title.setText(model.getTitle());
        holder2.textView_description.setText(model.getDescription());
        holder2.textView_date.setText(model.getDate());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
