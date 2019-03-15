package com.example.momomo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.momomo.myapplication.login_activity.login;
import com.example.momomo.myapplication.login_activity.tologin;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sign=(Button) findViewById(R.id.create);
        Button Login=(Button) findViewById(R.id.login);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentlogin=new Intent(MainActivity.this,tologin.class);
                startActivity(intentlogin);
            }
        });
    }

}
