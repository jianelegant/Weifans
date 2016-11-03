package com.torv.adam.weifans;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.torv.adam.commonlibs.L;
import com.torv.adam.so.Secure;
import com.torv.adam.weifans.util.Constant;
import com.torv.adam.weifans.weibo.AccessTokenKeeper;

/**
 * Created by AdamLi on 2016/10/21.
 */
public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        L.setIsDebug(BuildConfig.DEBUG);
        L.d("Lifecycle");

        Fresco.initialize(this);
        AccessTokenKeeper.readAccessToken(this);
        initKey();
    }

    private void initKey() {
        Secure secure = new Secure();
        Constant.APP_KEY = secure.getAppKey();
        Constant.REDIRECT_URL = secure.getRedirectUrl();
    }
}
