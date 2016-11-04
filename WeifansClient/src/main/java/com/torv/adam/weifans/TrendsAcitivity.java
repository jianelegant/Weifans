package com.torv.adam.weifans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.TrendsAPI;
import com.torv.adam.commonlibs.L;
import com.torv.adam.weifans.util.Constant;
import com.torv.adam.weifans.weibo.AccessTokenKeeper;
import com.torv.adam.weifans.weibo.TrendsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class TrendsAcitivity extends Activity{

    private static final java.lang.String TAG = TrendsAcitivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    TrendsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(TrendsAcitivity.this));
        mAdapter = new TrendsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Oauth2AccessToken token = AccessTokenKeeper.getToken();
        new TrendsAPI(TrendsAcitivity.this, Constant.APP_KEY, token).hourly(true, mTrendsRequestListener);
    }

    class TrendsAdapter extends RecyclerView.Adapter<TrendViewHolder> {

        List<TrendsList.Trend> treads = new ArrayList<>();

        public void setData(List<TrendsList.Trend> treadList) {
            if(treadList != null) {
                treads.clear();
                treads.addAll(treadList);
            }
        }

        @Override
        public TrendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(TrendsAcitivity.this);
            View view = inflater.inflate(R.layout.trends_item, null);
            TrendViewHolder viewHolder = new TrendViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TrendViewHolder holder, int position) {
            final TrendsList.Trend trend = treads.get(position);
            if(trend != null) {
                holder.text.setText(trend.text);
                holder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TrendsAcitivity.this, StatusListActivity.class);
                        intent.putExtra("topic", trend.text);
                        startActivity(intent);
                    }
                });
                holder.amount.setText(trend.amount);
            }
        }

        @Override
        public int getItemCount() {
            return treads.size();
        }
    }

    static class TrendViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView amount;

        public TrendViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.id_trend_text);
            amount = (TextView) itemView.findViewById(R.id.id_trend_amount);
        }
    }

    private RequestListener mTrendsRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            L.d(TAG, s);
            TrendsList trendsList = TrendsList.parse(s);
            if(trendsList != null) {
                mAdapter.setData(trendsList.trendList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            L.e(TAG, e.getMessage());
        }
    };
}
