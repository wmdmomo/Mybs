package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.momomo.myapplication.Manager.Fooddatas;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class fooddayAdapter extends RecyclerView.Adapter<fooddayAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private Fooddatas fooddatas;
    private OnTimeClickListener onTimeClickListener;
    private fooddetailAdapter fooddetailAdapter;

    public fooddayAdapter(Context context, Fooddatas fooddatas) {
        this.context = context;
        this.fooddatas = fooddatas;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCategoryBean(Fooddatas fooddatas) {
        this.fooddatas = fooddatas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView time;
        RecyclerView recyclerView;
        TextView cal;
        ImageView button;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            time = (TextView) view.findViewById(R.id.food_time);
            cal = (TextView) view.findViewById(R.id.food_total);
            recyclerView = (RecyclerView) view.findViewById(R.id.food_detail);
            button=(ImageView) view.findViewById(R.id.food_get);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.food_day, viewGroup, false);
        return new ViewHolder(view);
    }

    public interface OnTimeClickListener {
        void onClick(int position);
    }

    private int thisposition = 0;

    public int getThisposition() {
        return thisposition;
    }

    public void setThisposition(int thisposition) {
        this.thisposition = thisposition;
    }

    public void setOnTimeClickListener(OnTimeClickListener onTimeClickListener) {
        this.onTimeClickListener = onTimeClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        fooddetailAdapter = new fooddetailAdapter(context, fooddatas.getMoring());
        if (i == 0) {
            viewHolder.time.setText("早上");
            fooddetailAdapter.setCategoryBeans(fooddatas.getMoring());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        if (i == 1) {
            viewHolder.time.setText("中午");
            fooddetailAdapter.setCategoryBeans(fooddatas.getNoon());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        if (i == 2) {
            viewHolder.time.setText("晚上");
            fooddetailAdapter.setCategoryBeans(fooddatas.getAfternoon());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        viewHolder.cal.setText(String.valueOf(fooddetailAdapter.getCal()));

        if (onTimeClickListener != null) {
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTimeClickListener.onClick(i);
                }
            });


        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
        viewHolder.recyclerView.setLayoutManager(layoutManager);
        viewHolder.recyclerView.setAdapter(fooddetailAdapter);
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}