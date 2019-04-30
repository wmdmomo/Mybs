package com.example.momomo.myapplication.note_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.momomo.myapplication.Adapter.noteAdapter;
import com.example.momomo.myapplication.Manager.BaseActivity;
import com.example.momomo.myapplication.Manager.myItemTouchHelperCallBack;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.Notes;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.home;
import com.example.momomo.myapplication.mine_activity.mine;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class note extends BaseActivity {
    List<Notes> notesList = new ArrayList<>();
    private noteAdapter noteAdapter;
    private Button addnote;
    private LinearLayout enternote;
    private String username;
//    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.bg);
        linearLayout.getBackground().setAlpha(120);
        initdata();
        enternote = (LinearLayout) findViewById(R.id.enternote);
        enternote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(note.this, addnote.class);
                intent.putExtra("user",username);
                startActivity(intent);
            }
        });
        noteAdapter.setOnNoteClickListener(new noteAdapter.OnNoteClickListener() {
            @Override
            public void onClick(int position) {
//                notesList.remove(position);
//                noteAdapter.notifyItemRemoved(position);
//                noteAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initdata() {
        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        User user=LitePal.find(User.class, userId);
        username=user.getName();
        notesList = LitePal.where("username=?", username).find(Notes.class);
        if (notesList != null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.noterv);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            noteAdapter = new noteAdapter(this, notesList);
            recyclerView.setAdapter(noteAdapter);
            ItemTouchHelper.Callback callback = new myItemTouchHelperCallBack(noteAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(note.this, home.class);

        startActivity(intent);
    }
}

