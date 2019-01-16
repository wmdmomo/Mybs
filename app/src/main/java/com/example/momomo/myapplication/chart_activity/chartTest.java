package com.example.momomo.myapplication.chart_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.momomo.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class chartTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);
        LineChart lineChart=(LineChart)findViewById(R.id.line1);
        lineChart.setDrawBorders(true);
        List<Entry> lineList=new ArrayList<>();
        for(int i=0;i<10;i++){
            lineList.add(new Entry(i,(float)(Math.random())*80));
        }
        LineDataSet lineDataSet=new LineDataSet(lineList,"温度");
        LineData data=new LineData(lineDataSet);
        lineChart.setData(data);
    }
}
