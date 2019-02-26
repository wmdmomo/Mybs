package com.example.momomo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.Adapter.homeitemAdapter;
import com.example.momomo.myapplication.activities.SportActivity;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.homeitem;
import com.example.momomo.myapplication.food_activity.food;
import com.example.momomo.myapplication.mine_activity.mine;
import com.example.momomo.myapplication.note_activity.note;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private List<homeitem> homeitemList=new ArrayList<>();
    private  String[] words={"跑步","心率","记事本","卡路里"};
    private  int[] imgids={R.mipmap.running,R.mipmap.gym,R.mipmap.work,R.mipmap.eating};
    private int userId;
    private User user;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private String imgpath;
    private int Weight;
    private int Goal_weight;
    private TextView weight;
    private TextView goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final saveVarible app=(saveVarible) getApplication();
        userId=app.getUserId();
        user= LitePal.find(User.class,userId);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.homerv);
        ImageView avatar=(ImageView) findViewById(R.id.avatar);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initdata();
        homeitemAdapter homeitemAdapter=new homeitemAdapter(homeitemList);
        recyclerView.setAdapter(homeitemAdapter);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, mine.class);
                startActivity(intent);
            }
        });
        homeitemAdapter.setOnhomeClickListener(new homeitemAdapter.OnhomeClickListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:{
                        Intent intent = new Intent(home.this, SportActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(home.this, SportActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(home.this, note.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Intent intent = new Intent(home.this, food.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

    }
    private void initdata(){
        imageView=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.avatar);
        weight=(TextView) findViewById(R.id.my_weight);
        goal=(TextView) findViewById(R.id.goal_weight);
        imgpath=user.getAvatar_path();
        Weight=user.getWeight();
        Goal_weight=user.getGoal_weight();
        weight.setText(String.valueOf(Weight));
        goal.setText(String.valueOf(Goal_weight));
        if(!imgpath.equals(" ")) Glide.with(this).load(imgpath).into(imageView);
        else{
            int resourceId = R.mipmap.avatar;
            Glide.with(this)
                    .load(resourceId)
                    .into(imageView);
        }
        for(int i=0;i<4;i++){
            homeitem homeitem=new homeitem();
            homeitem.setImgid(imgids[i]);
            homeitem.setWord(words[i]);
            homeitemList.add(homeitem);
        }
    }
}
