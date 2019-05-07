package com.example.momomo.myapplication.food_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momomo.myapplication.Adapter.fooddayAdapter;
import com.example.momomo.myapplication.Manager.Dish;
import com.example.momomo.myapplication.Manager.Fooddatas;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.foodcal;
import com.example.momomo.myapplication.data_save.selectfoods;
import com.example.momomo.myapplication.utils.LocalTime;
import com.example.momomo.myapplication.utils.saveVarible;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class dish_home extends TakePhotoActivity {
    //    TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;  //图片保存路径

    private String iconPath = "";
    private String result = "";
    private String[] foods;
    private List<selectfoods> selectfoodsList = new ArrayList<>();

    private AlertDialog.Builder builder;
    private AlertDialog dlg;
    private fooddayAdapter fooddayAdapter;
    private int timeid;

    private Fooddatas fooddatas;
    private foodclick foodclick = new foodclick(0);
    private TextView textView_total;
    private TextView textView_user;

    private String username;
    private Boolean suc_flag = false;
    private LocalTime localTime;
    private double caltotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_home);
        builder = new AlertDialog.Builder(this);
        initData();
        initView();
        fooddayAdapter.setOnTimeClickListener(new fooddayAdapter.OnTimeClickListener() {
            @Override
            public void onClick(int position) {
                timeid = position;
                imageUri = getImageCropUri();
                //从照相机中选取图片并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
            }
        });
    }

    @Override
    public void takeSuccess(TResult results) {
        super.takeSuccess(results);
        iconPath = results.getImage().getOriginalPath();
        try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Dish dish = new Dish();
                    result = dish.dish(iconPath);
                }
            });
            //还是得把拍照按钮放在每个页面的下面 知道他是第几个按的按钮 然后再添加食物
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        selectfoodsList.clear();
                        foods = new String[5];
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray array = jsonObject.getJSONArray("result");
                        if (array.length() == 1) {
                            suc_flag = false;
                        }

//                        接口返回了5个数据 供用户选择 单位是KJ/100G 这里转成大卡了
                        else {
                            suc_flag = true;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String food_name = object.getString("name");
                                double calorie = object.getDouble("calorie");
                                calorie*=0.239;
                                foods[i] = food_name;
                                selectfoods selectfoods = new selectfoods();
                                selectfoods.setCal(calorie);
                                selectfoods.setName(food_name);
                                selectfoodsList.add(selectfoods);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread1.start();
            thread1.join();
            thread2.start();
            thread2.join();
        } catch (Exception e) {
        }
        if (suc_flag == true) {
            createSingle();
        } else {
            Toast.makeText(dish_home.this, "Error:未能识别该菜品", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        textView_total = (TextView) findViewById(R.id.f_usercal);
        textView_user = (TextView) findViewById(R.id.f_username);
        textView_user.setText(username);
        caltotal = getCal();
        textView_total.setText(String.valueOf(caltotal));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.food_day);
        fooddayAdapter = new fooddayAdapter(this, fooddatas);
        fooddayAdapter.setCategoryBean(fooddatas);
        recyclerView.setAdapter(fooddayAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dish_home.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        User user = LitePal.find(User.class, userId);
        username = user.getName();
        fooddatas = new Fooddatas(username);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(dish_home.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    public void createSingle() {
        builder
                .setIcon(R.drawable.ic_foods)
                .setTitle("选择食物")
                .setSingleChoiceItems(foods, 0, foodclick)
                .setPositiveButton("确定", foodclick);
        dlg = builder.create();
        dlg.show();
    }

    private double getCal() {
        List<selectfoods> surefoodsList = new ArrayList<>();
        localTime = new LocalTime();
        surefoodsList = LitePal.where("user=? and time=?", username, localTime.LocalTime()).find(selectfoods.class);
        double total = 0;
        for (selectfoods selectfoods : surefoodsList) {
            total += selectfoods.getCal();
        }
        BigDecimal bigDecimal=new BigDecimal(total);
        double total1=bigDecimal.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
        return total1;
    }

    private class foodclick implements DialogInterface.OnClickListener {
        private int index;

        public foodclick(int index) {
            this.index = index;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which >= 0) {
                index = which;
            }
            if (which == DialogInterface.BUTTON_POSITIVE) {
                selectfoods selectfoods1 = new selectfoods();
                selectfoods1.setImgpath(iconPath);
                selectfoods1.setCal(selectfoodsList.get(index).getCal());
                selectfoods1.setName(selectfoodsList.get(index).getName());
                selectfoods1.setTimeid(timeid);
                selectfoods1.setUser(username);
                LocalTime localTime = new LocalTime();
                selectfoods1.setTime(localTime.LocalTime());
                selectfoods1.save();

                final AlertDialog ad = new AlertDialog.Builder(
                        dish_home.this).setMessage(
                        "你选择的食物是：" + foods[index]).show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ad.dismiss();
                    }
                };
                handler.postDelayed(runnable, 2 * 1000);
                caltotal = getCal();
                textView_total.setText(String.valueOf(caltotal));
                List<foodcal> foodDataList = new ArrayList<>();
                foodDataList = LitePal.where("user=? and time=?", username, localTime.LocalTime()).find(foodcal.class);
                if (foodDataList.size() != 0) {
                    int id = foodDataList.get(foodDataList.size() - 1).getId();
                    foodcal foodcal1 = LitePal.find(foodcal.class, id);
                    foodcal1.setCal(caltotal);
                    foodcal1.save();
                } else {
                    foodcal foodcal = new foodcal();
                    foodcal.setUser(username);
                    foodcal.setCal(caltotal);
                    foodcal.setTime(localTime.LocalTime());
                    foodcal.save();
                }
                fooddayAdapter.setCategoryBean(fooddatas);
            }
        }
    }
}
