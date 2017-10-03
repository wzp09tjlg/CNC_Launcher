package cn.cncgroup.tv.cncplayer.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import cn.cncgroup.tv.cncplayer.bean.VideoSize;

/**
 * Created by Administrator on 2015/10/13.
 * yqh
 */
public class ScaleUtil {

    private static final String TAG = "ScaleUtil";

    public static VideoSize getVideoSizeFullScreen(Context ctx) {
        VideoSize size = new VideoSize();
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        size.width = dm.widthPixels;
        size.height = dm.heightPixels;
        return size;
    }

    public static VideoSize getVideoSizeW4H3(Context ctx, int w, int h) {
        float realScal = (float) w / (float) h;
        float targetScal = 4.0F / 3.0F;
        int width;
        int height;
        if (realScal - targetScal > 0.01) {     //放大H
            width = w;
            height = (int) (w / targetScal + 0.5);
        } else if (realScal - targetScal < -0.01) {  //放大W
            width = (int) (targetScal * h + 0.5);
            height = h;
        } else {
            width = w;
            height = h;
        }
        return getMatchScreenSize(ctx, width, height);
    }

    public static VideoSize getVideoSizeW16H9(Context ctx, int w, int h) {
        float realScal = (float) w / (float) h;
        float targetScal = 16.0F / 9.0F;
        int width;
        int height;
        if (realScal - targetScal > 0.01) {     //放大H
            width = w;
            height = (int) (w / targetScal + 0.5);
        } else if (realScal - targetScal < -0.01) {  //放大W
            width = (int) (targetScal * h + 0.5);
            height = h;
        } else {
            width = w;
            height = h;
        }
        return getMatchScreenSize(ctx, width, height);
    }

    public static VideoSize getMatchScreenSize(Context ctx, int videoWidth, int videoHeight) {
        Log.i(TAG,"videoWidth, int videoHeight:"+videoWidth+","+ videoHeight+"----");
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        float widthRatio = (float) videoWidth / (float) w_screen;
        float heightRatio = (float) videoHeight / (float) h_screen;
        if (widthRatio > heightRatio) {
            videoWidth = (int) Math.ceil((float) videoWidth / widthRatio);
            videoHeight = (int) Math.ceil((float) videoHeight / widthRatio);
        } else {
            videoWidth = (int) Math.ceil((float) videoWidth / heightRatio);
            videoHeight = (int) Math.ceil((float) videoHeight / heightRatio);
        }
        VideoSize size = new VideoSize();
        size.width = videoWidth;
        size.height = videoHeight;
        return size;
    }
}
