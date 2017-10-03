package cn.cncgroup.tv.view.setting;

import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.utils.VersionsInfo;
import cn.cncgroup.tv.view.setting.adapter.FacilityInformationAdapter;
import cn.cncgroup.tv.view.setting.adapter.FacilityInformationAdapter.ModelNumberModel;

/**
 * Created by Administrator on 2015/11/23.
 */
public class FacilityInformationFragment extends BaseFragment implements View.OnFocusChangeListener {
    private String addressname = "address";
    private TextView mAddress_tview;
    private ImageView mShutdown_img_up;
    private ImageView mShutdown_img_down;
    private RecyclerView mList_facility_information;
    FacilityInformationAdapter mAdapter;
    ArrayList<ModelNumberModel> mModelList = new ArrayList<ModelNumberModel>();
    private int mCurrentIndex = 0;
    private PackageManager packageManager;

    @Override
    protected int getContentView() {
        return R.layout.setting_facility_information;
    }

    @Override
    protected void findView(View view) {
        mAddress_tview = (TextView) view.findViewById(R.id.address_tview);
        mShutdown_img_up = (ImageView) view.findViewById(R.id.shutdown_img_up);
        mShutdown_img_down = (ImageView) view.findViewById(R.id.shutdown_img_down);
        mList_facility_information = (RecyclerView) view.findViewById(R.id.list_facility_information);
    }


    private ArrayList<String> address;

    @Override
    protected void initView() {
        address = new ArrayList<String>();
        address.add("客厅");
        address.add("厨房");
        address.add("卧室");
        address.add("书房");

        String name = Preferences.build(getActivity()).getString(addressname,
                address.get(Preferences.build(getActivity()).getInt("position", mCurrentIndex)));
        mCurrentIndex = address.indexOf(name);
        mAddress_tview.setText(name);
        mAddress_tview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        int pos = mCurrentIndex == 0 ? 0 : mCurrentIndex - 1;
                        String nextName = address.get(pos);
                        mAddress_tview.setText(nextName);
                        mCurrentIndex = pos;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        int pos = mCurrentIndex == address.size() - 1 ? mCurrentIndex : mCurrentIndex + 1;
                        String nextName = address.get(pos);
                        mAddress_tview.setText(nextName);
                        mCurrentIndex = pos;
                    }
                    if (mCurrentIndex == 0) {
                        mShutdown_img_up.setVisibility(View.INVISIBLE);
                        mShutdown_img_down.setVisibility(View.VISIBLE);
                    } else if (mCurrentIndex == address.size() - 1) {
                        mShutdown_img_up.setVisibility(View.VISIBLE);
                        mShutdown_img_down.setVisibility(View.INVISIBLE);
                    } else {
                        mShutdown_img_up.setVisibility(View.VISIBLE);
                        mShutdown_img_down.setVisibility(View.VISIBLE);
                    }
                }
                Preferences.build(getActivity()).putInt("position", mCurrentIndex);
                return false;
            }
        });
        mModelList.clear();
        mModelList.add(new ModelNumberModel("型号", android.os.Build.MODEL));
        mModelList.add(new ModelNumberModel("软件版本", Project.get().getConfig().getRevision()));
        mModelList.add(new ModelNumberModel("安卓版本", android.os.Build.VERSION.RELEASE));
        mModelList.add(new ModelNumberModel("IP地址", Utils.getLocalIPAddress()));
        mModelList.add(new ModelNumberModel("以太网MAC", Utils.getMacAddress(getActivity())));
        mModelList.add(new ModelNumberModel("WIFI MAC", VersionsInfo.getWifiMAC(getActivity())));
        mAdapter = new FacilityInformationAdapter(getActivity(), mModelList);
        mList_facility_information.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList_facility_information.setAdapter(mAdapter);

        mList_facility_information.setFocusable(false);
        mAddress_tview.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            if (Preferences.build(getActivity()).getInt("position", mCurrentIndex) == 0) {
                mShutdown_img_up.setVisibility(View.INVISIBLE);
                mShutdown_img_down.setVisibility(View.VISIBLE);
            } else if (Preferences.build(getActivity()).getInt("position", mCurrentIndex) == address.size() - 1) {
                mShutdown_img_up.setVisibility(View.VISIBLE);
                mShutdown_img_down.setVisibility(View.INVISIBLE);
            } else {
                mShutdown_img_up.setVisibility(View.VISIBLE);
                mShutdown_img_down.setVisibility(View.VISIBLE);
            }

        } else {
            mShutdown_img_up.setVisibility(View.INVISIBLE);
            mShutdown_img_down.setVisibility(View.INVISIBLE);
        }
    }

}
