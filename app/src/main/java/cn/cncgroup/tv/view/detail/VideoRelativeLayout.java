package cn.cncgroup.tv.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/11/18.
 */
public class VideoRelativeLayout extends RelativeLayout {

    public VideoRelativeLayout(Context context) {
        this(context, null);
    }

    public VideoRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View findFocus() {
        return this;
    }
}
