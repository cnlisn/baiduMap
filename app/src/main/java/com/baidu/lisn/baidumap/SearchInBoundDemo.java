package com.baidu.lisn.baidumap;

import android.widget.Toast;


import com.baidu.lisn.baidumap.overlayutil.PoiOverlay;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

/**
 * Created by lisn on 2016/10/25.
 * 范围内查找
 */
public class SearchInBoundDemo extends BaseActivity {

    private PoiSearch mPoiSearch;

    @Override
    public void init() {
        mPoiSearch = PoiSearch.newInstance();

        PoiBoundSearchOption boundSearch = new PoiBoundSearchOption();
        //查找关键字
        boundSearch.keyword("加油站");
        //查找范围 左下脚 118.797971,32.090424， 右上脚 118.813062,32.10009
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(32.090424,118.797971))
                .include(new LatLng(32.10009,118.813062))
//                .include(new LatLng(34.789233, 113.550675))//包含
//                .include(new LatLng(34.790645, 113.558995))
                .build();
        boundSearch.bound(latLngBounds);
        //范围内查找 发起范围检索
        mPoiSearch.searchInBound(boundSearch);

        //获取结果监听
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override //获取所有查询结果
            public void onGetPoiResult(PoiResult poiResult) {

                // 没有找到检索结果
                if (poiResult == null
                        || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(SearchInBoundDemo.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    baiduMap.clear();
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
                    poiOverlay.setData(poiResult);// 设置POI数据
                    baiduMap.setOnMarkerClickListener(poiOverlay);
                    poiOverlay.addToMap();// 将所有的overlay添加到地图上
                    poiOverlay.zoomToSpan(); //缩放到适合的视野

                    int totalPage = poiResult.getTotalPageNum();// 获取总分页数
                    Toast.makeText(
                            SearchInBoundDemo.this,
                            "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
                                    + totalPage + "页", Toast.LENGTH_SHORT).show();

                }

                //下边这一段和上边内容一样 上边添加了搜索结果判断

//                //展示结果，PoiOverlay展示
//                MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
//                //设置数据
//                poiOverlay.setData(poiResult);
//
//                poiOverlay.addToMap();//baidumap.addOverlay(); 将所有的overlay添加到地图上
//                poiOverlay.zoomToSpan();//缩放到适合的视野
//
//                //给poi覆盖物添加点击事件
//                baiduMap.setOnMarkerClickListener(poiOverlay);
            }

            @Override //获取单个poi的详情
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                Toast.makeText(getApplicationContext(),
                        "综合评价："+poiDetailResult.getOverallRating(), Toast.LENGTH_SHORT).show();
            }

            @Override //获取室内搜寻结果
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }


    class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }

        /**
         * 覆写此方法以改变默认点击行为
         * <p/>
         * index 点击哪一个 PoiInfo
         */
        @Override
        public boolean onPoiClick(int index) {
            //获取所有的poi数据
            PoiResult poiResult = getPoiResult();
            List<PoiInfo> allPoi = poiResult.getAllPoi();
            PoiInfo clickedPoiInfo = allPoi.get(index);
            Toast.makeText(getApplicationContext(),
                    clickedPoiInfo.name, Toast.LENGTH_SHORT).show();

            //发起详情搜索
            PoiDetailSearchOption detailOption = new PoiDetailSearchOption();
            //设置检索的poi的uid
            detailOption.poiUid(clickedPoiInfo.uid);
            mPoiSearch.searchPoiDetail(detailOption);
            return super.onPoiClick(index);
        }

    }
}