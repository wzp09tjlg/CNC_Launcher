package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;

import cn.cncgroup.tv.CApplication;

/**
 * Created by zhang on 2015/9/2. 横向显示图片控件，可倾斜左右两边图片
 */
public class CoverFlowVertical extends BaseCoverFlow {
    private String TAG="CoverFlowVertical";
    public CoverFlowVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            child.invalidate();
        }
        final int coverFlowWidth = this.getWidth();
        final int coverFlowCenter = coverFlowWidth / 2;
        final int childWidth = child.getWidth();
        final int childHeight = child.getHeight();
        final int childCenter = (int) (child.getLeft() + childWidth / 2.0f);
        final int actionDistance = (int) ((coverFlowWidth + childWidth) / 2.0f);
        final float effectsAmount = Math.min(1.0f, Math.max(-1.0f,
                (1.0f / actionDistance) * (childCenter - coverFlowCenter)));
//        Log.i(TAG,"child="+child+"childCenter="+childCenter+"--actionDistance="+actionDistance+"---effectsAmount="+effectsAmount);
        t.clear();
        t.setTransformationType(Transformation.TYPE_BOTH);
        final float alphaAmount = (0.9f - 1) * Math.abs(effectsAmount) + 1;
        t.setAlpha(alphaAmount);// 透明度
            //设置倾斜角度
            int isangle = CApplication.CoverFlowVertical_angle_inclination;
            int rotationAngle = (int) (-effectsAmount * 90);
            if (rotationAngle > isangle) {
                rotationAngle = isangle;
            } else if (rotationAngle < -isangle) {
                rotationAngle = -isangle;
            }
            child.setRotationY(rotationAngle);

        // 缩放.
        float zoomAmount = (0.4f - 1) * Math.abs(effectsAmount) + 1;
        if (zoomAmount < 0.8) {
            zoomAmount = (float) 0.8;
        }
        child.setScaleY(zoomAmount);
        if (zoomAmount < 1.0) {
            zoomAmount = (float) 1.0;
        }
        child.setScaleX(zoomAmount);
        return true;
    }
}
