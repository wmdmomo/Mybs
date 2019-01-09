package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.momomo.myapplication.Manager.ItemTouchHelperAdapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.Notes;

import java.util.Collections;
import java.util.List;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.ViewHolder>implements ItemTouchHelperAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Notes> notesList;
    private OnNoteClickListener onNoteClickListener;
    public noteAdapter(Context context,List<Notes> notes){
        this.context=context;
        this.notesList=notes;
        layoutInflater=LayoutInflater.from(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        CheckBox checkBox;
        TextView time;
        TextView title;
        TextView content;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            checkBox=(CheckBox) view.findViewById(R.id.delete);
            time=(TextView) view.findViewById(R.id.Time);
            title=(TextView) view.findViewById(R.id.title);
            content=(TextView) view.findViewById(R.id.content);
        }
    }

    @Override
    public void onItemDelete(int position) {
        Notes notes=notesList.get(position);
        notes.delete();
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(notesList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.note,viewGroup,false);
        return new ViewHolder(view);
    }
    public interface OnNoteClickListener{
        void onClick( int position);
    }
    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener){
        this.onNoteClickListener=onNoteClickListener;
    }
    @Override
    public void onBindViewHolder( ViewHolder viewHolder, final int i) {
        final Notes notes=notesList.get(i);
        viewHolder.content.setText(notes.getContent());
        viewHolder.title.setText(notes.getTitle());
        viewHolder.time.setText(notes.getTime());
        if(onNoteClickListener!=null){
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoteClickListener.onClick(i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

