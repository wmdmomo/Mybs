package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.Manager.BaseActivity;
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

public class mine_set extends BaseActivity {
    private EditText signature, weight, height, goal_weight;
    private TextView name;
    private String sign, sign_set, nam, wei_s, hei_s, imgpath, goal_s;
    private int hei,  hei_set;
    private double wei,goal,wei_set,goal_set;
    private android.support.v7.widget.Toolbar toolbar;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    //    TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;  //图片保存路径
    private int userId;
    private User user;
    private String iconPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_set);
        final saveVarible app = (saveVarible) getApplication();
        userId = app.getUserId();
        user = LitePal.find(User.class, userId);
        initSet();
        initData();
        initBar();
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_img_set);
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
        Button done = (Button) findViewById(R.id.set_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_set = signature.getText().toString();
                wei_s = weight.getText().toString();
                hei_s = height.getText().toString();
                goal_s = goal_weight.getText().toString();
                if (!wei_s.equals("")) {
                    wei_set = Double.parseDouble(wei_s);

                    user.setNow_weight(wei_set);
                }
                if (!hei_s.equals("")) {
                    hei_set = Integer.parseInt(hei_s);
                    user.setHeight(hei_set);
                }
                if (!goal_s.equals("")) {
                    goal_set = Double.parseDouble(goal_s);
                    user.setGoal_weight(goal_set);
                }
                if (!sign_set.equals("")) user.setSignature(sign_set);
                //这句有问题 因为之前没有对iconPath 进行初始化
                if (!iconPath.equals("")) user.setAvatar_path(iconPath);
                user.save();
                Intent intent = new Intent(mine_set.this, mine.class);
                startActivity(intent);
            }
        });
    }

    private void initSet() {
        sign = user.getSignature();
        nam = user.getName();
        wei = user.getNow_weight();
        hei = user.getHeight();
        imgpath = user.getAvatar_path();
        goal = user.getGoal_weight();
        signature = (EditText) findViewById(R.id.user_word_set);
        name = (TextView) findViewById(R.id.user_name_set);
        weight = (EditText) findViewById(R.id.user_weight_set);
        height = (EditText) findViewById(R.id.user_height_set);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_img_set);
        goal_weight = (EditText) findViewById(R.id.user_goal_set);
        signature.setHint(sign);
        name.setText(nam);
        weight.setHint(String.valueOf(wei));
        height.setHint(String.valueOf(hei));
        goal_weight.setHint(String.valueOf(goal));

        if (!imgpath.equals(" ")) Glide.with(this).load(imgpath).into(imageView);
        else {
            int resourceId = R.mipmap.avatar;
            Glide.with(this)
                    .load(resourceId)
                    .into(imageView);
        }
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
        Toast.makeText(mine_set.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
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

    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    private void initBar() {
        toolbar = (Toolbar) findViewById(R.id.about_mine_set_bar);
        TextView textView = (TextView) findViewById(R.id.mine_set_title);
        textView.setText("我的信息");
    }
}
