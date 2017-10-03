package cn.cncgroup.tv.cncplayer.widget.selectview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * 选集第一行PagerAdapter
 */
public class ItemPageAdapter extends FragmentPagerAdapter
{

	private static final String TAG = "CollectPageAdapter";
	private ItemFocusListener mItemFocusLinListener;
	private ItemClickListener mItemClickListener;

	public ItemPageAdapter(FragmentManager fm,
                           ItemFocusListener focusListener, ItemClickListener clickListener)
	{
		super(fm);
		this.mItemFocusLinListener = focusListener;
		this.mItemClickListener = clickListener;
	}

	public void setSelected(ViewGroup container, int page, int position)
	{
		ItemGridFragment fragment = (ItemGridFragment) instantiateItem(
		        container, page);
		fragment.setSelected(position);
	}

	@Override
	public Fragment getItem(int position)
	{
		Log.i(TAG, "CollectPageAdapter position:" + position);
		return ItemGridFragment.getInstance(position, mItemFocusLinListener,
				mItemClickListener);
	}

	@Override
	public int getCount()
	{
		return Constant.COLLECT_PAGER_COUNT;
	}
}
