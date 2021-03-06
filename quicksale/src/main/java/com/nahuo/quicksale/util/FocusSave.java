package com.nahuo.quicksale.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/4/12.
 */

public class FocusSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public FocusSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        editor = preferences.edit();
    }

    public void setFocus(String tag, boolean focus) {
        editor.clear();
        editor.putBoolean(tag, focus);
        editor.commit();
    }

    public boolean getFocus(String tag) {
        boolean focus = preferences.getBoolean(tag, false);
        return focus;
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }
}
