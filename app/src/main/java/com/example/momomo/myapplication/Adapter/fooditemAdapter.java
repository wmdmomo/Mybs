package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.fooditem;

import java.util.ArrayList;
import java.util.List;

public class fooditemAdapter extends RecyclerView.Adapter<fooditemAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<fooditem> fooditemList;
    private OnItemClickListener onItemClickListener;
    public fooditemAdapter(Context context,List<fooditem> foodList){
        this.fooditemList=foodList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        View view;
        ImageView food;
        TextView name;
        TextView cal;
        ImageView adds;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            food=(ImageView)view.findViewById(R.id.food);
            name=(TextView) view.findViewById(R.id.name);
            cal=(TextView) view.findViewById(R.id.cals);
            adds=(ImageView)view.findViewById(R.id.adds);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.fooditem,viewGroup,false);
        return new ViewHolder(view);
    }
    public interface OnItemClickListener{
        void onClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener
                                               onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
    @Override
    public void onBindViewHolder( ViewHolder viewHolder, final int position) {
        final fooditem fooditem=fooditemList.get(position);
        final String foodname=fooditem.getName();
        final int foodcal=fooditem.getCal();
        final int foodimg=fooditem.getImgid();
        viewHolder.name.setText(foodname);
        viewHolder.cal.setText(String.valueOf(foodcal));
        viewHolder.food.setImageResource(foodimg);
        if( onItemClickListener!= null) {
            viewHolder.adds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fooditemList.size();
    }
}

