package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhang on 2015/6/3.
 */
public class SkewImageView extends ImageView
{
	public SkewImageView(Context context)
	{
		super(context);
	}

	public SkewImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		canvas.skew(0.2f, 0f);
		super.onDraw(canvas);
		canvas.restore();
	}
}
