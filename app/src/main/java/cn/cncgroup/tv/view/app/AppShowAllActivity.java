package cn.cncgroup.tv.view.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.AppShowListAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.BaseGridView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.AppInfoUtils;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.homesecond.AppUninstallBroadcast;

/**
 * Created by jinwenchao123 on 15/9/24.
 */
public class AppShowAllActivity extends BaseActivity implements
        OnItemClickListener<AppInfo>,
        OnItemFocusListener<AppInfo>,
        AppUninstallBroadcast.AppUninstallListener
{
	private static final String TOP_QUEUE = "TOP_QUEUE";
	private static final String TOP_QUEUE_NAME = "TOP_QUEUE_NAME";
	private final String TAG = "AppShowAllActivity";
	private final String OPEN_APP_ERROR = "打开应用异常!";
	private final int TOTAL_APP_COUNT = 8;
	// widget
	private VerticalGridView gridview_all_app;
	private LinearLayout ll_title;
	// data
	private AppShowListAdapter mAdapter;
	private ArrayList<AppInfo> mList;
	private int USERAPP = 100;
	private Boolean hasTitle = true;
	private SharedPreferences sharedPreAppTop;

    private Handler mHandler = new Handler();
    private ShadowView mShadowView;
    private View mFocusedView;
    private Runnable mHideRunable = new Runnable() {
        @Override
        public void run() {
            mShadowView.setVisibility(View.INVISIBLE);
        }
    };
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mFocusedView != null) {
                mShadowView.moveTo(mFocusedView);
            }
        }
    };
	// interface

	@Override
	public void doAppUninstallListener(String packageName)
	{
		mList = AppInfoUtils.getAppsData(this);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void doAppInstallListener(String namePackage)
	{
		mList = AppInfoUtils.getAppsData(this);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
	}

	private void getTitleIntent()
	{
		hasTitle = getIntent().getBooleanExtra("HASTITLE", true);
	}

	@Override
	protected void setContentView()
	{
		setContentView(R.layout.activity_app_list);
		getTitleIntent();
	}

	@Override
	protected void findView()
	{
		gridview_all_app = (VerticalGridView) findViewById(R.id.gridview_all_app);
		gridview_all_app.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
        mShadowView = (ShadowView) findViewById(R.id.cover);
	}

	@Override
	protected void initView()
	{
		mList = AppInfoUtils.getAppsData(this);
		mAdapter = new AppShowListAdapter(this, mList, this,this);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)gridview_all_app.getLayoutParams();
        int marginTop ;
        int marginLeft = getResources().getDimensionPixelOffset(R.dimen.dimen_104dp);
        int marginRight = getResources().getDimensionPixelOffset(R.dimen.dimen_70dp);
		if (hasTitle){
			ll_title.setVisibility(View.VISIBLE);
            marginTop = getResources().getDimensionPixelOffset(R.dimen.dimen_200dp);

		}else{
            marginTop = getResources().getDimensionPixelOffset(R.dimen.dimen_100dp);
			ll_title.setVisibility(View.GONE);
		}
        param.setMargins(marginLeft,marginTop,marginRight,0);
        gridview_all_app.setLayoutParams(param);
		gridview_all_app.setAdapter(mAdapter);
        gridview_all_app.addOnScrollListener(mScrollListener);
        gridview_all_app.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ALIGNED);
		AppUninstallBroadcast.addUninstallListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		AppUninstallBroadcast.deleteUninstallListener(this);
	}

	@Override
	public void onItemClickLister(View view, int position, AppInfo appInfo)
	{
		if (hasTitle)
		{ // 全部应用中 点击每个应用时打开应用
			try
			{
				Utils.startAppByPackageName(this, appInfo.getPackageName());
			} catch (Exception e)
			{
				GlobalToast.makeText(getApplicationContext(),OPEN_APP_ERROR,GlobalConstant.ToastShowTime);
			}
		}
		else
		{ // 在所有的应用中，点击每个应用时添加应用到置顶队列中
            boolean isAlreadyExist = false;
            sharedPreAppTop = getSharedPreferences( TOP_QUEUE,MODE_PRIVATE);
			for (int i = 0; i < TOTAL_APP_COUNT; i++)
			{
				if (sharedPreAppTop.contains(TOP_QUEUE_NAME + "" + i))
				{
					String packageName = sharedPreAppTop.getString(
					        TOP_QUEUE_NAME + "" + i, "");
					if (!packageName.equals(""))
					{
						if (packageName.equals(appInfo.getPackageName()))
						{
							Intent intent = new Intent();
							intent.putExtra("datapackname", "");
							setResult(USERAPP, intent);
                            isAlreadyExist = true;
                        }
					}
				}
			}
            if (!isAlreadyExist) {
                Intent intent = new Intent();
                intent.putExtra("datapackname", mList.get(position)
                        .getPackageName());
                setResult(USERAPP, intent);
            }
            finish();
		}
	}

    @Override
    public void onItemFocusLister(View view, int position, AppInfo appInfo, boolean hasFocus) {
        if (hasFocus) {
            if (gridview_all_app.getFocusedChild() != null) {
                mHandler.removeCallbacks(mHideRunable);
                mFocusedView = view;
                mShadowView.moveTo(mFocusedView);
            }
        } else {
            mHandler.postDelayed(mHideRunable, 16);
        }
    }
}
