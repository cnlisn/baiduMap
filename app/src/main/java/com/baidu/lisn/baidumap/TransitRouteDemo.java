package com.baidu.lisn.baidumap;

import android.widget.Toast;

import com.baidu.lisn.baidumap.overlayutil.PoiOverlay;
import com.baidu.lisn.baidumap.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

/**
 * Created by lisn on 2016/10/25.
 * 乘车换乘路线搜索
 */
public class TransitRouteDemo extends BaseActivity {

    private RoutePlanSearch mRoutePlanSearch;

    @Override
    public void init() {
        transit();
    }

    private void transit() {
        //路径规划搜索接口
        mRoutePlanSearch = RoutePlanSearch.newInstance();

        final TransitRoutePlanOption transitRoutePlanOption=new TransitRoutePlanOption();
        //设置起点
        transitRoutePlanOption.from(PlanNode.withLocation(NJPosition));
        //设置终点
//        transitRoutePlanOption.to(PlanNode.withLocation(NJNPosition));
        transitRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("南京","凤翔新城"));
        //设置换乘路线规划城市，起终点中的城市将会被忽略
        transitRoutePlanOption.city("南京");
        //策略
        transitRoutePlanOption.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST); //时间优先
        //发起换乘路线规划
        mRoutePlanSearch.transitSearch(transitRoutePlanOption);

        mRoutePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override//步行
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override//换乘
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                //没有找到检索结果
                if (transitRouteResult==null||transitRouteResult.error== SearchResult.ERRORNO.RESULT_NOT_FOUND){
                    Toast.makeText(TransitRouteDemo.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //搜索结果正常返回
                if (transitRouteResult.error==SearchResult.ERRORNO.NO_ERROR){
                    //通过覆盖物进行展示
                    TransitRouteOverlay transitRouteOverlay=new TransitRouteOverlay(baiduMap);
                    transitRouteOverlay.setData(transitRouteResult.getRouteLines().get(0));
                    int size = transitRouteResult.getRouteLines().size();
                    Toast.makeText(TransitRouteDemo.this, "可供选择的路线有" + size + "条", Toast.LENGTH_SHORT).show();
                    //添加到地图上
                    transitRouteOverlay.addToMap();
                    transitRouteOverlay.zoomToSpan();
                    MyPoiOverlay myPoiOverlay=new MyPoiOverlay(baiduMap);
                    //设置点击监听
//                    baiduMap.setOnMarkerClickListener(transitRouteOverlay);//注意不用重写     onRouteNodeClick(int i)，百度处理过了
                    baiduMap.setOnMarkerClickListener(myPoiOverlay);//自己处理

                }
            }

            @Override//驾驶
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override//骑车
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

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

//            //发起详情搜索
//            PoiDetailSearchOption detailOption = new PoiDetailSearchOption();
//            //设置检索的poi的uid
//            detailOption.poiUid(clickedPoiInfo.uid);
//            mPoiSearch.searchPoiDetail(detailOption);
            return super.onPoiClick(index);
        }

    }
}