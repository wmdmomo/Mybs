package com.example.momomo.myapplication.login_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.momomo.myapplication.R;

import org.litepal.LitePal;

import com.example.momomo.myapplication.data_save.User;


public class login extends AppCompatActivity {
    private EditText user;
    private EditText pas;
    private EditText cpas;
    private String username;
    private String pas1;
    private String pas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LitePal.getDatabase();
        Button enter = (Button) findViewById(R.id.enter);
        user = (EditText) findViewById(R.id.user);
        pas = (EditText) findViewById(R.id.pas);
        cpas = (EditText) findViewById(R.id.cpas);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                pas1 = pas.getText().toString();
                pas2 = cpas.getText().toString();
                if (pas1.equals("") || pas2.equals("")) {
                    Toast.makeText(login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (pas1.equals(pas2)) {
                    User person = new User();
                    person.setName(username);
                    person.setPassword(pas1);
                    person.save();
                    Intent intentlogin = new Intent(login.this, tologin.class);
                    startActivity(intentlogin);
                } else {
                    Toast.makeText(login.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
