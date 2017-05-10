package com.baidu.lisn.baidumap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String[] objects = new String[] { "hello world", "图层", "圆形覆盖物", "展示文字", "marker覆盖物", "矩形范围内搜索",
            "圆形区域", "全城搜索", "驾车路线", "步行路线", "公交换乘", "我的位置" };
    private static Class[] clazzs = new Class[] { HelloWorld.class, LayerDemo.class
            ,CircleOptionsDemo.class,TextOptionsDemo.class,MarkerOptionsDemo.class
            ,SearchInBoundDemo.class,SearchNearByDemo.class,SearchInCityDemo.class
            ,DrivingRouteDemo.class,WalkingRouteDemo.class,TransitRouteDemo.class
            ,LocationDemo.class
    };


    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.list);
        //设置适配器
        mAdapter = new ArrayAdapter<>(getApplication(), R.layout.item, objects);
        list.setAdapter(mAdapter);
        //设置点击事件跳转到对应界面
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),clazzs[position]);
                startActivity(intent);
            }
        });

    }

    /*private void poi() {
        //一、 创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //二、创建POI检索监听者
        OnGetPoiSearchResultListener PoiListener = new OnGetPoiSearchResultListener(){

            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                //获取POI检索结果
                List<PoiInfo> allAddr = poiResult.getAllPoi();
                for (PoiInfo p: allAddr) {
                    Log.d("MainActivity", "p.name--->" + p.name +"p.phoneNum" + p.phoneNum +" -->p.address:" + p.address + "p.location" + p.location);
                }
            }

            @Override
            //详细结果
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            //室内结果
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };

        //第三步，设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(PoiListener);

        //第四步，发起检索请求
        mPoiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("ktv").pageNum(10));

        //第五步，释放POI检索实例；
        mPoiSearch.destroy();
    }*/

}
