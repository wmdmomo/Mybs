package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.util.List;

public class mine extends AppCompatActivity {
    private int userId;
    private TextView name;
    private TextView word;
    private TextView height;
    private TextView weight;
    private TextView days;
    private User user;
    private String nam;
    private String sign;
    private int hei;
    private int wei;
    private int daycount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        final saveVarible app=(saveVarible) getApplication();
        userId=app.getUserId();
        user=LitePal.find(User.class,userId);
        initEdit();
        Button punch=(Button) findViewById(R.id.getdays);
        Button setting=(Button) findViewById(R.id.setting);
        setEdit();
        punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daycount+=1;
                user.setPunch(daycount);
                days.setText(String.valueOf(user.getPunch()));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mine.this,mine_set.class);
                intent.putExtra("sign",sign);
                intent.putExtra("height",hei);
                intent.putExtra("weight",wei);
                startActivity(intent);
            }
        });
    }
    private void initEdit(){
        name=(TextView) findViewById(R.id.avatar_name);
        word=(TextView) findViewById(R.id.avatar_word);
        height=(TextView) findViewById(R.id.height);
        weight=(TextView) findViewById(R.id.weight);
        days=(TextView) findViewById(R.id.day);
        nam=user.getName();
        sign=user.getSignature();
        hei=user.getHeight();
        wei=user.getWeight();
        daycount=user.getPunch();
    }
    private void setEdit(){
        name.setText(nam);
        if(sign.equals(" ")){
            word.setText("快去编辑你的个性签名吧~");
        }
        else word.setText(sign);
        height.setText(String.valueOf(hei));
        weight.setText(String.valueOf(wei));
        days.setText(String.valueOf(daycount));
    }
}
