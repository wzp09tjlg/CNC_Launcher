package cn.cncgroup.tv.settings;

import java.util.ArrayList;

import cn.cncgroup.tv.R;

/**
 * Created by zhang on 2015/11/20.
 */
public class TestActivity extends GeneralSettingActivity {
    @Override
    protected String getSettingTitle() {
        return "测试";
    }

    @Override
    protected ArrayList<GeneralMenu> getMenuList() {
        ArrayList<GeneralMenu> list = new ArrayList<GeneralMenu>();
        list.add(new GeneralMenu("0-99", TestFragment.class));
        list.add(new GeneralMenu("100-199", TestFragment.class));
        list.add(new GeneralMenu("200-299", TestFragment.class));
        return list;
    }
}
