package com.baidu.lisn.baidumap;

import com.baidu.mapapi.map.TextOptions;

/**
 * Created by lisn on 2016/10/25.
 *
 * 添加文字覆盖物
 */
public class TextOptionsDemo extends BaseActivity {


    @Override
    public void init() {
        /**
         * 添加文字覆盖物
         * 文字内容
         * 文字大小
         * 文字颜色
         * 文字位置
         * 旋转角度
         */
        TextOptions textOptions=new TextOptions();
        textOptions.text("百度地图——李珊制作");
        textOptions.fontSize(30); //单位px
        textOptions.fontColor(0x77ff0000);
        textOptions.position(NJPosition);
        textOptions.rotate(30);
        baiduMap.addOverlay(textOptions);
    }
}