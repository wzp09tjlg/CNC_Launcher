package cn.cncgroup.tv;

import android.app.Application;
import android.test.ApplicationTestCase;

import cn.cncgroup.tv.utils.LogUtil;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">TestingFundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LogUtil.i(HomeTabManager.getInstance().getHomeTabNames().toString());
    }
}