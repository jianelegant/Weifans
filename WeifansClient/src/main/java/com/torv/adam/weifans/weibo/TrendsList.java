package com.torv.adam.weifans.weibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AdamLi on 2016/11/4.
 */

public class TrendsList {

    public List<Trend> trendList = new ArrayList<>();

    public static TrendsList parse(String jsonStr) {
        TrendsList trendsList = new TrendsList();
        if (jsonStr != null) {
            try {
                JSONObject trendsJson = new JSONObject(jsonStr);
                JSONObject timeJson = trendsJson.optJSONObject("trends");
                if (timeJson != null) {
                    Iterator<String> iterator = timeJson.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONArray trendsArray = timeJson.optJSONArray(key);
                        if (trendsArray != null) {
                            int lenth = trendsArray.length();
                            for (int i = 0; i < lenth; i++) {
                                JSONObject trendObj = trendsArray.optJSONObject(i);
                                if(trendObj != null) {
                                    Trend trend = new Trend();
                                    trend.text = trendObj.optString("name");
                                    trend.amount = trendObj.optString("amount");
                                    trendsList.trendList.add(trend);
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trendsList;
    }

    public static class Trend {
        public String text;
        public String amount;
    }
}
