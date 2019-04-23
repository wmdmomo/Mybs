package com.example.momomo.myapplication.chart_activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.stepData;
import com.example.momomo.myapplication.utils.saveVarible;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class step_chart extends DemoBase implements SeekBar.OnSeekBarChangeListener {
    private String username;
    private List<stepData> stepDataList=new ArrayList<>();
    private SeekBar seekBarX;
    private BarChart chart;
    private TextView tvX;
    private XAxis xAxis;
    List<String> names=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_test);
        initdata();
//        LineChart lineChart=(LineChart)findViewById(R.id.line1);
//        lineChart.setDrawBorders(true);
//        Description description=new Description();
//        description.setText("步数统计图");
//        lineChart.setDescription(description);
//        List<Entry> lineList=new ArrayList<>();
//        List<String> names=new ArrayList<>();
//        for(int i=0;i<stepDataList.size();i++){
//            lineList.add(new Entry(i,stepDataList.get(i).getStep()));
//            names.add(stepDataList.get(i).getDate());
//        }
//        XAxis xAxis=lineChart.getXAxis();
//        xAxis.setTextColor(Color.parseColor("#333333"));
//        xAxis.setTextSize(11f);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setDrawAxisLine(true);//是否绘制轴线
//        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
//        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
//        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
//
//        LineDataSet lineDataSet=new LineDataSet(lineList,"步数数据");
//        LineData data=new LineData(lineDataSet);
//        lineChart.setData(data);
    }
    private void initdata(){
        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        User user = LitePal.find(User.class, userId);
        username=user.getName();
        stepDataList = LitePal.where("user=?",username).find(stepData.class);
        tvX = findViewById(R.id.tvXMax);


        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);



        chart = findViewById(R.id.chart1);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.LTGRAY);


        xAxis = chart.getXAxis();
        YAxis left=chart.getAxisLeft();
        left.setTypeface(tfItalic);
        YAxis right=chart.getAxisRight();
        right.setTypeface(tfItalic);
        xAxis.setTypeface(tfItalic);
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(8f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
//        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘



        chart.getAxisLeft().setDrawGridLines(false);

        // setting data
        seekBarX.setProgress(5);


        // add a nice and smooth animation
        chart.animateY(1500);

        chart.getLegend().setEnabled(false);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("显示"+String.valueOf(seekBarX.getProgress())+"天的步数");

        ArrayList<BarEntry> values = new ArrayList<>();

//        List<Entry> lineList=new ArrayList<>();

        int min=Math.min(seekBarX.getProgress(),stepDataList.size());
        for(int i=0;i<min;i++){
            values.add(new BarEntry(i,stepDataList.get(i).getStep()));
            names.add(stepDataList.get(i).getDate());
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));


        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.PASTEL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.25f);

            chart.setData(data);
            chart.setFitBars(true);
        }

        chart.invalidate();
    }
    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "AnotherBarActivity");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

}
