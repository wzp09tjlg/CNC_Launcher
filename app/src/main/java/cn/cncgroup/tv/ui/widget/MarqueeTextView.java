package cn.cncgroup.tv.ui.widget;

import java.util.Arrays;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView
{
	public MarqueeTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public MarqueeTextView(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		setDuplicateParentStateEnabled(true);
		setEllipsize(TruncateAt.MARQUEE);
		setSingleLine(true);
		setMarqueeRepeatLimit(-1);
	}

	@Override
	protected void drawableStateChanged()
	{
		super.drawableStateChanged();
		if (Arrays.binarySearch(getDrawableState(),
		        android.R.attr.state_focused) != -1)
		{
			setSelected(true);
		}
		else
		{
			setSelected(false);
		}
	}
}
