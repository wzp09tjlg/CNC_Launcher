package cn.cncgroup.tv.view.setting;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.settings.GeneralMenu;

/**
 * Created by zhangtao on 2015/11/23.
 */
public class GeneralSettingsActivity extends cn.cncgroup.tv.settings.GeneralSettingActivity {

    @Override
    protected ArrayList<GeneralMenu> getMenuList() {
        ArrayList<GeneralMenu> list = new ArrayList<GeneralMenu>();
        list.add(new GeneralMenu("软件升级", SoftwareUpgradeFragment.class));
        list.add(new GeneralMenu("设备信息", FacilityInformationFragment.class));
        list.add(new GeneralMenu("问题反馈  ", SettingFeedBackFragment.class));
        list.add(new GeneralMenu("恢复出厂设置", RestoreFactorySetting.class));
        return list;
    }

    @Override
    protected String getSettingTitle() {
        return getString(R.string.general_setting);
    }
}
