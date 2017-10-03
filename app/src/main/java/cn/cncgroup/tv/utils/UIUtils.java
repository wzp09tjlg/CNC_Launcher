package cn.cncgroup.tv.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

/**
 * Created by Administrator on 2015/4/22.
 */
public class UIUtils {
    /**
     * 获取屏幕宽度，单位px
     * @return 屏幕宽度，单位px
     */
    public static int getScreenWidth(Resources resources){

        DisplayMetrics dm =resources.getDisplayMetrics();
        int w_screen = dm.widthPixels;
//        int h_screen = dm.heightPixels;
        return w_screen;
    }

    public static int getScreenHeight(Resources resources){

        DisplayMetrics dm =resources.getDisplayMetrics();
//        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return h_screen;
    }

    /**
     * 将屏幕等分
     * @param resources
     * @param col 分的块数
     * @return 每列宽度
     */
    public static int splitScreen(Resources resources, int col){
        return getScreenWidth(resources)/col;
    }

    /**
     * 等分屏幕
     * @param resources
     * @param row
     * @return 每行的高度
     */
    public static int splitScreenHeight(Resources resources, int row){
        return getScreenHeight(resources)/row;
    }

//    public static void scalImage(){
//        Uri uri = Uri.parse("file:///mnt/sdcard/MyApp/myfile.jpg");
//        int width = 50, height = 50;
//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setResizeOptions(new ResizeOptions(width, height))
//                .build();
//        PipelineDraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeView.getController())
//                .setImageRequest(request)
//                .build();
//        mSimpleDraweeView.setController(controller);
//    }

    /**
     * 获取 density
     * @param contect
     * @return
     */
    public static double getDescni(Context contect){
		DisplayMetrics dm;
        dm = contect.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static void setFrescoImageRequests(SimpleDraweeView view, Uri[] uriArray) {
        ImageRequest[] requests = new ImageRequest[uriArray.length];
        for(int index = 0; index < uriArray.length; index++) {
            requests[index] = ImageRequest.fromUri(uriArray[index]);
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setFirstAvailableImageRequests(requests)
                .setOldController(view.getController())
                .build();
        view.setController(controller);
    }
}
