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

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.iapi.SearchTopicApi;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.torv.adam.commonlibs.L;
import com.torv.adam.weifans.util.Constant;
import com.torv.adam.weifans.weibo.AccessTokenKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class StatusListActivity extends Activity{

    private static final java.lang.String TAG = StatusListActivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    StatusAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuslist);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StatusListActivity.this));
        mAdapter = new StatusAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            String topic = intent.getStringExtra("topic");
            if(topic != null) {
                new SearchTopicApi(StatusListActivity.this, Constant.APP_KEY, AccessTokenKeeper.getToken()).searchTopic(topic, 50, mPublicTimelineRequestListener);
            }
        }
    }

    class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {

        List<Status> mStatusList = new ArrayList<>();

        public void setData(List<Status> dataList) {
            if(dataList != null) {
                mStatusList.clear();
                mStatusList.addAll(dataList);
            }
        }

        @Override
        public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(StatusListActivity.this);
            View view = inflater.inflate(R.layout.status_item, null);
            StatusViewHolder viewHolder = new StatusViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(StatusViewHolder holder, int position) {
            Status status = mStatusList.get(position);
            holder.header.setImageURI(status.user.profile_image_url);
            holder.name.setText(status.user.name);
            holder.time.setText(status.created_at);
            holder.status.setText(status.text);
        }

        @Override
        public int getItemCount() {
            return mStatusList.size();
        }
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView header;
        public TextView name;
        public TextView time;
        public TextView status;

        public StatusViewHolder(View itemView) {
            super(itemView);
            header = (SimpleDraweeView) itemView.findViewById(R.id.id_header);
            name = (TextView) itemView.findViewById(R.id.id_name);
            time = (TextView) itemView.findViewById(R.id.id_time);
            status = (TextView) itemView.findViewById(R.id.id_status);
        }
    }

    private RequestListener mPublicTimelineRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            L.d(TAG, s);
            if(s != null) {
                StatusList statusList = StatusList.parse(s);
                mAdapter.setData(statusList.statusList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            L.d(TAG, e.getMessage());
        }
    };
}
