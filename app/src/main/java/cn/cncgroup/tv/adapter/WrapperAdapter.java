package cn.cncgroup.tv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhang on 2015/6/12.
 */
public abstract class WrapperAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter
{
	private int mPageCount;

	/**
	 * 
	 * @param pageCount
	 *            每页有几个item
	 */
	public WrapperAdapter(int pageCount)
	{
		mPageCount = pageCount;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		// 超过真实个数就隐藏，反之显示
		if (position > getCount() - 1)
		{
			holder.itemView.setVisibility(View.GONE);
		}
		else
		{
			holder.itemView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 假的个数为了凑齐一页
	 */
	@Override
	public int getItemCount()
	{
		int count = getCount();
		if (count % mPageCount == 0)
		{
			return getCount();
		}
		return (mPageCount - count % mPageCount) + count;
	}

	/**
	 * 获取真实的个数
	 */
	public abstract int getCount();
}
