package com.baidu.lisn.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by lisn on 2016/10/25.
 * 图层demo
 */
public class LayerDemo extends Activity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean mIsTrafficEnabledd=false; //是显示交通实时路况

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_demo);
        mMapView = (MapView) findViewById(R.id.bmapView);
        Button bt1 = (Button) findViewById(R.id.bt1Click);
        Button bt2 = (Button) findViewById(R.id.bt2Click);
        Button bt3 = (Button) findViewById(R.id.bt3Click);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        initMap();
    }

    // 初始化地图
    private void initMap() {
        // 获取baiduMap
        mBaiduMap = mMapView.getMap();
        // 设置默认缩放级别
        MapStatusUpdate zoomStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(zoomStatusUpdate);
        // 设置中心点
        LatLng centerLatlng = new LatLng(32.093434,118.804586);//南京站 纬度、经度
        MapStatusUpdate centerStatusUpdate = MapStatusUpdateFactory.newLatLng(centerLatlng);
        mBaiduMap.setMapStatus(centerStatusUpdate);
        // 隐藏标尺和按钮
        // mapView.showScaleControl(false);
        // mapView.showZoomControls(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();//mapview的生命周期方法
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt1Click:
                Toast.makeText(this, "显示地图", Toast.LENGTH_SHORT).show();
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                //设置实时交通图为false
                mBaiduMap.setTrafficEnabled(false);
                break;
            case R.id.bt2Click:
                Toast.makeText(this, "显示卫星图", Toast.LENGTH_SHORT).show();
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.bt3Click:
                Toast.makeText(this, "显示实时路况", Toast.LENGTH_SHORT).show();
                if (mIsTrafficEnabledd){
                    mIsTrafficEnabledd=false;
                    mBaiduMap.setTrafficEnabled(false);
                }else {
                    mBaiduMap.setTrafficEnabled(true);
                    mIsTrafficEnabledd=true;
                }
                break;

        }
    }
}