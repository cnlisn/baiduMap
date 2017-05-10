package com.baidu.lisn.baidumap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;


/**
 * Created by lisn on 2016/10/25.
 */
public class HelloWorld extends Activity{
    private MapView mMapView;
    private MyBroadcastReceiver mReceiver;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hello_world);

        //校验结果，百度地图通过广播告诉开发者
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter();
        //添加接收什么广播
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR); //网络错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE); //key、错误广播
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR); //权限错误广播
        //注册广播接收者
        registerReceiver(mReceiver,filter );

        initMap();

    }

    /**
     * 初始化地图
     */
    private void initMap() {
        //修改地图中心点(通过BaiduMap)

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        BaiduMap map = mMapView.getMap();
        //纬度经度
        MapStatusUpdate newMapStatusUpdate= MapStatusUpdateFactory.newLatLng(new LatLng(31.976042,118.765519));
        map.setMapStatus(newMapStatusUpdate);
    }

    /**
     * 自定义广播接收者
     */
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(action)){
                Toast.makeText(HelloWorld.this, "网络错误", Toast.LENGTH_SHORT).show();
            }else if (SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(action)){
                Toast.makeText(HelloWorld.this, "权限错误", Toast.LENGTH_SHORT).show();
            }else if (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE.equals(action)){
                Toast.makeText(HelloWorld.this, "KEY错误", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if (mReceiver!=null)
        unregisterReceiver(mReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_1://位移
                //传智总部
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(40.065796,116.349868)));
                break;
            case KeyEvent.KEYCODE_2://变大 （缩放级别）
                //一级一级变化
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case KeyEvent.KEYCODE_3://变小
                //一级一级变化
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case KeyEvent.KEYCODE_4://平角角度  （0-360）
                //新的平角角度
                float newRotate = mBaiduMap.getMapStatus().rotate + 30;
                System.out.println("新的角度："+newRotate);
            /*
             * 改变角度，是通过改变新状态
             */
                MapStatus rotateMapStatus = new MapStatus.Builder()
                        .rotate(newRotate)
                        .build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(rotateMapStatus));
                break;
            case KeyEvent.KEYCODE_5://俯角角度 (0 到 -45)
                //新的俯角角度
                float newOverLook = mBaiduMap.getMapStatus().overlook - 5;
                System.out.println("新的角度："+newOverLook);
            /*
             * 改变角度，是通过改变新状态
             */
                MapStatus overLookMapStatus = new MapStatus.Builder()
                        .overlook(newOverLook)
                        .build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(overLookMapStatus));

                break;
            case KeyEvent.KEYCODE_6://隐藏标尺、缩放控件
                mMapView.showScaleControl(false);//标尺
                mMapView.showZoomControls(false);//缩放控件
                break;
            case KeyEvent.KEYCODE_7://显示指南针
                mMapView.showScaleControl(true);//标尺
                mMapView.showZoomControls(true);//缩放控件

                mBaiduMap.getUiSettings().setCompassEnabled(true);//显示指南针
//                mBaiduMap.getUiSettings().setCompassPosition(new Point(100, 100));//屏幕上的位置、xy坐标。不是经纬度


                break;
            case KeyEvent.KEYCODE_8://隐藏指南针
                mBaiduMap.getUiSettings().setCompassEnabled(false);
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
