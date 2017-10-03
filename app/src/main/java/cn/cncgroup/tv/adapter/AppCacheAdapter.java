package cn.cncgroup.tv.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**影视推荐Adapter
 * Created by zhangguang 2015/5/23.
 */
public class AppCacheAdapter extends
        RecyclerView.Adapter<AppCacheAdapter.ViewHolder>
{
	private ArrayList<AppInfo> mAppList = new ArrayList<AppInfo>();
	private OnItemClickListener<AppInfo> mClickListener;
	private OnItemFocusListener<AppInfo> mFocusListener;


	public AppCacheAdapter(ArrayList<AppInfo> applist, OnItemClickListener<AppInfo> clickListener,
							OnItemFocusListener focusListener) {
		mClickListener = clickListener;
		mFocusListener = focusListener;
		mAppList = applist;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
				R.layout.app_set_cache_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		final AppInfo item = mAppList.get(position);
		holder.tv_appset_iconname.setText(item.getAppName());
		holder.dwv_appset_icon.setBackground(item.getIcon());

		if (TextUtils.isEmpty(item.getCachesize())||TextUtils.isEmpty(item.getCodesize())){
			holder.tv_cache_cachesize.setText("缓存："+"获取中");
			holder.tv_cache_codesize.setText("大小："+"获取中");
		}else{
			holder.tv_cache_cachesize.setText("缓存："+item.getCachesize());
			holder.tv_cache_codesize.setText("大小："+item.getCodesize());
		}
		holder.itemView.setTag(item);
		holder.itemView
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View view, boolean b) {
						mFocusListener.onItemFocusLister(view, position, item,
								b);
					}
				});
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mClickListener.onItemClickLister(view, position, item);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return mAppList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		SimpleDraweeView dwv_appset_icon;
		TextView tv_appset_iconname;
		RelativeLayout rl_focus_msg_cache;
		TextView tv_cache_codesize;
		TextView tv_cache_cachesize;
		public ViewHolder(View itemView) {
			super(itemView);
			dwv_appset_icon = (SimpleDraweeView) itemView.findViewById(R.id.dwv_appset_icon);
			tv_appset_iconname = (TextView) itemView.findViewById(R.id.tv_appset_iconname);
			rl_focus_msg_cache = (RelativeLayout) itemView.findViewById(R.id.rl_focus_msg_cache);
			tv_cache_codesize= (TextView) itemView.findViewById(R.id.tv_cache_codesize);
			tv_cache_cachesize = (TextView) itemView.findViewById(R.id.tv_cache_cachesize);

		}
	}
}
