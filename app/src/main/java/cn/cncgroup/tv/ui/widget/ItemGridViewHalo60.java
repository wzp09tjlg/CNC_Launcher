package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wu on 2015/10/16.
 */
public class ItemGridViewHalo60 extends View
{ // refer to ItemGridView,and the halo's width is 60dip.
	public ItemGridViewHalo60(Context context)
	{
		super(context);
	}

	public ItemGridViewHalo60(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ItemGridViewHalo60(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize + 60,
		        widthMode);
		int HeightMode = MeasureSpec.getMode(heightMeasureSpec);
		int HeightSize = MeasureSpec.getSize(heightMeasureSpec);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(HeightSize + 60,
		        HeightMode);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
}
