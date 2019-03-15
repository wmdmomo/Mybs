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
import com.example.momomo.myapplication.data_save.User;

import java.util.List;

public class rankitemAdapter extends RecyclerView.Adapter<rankitemAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<User> rankList;
    private rankitemAdapter.OnRankClickListener onRankClickListener;
    public rankitemAdapter(Context context,List<User> rankList){
        this.rankList=rankList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        View view;
        TextView rank_num;
        TextView rank_name;
        TextView rank_start;
        de.hdodenhof.circleimageview.CircleImageView imageView;
        ImageView rank_attention;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            rank_num=(TextView) view.findViewById(R.id.rank_num);
            rank_name=(TextView) view.findViewById(R.id.rank_name);
            rank_start=(TextView) view.findViewById(R.id.rank_star);
            rank_attention=(ImageView)view.findViewById(R.id.rank_attention);
            imageView=(de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.rank_img);
        }
    }

    @Override
    public rankitemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.rank_item,viewGroup,false);
        return new rankitemAdapter.ViewHolder(view);
    }
    public interface OnRankClickListener{
        void onClick( int position);
    }

    public void setOnRankClickListener(rankitemAdapter.OnRankClickListener
                                                 onRankClickListener){
        this.onRankClickListener=onRankClickListener;
    }
    @Override
    public void onBindViewHolder(rankitemAdapter.ViewHolder viewHolder, final int position) {
        final User rank=rankList.get(position);
        viewHolder.rank_num.setText(String.valueOf(position+1));
        viewHolder.rank_name.setText(rank.getName());
        viewHolder.rank_start.setText(String.valueOf(rank.getPunch()));
        final String imgpath=rank.getAvatar_path();
        if (!imgpath.equals(" ")) Glide.with(context).load(imgpath).into(viewHolder.imageView);
        else {
            int resourceId = R.mipmap.avatar;
            Glide.with(context)
                    .load(resourceId)
                    .into(viewHolder.imageView);
        }
        if( onRankClickListener!= null) {
            viewHolder.rank_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRankClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }
}
