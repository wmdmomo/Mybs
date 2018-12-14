package com.example.momomo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class tologin extends AppCompatActivity {
    private EditText user;
    private EditText pas;
    private String username;
    private String pass;
    private Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tologin);
        Button enter=(Button)findViewById(R.id.denglu);
        user=(EditText)findViewById(R.id.name);
        pas=(EditText)findViewById(R.id.mima);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=user.getText().toString();
                pass=pas.getText().toString();
                List<User> userList = LitePal.findAll(User.class);
                for (User users:userList){
                    if(users.getName().equals(username)&&users.getPassword().equals(pass)){
//                        Log.i("first","flag:"+flag+" "+username);
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Intent intentlogin=new Intent(tologin.this,home.class);
                    startActivity(intentlogin);
                }
                else{
                    Toast.makeText(tologin.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
