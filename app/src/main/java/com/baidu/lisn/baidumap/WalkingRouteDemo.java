package com.baidu.lisn.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lisn.baidumap.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * Created by lisn on 2016/10/25.
 * 步行线路搜索
 */
public class WalkingRouteDemo extends BaseActivity {

    @Override
    public void init() {
        walking();
    }

    private void walking() {
        //路线方案搜索
        RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();
        //发起请求
        WalkingRoutePlanOption walkingRoutePlanOption=new WalkingRoutePlanOption();
        walkingRoutePlanOption.from(PlanNode.withLocation(NJPosition));
//        walkingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("南京","南京长途客运总站"));
        walkingRoutePlanOption.to(PlanNode.withLocation(NJctPosition));
        //步行搜索
        routePlanSearch.walkingSearch(walkingRoutePlanOption);
        //设置结果监听
        routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override//步行
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                //展示 -- 通过覆盖物
                WalkingRouteOverlay walkingRouteOverlay  = new WalkingRouteOverlay(baiduMap);
                // 没有找到检索结果
                if (walkingRouteResult == null
                        || walkingRouteResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(WalkingRouteDemo.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    //设置数据
                    walkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(0));
                    int size = walkingRouteResult.getRouteLines().size();
                    Toast.makeText(WalkingRouteDemo.this, "可供选择的路线有" + size + "条", Toast.LENGTH_SHORT).show();

                    //添加到地图上
                    walkingRouteOverlay.addToMap();
                    walkingRouteOverlay.zoomToSpan();
                    //设置点击监听
                    baiduMap.setOnMarkerClickListener(walkingRouteOverlay);//注意不用重写     onRouteNodeClick(int i)，百度处理过了
                }
            }

            @Override//换乘
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override//驾车 drivingRouteResult：包含多条路线
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override//自行车
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }
}
