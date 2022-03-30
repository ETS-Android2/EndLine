package com.example.endline_v1.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.endline_v1.activity.ItemInfo;
import com.example.endline_v1.R;
import com.example.endline_v1.dto.ItemDataSet;

import java.util.ArrayList;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.CustomViewHolder> {

    private ArrayList<ItemDataSet> list;
    private Context context;

    public ItemRecyclerAdapter(ArrayList<ItemDataSet> list){
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.productitem_rv_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Uri photo_uri = Uri.parse(list.get(position).getPhoto_url());
        Glide.with(context).load(photo_uri).into(holder.iv_rv_img);
        holder.tv_rv_name.setText(list.get(position).getName());
        holder.tv_rv_category.setText(list.get(position).getCategory());
        holder.tv_rv_date.setText(list.get(position).getBuy_date() + " / " + list.get(position).getEnd_date());
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_rv_img;
        protected TextView tv_rv_name, tv_rv_category, tv_rv_date;

        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.iv_rv_img = (ImageView) view.findViewById(R.id.iv_rv_product_img);
            this.tv_rv_name = (TextView) view.findViewById(R.id.tv_rv_product_name);
            this.tv_rv_category = (TextView) view.findViewById(R.id.tv_rv_product_category);
            this.tv_rv_date = (TextView) view.findViewById(R.id.tv_rv_product_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemInfo.class);
                    intent.putExtra("product_name", tv_rv_name.getText());
                    context.startActivity(intent);
//                    Toast.makeText(context, tv_rv_name.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
