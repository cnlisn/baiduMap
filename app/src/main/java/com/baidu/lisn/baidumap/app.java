package com.baidu.lisn.baidumap;

import android.app.Application;
import android.content.Context;

/**
 * Created by lisn on 2016/10/27.
 */
public class app extends Application {

    public static app instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;// 赋值

    }

    /**
     * 获取上下文
     * @return
     */
    public static Context getInstance() {
        return instance;
    }
}
