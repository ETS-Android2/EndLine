package com.example.endline_v1.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.R;
import com.example.endline_v1.dto.DataSet;

import java.util.ArrayList;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.CustomViewHolder> {

    private ArrayList<DataSet> list;

    public SettingRecyclerAdapter(ArrayList<DataSet> list){
        this.list = list;
    }

    @NonNull
    @Override
    public SettingRecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingRecyclerAdapter.CustomViewHolder holder, int position) {
        holder.tv_rv_item.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_rv_item;

        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.tv_rv_item = (TextView) view.findViewById(R.id.tv_rv_item);
        }
    }
}
