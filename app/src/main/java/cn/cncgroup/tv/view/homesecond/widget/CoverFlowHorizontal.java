package cn.cncgroup.tv.view.homesecond.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;

import cn.cncgroup.tv.CApplication;

/**
 * Created by zhang on 2015/9/2. 横向显示图片控件，可倾斜左右两边图片
 */
public class CoverFlowHorizontal extends BaseCoverFlow {

    public CoverFlowHorizontal(Context context, AttributeSet attrs) {
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
        final int childCenter = child.getLeft() + childWidth / 2;
        final int actionDistance = (int) ((coverFlowWidth + childWidth) / 2.0f);
        final float effectsAmount = Math.min(1.0f, Math.max(-1.0f, (1.0f / actionDistance) * (childCenter - coverFlowCenter)));
        t.clear();
        t.setTransformationType(Transformation.TYPE_BOTH);
        final float alphaAmount = (0.9f - 1) * Math.abs(effectsAmount) + 1;
        t.setAlpha(alphaAmount);// 透明度

            //设置旋转角度
            int isangle = CApplication.CoverFlowHorizontal_angle_inclination;
            int rotationAngle = (int) (-effectsAmount * 75);
            if (rotationAngle > isangle) {
                rotationAngle = isangle;
            } else if (rotationAngle < -isangle) {
                rotationAngle = -isangle;
            }
            child.setRotationY(rotationAngle);


        final float zoomAmount = (0.3f - 1) * Math.abs(effectsAmount) + 1;
        child.setScaleX(zoomAmount);// 缩放.
        child.setScaleY(zoomAmount);
        return true;
    }
}
