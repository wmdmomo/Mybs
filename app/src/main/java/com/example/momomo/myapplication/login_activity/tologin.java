package com.example.momomo.myapplication.login_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.momomo.myapplication.Manager.ActivityCollector;
import com.example.momomo.myapplication.Manager.BaseActivity;
import com.example.momomo.myapplication.R;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.punchday;
import com.example.momomo.myapplication.data_save.stepData;
import com.example.momomo.myapplication.home;
import com.example.momomo.myapplication.utils.saveVarible;

public class tologin extends BaseActivity {
    private EditText user;
    private EditText pas;
    private String username;
    private String pass;
    private Boolean flag = false;
    private Boolean Flag = false;
    private int userId;
    private int punchId;
    private punchday punchday;
    private String time;
    private Intent intentlogin;
    private int isFirst = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tologin);
        final saveVarible app = (saveVarible) getApplication();
        Button enter = (Button) findViewById(R.id.denglu);
        user = (EditText) findViewById(R.id.name);
        pas = (EditText) findViewById(R.id.mima);
        Intent intent = getIntent();
        if (intent != null) {
            isFirst = intent.getIntExtra("isFirst", 0);
        }
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                pass = pas.getText().toString();
                if(username.equals("")||pass.equals("")){
                    Toast.makeText(tologin.this, "信息不完整", Toast.LENGTH_SHORT).show();
                }
                else {
                    List<User> userList = LitePal.findAll(User.class);
                    for (User users : userList) {
                        if (users.getName().equals(username) && users.getPassword().equals(pass)) {
                            flag = true;
                            userId = users.getId();
                            app.setUserId(userId);
                            break;
                        }
                    }
                    if (flag) {
                        getTime();
                        List<punchday> punchdayList = LitePal.findAll(punchday.class);
                        if (punchdayList != null) {
                            for (punchday punchdays : punchdayList) {
                                if (punchdays.getUser() != null & punchdays.getTime() != null) {
                                    if (punchdays.getUser().equals(username) && punchdays.getTime().equals(time)) {
                                        Flag = true;
                                        punchId = punchdays.getId();
                                        app.setPunchId(punchId);
                                        break;
                                    }
                                }
                            }
                        }
                        if (Flag == false) {
                            punchday = new punchday();
                            punchday.setUser(username);
                            punchday.setFlag(false);
                            punchday.setTime(time);
                            punchday.save();
                            punchId = punchday.getId();
                            app.setPunchId(punchId);
                        }
                        if (isFirst == 1) {
                            intentlogin = new Intent(tologin.this, login_after.class);
                        } else {
                            intentlogin = new Intent(tologin.this, home.class);
                        }
                        startActivity(intentlogin);
                    } else {
                        Toast.makeText(tologin.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void getTime() {
        long times = System.currentTimeMillis();
        Date date = new Date(times);
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd");
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        time = now.format(date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("ttt","anxiale");
        ActivityCollector.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
