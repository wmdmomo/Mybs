package com.example.momomo.myapplication.mine_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.punchday;
import com.example.momomo.myapplication.login_activity.tologin;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class about_punch extends AppCompatActivity {
    private int userId, punchId, daycount;
    private String imgpath;
    private User user;
    private punchday punchday;
    private Button button_punch;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_punch);
        final saveVarible app = (saveVarible) getApplication();
        userId = app.getUserId();
        user = LitePal.find(User.class, userId);
        punchId = app.getPunchId();
        punchday = LitePal.find(punchday.class, punchId);
        initView();
        setData();
        button_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (punchday.getFlag() == false) {
                    daycount += 1;
                    user.setPunch(daycount);
                    user.save();
                    Toast.makeText(about_punch.this, "今日打卡成功", Toast.LENGTH_SHORT).show();
                    textView.setText(String.valueOf(user.getPunch()));
                }else{
                    Toast.makeText(about_punch.this, "打卡失败", Toast.LENGTH_SHORT).show();
                }
                punchday.setFlag(true);
                punchday.save();
            }
        });

    }

    private void initView() {
        button_punch = (Button) findViewById(R.id.punch_day);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.punch_img);
        textView = (TextView) findViewById(R.id.punch_num);
        daycount = user.getPunch();
        imgpath = user.getAvatar_path();
    }

    private void setData() {
        if (!imgpath.equals(" ")) Glide.with(this).load(imgpath).into(imageView);
        else {
            int resourceId = R.mipmap.avatar;
            Glide.with(this)
                    .load(resourceId)
                    .into(imageView);
        }
        textView.setText(String.valueOf(daycount));
    }

}
