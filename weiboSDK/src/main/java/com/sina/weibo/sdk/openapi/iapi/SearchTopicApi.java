package com.sina.weibo.sdk.openapi.iapi;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class SearchTopicApi extends AbsOpenAPI{

    private static final String API_URL = API_SERVER + "/search/topics.json";

    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     *
     * @param context
     * @param appKey
     * @param accessToken
     */
    public SearchTopicApi(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    public void searchTopic(String q, int count, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("q", q);
        params.put("count", count);
        requestAsync(API_URL, params, HTTPMETHOD_GET, listener);
    }
}
