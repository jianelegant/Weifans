package com.torv.adam.weifans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class NaviActivity extends Activity implements View.OnClickListener{

    public static final String TOPIC_KEY = "type";
    public static final int TOPIC_MINE = 0;
    public static final int TOPIC_HOUR = 1;
    public static final int TOPIC_DAY = 2;
    public static final int TOPIC_WEEK= 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        findViewById(R.id.id_my_topic).setOnClickListener(this);
        findViewById(R.id.id_hour_topic).setOnClickListener(this);
        findViewById(R.id.id_daily_topic).setOnClickListener(this);
        findViewById(R.id.id_week_topic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(NaviActivity.this, TrendsAcitivity.class);
        if(v.getId() == R.id.id_my_topic) {
            intent.putExtra(TOPIC_KEY, TOPIC_MINE);
        } else if(v.getId() == R.id.id_hour_topic) {
            intent.putExtra(TOPIC_KEY, TOPIC_HOUR);
        } else if(v.getId() == R.id.id_daily_topic) {
            intent.putExtra(TOPIC_KEY, TOPIC_DAY);
        } else if(v.getId() == R.id.id_week_topic) {
            intent.putExtra(TOPIC_KEY, TOPIC_WEEK);
        }
        startActivity(intent);
    }
}
