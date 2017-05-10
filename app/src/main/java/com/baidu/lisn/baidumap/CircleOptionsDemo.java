package com.baidu.lisn.baidumap;

import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.Stroke;

/**
 * Created by lisn on 2016/10/25.
 * 圆形覆盖物
 */
public class CircleOptionsDemo extends BaseActivity {

    @Override
    public void init() {
        onDrawMap();
    }

    private void onDrawMap() {
        /**
         * 圆心
         * 半径
         * 边框颜色、宽度
         * 圆的填充色
         */
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(NJPosition); //设置圆形覆盖物的圆心

        circleOptions.radius(100);//设置圆半径 单位米
        //设置边框宽度和边框颜色
        Stroke stroke = new Stroke(5, 0x77ff0000);
        circleOptions.stroke(stroke);

        //圆的填充色
        circleOptions.fillColor(0x7700ff00);
        //添加覆盖物
        baiduMap.addOverlay(circleOptions);
    }
}
