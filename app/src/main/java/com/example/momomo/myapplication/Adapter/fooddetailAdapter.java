package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.selectfoods;

import java.util.List;

public class fooddetailAdapter extends RecyclerView.Adapter<fooddetailAdapter.ViewHolder> {
    private List<selectfoods> selectfoodsList;
    private OnfoodClickListener onfoodClickListener;
    private Context context;

    public fooddetailAdapter(Context context,List<selectfoods> selectfoodsList) {
        this.context=context;
        this.selectfoodsList = selectfoodsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        de.hdodenhof.circleimageview.CircleImageView food_img;
        TextView food_name;
        TextView food_cal;
        ImageView select;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            food_img = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.food_img);
            food_cal=(TextView) view.findViewById(R.id.food_cal);
            food_name=(TextView) view.findViewById(R.id.food_name);
            select=(ImageView)view.findViewById(R.id.food_select);

        }

    }

    public void setCategoryBeans(List<selectfoods> selectfoodsList) {
        this.selectfoodsList = selectfoodsList;
        notifyDataSetChanged();
    }

    public double getCal() {
        double cal = 0;
        for (selectfoods selectfoods : selectfoodsList) {
            cal += selectfoods.getCal();
        }
        return cal;
    }

    public interface OnfoodClickListener {
        void onClick(int position);
    }

    public void setOnfoodClickListener(OnfoodClickListener
                                               onfoodClickListener) {
        this.onfoodClickListener = onfoodClickListener;
    }

    @Override
    public fooddetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final selectfoods selectfoods = selectfoodsList.get(position);
        final String imgpath = selectfoods.getImgpath();
        if (!imgpath.equals(" ")) Glide.with(context).load(imgpath).into(holder.food_img);
        else {
            int resourceId = R.mipmap.avatar;
            Glide.with(context)
                    .load(resourceId)
                    .into(holder.food_img);
        }
        holder.food_cal.setText(String.valueOf(selectfoods.getCal()));
        holder.food_name.setText(selectfoods.getName());
        if (onfoodClickListener != null) {
            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectfoods.delete();
                    selectfoodsList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selectfoodsList.size();
    }
}

