package com.baidu.lisn.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.lisn.baidumap.overlayutil.PoiOverlay;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

/**
 * Created by lisn on 2016/10/25.
 * 周边搜索--圆形搜索
 */
public class SearchNearByDemo extends BaseActivity implements View.OnClickListener {

    private PoiSearch mPoiSearch;

    private int pageNum = 0;

    private int mTotalPage; //查询结果总页数

    @Override
    public void init() {
        //添加上一页 下一页按钮
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_container);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        Button bt1=new Button(this);
        bt1.setText("上一页");
        bt1.setOnClickListener(this);
        bt1.setId(R.id.bt11);
        Button bt2=new Button(this);
        bt2.setText("下一页");
        bt2.setOnClickListener(this);
        bt2.setId(R.id.bt22);
        ll.addView(bt1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,-2));
        ll.addView(bt2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,-2));

        rl.addView(ll, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,-2));

        mPoiSearch = PoiSearch.newInstance();
        search();
    }

    /**
     * 查询
     */
    private void search() {

        PoiNearbySearchOption NearbySearch=new PoiNearbySearchOption();
        NearbySearch.keyword("加油站");
        NearbySearch.location(NJPosition); //圆心
        NearbySearch.radius(5000);//单位m
//        NearbySearch.pageCapacity(10); //查询结果每页容量 默认是10个
        NearbySearch.pageNum(pageNum);//查询第几页
        //发起圆形搜索
        mPoiSearch.searchNearby(NearbySearch);

        //获取结果监听
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            /**
             * 获取所有结果
             */
            @Override
            public void onGetPoiResult(PoiResult result) {
//                baiduMap.clear();//添加之前，先清空所有的覆盖物
//                //展示结果，PoiOverlay展示
//                MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
//                //设置数据
//                poiOverlay.setData(result);
//
//                poiOverlay.addToMap();//baidumap.addOverlay(); 将所有的overlay添加到地图上
//                poiOverlay.zoomToSpan();//缩放到适合的视野
//
//                //给poi覆盖物添加点击事件
//                baiduMap.setOnMarkerClickListener(poiOverlay);

                // 获取总分页数
                mTotalPage = result.getTotalPageNum();

                // 没有找到检索结果
                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(SearchNearByDemo.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    baiduMap.clear();
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
                    poiOverlay.setData(result);// 设置POI数据
                    baiduMap.setOnMarkerClickListener(poiOverlay);
                    poiOverlay.addToMap();// 将所有的overlay添加到地图上
                    poiOverlay.zoomToSpan(); //缩放到适合的视野

                    int totalPage = result.getTotalPageNum();// 获取总分页数
                    Toast.makeText(
                            SearchNearByDemo.this,
                            "总共查到" + result.getTotalPoiNum() + "个兴趣点, 分为"
                                    + totalPage + "页", Toast.LENGTH_SHORT).show();

                }
            }

            /**
             * 获取单个poi的详情
             */
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                Toast.makeText(getApplicationContext(),
                        "综合评价：" + poiDetailResult.getOverallRating(), Toast.LENGTH_SHORT).show();
            }

            @Override //室内查询结果
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt11:
                //翻页上一页
                if (pageNum!=0){
                    pageNum--;
                    search();
                }else {
                    Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt22:
                //翻页下一页
                if (pageNum<mTotalPage) {
                    pageNum++;
                    search();
                }else {
                    Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
