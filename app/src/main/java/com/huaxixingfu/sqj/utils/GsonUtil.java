package com.huaxixingfu.sqj.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hjq.gson.factory.GsonFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            // 获取单例的 Gson 对象（已处理容错）
            gson = GsonFactory.getSingletonGson();
        }
    }


    private GsonUtil() {
    }


    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            List<T> list = new ArrayList<T>();
            JsonArray array =JsonParser.parseString(json).getAsJsonArray();
//            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
            return list;
//

    }

//

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        try {
            T t = null;
            if (gson != null && !TextUtils.isEmpty(gsonString)) {
                t = gson.fromJson(gsonString, cls);
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Map<String, Object> toMap(String jsonStr) {
        Map<String, Object> map = null;
        if (gson != null) {
            map = gson.fromJson(jsonStr, new TypeToken<HashMap<String, Object>>() {
            }.getType());

        }
        return map;
    }


}
