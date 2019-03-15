package com.example.momomo.myapplication.mine_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.momomo.myapplication.R;

public class about_us extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initBar();
    }
    private void initBar(){
        toolbar=(Toolbar) findViewById(R.id.about_us_bar);
        TextView textView=(TextView)findViewById(R.id.us_title);
        textView.setText("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左边的Home图标为返回按钮

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //这个就是toolbar的返回按钮空间的id
                onBackPressed();
                break;
        }
        return true; //告诉系统我们自己处理了点击事件
    }

}
