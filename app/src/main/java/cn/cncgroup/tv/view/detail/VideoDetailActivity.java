package cn.cncgroup.tv.view.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.utils.GlobalConstant;

/**
 * 详情页 Activity Created by zhangguang on 2015/6/24.
 */
public class VideoDetailActivity extends BaseActivity
        implements BaseRequest.Listener<VideoSetDetailData>
{
	public static String TAG = "VideoDetailActivity";
	private MovieDetailFragment mMovieDetailFragment;
	private VarietyDetailFragment mVarietyDetailFragment;

	@Override
	public void onResponse(VideoSetDetailData result,boolean isFromCache)
	{
		if (result.result != null)
		{
			showContent();
			this.hideLoading();
			hideError();
			Bundle bundle = new Bundle();
			bundle.putSerializable(GlobalConstant.KEY_VIDEOSET, result.result);
			Log.i(TAG, "id:" + result.result.channelId);
			Log.i(TAG, "CategoryById:"
			        + CategoryUtils.getCategoryById(result.result.channelId));
			// Log.i(TAG,"CategoryById:"+CategoryUtils.getCategoryById(result.result.channelId).getName());
			if (CategoryUtils.getCategoryById(result.result.channelId) != null)
			{
				if (CategoryUtils.getCategoryById(result.result.channelId)
				        .getName().equals("综艺"))
				{
					initVarietyDetailFragment(bundle);
				}
				else
				{
					initMovieDetailFragment(bundle);
				}
			}
			else
			{
				// 因为3D的channelID是乱的
				initMovieDetailFragment(bundle);
			}
		}
		else
		{
			hideContent();
			this.hideLoading();
			showError();
		}

	}

	@Override
	public void onFailure(int errorCode,Request request)
	{
		this.hideLoading();
		hideContent();
		showError();
	}

	@Override
	protected void setContentView()
	{
		setContentView(R.layout.activity_detail);
	}

	@Override
	protected void findView()
	{
		initLoadingState();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		NetworkManager.cancelRequest(TAG);
	}

	@Override
	protected void initView()
	{
		VideoSet set = (VideoSet) getIntent()
		        .getSerializableExtra(GlobalConstant.KEY_VIDEOSET);
		if (set != null)
		{
			NetworkManager.getInstance().getVideoSetDetail(set.getSourceUrl(),
			        this, TAG);
		}
	}

	@Override
	protected void onDestroy()
	{

		super.onDestroy();
	}

	/**
	 * 显示综艺的详情页
	 */
	public void initVarietyDetailFragment(Bundle bundle)
	{
		FragmentTransaction transaction = getSupportFragmentManager()
		        .beginTransaction();
		if (mVarietyDetailFragment == null)
		{
			mVarietyDetailFragment = new VarietyDetailFragment();
			mVarietyDetailFragment.setArguments(bundle);
			transaction.add(R.id.content, mVarietyDetailFragment);
			transaction.commit();
		}
		else
		{
			transaction.show(mVarietyDetailFragment);
		}
	}

	/**
	 * 显示影视的详情页
	 */
	public void initMovieDetailFragment(Bundle bundle)
	{
		FragmentTransaction transaction = getSupportFragmentManager()
		        .beginTransaction();
		if (mMovieDetailFragment == null)
		{
			mMovieDetailFragment = new MovieDetailFragment();
			mMovieDetailFragment.setArguments(bundle);
			transaction.add(R.id.content, mMovieDetailFragment);
			transaction.commit();
		}
		else
		{
			transaction.show(mMovieDetailFragment);
		}
	}
}
