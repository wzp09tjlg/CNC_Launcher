package cn.cncgroup.tv.ui.widget.selectview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 选集第二行Adapter
 */
public class SectionPageAdapter extends FragmentPagerAdapter
{

	private SectionClickListener mListener;
    private SectionFocusListener mFocuseListener;

	public SectionPageAdapter(FragmentManager fm, SectionClickListener mListener,SectionFocusListener sectionFocusListener)
	{
		super(fm);
		this.mListener = mListener;
        this.mFocuseListener = sectionFocusListener;
	}

	public void setSelected(ViewGroup container, int page, int position)
	{
		SectionGridFragment fragment = (SectionGridFragment) instantiateItem(
		        container, page);
		fragment.setSelected(position);
	}

	@Override
	public Fragment getItem(int position)
	{

		return SectionGridFragment.getInstance(position, mListener,mFocuseListener);
	}

	@Override
	public int getCount()
	{
		return Constant.SELECT_PAGER_COUNT;
	}
}
