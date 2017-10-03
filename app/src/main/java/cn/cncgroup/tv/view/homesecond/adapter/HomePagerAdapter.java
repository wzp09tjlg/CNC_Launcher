package cn.cncgroup.tv.view.homesecond.adapter;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import cn.cncgroup.tv.utils.LogUtil;
import cn.cncgroup.tv.view.homesecond.BaseHomeFragment;
import cn.cncgroup.tv.view.homesecond.HomeActivity;
import cn.cncgroup.tv.view.homesecond.utils.HomeTab;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * Created by zhang on 2015/11/17.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "HomePagerAdapter";
    private HomeActivity mHomeActivity;
    private HomeTabManager mHomeTabManager;

    public HomePagerAdapter(HomeActivity activity, HomeTabManager manager) {
        super(activity.getSupportFragmentManager());
        this.mHomeActivity = activity;
        this.mHomeTabManager = manager;
        this.mHomeTabManager.bindHomePagerAdapter(this);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogUtil.i(TAG, "instantiateItem");
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        HomeTab tab = mHomeTabManager.getHomeTabByIndex(position);
        Fragment item = Fragment.instantiate(mHomeActivity,
                tab.getFragmentName(), tab.getBundle(position));
        tab.holder = (BaseHomeFragment) item;
        return item;
    }

    @Override
    public int getCount() {
        return mHomeTabManager.getHomeTabSize() - 1;
    }

    @Override
    public int getItemPosition(Object object) {
        int position = getFragmentPosition(object);
        if (position > mHomeTabManager.getStartFixedTabsSize() - 1) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    private int getFragmentPosition(Object object) {
        String tag = ((Fragment) object).getTag();
        return Integer.parseInt(tag.substring(tag.lastIndexOf(':') + 1, tag.length()));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mHomeTabManager.getHomeTabByIndex(position).name;
    }
}
