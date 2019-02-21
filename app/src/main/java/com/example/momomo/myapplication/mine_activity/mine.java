package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.punchday;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;



public class mine extends AppCompatActivity {
    private int userId;
    private int punchId;
    private TextView name;
    private TextView word;
    private TextView height;
    private TextView weight;
    private TextView days;
    private User user;
    private punchday punchday;
    private String nam;
    private String sign;
    private int hei;
    private int wei;
    private int daycount;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private String imgpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        final saveVarible app=(saveVarible) getApplication();
        userId=app.getUserId();
        user=LitePal.find(User.class,userId);
        punchId=app.getPunchId();
        punchday=LitePal.find(punchday.class,punchId);
        initEdit();
        Button punch=(Button) findViewById(R.id.getdays);
        Button setting=(Button) findViewById(R.id.setting);
        setEdit();
        punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(punchday.getFlag()==false){
                    daycount+=1;
                    user.setPunch(daycount);
                    user.save();
                    days.setText(String.valueOf(user.getPunch()));
                }
                punchday.setFlag(true);
                punchday.save();

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mine.this,mine_set.class);
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
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_img);
        nam=user.getName();
        sign=user.getSignature();
        hei=user.getHeight();
        wei=user.getWeight();
        daycount=user.getPunch();
        imgpath=user.getAvatar_path();
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
        if(!imgpath.equals(" "))Glide.with(this).load(imgpath).into(imageView);
        else{
            int resourceId = R.mipmap.avatar;
            Glide.with(this)
                    .load(resourceId)
                    .into(imageView);
        }
    }
}
