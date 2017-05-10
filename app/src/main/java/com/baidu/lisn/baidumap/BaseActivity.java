package com.baidu.lisn.baidumap;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * 基类
 * 初始化地图
 * 初始化一些经纬度
 * @author wangdh
 *
 */
public abstract class BaseActivity extends Activity {
    public MapView mapView;
    public BaiduMap baiduMap;

    public double NJNLat = 31.975024;//南京南站的纬度
    public double NJNLnt = 118.804341;//南京南站的经度

    public double NJLat = 32.094316;//南京站的纬度
    public double NJLnt = 118.805325;//南京站的经度


    public double NJctLat = 32.094053;//南京长途客运总站的纬度
    public double NJctLnt = 118.806763;//南京长途客运总站的经度
    
    public LatLng NJNPosition = new LatLng(NJNLat, NJNLnt);//南京南站坐标
    public LatLng NJctPosition = new LatLng(NJctLat, NJctLnt);//南京长途客运总站坐标
    public LatLng NJPosition = new LatLng(32.093434,118.804586);//南京站

    //用final修饰可以防止子类重写
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_hello_world);
        mapView = (MapView) findViewById(R.id.bmapView);
        
        initMap();

        init();
    }

    public abstract void init();

    // 初始化地图
    private void initMap() {
        // 获取baiduMap
        baiduMap = mapView.getMap();
        // 设置默认缩放级别
        MapStatusUpdate zoomStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        baiduMap.setMapStatus(zoomStatusUpdate);
        // 设置中心点
        LatLng centerLatlng = new LatLng(32.093434,118.804586);//南京站 纬度、经度
        MapStatusUpdate centerStatusUpdate = MapStatusUpdateFactory.newLatLng(centerLatlng);
        baiduMap.setMapStatus(centerStatusUpdate);
        // 隐藏标尺和按钮
        // mapView.showScaleControl(false);
        // mapView.showZoomControls(false);
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();//mapview的生命周期方法
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
