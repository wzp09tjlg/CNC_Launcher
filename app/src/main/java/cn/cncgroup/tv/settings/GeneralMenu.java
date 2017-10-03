package cn.cncgroup.tv.settings;

import android.support.v4.app.Fragment;

/**
 * Created by zhang on 2015/11/20.
 */
public class GeneralMenu extends BaseMenu {

    private Class<? extends Fragment> fragment;

    public GeneralMenu(String name, Class<? extends Fragment> fragment) {
        super(name);
        this.fragment = fragment;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }
}