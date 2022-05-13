package com.huaxixingfu.sqj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huaxixingfu.sqj.R;

public class ImageLoaderUtils {

    public static boolean assertValidRequest(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !isDestroy(activity);
        } else if (context instanceof ContextWrapper){
            ContextWrapper contextWrapper = (ContextWrapper) context;
            if (contextWrapper.getBaseContext() instanceof Activity){
                Activity activity = (Activity) contextWrapper.getBaseContext();
                return !isDestroy(activity);
            }
        }
        return true;
    }

    private static boolean isDestroy(Activity activity) {
        if (activity == null) {
            return true;
        }
        return activity.isFinishing() || activity.isDestroyed();
    }
    /**
     *
     */
    public static void loadBurGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .placeholder(R.drawable.picture_image_placeholder)
                .apply(RequestOptions.bitmapTransform(new GlideBlurTransformation(context)))
                .into(imageView);
    }
}
