package cn.cncgroup.tv.ui.widget.gridview;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ScrollbarHelper
{
	ScrollbarHelper()
	{
	}

	static int computeScrollOffset(int count, OrientationHelper orientation,
	        View startChild, View endChild, RecyclerView.LayoutManager lm,
	        boolean smoothScrollbarEnabled)
	{
		if (lm.getChildCount() != 0 && count != 0 && startChild != null
		        && endChild != null)
		{
			int minPosition = Math.min(lm.getPosition(startChild),
			        lm.getPosition(endChild));
			// int maxPosition = Math.max(lm.getPosition(startChild),
			// lm.getPosition(endChild));
			int itemsBefore = Math.max(0, minPosition);
			if (!smoothScrollbarEnabled)
			{
				return itemsBefore;
			}
			else
			{
				int laidOutArea = Math.abs(orientation
				        .getDecoratedEnd(endChild)
				        - orientation.getDecoratedStart(startChild));
				int itemRange = Math.abs(lm.getPosition(startChild)
				        - lm.getPosition(endChild)) + 1;
				float avgSizePerRow = (float) laidOutArea / (float) itemRange;
				return Math
				        .round((float) itemsBefore
				                * avgSizePerRow
				                + (float) (orientation.getStartAfterPadding() - orientation
				                        .getDecoratedStart(startChild)));
			}
		}
		else
		{
			return 0;
		}
	}

	static int computeScrollExtent(int count, OrientationHelper orientation,
	        View startChild, View endChild, RecyclerView.LayoutManager lm,
	        boolean smoothScrollbarEnabled)
	{
		if (lm.getChildCount() != 0 && count != 0 && startChild != null
		        && endChild != null)
		{
			if (!smoothScrollbarEnabled)
			{
				return Math.abs(lm.getPosition(startChild)
				        - lm.getPosition(endChild)) + 1;
			}
			else
			{
				int extend = orientation.getDecoratedEnd(endChild)
				        - orientation.getDecoratedStart(startChild);
				return Math.min(orientation.getTotalSpace(), extend);
			}
		}
		else
		{
			return 0;
		}
	}

	static int computeScrollRange(int count, OrientationHelper orientation,
	        View startChild, View endChild, RecyclerView.LayoutManager lm,
	        boolean smoothScrollbarEnabled)
	{
		if (lm.getChildCount() != 0 && count != 0 && startChild != null
		        && endChild != null)
		{
			if (!smoothScrollbarEnabled)
			{
				return count;
			}
			else
			{
				int laidOutArea = orientation.getDecoratedEnd(endChild)
				        - orientation.getDecoratedStart(startChild);
				int laidOutRange = Math.abs(lm.getPosition(startChild)
				        - lm.getPosition(endChild)) + 1;
				return (int) ((float) laidOutArea / (float) laidOutRange * (float) count);
			}
		}
		else
		{
			return 0;
		}
	}
}
