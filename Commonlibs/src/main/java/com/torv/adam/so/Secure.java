package com.torv.adam.so;

/**
 * Created by AdamLi on 2016/11/3.
 */

public class Secure {

    public native String getAppKey();

    public native String getRedirectUrl();

    // Used to load the so library on application startup.
    static {
        System.loadLibrary("Secure");
    }
}
