package com.example.momomo.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.homeitem;

import java.util.List;

public class homeitemAdapter extends RecyclerView.Adapter<homeitemAdapter.ViewHolder> {
    private List<homeitem> homeitemList;
    private OnhomeClickListener onhomeClickListener;
    public homeitemAdapter(List<homeitem> homeitemList) {
        this.homeitemList = homeitemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.homeimg);
        }

    }

    public interface OnhomeClickListener {
        void onClick(int position);
    }

    public void setOnhomeClickListener(OnhomeClickListener
                                               onhomeClickListener) {
        this.onhomeClickListener = onhomeClickListener;
    }

    @Override
    public homeitemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final homeitem homeitem = homeitemList.get(position);
        holder.imageView.setImageResource(homeitem.getImgid());
        if (onhomeClickListener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onhomeClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return homeitemList.size();
    }
}

