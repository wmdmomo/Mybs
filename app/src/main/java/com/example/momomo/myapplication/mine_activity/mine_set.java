package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class mine_set extends AppCompatActivity {
    private EditText signature;
    private EditText weight;
    private EditText height;
    private String sign;
    private int wei;
    private int hei;
    private String sign_set;
    private int wei_set;
    private int hei_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_set);
        Button done=(Button)findViewById(R.id.set_done);
        Intent intent=this.getIntent();
        sign=intent.getStringExtra("sign");
        wei=intent.getIntExtra("weight",0);
        hei=intent.getIntExtra("height",0);
        initSet();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_set=signature.getText().toString();
                wei_set=Integer.parseInt(weight.getText().toString());
                hei_set=Integer.parseInt(height.getText().toString());
                final saveVarible app=(saveVarible) getApplication();
                User user=LitePal.find(User.class,app.getUserId());
                user.setSignature(sign_set);
                user.setWeight(wei_set);
                user.setHeight(hei_set);
                user.save();
                Intent intent=new Intent(mine_set.this,mine.class);
                startActivity(intent);
            }
        });
    }
    private void initSet(){
        signature=(EditText) findViewById(R.id.avatar_word_set);
        weight=(EditText) findViewById(R.id.weight_set);
        height=(EditText) findViewById(R.id.height_set);
        signature.setHint(sign);
        weight.setHint(String.valueOf(wei));
        height.setHint(String.valueOf(hei));
    }
}
