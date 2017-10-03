package cn.cncgroup.tv.view.setting.bean;

/**
 * Created by Wu on 2015/11/19.
 */
public class SettingBean {
    private String name;
    private boolean state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "name:" + name + " state:" + state;
    }
}
