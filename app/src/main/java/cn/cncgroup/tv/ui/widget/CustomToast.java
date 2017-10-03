package cn.cncgroup.tv.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import cn.cncgroup.tv.R;

/**
 * Created by zhang on 2015/7/9.
 */
public class CustomToast
{
	private static PopupWindow sPopupWindow;
	private static int sOffset;

	public static void showCustomToast(Context context, final View anchor,
	        String msg)
	{
		removeCustomToast();
		final MarqueeTextView textView;
		sPopupWindow = getPopupWindow(context);
		sPopupWindow.showAsDropDown(anchor);
		textView = (MarqueeTextView) sPopupWindow.getContentView();
		textView.setEms(8);
		textView.setText(msg);
		textView.setSelected(true);
		textView.post(new Runnable()
		{
			@Override
			public void run()
			{
				updateCustomToast(textView, anchor);
			}
		});
	}

	private static void updateCustomToast(View window, View anchor)
	{
		int anchorHeight = anchor.getMeasuredHeight();
		int anchorWidth = anchor.getMeasuredWidth();
		int toastHeight = window.getMeasuredHeight();
		int toastWidth = window.getMeasuredWidth();
		int xoff = anchorWidth / 2 - toastWidth / 2;
		int yoff = -anchorHeight - toastHeight - sOffset;
		if (sPopupWindow != null)
		{
			sPopupWindow.update(anchor, xoff, yoff, LayoutParams.WRAP_CONTENT,
			        LayoutParams.WRAP_CONTENT);
		}
	}

	private static PopupWindow getPopupWindow(Context context)
	{
		sOffset = context.getResources().getDimensionPixelOffset(
		        R.dimen.dimen_8dp);
		return new PopupWindow(LayoutInflater.from(context).inflate(
		        R.layout.textview, null), LayoutParams.WRAP_CONTENT,
		        LayoutParams.WRAP_CONTENT);
	}

	public static void removeCustomToast()
	{
		if (sPopupWindow != null)
		{
			sPopupWindow.dismiss();
			sPopupWindow = null;
		}
	}
}
