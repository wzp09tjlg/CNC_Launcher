package cn.cncgroup.tv;

import android.test.ActivityInstrumentationTestCase2;

import cn.cncgroup.tv.network.TestActivity;

/**
 * Created by zhang on 2015/4/23.
 */
public class NetworkTest extends ActivityInstrumentationTestCase2<TestActivity>
{

	public NetworkTest()
	{
		super(TestActivity.class);
	}

	public void testNetwork()
	{
		getActivity();
	}
}
