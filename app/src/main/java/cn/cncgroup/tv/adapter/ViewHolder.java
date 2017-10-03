package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder
{
	private final SparseArray<View> mViews;
	private View mConvertView;

	ViewHolder(Context context, ViewGroup parent, int layoutId,
               int position)
	{
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		//setTag
		mConvertView.setTag(this);
		
		
	}

    /**
	 * 获取ViewHolder
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position)
	{
	
		if (convertView == null)
		{
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}


	/**
	 * 根据ID返回view
	 * @param viewId
	 * @return 返回viewId指定的view
	 */
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView()
	{
		return mConvertView;
	}

	

}
