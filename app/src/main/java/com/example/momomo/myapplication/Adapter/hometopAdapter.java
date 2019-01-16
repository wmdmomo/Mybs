package com.example.momomo.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.hometopitem;

import java.util.List;

public class hometopAdapter extends RecyclerView.Adapter<hometopAdapter.ViewHolder> {
    private List<hometopitem> hometopitemList;
    private OnhometopClickListener onhometopClickListener;
    public hometopAdapter(List<hometopitem> hometopitemList) {
        this.hometopitemList = hometopitemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        LinearLayout linearLayout;
        TextView title;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            linearLayout=(LinearLayout) view.findViewById(R.id.hometop);
            title=(TextView) view.findViewById(R.id.hometitle);
        }

    }

    public interface OnhometopClickListener {
        void onClick(int position);
    }

    public void setOnhometopClickListener(OnhometopClickListener
                                               onhometopClickListener) {
        this.onhometopClickListener = onhometopClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeitemtop, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final hometopitem hometopitem = hometopitemList.get(position);
        holder.linearLayout.setBackgroundResource(hometopitem.getImgid());
        holder.title.setText(hometopitem.getTitle());
        if (onhometopClickListener != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onhometopClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return hometopitemList.size();
    }
}