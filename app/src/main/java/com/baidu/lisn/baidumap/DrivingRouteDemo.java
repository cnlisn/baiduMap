package com.baidu.lisn.baidumap;

import android.widget.Toast;

import com.baidu.lisn.baidumap.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisn on 2016/10/25.
 * 驾车路线搜索
 */
public class DrivingRouteDemo extends BaseActivity {

    @Override
    public void init() {
        driving();
    }

    private void driving() {
        RoutePlanSearch routePlanSearch=RoutePlanSearch.newInstance();
        //发起请求
        DrivingRoutePlanOption drivingOption= new DrivingRoutePlanOption();
        //起点、终点、途经点
        drivingOption.from(PlanNode.withLocation(NJPosition));
        drivingOption.to(PlanNode.withLocation(NJNPosition));

        List<PlanNode> planNodes=new ArrayList<>();
        //途经点：城市、地名 ,途径点可以多个添加到集合中
        planNodes.add(PlanNode.withCityNameAndPlaceName("南京","动物园"));
        drivingOption.passBy(planNodes);
        //策略
        drivingOption.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST); //时间优先
        routePlanSearch.drivingSearch(drivingOption);

        //设置结果监听
        routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override//步行
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override//换乘
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override//驾车 drivingRouteResult：包含多条路线
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //展示 --通过覆盖物
                DrivingRouteOverlay drivingRouteOverlay=new DrivingRouteOverlay(baiduMap);
                // 没有找到检索结果
                if (drivingRouteResult == null
                        || drivingRouteResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(DrivingRouteDemo.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//                    baiduMap.clear();

                    //设置数据
                    drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));
                    int size = drivingRouteResult.getRouteLines().size();
                    Toast.makeText(DrivingRouteDemo.this, "可供选择的路线有" + size + "条", Toast.LENGTH_SHORT).show();
                    //添加到地图上
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    //设置点击监听
                    baiduMap.setOnMarkerClickListener(drivingRouteOverlay);//注意不用重写     onRouteNodeClick(int i)，百度处理过了
                }
            }

            @Override//自行车
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }
}
