package com.baidu.lisn.baidumap;

import android.view.KeyEvent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

/**
 * Created by lisn on 2016/10/25.
 * 定位
 */
public class LocationDemo extends BaseActivity {
    // 定位核心类
    public LocationClient mLocationClient = null;
    // 定位结果监听
    public BDLocationListener myListener = new MyLocationListener();
    private BitmapDescriptor locationIcon;
    @Override
    public void init() {
        mLocationClient = new LocationClient(getApplicationContext());     // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    // 注册监听函数
        initLocation();
        location();

    }
    private void location() {
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //配置定位图层显示方式
        /**
         * BitmapDescriptor     customMarker
         用户自定义定位图标
         boolean     enableDirection
         是否允许显示方向信息
         MyLocationConfiguration.LocationMode    locationMode
         定位图层显示方式
         */
        locationIcon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING, //跟随模式
                true, locationIcon ));
        //开启定位
        mLocationClient.start();
    }
    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener  implements BDLocationListener{
        /**
         * 获取到位置
         * location:定位之后的经纬度
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位结果获取
            MyLocationData locationData =new MyLocationData.Builder()
                    //经纬度
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            if (locationData!=null) {
                baiduMap.setMyLocationData(locationData);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_1://普通
                baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL,
                        true, locationIcon ));
                break;
            case KeyEvent.KEYCODE_2://跟随
                baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
                        true, locationIcon ));

                break;
            case KeyEvent.KEYCODE_3://罗盘
                baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.COMPASS,
                        true, locationIcon ));

                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop(); //停止定位
        super.onDestroy();
    }
}
