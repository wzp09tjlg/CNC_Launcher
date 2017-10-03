package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.jar.Attributes;

import cn.cncgroup.tv.R;

/**
 * Created by futuo on 2015/10/12.
 */
public class ItemGridview extends View {

    public ItemGridview(Context context) {
        super(context);
    }

    public ItemGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemGridview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize+50,widthMode);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int HeightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(HeightSize+50,HeightMode);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
