package com.example.momomo.myapplication.map_activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.EntityListRequest;
import com.baidu.trace.api.entity.EntityListResponse;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.QueryCacheTrackRequest;
import com.baidu.trace.api.track.QueryCacheTrackResponse;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.ProtocolType;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.Point;
import com.baidu.trace.model.TransportMode;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.trackData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.baidu.mapapi.CoordType.BD09LL;

public class MapMainActivity extends Activity {
    private Button benginrun;
    private Button endrun;
    private Button noise;
    private Button nonoise;
    private Button road;
    private Button setdate;
    private Button setbtime;
    private Button setetime;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private MyLocationListener myLocationListener = null;

    private double latitude ;    //获取纬度信息
    private double longitude;    //获取经度信息
    private boolean centerbool = false;
    public Context mContext = null;

    //鹰眼轨迹
    private LBSTraceClient mTraceClient=null;
    private Trace mTrace = null;
    private setOnTraceListener mTraceListener = null;
    //鹰眼实时位置
    private EntityListRequest mEntity = null;
    private setOnEntityListener mEntityListener = null;
    //鹰眼缓存轨迹获取
    private QueryCacheTrackRequest mCacheTrack = null;
    private setOnTrackListener mTrackListener = null;
    //鹰眼历史轨迹
    private HistoryTrackRequest mHistoryTrack = null;
    private int tag = 1;
    private boolean noisebool = false;
    private int gatherbegin = 0;
    private int gatherend = 0;
    private Boolean saveBool = false;
    private Boolean savenoise = false;
    //开始时间（Unix时间戳）
    private int startTime = 0;
    //结束时间（Unix时间戳）
    private int endTime = 0;
    private int year = 0;
    private int month = 0;
    private int dayOfmonth = 0;
    private int beginhour = -1;
    private int beginminute = -1;
    private int endhour = -1;
    private int endminute = -1;
    boolean isProcessed = false;
    ProcessOption processOption = null;
    SupplementMode supplementModel = SupplementMode.no_supplement;
    SortType sortType = SortType.asc;
    int pageIndex = 1;
    int pageSize = 1000;


    private Boolean startfirstbool = true;
    private Boolean stopfirstbool = true;
    // 轨迹服务ID
    private long serviceId = 211616;
    // 设备标识
    private String entityName = null;
    // 是否需要对象存储服务，注意：若需要对象存储服务，一定要导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;

    private SetOnTimelistener timelistener = null;
    private SetOnDatelistener datelistener = null;
    private Boolean roadbool = false;
    private Boolean timebool = false;

    private int beginendsign = 0;
    private Calendar calendar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        judgePermission();
        SDKInitializer.initialize(mContext);
        SDKInitializer.setCoordType(BD09LL);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.bmapView);     //获取地图控件引用
        benginrun =(Button) findViewById(R.id.beginrun);
        endrun = (Button)findViewById(R.id.endrun);
        noise = (Button) findViewById(R.id.noise);
        nonoise = (Button)findViewById(R.id.nonoise);
        road = (Button)findViewById(R.id.road);
        setdate = (Button)findViewById(R.id.datedialog);
        setbtime = (Button)findViewById(R.id.begindialog);
        setetime = (Button)findViewById(R.id.enddialog);
        initbtn();
        mBaiduMap = mMapView.getMap();
        myLocationListener = new MyLocationListener();
        centerbool = true;
        initMap();
        initGps();


        //鹰眼轨迹
        traceinit(mContext);
        startfirstbool = true;



    }





    //设置时间
    private void timedialog(){
        if(timebool == false){
            setdate.setVisibility(View.VISIBLE);
            setbtime.setVisibility(View.VISIBLE);
            setetime.setVisibility(View.VISIBLE);
            timebool = true;
        }else {
            setdate.setVisibility(View.INVISIBLE);
            setbtime.setVisibility(View.INVISIBLE);
            setetime.setVisibility(View.INVISIBLE);
            timebool = false;
        }
    }


    private void datedialog(){
        datelistener = new SetOnDatelistener();
        if(year == 0 || month ==0 || dayOfmonth == 0){
            new DatePickerDialog(MapMainActivity.this,datelistener,2019,3,17).show();
        }else {
            new DatePickerDialog(MapMainActivity.this,datelistener,year,month,dayOfmonth).show();
        }
    }

    private void begintime(){
        beginendsign = 0;
        timelistener = new SetOnTimelistener();
        if (beginhour == -1 || beginminute == -1) {
            new TimePickerDialog(MapMainActivity.this, timelistener, 0, 0, true).show();

        }else {
            new TimePickerDialog(MapMainActivity.this, timelistener, beginhour, beginminute, true).show();
        }
    }

    private void endtime(){
        beginendsign = 1;
        timelistener = new SetOnTimelistener();
        if (endhour == -1 || endminute == -1) {
            new TimePickerDialog(MapMainActivity.this, timelistener, 0, 0, true).show();

        }else {
            new TimePickerDialog(MapMainActivity.this, timelistener, endhour, endminute, true).show();
        }
    }


    //初始化按钮
    private void initbtn(){
        RelativeLayout.LayoutParams layoutParamsbengin =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamsend =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamsroad =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamsyes =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamsno =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamssetdate =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamssetbtime =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamssetetime =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        android.graphics.Point sizePoint = new android.graphics.Point();
        display.getSize(sizePoint);
        int width = sizePoint.x;
        int height = sizePoint.y;
        layoutParamsbengin.setMargins(width/5,9*height/10,0,0);
        layoutParamsend.setMargins(3*width/5,9*height/10,0,0);
        layoutParamsroad.setMargins(0,height/10,0,0);
        layoutParamsyes.setMargins(width/5,height/10,0,0);
        layoutParamsno.setMargins(2*width/5,height/10,0,0);
        layoutParamssetdate.setMargins(width/5,0,0,0);
        layoutParamssetbtime.setMargins(2*width/5,0,0,0);
        layoutParamssetetime.setMargins(3*width/5,0,0,0);
        benginrun.setLayoutParams(layoutParamsbengin);
        endrun.setLayoutParams(layoutParamsend);
        road.setLayoutParams(layoutParamsroad);
        noise.setLayoutParams(layoutParamsyes);
        nonoise.setLayoutParams(layoutParamsno);
        setdate.setLayoutParams(layoutParamssetdate);
        setbtime.setLayoutParams(layoutParamssetbtime);
        setetime.setLayoutParams(layoutParamssetetime);
        noise.setVisibility(View.INVISIBLE);
        nonoise.setVisibility(View.INVISIBLE);
        setdate.setVisibility(View.INVISIBLE);
        setbtime.setVisibility(View.INVISIBLE);
        setetime.setVisibility(View.INVISIBLE);
    }



    private void initMap(){
         // 显示缩放比例尺
        mMapView.showZoomControls(true);
        //百度地图
        mBaiduMap.setMyLocationEnabled(true);
        // 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }






    private  void initGps(){
        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }



    //6.0之后要动态获取权限，重要！！！
    private void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            //gps权限

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }

        }else{
            //doSdCardResult();
        }
    }




    public void onclick(View v){
        switch(v.getId()) {
            case R.id.beginrun:
               startgather();
                break;
            case R.id.endrun:
               stopgather();
                break;
            case R.id.road:
                getroad();
                break;
            case R.id.noise:
                noisebool = false;
                noise(noisebool);
                break;
            case R.id.nonoise:
                noisebool = true;
                noise(noisebool);
                break;
            case R.id.timedialog:
                timedialog();
                break;
            case R.id.datedialog:
                datedialog();
                break;
            case R.id.begindialog:
                begintime();
                break;
            case R.id.enddialog:
                endtime();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMapView == null){
            Toast.makeText(MapMainActivity.this,"地图初始化失败1",Toast.LENGTH_SHORT).show();
            return ;
        }
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mMapView == null){
            Toast.makeText(MapMainActivity.this,"地图初始化失败2",Toast.LENGTH_SHORT).show();
            return ;
        }
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMapView == null){
            Toast.makeText(MapMainActivity.this,"地图初始化失败3",Toast.LENGTH_SHORT).show();
            return ;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.unRegisterLocationListener(myLocationListener);
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }



    //画轨迹
    private void drawmap(HistoryTrackResponse response){
        mBaiduMap.clear();

        //标记起始终止点
        Point start = response.getStartPoint();
        LatLng start1 = new LatLng(start.getLocation().latitude,start.getLocation().longitude);
        BitmapDescriptor startbt = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_st);
        Point end = response.getEndPoint();
        LatLng end1 = new LatLng(end.getLocation().latitude,end.getLocation().longitude);
        BitmapDescriptor endbt = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_end);
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        OverlayOptions option1 =  new MarkerOptions()
                .position(start1)
                .icon(startbt);
        OverlayOptions option2 =  new MarkerOptions()
                .position(end1)
                .icon(endbt);
        options.add(option1);
        options.add(option2);
        mBaiduMap.addOverlays(options);

        List<LatLng> points = new ArrayList<LatLng>();
        List<TrackPoint> hpoints = response.getTrackPoints();
        for (TrackPoint trackPoint : hpoints) {
            points.add(convertTrace2Map(trackPoint.getLocation()));
        }
//            Toast.makeText(MapMainActivity.this,"历史"+points,Toast.LENGTH_SHORT).show();
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAA0000FF)
                .points(points);
        mBaiduMap.addOverlay(mOverlayOptions);
    }


    public static LatLng convertTrace2Map(com.baidu.trace.model.LatLng traceLatLng) {
        return new LatLng(traceLatLng.latitude, traceLatLng.longitude);
    }






    //鹰眼轨迹
    protected void traceinit(Context mContext){
        //获取用户设备Imei
        entityName = getImei(mContext);
        // 初始化轨迹服务
        mTrace = new Trace(serviceId,entityName,isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(mContext);
        mTraceListener = new setOnTraceListener();
        mTraceClient.setProtocolType(ProtocolType.HTTP);
        //获取鹰眼实时位置
        mEntity = new EntityListRequest(tag,serviceId);
        mEntityListener = new setOnEntityListener();
        //获取鹰眼缓存轨迹
        mCacheTrack = new QueryCacheTrackRequest(tag,serviceId);
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        mTraceClient.setOnTraceListener(mTraceListener);
    }


    private void getroad(){
        if(roadbool == false){
            nonoise.setVisibility(View.VISIBLE);
            noise.setVisibility(View.VISIBLE);
            roadbool = true;
        }else {
            nonoise.setVisibility(View.INVISIBLE);
            noise.setVisibility(View.INVISIBLE);
            roadbool = false;
        }
    }


    //历史轨迹
    private void noise(boolean noiseb){
        if (network()) {
            if (settime()) {
                setprocess(noiseb);
                mTrackListener = new setOnTrackListener();
                mHistoryTrack = new HistoryTrackRequest(tag, serviceId, entityName, startTime, endTime, isProcessed, processOption, supplementModel,
                        sortType, com.baidu.trace.model.CoordType.bd09ll, pageIndex, pageSize);
                mTraceClient.queryHistoryTrack(mHistoryTrack, mTrackListener);
            }
        }else {

        }
    }

    private void setprocess(boolean noise){
        if (noise == false) {
            isProcessed = false;
            processOption = new ProcessOption();
            processOption.setNeedDenoise(false);
            // 设置需要抽稀
            processOption.setNeedVacuate(false);
            // 设置需要绑路
            processOption.setNeedMapMatch(false);
            processOption.setRadiusThreshold(0);
            processOption.setTransportMode(TransportMode.walking);
        } else {
            isProcessed = true;
            processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            // 设置需要抽稀
            processOption.setNeedVacuate(true);
            // 设置需要绑路
            processOption.setNeedMapMatch(true);
            processOption.setRadiusThreshold(100);
            processOption.setTransportMode(TransportMode.walking);
        }
    }


    private Boolean settime(){
        if (year == 0 || month == 0 ){
            Toast.makeText(MapMainActivity.this,"请设置日期",Toast.LENGTH_SHORT).show();
            return false;
        }else if(beginhour == -1 || beginminute == -1){
            Toast.makeText(MapMainActivity.this,"请设置开始时间",Toast.LENGTH_SHORT).show();
            return false;
        }else if(endhour == -1 || endminute == -1){
            Toast.makeText(MapMainActivity.this,"请设置结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }else if (beginhour > endhour){
            Toast.makeText(MapMainActivity.this,"开始时间大于结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }else if (beginhour == endhour && beginminute > endminute){
            Toast.makeText(MapMainActivity.this,"开始时间大于结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            calendar = calendartime(year,month,dayOfmonth,beginhour,beginminute);
            startTime = (int)(calendar.getTimeInMillis()/1000);
            calendar = calendartime(year,month,dayOfmonth,endhour,endminute);
            endTime = (int)(calendar.getTimeInMillis()/1000);
            Toast toast = Toast.makeText(this,"开始时间"+startTime+"结束时间"+endTime,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }
    }


    private Calendar calendartime(int year,int month,int dayOfmonth,int hour,int endminute){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(year,month,dayOfmonth,hour,endminute);
        TimeZone tz = TimeZone.getTimeZone("GMT+08:00");
        calendar1.setTimeZone(tz);
        return calendar1;
    }




    private Boolean network(){
        /** 判断网络是否连接 */
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String intentName = info.getTypeName();
            Log.i("通了没！", "当前网络名称：" + intentName);
            return true;
        } else {
            Log.i("通了没！", "没有可用网络");
            return false;
        }
    }



    private void startgather(){
        if (startfirstbool == true){
            mTraceClient.startTrace(mTrace, mTraceListener);
            mTraceClient.startGather(mTraceListener);
            startfirstbool = false;
        }else {
            mTraceClient.startTrace(mTrace, null);
            mTraceClient.startGather(null);
        }
    }

    private void stopgather(){
        if (stopfirstbool ==true) {
            mTraceClient.stopTrace(mTrace, mTraceListener);
            mTraceClient.stopGather(mTraceListener);
            stopfirstbool = false;
        }else {
            mTraceClient.stopTrace(mTrace, null);
            mTraceClient.stopGather(null);
        }
    }



    public static String getImei(Context context) {
        String imei;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(0);
            }
            else{
                imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            }
        } catch (SecurityException e) {
            imei = "myTrace";
        }
        return imei;
    }


    //监听器
    public class setOnTraceListener implements OnTraceListener{
        @Override
        public void onBindServiceCallback(int i, String s) {
            Toast.makeText(MapMainActivity.this,"绑定"+s,Toast.LENGTH_SHORT).show();
        }
        // 开启服务回调
        @Override
        public void onStartTraceCallback(int status, String message) {
            Toast.makeText(MapMainActivity.this,"开启服务"+message,Toast.LENGTH_SHORT).show();
        }
        // 停止服务回调
        @Override
        public void onStopTraceCallback(int status, String message) {
            Toast.makeText(MapMainActivity.this,"停止服务"+message,Toast.LENGTH_SHORT).show();
            saveBool = true;
            setOnTrackListener TrackListener = new setOnTrackListener();
            setprocess(savenoise);
            HistoryTrackRequest HistoryTrack = new HistoryTrackRequest(tag, serviceId, entityName, gatherbegin, gatherend, isProcessed, processOption, supplementModel,
                    sortType, com.baidu.trace.model.CoordType.bd09ll, pageIndex, pageSize);
            mTraceClient.queryHistoryTrack(HistoryTrack, TrackListener);
            while (!savenoise);
            setprocess(savenoise);
            HistoryTrackRequest HistoryTrack1 = new HistoryTrackRequest(tag, serviceId, entityName, gatherbegin, gatherend, isProcessed, processOption, supplementModel,
                    sortType, com.baidu.trace.model.CoordType.bd09ll, pageIndex, pageSize);
            mTraceClient.queryHistoryTrack(HistoryTrack1, TrackListener);
        }
        // 开启采集回调
        @Override
        public void onStartGatherCallback(int status, String message) {
            Toast.makeText(MapMainActivity.this,"开启采集"+message,Toast.LENGTH_SHORT).show();
            gatherbegin = (int) (System.currentTimeMillis() / 1000);
        }
        // 停止采集回调
        @Override
        public void onStopGatherCallback(int status, String message) {
            Toast.makeText(MapMainActivity.this,"停止采集"+message,Toast.LENGTH_SHORT).show();
            gatherend = (int) (System.currentTimeMillis() / 1000);
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {}

        @Override
        public void onInitBOSCallback(int i, String s) {}
    }



    public class setOnEntityListener extends OnEntityListener{
        @Override
        public void onEntityListCallback(EntityListResponse response){
            Toast.makeText(MapMainActivity.this,"实时位置"+response,Toast.LENGTH_SHORT).show();

        }
    }

    public class setOnTrackListener extends OnTrackListener{
        @Override
        public void onQueryCacheTrackCallback(QueryCacheTrackResponse response){
            Toast.makeText(MapMainActivity.this,"缓存"+response,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse response){
//           Toast.makeText(MapMainActivity.this,"历史"+response,Toast.LENGTH_SHORT).show();
            if(response.status == 0){
                if (response.getTotal() != 0){
                    if (saveBool == true){
                            trackData trackda = new trackData();
                            trackda.setTrackdata(response.toString());
                            trackda.setIsProcessed(savenoise);
                            trackda.save();
                            if (savenoise == false){
                                savenoise = true;
                            }else{
                                savenoise = false;
                                saveBool = false;
                            }
                    }else {
                        drawmap(response);
                    }
                }else {
                    if (saveBool == true){
                        Toast.makeText(MapMainActivity.this, "无轨迹可保存", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MapMainActivity.this, "无历史轨迹", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(MapMainActivity.this,"获取历史轨迹失败:"+response.message,Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }

            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            if (centerbool == true) {
                LatLng center = new LatLng(latitude, longitude);
                MapStatusUpdate status1 = MapStatusUpdateFactory.newLatLng(center);
                mBaiduMap.animateMapStatus(status1, 500);
                centerbool = false;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }

    //时间弹框监听器
    public class SetOnTimelistener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {
            if (beginendsign == 0) {
                beginhour = hourOfDay;
                beginminute = min;
                if (min < 10) {
                    setbtime.setText(hourOfDay + ": 0" + min);
                }else {
                    setbtime.setText(hourOfDay + ":" + min);
                }
            }else {
                endhour = hourOfDay;
                endminute = min;
                if (min < 10) {
                    setetime.setText(hourOfDay + ": 0" + min);
                }else {
                    setetime.setText(hourOfDay + ":" + min);
                }
            }
        }
    }

    public class SetOnDatelistener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker datePicker,int y,int mon,int day){
            year = y;
            month = mon;
            dayOfmonth = day;
            setdate.setText(y+ "-" + (month+1) + "-" + day);
        }
    }
}
