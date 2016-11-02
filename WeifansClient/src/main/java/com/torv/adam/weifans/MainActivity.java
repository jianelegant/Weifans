package com.torv.adam.weifans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.FollowAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.torv.adam.commonlibs.L;
import com.torv.adam.weifans.util.Constant;
import com.torv.adam.weifans.weibo.AccessTokenKeeper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "fuckweibo";

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    private Button mAuthBtn;
    private SimpleDraweeView mProfileView;
    private TextView mNameView;
    private Button mFollowBtn;

    UsersAPI mUsersAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.d(TAG, "Lifecycle");

        mAuthInfo = new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
        mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);

        mAuthBtn = (Button) findViewById(R.id.id_auth_btn);
        mProfileView = (SimpleDraweeView) findViewById(R.id.id_profile_view);
        mNameView = (TextView) findViewById(R.id.id_name_view);
        mFollowBtn = (Button) findViewById(R.id.id_follow_btn);

        mAccessToken = AccessTokenKeeper.getToken();
        if(mAccessToken.isSessionValid()) {
            mAuthBtn.setVisibility(View.GONE);
            mUsersAPI = new UsersAPI(this, Constant.APP_KEY, mAccessToken);
            L.d(TAG, "uid = " + mAccessToken.getUid());
            mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), mUserInfoRequestListener);
        }else {
            mAuthBtn.setVisibility(View.VISIBLE);
        }
        mAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });

        mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowAPI followAPI = new FollowAPI(MainActivity.this, Constant.APP_KEY, mAccessToken);
                followAPI.follow("1712539910", new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        L.d(TAG, s);
                        if(!TextUtils.isEmpty(s)) {
                            User user = User.parse(s);
                            if(user != null) {
                                mNameView.setText(user.screen_name);
                                mProfileView.setImageURI(user.profile_image_url);
                            } else {
                                L.e(TAG, "user is null");
                            }
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        L.e(TAG, e.getMessage());
                    }
                });
            }
        });
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            L.d(TAG, "onComplete, phoneNum = " + phoneNum + ", valid : " + mAccessToken.isSessionValid());
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
//                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                mUsersAPI = new UsersAPI(MainActivity.this, Constant.APP_KEY, mAccessToken);
                mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), mUserInfoRequestListener);
//                Toast.makeText(MainActivity.this,
//                        "get token,"+phoneNum, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "fail:";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
//                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            L.d(TAG, "onCancel");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            L.d(TAG, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d(TAG, "" + data);
    }

    private RequestListener mUserInfoRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            L.d(TAG, s);
            if(!TextUtils.isEmpty(s)) {
                User user = User.parse(s);
                if(user != null) {
                    mNameView.setText(user.screen_name);
                    mProfileView.setImageURI(user.profile_image_url);
                } else {
                    L.e(TAG, "user is null");
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            L.e(TAG, e.getMessage());
        }
    };
}
