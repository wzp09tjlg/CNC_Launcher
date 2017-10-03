package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by Wuzhenpeng on 2015/6/18.
 */
public class FilmVerticalGridView extends VerticalGridView
{
	private DoRequestDataListener mDoRequestDataListener;
    private int mTotalCount;
	//这个参数指的是使用该控件的页面，每行有几个item，在这里默认的是4个
	private int lineNum = 4;
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public FilmVerticalGridView(Context context)
	{
		super(context);
	}


	public FilmVerticalGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setDoRequestDataListener(
	        DoRequestDataListener doRequestDataListener)
	{
		this.mDoRequestDataListener = doRequestDataListener;
	}

    public void setmTotalCount(int totalCount){
        this.mTotalCount=totalCount;
    }

	@Override
	public View focusSearch(View focused, int direction)
	{
//		int count = getAdapter().getItemCount();
		View view = super.focusSearch(focused, direction);
		final int position = ((RecyclerView.LayoutParams) focused
		        .getLayoutParams()).getViewAdapterPosition();
		if (direction == View.FOCUS_DOWN && (position >= (mTotalCount - lineNum*4)))
		{
			if (mDoRequestDataListener != null)
				mDoRequestDataListener.doRequestDataListner();
		}

		return view;
	}

	public interface DoRequestDataListener
	{
		void doRequestDataListner();
	}
}
