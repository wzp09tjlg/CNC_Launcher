package cn.cncgroup.tv.view.setting;


import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.settings.GeneralMenu;
import cn.cncgroup.tv.settings.GeneralSettingActivity;


/**
 * Created by zhangtao on 2015/11/2.
 */
public class SettingBootCustomActivity extends GeneralSettingActivity {

    @Override
    protected ArrayList<GeneralMenu> getMenuList() {
        ArrayList<GeneralMenu> list = new ArrayList<GeneralMenu>();
        list.add(new GeneralMenu("默认开启频道", SettingOpenChannelFragment.class));
        list.add(new GeneralMenu("开机自启动", SettingBootStartFragment.class));
        return list;
    }

    @Override
    protected String getSettingTitle() {
        return getString(R.string.boot_custom);
    }
}
