package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wuzhenpeng on 2015/7/2.
 */
public class VideoVerticalGridView extends WrapperVerticalGridView // VerticalGridView
{
	private boolean isFling = false;

	public VideoVerticalGridView(Context context)
	{
		super(context);
	}

	public VideoVerticalGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void onScrollStateChanged(int state)
	{
		super.onScrollStateChanged(state);
		if (state == RecyclerView.SCROLL_STATE_SETTLING)
			isFling = true;
		else
			isFling = false;
	}

	@Override
	public View focusSearch(View focused, int direction)
	{
		if (isFling)
			return focused;
		return super.focusSearch(focused, direction);
	}
}
