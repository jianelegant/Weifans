package com.sina.weibo.sdk.openapi.iapi;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class PublicTimelineAPI extends AbsOpenAPI{

    private static final String API_URL = API_SERVER + "/statuses/public_timeline.json";
    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     *
     * @param context
     * @param appKey
     * @param accessToken
     */
    public PublicTimelineAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    public void getPublicTimeline(RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        requestAsync(API_URL, params, HTTPMETHOD_GET, listener);
    }
}
