package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.Manager.Fooddatas;

public class
foodwatchAdapter extends RecyclerView.Adapter<foodwatchAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private Fooddatas fooddatas;
    private OnTimeClickListener onTimeClickListener;
    private foodwatchInsideAdapter foodwatchInsideAdapter;

    public foodwatchAdapter(Context context,Fooddatas fooddatas){
        this.context=context;
        this.fooddatas=fooddatas;
        layoutInflater=LayoutInflater.from(context);
    }
    public void setCategoryBean(Fooddatas fooddatas) {
        this.fooddatas = fooddatas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        View view;
        ImageView weather;
        TextView time;
//        ImageView food1;
//        ImageView food2;
        RecyclerView recyclerView;
        ImageView select;
        TextView cal;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            weather=(ImageView)view.findViewById(R.id.weather);
            time=(TextView) view.findViewById(R.id.time);
            cal=(TextView) view.findViewById(R.id.cal);
            recyclerView=(RecyclerView) view.findViewById(R.id.insiderv);
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
    private int thisposition=0;
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
        foodwatchInsideAdapter=new foodwatchInsideAdapter(fooddatas.getMoring());
//        final foodwatch foodwatch=foodwatchList.get(i);
//        viewHolder.time.setText(foodwatch.getTime());
//        viewHolder.cal.setText(String.valueOf(foodwatch.getCal()));
//        viewHolder.weather.setImageResource(foodwatch.getWeather());
        if(i==0){
            viewHolder.time.setText("早上");
            viewHolder.weather.setImageResource(R.drawable.ic_zaoshang);
            foodwatchInsideAdapter.setCategoryBeans(fooddatas.getMoring());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        if(i==1){
            viewHolder.time.setText("中午");
            viewHolder.weather.setImageResource(R.drawable.ic_zhongwu);
            foodwatchInsideAdapter.setCategoryBeans(fooddatas.getNoon());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        if(i==2){
            viewHolder.time.setText("下午");
            viewHolder.weather.setImageResource(R.drawable.ic_xiawucha);
            foodwatchInsideAdapter.setCategoryBeans(fooddatas.getAfternoon());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));

        }
        if(i==3) {
            viewHolder.time.setText("晚上");
            viewHolder.weather.setImageResource(R.drawable.ic_wanshang);
            foodwatchInsideAdapter.setCategoryBeans(fooddatas.getEvening());
//            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));
        }

        if(i==getThisposition()){
            viewHolder.select.setImageResource(R.drawable.ic_wancheng);
            viewHolder.cal.setText(String.valueOf(foodwatchInsideAdapter.getCal()));
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
        viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        viewHolder.recyclerView.setAdapter(foodwatchInsideAdapter);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
