
package com.example.momomo.myapplication.note_activity;

import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.momomo.myapplication.Manager.BaseActivity;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.Notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class addnote extends BaseActivity {
    private TextView noteTime;
    private String nowtime;
    private EditText noteTitle;
    private EditText noteContent;
    private LinearLayout noteDone;
    private PercentRelativeLayout noteBG;
    private String title;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        noteTime=(TextView) findViewById(R.id.noteTime);
        noteDone=(LinearLayout) findViewById(R.id.noteDone);
        noteBG=(PercentRelativeLayout) findViewById(R.id.addnotebg);
        noteBG.getBackground().setAlpha(80);
        getTime();
        noteDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });
    }
    private void getTime(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat now=new SimpleDateFormat("yyyy.MM.dd HH:mm EE");
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        nowtime=now.format(date);
        noteTime.setText(nowtime);
    }
    private void addNotes(){
        noteTitle=(EditText)findViewById(R.id.noteTitle);
        noteContent=(EditText)findViewById(R.id.noteContent);
        title=noteTitle.getText().toString();
        content=noteContent.getText().toString();
        Notes notes=new Notes();
        notes.setTime(nowtime);
        notes.setTitle(title);
        notes.setContent(content);
        notes.save();
        Intent intent=new Intent(addnote.this,note.class);
        startActivity(intent);
    }
}
