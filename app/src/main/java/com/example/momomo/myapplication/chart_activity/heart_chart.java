package com.example.momomo.myapplication.chart_activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.heartData;
import com.example.momomo.myapplication.utils.saveVarible;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class heart_chart extends AppCompatActivity {
    private String username;
    private List<heartData> heartDataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);
        initdata();
        LineChart lineChart=(LineChart)findViewById(R.id.line1);
        lineChart.setDrawBorders(true);
        List<Entry> lineList=new ArrayList<>();
        List<String> names=new ArrayList<>();
        for(int i=0;i<heartDataList.size();i++){
            lineList.add(new Entry(i,heartDataList.get(i).getHeart()));
            names.add(heartDataList.get(i).getDate());
        }
        XAxis xAxis=lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘
        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));

        LineDataSet lineDataSet=new LineDataSet(lineList,"心率数据");
        LineData data=new LineData(lineDataSet);
        lineChart.setData(data);
    }
    private void initdata(){
        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        User user = LitePal.find(User.class, userId);
        username=user.getName();
        heartDataList = LitePal.where("user=?",username).find(heartData.class);
    }

}
