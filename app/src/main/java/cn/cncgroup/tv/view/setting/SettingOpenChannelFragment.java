package cn.cncgroup.tv.view.setting;

import android.view.View;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;


/**
 * Created by zhangtao on 2015/11/5.
 */
public class SettingOpenChannelFragment extends BaseFragment implements OnItemClickListener<String> {
    private static final String TAG = "SettingOpenChannelFragment";
    private VerticalGridView mGridView;
    private SettingOpenChannelAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_default_open_channel;
    }

    @Override
    protected void findView(View view) {
        mGridView = (VerticalGridView) view.findViewById(R.id.list_verticalgridview);
    }

    @Override
    protected void initView() {
        String defaultChannel = Preferences.build(getActivity()).getString("selectedItem", "电影");
        int defaultIndex = 1;
        ArrayList<String> list = HomeTabManager.getInstance().getHomeTabNames();
        for (int i = 0; i < list.size(); i++) {
            if (defaultChannel.endsWith(list.get(i))) {
                defaultIndex = i;
            }
        }
        mAdapter = new SettingOpenChannelAdapter(defaultIndex, list, this);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickLister(View view, int position, String channelModel) {
        mAdapter.setSelectedPosition(position);
        Preferences.build(getActivity()).putString("selectedItem", channelModel);
        HomeTabManager.getInstance().setDefaultTabIndex(position);
    }
}