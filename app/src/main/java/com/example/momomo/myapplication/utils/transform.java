package com.example.momomo.myapplication.utils;

import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class transform {
    public void transform(Window window) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);//防止5.x以后半透明影响效果，使用这种透明方式
        }
    }
}
