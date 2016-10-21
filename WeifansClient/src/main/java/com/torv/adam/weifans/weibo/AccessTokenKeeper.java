/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.torv.adam.weifans.weibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_REFRESH_TOKEN    = "refresh_token";

    private static Oauth2AccessToken mToken = new Oauth2AccessToken();

    /**
     * 保存 Token 对象到 SharedPreferences。
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return;
        }

        mToken.setUid(token.getUid());
        mToken.setToken(token.getToken());
        mToken.setRefreshToken(token.getRefreshToken());
        mToken.setExpiresTime(token.getExpiresTime());

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_UID, token.getUid());
        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * @param context 应用程序上下文环境
     * @return 返回 Token 对象
     */
    public static void readAccessToken(Context context) {
        if (null == context) {
            return;
        }

        if(TextUtils.isEmpty(mToken.getUid())) {
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            mToken.setUid(pref.getString(KEY_UID, ""));
            mToken.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
            mToken.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
            mToken.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        }
    }

    public static Oauth2AccessToken getToken(){
        return mToken;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
