package com.torv.adam.weifans;

import android.app.Application;

import com.torv.adam.weifans.util.L;

/**
 * Created by AdamLi on 2016/10/21.
 */
public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        L.setIsDebug(BuildConfig.DEBUG);
        L.d("Lifecycle");
    }
}
