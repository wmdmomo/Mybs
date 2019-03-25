package com.example.momomo.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.iconitem;

import java.util.List;

public class homeitem2Adapter extends RecyclerView.Adapter<homeitem2Adapter.ViewHolder> {
    private List<iconitem> iconitemList;
    public homeitem2Adapter(List<iconitem> iconitemList) {
        this.iconitemList = iconitemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;
        TextView item_title;
        TextView item_size;
        TextView item_danwei;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.rv_icon);
            item_danwei=(TextView) view.findViewById(R.id.item_danwei);
            item_size=(TextView) view.findViewById(R.id.item_size);
            item_title=(TextView) view.findViewById(R.id.item_title);
        }

    }
    @Override
    public homeitem2Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeicon, viewGroup, false);
        return new homeitem2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(homeitem2Adapter.ViewHolder holder, final int position) {
        final iconitem iconitem = iconitemList.get(position);
        holder.imageView.setBackgroundResource(iconitem.getImgid());
        holder.item_title.setText(iconitem.getTitle());
        holder.item_danwei.setText(iconitem.getDes());
        holder.item_size.setText(String.valueOf(iconitem.getSize()));
    }

    @Override
    public int getItemCount() {
        return iconitemList.size();
    }
}

