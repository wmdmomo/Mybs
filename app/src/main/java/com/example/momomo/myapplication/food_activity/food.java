package com.example.momomo.myapplication.food_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.momomo.myapplication.Adapter.foodwatchAdapter;
import com.example.momomo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import com.example.momomo.myapplication.data_save.fooditem;
import com.example.momomo.myapplication.data_save.foodwatch;
import com.example.momomo.myapplication.data_save.selectfood.afternoon;
import com.example.momomo.myapplication.data_save.selectfood.evening;
import com.example.momomo.myapplication.data_save.selectfood.morning;
import com.example.momomo.myapplication.data_save.selectfood.noon;


import org.litepal.LitePal;

public class food extends AppCompatActivity {
    private List<foodwatch> foodwatchList = new ArrayList<>(4);
    private foodwatch[] foodwatches;
    private com.example.momomo.myapplication.Adapter.foodwatchAdapter foodwatchAdapter;
    private ImageView add;
    private int Position = -1;
    private foodwatch turnfood;
    private Intent intent;
    private List<fooditem> resultList;
    private String[] times = {"早上", "中午", "下午", "晚上"};
    private int[] weathers = {R.drawable.ic_zaoshang, R.drawable.ic_zhongwu, R.drawable.ic_xiawucha, R.drawable.ic_wanshang};
    private int calvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        Log.i("pooo", "ss:" + Position);
        Intent intent = getIntent();
        int sp = intent.getIntExtra("spvalue", -1);
        calvalue=intent.getIntExtra("calvalue", -1);
        if (sp != -1) {
            Position = sp;
        }
        initview();
        Log.i("pooo", "传回来的值：" + Position);
        updateview();
        recyclerView.setLayoutManager(layoutManager);
        foodwatchAdapter = new foodwatchAdapter(this, foodwatchList);
        recyclerView.setAdapter(foodwatchAdapter);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Position != -1) {
                    Log.i("pooo", "改变了去传：" + Position);
                    Intent addfood = new Intent(food.this, addfood.class);
                    addfood.putExtra("positionvalue", Position);
                    startActivity(addfood);
                }
            }
        });


        resultList = (List<fooditem>) intent.getSerializableExtra("foodList");
        foodwatchAdapter.setOnTimeClickListener(new foodwatchAdapter.OnTimeClickListener() {
            @Override
            public void onClick(int position) {
                Position = position;
                Log.i("pooo", "改变了：" + Position);
                foodwatchAdapter.setThisposition(position);
                foodwatchAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initview() {
        for (int i = 0; i < 4; i++) {
            foodwatchList.add(new foodwatch(times[i], weathers[i]));
        }
        List<morning> morningList = LitePal.findAll(morning.class);
        int numsize1 = morningList.size();
        turnfood = foodwatchList.get(0);
        if (numsize1 >= 2) {
            turnfood.setFood1(morningList.get(numsize1 - 1).getImgid());
            turnfood.setFood2(morningList.get(numsize1 - 2).getImgid());
            turnfood.setCal(morningList.get(numsize1 - 1).getCal()+morningList.get(numsize1 - 2).getCal());
        } else {
            turnfood.setFood1(R.drawable.ic_apple);
            turnfood.setFood2(R.drawable.ic_apple);
        }

        List<noon> noonList = LitePal.findAll(noon.class);
        int numsize2 = noonList.size();
        turnfood = foodwatchList.get(1);
        if (numsize2 >= 2) {
            turnfood.setFood1(noonList.get(numsize2 - 1).getImgid());
            turnfood.setFood2(noonList.get(numsize2 - 2).getImgid());
            turnfood.setCal(noonList.get(numsize2 - 1).getCal()+noonList.get(numsize2- 2).getCal());
        } else {
            turnfood.setFood1(R.drawable.ic_apple);
            turnfood.setFood2(R.drawable.ic_apple);
        }

        List<afternoon> afternoonList = LitePal.findAll(afternoon.class);
        int numsize3 = afternoonList.size();
        turnfood = foodwatchList.get(2);
        if (numsize3 >= 2) {
            turnfood.setFood1(afternoonList.get(numsize3 - 1).getImgid());
            turnfood.setFood2(afternoonList.get(numsize3 - 2).getImgid());
            turnfood.setCal(afternoonList.get(numsize3 - 1).getCal()+afternoonList.get(numsize3 - 2).getCal());
        } else {
            turnfood.setFood1(R.drawable.ic_apple);
            turnfood.setFood2(R.drawable.ic_apple);
        }

        List<evening> eveningList = LitePal.findAll(evening.class);
        int numsize4 = eveningList.size();
        turnfood = foodwatchList.get(3);
        if (numsize4 >= 2) {
            turnfood.setFood1(eveningList.get(numsize4 - 1).getImgid());
            turnfood.setFood2(eveningList.get(numsize4 - 2).getImgid());
            turnfood.setCal(eveningList.get(numsize4 - 1).getCal()+eveningList.get(numsize4 - 2).getCal());
        } else {
            turnfood.setFood1(R.drawable.ic_apple);
            turnfood.setFood2(R.drawable.ic_apple);
        }
    }

    private void updateview() {
        if (Position != -1) {
            foodwatch food = foodwatchList.get(Position);
            if (resultList != null) {
                food.setFood1(resultList.get(0).getImgid());
                food.setFood2(resultList.get(1).getImgid());
            }
            food.setCal(calvalue);
        }
    }
}
