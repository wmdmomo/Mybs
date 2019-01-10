package com.example.momomo.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.selectfoods;

import java.util.List;

public class foodwatchInsideAdapter extends RecyclerView.Adapter<foodwatchInsideAdapter.ViewHolder>{
    private List<selectfoods> selectfoodsList;
//    private OnfoodClickListener onfoodClickListener;
    public foodwatchInsideAdapter(List<selectfoods> selectfoodsList){
        this.selectfoodsList=selectfoodsList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            imageView=(ImageView) view.findViewById(R.id.infood);
        }

    }
    public void setCategoryBeans(List<selectfoods> selectfoodsList) {
        this.selectfoodsList=selectfoodsList;
        notifyDataSetChanged();
    }
    public int getCal(){
        int cal=0;
        for(selectfoods selectfoods:selectfoodsList){
           cal+=selectfoods.getCal();
        }
        return cal;
    }
//    public interface OnfoodClickListener{
//        void onClick( int position);
//    }
//    public void setOnfoodClickListener(OnfoodClickListener
//                                               onfoodClickListener ){
//        this.onfoodClickListener=onfoodClickListener;
//    }
    @Override
    public foodwatchInsideAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foodinside,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final selectfoods selectfoods=selectfoodsList.get(position);
        holder.imageView.setImageResource(selectfoods.getImgid());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfoods.delete();
                selectfoodsList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectfoodsList.size();
    }
}
