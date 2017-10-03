package cn.cncgroup.tv.view.setting.media;

import java.util.ArrayList;

import cn.cncgroup.tv.settings.GeneralMenu;
import cn.cncgroup.tv.settings.GeneralSettingActivity;
import cn.cncgroup.tv.settings.TestFragment;

/**
 * Created by zhang on 2015/11/25.
 */
public class MediaSettingActivity extends GeneralSettingActivity {
    @Override
    protected ArrayList<GeneralMenu> getMenuList() {
        ArrayList<GeneralMenu> list = new ArrayList<GeneralMenu>();
        list.add(new GeneralMenu("壁纸设置", WallpaperFragment.class));
        list.add(new GeneralMenu("画面缩放", CalibrationFragment.class));
        list.add(new GeneralMenu("按键声音", VoiceFragment.class));
        list.add(new GeneralMenu("分辨率选择", ResolutionFragment.class));
        return list;
    }

    @Override
    protected String getSettingTitle() {
        return "显示与声音";
    }
}
