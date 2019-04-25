package com.example.momomo.myapplication.login_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.home;
import com.example.momomo.myapplication.utils.saveVarible;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class login_after extends TakePhotoActivity {
    private Spinner spinner;
    private List<String> xingbie=new ArrayList<String>();
    ArrayAdapter <String> adapter;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private Button button;

    //    TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;  //图片保存路径
    private int userId;
    private User user;
    private String iconPath = "";

    private EditText weight,height,age;
    private String wei_s,hei_s,sex,age_s;
    private int wei_set,hei_set,age_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setPrompt("性别");
        button=(Button)findViewById(R.id.full_done);
        initData();
        initdata();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,xingbie);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(login_after.this,"您选择的月份是："+xingbie.get(position),Toast.LENGTH_SHORT).show();
                sex=xingbie.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.full_avatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = getImageCropUri();
                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                //从相册中选取不裁剪
                //takePhoto.onPickFromGallery();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wei_s = weight.getText().toString();
                hei_s = height.getText().toString();
                age_s=age.getText().toString();
                if (!wei_s.equals("")) {
                    wei_set = Integer.parseInt(wei_s);
                    user.setWeight(wei_set);
                }
                if (!hei_s.equals("")) {
                    hei_set = Integer.parseInt(hei_s);
                    user.setHeight(hei_set);
                }
                if (!age_s.equals("")) {
                    age_set = Integer.parseInt(age_s);
                    user.setAge(age_set);
                }
                user.setSex(sex);
                if (!iconPath.equals("")) user.setAvatar_path(iconPath);
                user.save();
                Intent intent=new Intent(login_after.this, home.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        iconPath = result.getImage().getOriginalPath();
        //Google Glide库 用于加载图片资源
        //把他的iconpath 记住 在user表里！！！
        Glide.with(this).load(iconPath).into(imageView);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(login_after.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
    }
    private void initdata(){
        xingbie.add("男");
        xingbie.add("女");
        weight=(EditText)findViewById(R.id.full_weight);
        height=(EditText)findViewById(R.id.full_height);
        age=(EditText)findViewById(R.id.full_age);
        final saveVarible app = (saveVarible) getApplication();
        userId = app.getUserId();
        user = LitePal.find(User.class, userId);
    }

    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
}
