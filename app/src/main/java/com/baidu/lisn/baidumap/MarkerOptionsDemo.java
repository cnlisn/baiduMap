package com.baidu.lisn.baidumap;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

/**
 * Created by lisn on 2016/10/25.
 * <p/>
 * 标记覆盖物
 */
public class MarkerOptionsDemo extends BaseActivity {


    private View mPopView;
    private TextView mPopTitle;

    @Override
    public void init() {

        initPopView();
        /**
         * 1. 显示的 图标 2. 位置 3. title 4. 可拖拽
         */
        MarkerOptions markerOptions = new MarkerOptions();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.eat_icon);
        //创建bitmap集合添加闪烁切换的图片
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<>();
        bitmaps.add(bitmapDescriptor);
        bitmaps.add(BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo));
//        markerOptions.icon(bitmapDescriptor); //图标 添加单个图标
        markerOptions.icons(bitmaps); //图标集合
        markerOptions.period(15); //闪烁周期 值越小闪烁频率越快

        markerOptions.position(NJPosition); //位置
        markerOptions.title("我是标题");
        markerOptions.draggable(true); //可拖拽
        baiduMap.addOverlay(markerOptions); //添加覆盖物

        /**
         * 添加另外三个标记覆盖物
         */
        markerOptions = new MarkerOptions().title("向北").position(new LatLng(NJLat + 0.001, NJLnt))
                .icon(bitmapDescriptor);

        baiduMap.addOverlay(markerOptions);

        markerOptions = new MarkerOptions().title("向东").position(new LatLng(NJLat, NJLnt + 0.001))
                .icon(bitmapDescriptor);

        baiduMap.addOverlay(markerOptions);

        markerOptions = new MarkerOptions().title("向西南").position(new LatLng(NJLat - 0.001, NJLnt - 0.001))
                .icon(bitmapDescriptor);

        baiduMap.addOverlay(markerOptions);

        /**
         * 设置覆盖物点击事件
         */
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                MapViewLayoutParams newParams=new MapViewLayoutParams.Builder()
                        .width(MapViewLayoutParams.WRAP_CONTENT)
                        .height(MapViewLayoutParams.WRAP_CONTENT)
                        .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode) //布局模式mapMode
                        .position(marker.getPosition()) //显示位置 被点击覆盖物的位置
                        .build();
                mapView.updateViewLayout(mPopView,newParams);

                mPopView.setVisibility(View.VISIBLE); //显示popView
                mPopTitle.setText(marker.getTitle()); //设置显示标题
                return false;
            }
        });

        /**
         * map地图单击事件 隐藏popView
         */
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPopView.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 初始化popview，隐藏
     */
    private void initPopView() {
        //将pop.xml转为view对象，添加到mapview
        mPopView = View.inflate(this, R.layout.pop, null);
        mPopTitle = (TextView) mPopView.findViewById(R.id.title);
        /**
         * 1. popView
         * 2. popView的布局参数：宽高
         */
        MapViewLayoutParams layoutParams = new MapViewLayoutParams.Builder()
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                //布局模式  绝对、mapMode
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)//绝对----屏幕坐标
                .point(new Point(100, 100))
                .build();//布局参数，需要是当前view的父布局包下的布局参数
        mapView.addView(mPopView, layoutParams);
        //隐藏
        mPopView.setVisibility(View.GONE);
    }
}