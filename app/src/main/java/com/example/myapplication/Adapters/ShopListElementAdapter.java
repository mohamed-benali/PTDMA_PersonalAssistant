package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.ListModel;
import com.example.myapplication.R;

import java.util.List;

public class ShopListElementAdapter extends RecyclerView.Adapter {

    private List<String> mDataSet;

    public ShopListElementAdapter(List<String> myDataSet) {
        this.mDataSet = myDataSet;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title;
        public myViewHolder(View itemView) {
            super(itemView);
            textView_title          = itemView.findViewById(R.id.title);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_list, parent, false);
        // Aquí podemos definir tamaños, márgenes, paddings, etc
        return new ShopListElementAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String element = mDataSet.get(position);
        ShopListElementAdapter.myViewHolder holder2 = (ShopListElementAdapter.myViewHolder) holder;
        holder2.textView_title.setText(element);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
