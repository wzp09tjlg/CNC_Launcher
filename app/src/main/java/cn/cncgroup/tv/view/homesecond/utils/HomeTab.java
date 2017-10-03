package cn.cncgroup.tv.view.homesecond.utils;

import android.os.Bundle;

import com.google.gson.annotations.Expose;

import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.LogUtil;
import cn.cncgroup.tv.view.homesecond.BaseHomeFragment;

/**
 * Created by zhang on 2015/11/16.
 */
public class HomeTab {
    @Expose(serialize = true, deserialize = true)
    public String name;
    @Expose(serialize = false, deserialize = false)
    public BaseHomeFragment holder;

    public HomeTab(String name) {
        this.name = name;
    }

    public Bundle getBundle(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalConstant.KEY_INDEX, index);
        bundle.putSerializable(GlobalConstant.KEY_CATEGORY, getCategoryByName());
        return bundle;
    }

    public Category getCategoryByName() {
        return CategoryUtils.getCategoryByName(name);
    }

    public String getFragmentName() {
        return HomeTabManager.getFragmentName(name);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof HomeTab) {
            HomeTab tab = (HomeTab) o;
            result = name.equals(tab.name);
        }
        return result;
    }
}
