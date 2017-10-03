package cn.cncgroup.tv.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;

import cn.cncgroup.tv.R;

/**
 * 绘制倒影
 */
public class ImageReflectedUtil {
    public static Bitmap createReflectedImage(View view) {
        return createReflectedImage(view, 1f);
    }

    public static Bitmap createReflectedImage(View view, float s) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return createReflectedImage(bitmap, (int) (s * view.getHeight()));
    }

    public static Bitmap createHomeReflectedImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return createReflectedImage(bitmap, 100);
    }

    public static Bitmap createReflectedImage(Bitmap bitmap, int imageHeight) {
        if (bitmap == null || bitmap.isRecycled())
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        if (bitmap.isRecycled()) {
            return null;
        }
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height - imageHeight, width, imageHeight, matrix, false);
        Canvas canvas = new Canvas(reflectionImage);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, reflectionImage.getHeight(), 0x33ffffff, 0x00ffffff, TileMode.MIRROR);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, 0, width, reflectionImage.getHeight(), paint);
        bitmap.recycle();
        return reflectionImage;
    }
}
