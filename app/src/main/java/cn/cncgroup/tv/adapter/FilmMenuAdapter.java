package cn.cncgroup.tv.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.FocusZoomUtil;
import cn.cncgroup.tv.utils.StringUtil;

/**
 * Created by Wuzhenpeng on 2015/6/8.
 */
public class FilmMenuAdapter extends
        RecyclerView.Adapter<FilmMenuAdapter.ViewHolder>
{
	protected ArrayList<FilmMenu> mList;
	private OnItemFocusListener<FilmMenu> mFocusListener;
	private FocusZoomUtil mFocusZoomUtil;
	private ViewHolder mLastViewHolder;

	public FilmMenuAdapter(OnItemFocusListener<FilmMenu> mFocusListener,
	        ArrayList<FilmMenu> list)
	{
		this.mFocusListener = mFocusListener;
		this.mList = list;
		mFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_LARGE);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_film_menu, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		final FilmMenu filmMenu = mList.get(position);
		String name = filmMenu.name;
		holder.name.setText(StringUtil.insertBlankToStr(name));
		holder.itemView
		        .setOnFocusChangeListener(new View.OnFocusChangeListener()
		        {
			        @Override
			        public void onFocusChange(View v, boolean hasFocus)
			        {
				        if (hasFocus)
				        {
					        if (mLastViewHolder != null)
					        {
						        mFocusZoomUtil.onItemFocused(
						                mLastViewHolder.name, false);
						        mLastViewHolder.itemView.setSelected(false);
					        }
					        mLastViewHolder = holder;
					        mFocusZoomUtil.onItemFocused(mLastViewHolder.name,
					                true);
					        mLastViewHolder.itemView.setSelected(true);
				        }
				        mFocusListener.onItemFocusLister(v, position, filmMenu,
				                hasFocus);
			        }
		        });
	}

	public void clearSelect()
	{
		if (mLastViewHolder != null)
		{
			mFocusZoomUtil.onItemFocused(mLastViewHolder.name, false);
			mLastViewHolder.itemView.setSelected(false);
		}
		mLastViewHolder = null;
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public FilmMenu getItem(int position)
	{
		return mList.get(position);
	}

	static class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView name;

		public ViewHolder(View view)
		{
			super(view);
			name = (TextView) view.findViewById(R.id.menu_item_name);
		}
	}

	public static class FilmMenu
	{
		public String name;
		public Bundle bundle;
		public Class<? extends Fragment> fragment;

		public FilmMenu(String name, Bundle bundle,
		        Class<? extends Fragment> fragment)
		{
			this.name = name;
			this.bundle = bundle;
			this.fragment = fragment;
		}
	}
}
