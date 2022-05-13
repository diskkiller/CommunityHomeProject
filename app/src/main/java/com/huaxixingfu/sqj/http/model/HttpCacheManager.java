package com.huaxixingfu.sqj.http.model;

import androidx.annotation.NonNull;

import com.diskkiller.http.config.IRequestApi;
import com.diskkiller.http.request.HttpRequest;
import com.hjq.gson.factory.GsonFactory;
import com.tencent.mmkv.MMKV;

/**
 *    desc   : Http 缓存管理器
 */
public final class HttpCacheManager {

   private volatile static MMKV sMmkv;

   /**
    * 获取单例的 MMKV 实例
    */
   public static MMKV getMmkv() {
      if(sMmkv == null) {
         synchronized (RequestHandler.class) {
            if (sMmkv == null) {
               sMmkv = MMKV.mmkvWithID("http_cache_id");
            }
         }
      }
      return sMmkv;
   }

   /**
    * 生成缓存的 key
    */
   public static String generateCacheKey(@NonNull HttpRequest<?> httpRequest) {
      IRequestApi requestApi = httpRequest.getRequestApi();
      return "用户 id" + "\n" + requestApi.getApi() + "\n" + GsonFactory.getSingletonGson().toJson(requestApi);
   }
}