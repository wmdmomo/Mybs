package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.foodwatch;

import java.util.List;

public class foodwatchAdapter extends RecyclerView.Adapter<foodwatchAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<foodwatch> foodwatchList;
    private OnTimeClickListener onTimeClickListener;

    public foodwatchAdapter(Context context,List<foodwatch> foodList){
        this.foodwatchList=foodList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        View view;
        ImageView weather;
        TextView time;
        ImageView food1;
        ImageView food2;
        ImageView select;
        TextView cal;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            weather=(ImageView)view.findViewById(R.id.weather);
            time=(TextView) view.findViewById(R.id.time);
            cal=(TextView) view.findViewById(R.id.cal);
            food1=(ImageView)view.findViewById(R.id.food1);
            food2=(ImageView)view.findViewById(R.id.food2);
            select=(ImageView) view.findViewById(R.id.select);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.food,viewGroup,false);
        return new ViewHolder(view);
    }
    public interface OnTimeClickListener{
        void onClick( int position);
    }
    private int thisposition;
    public int getThisposition(){
        return thisposition;
    }
    public void setThisposition(int thisposition){
        this.thisposition=thisposition;
    }
    public void setOnTimeClickListener(OnTimeClickListener onTimeClickListener){
        this.onTimeClickListener=onTimeClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final foodwatch foodwatch=foodwatchList.get(i);
        viewHolder.time.setText(foodwatch.getTime());
        viewHolder.cal.setText(String.valueOf(foodwatch.getCal()));
        viewHolder.weather.setImageResource(foodwatch.getWeather());
        viewHolder.food1.setImageResource(foodwatch.getFood1());
        viewHolder.food2.setImageResource(foodwatch.getFood2());
        if(i==getThisposition()){
            viewHolder.select.setImageResource(R.drawable.ic_wancheng);
        }else{
            viewHolder.select.setImageResource(R.drawable.ic_uwancheng);
        }
        if(onTimeClickListener!=null){
            viewHolder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTimeClickListener.onClick(i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return foodwatchList.size();
    }
}
