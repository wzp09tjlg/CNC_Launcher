package cn.cncgroup.tv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.utils.FocusZoomUtil;

/**
 * Created by Wu on 2015/8/25.
 */
public class VideoListMenuAdapter extends
        RecyclerView.Adapter<VideoListMenuAdapter.ViewHolder>
{
	private final String TAG = "VideoListMenuAdapter";
	private ArrayList<FilmMenuAdapter.FilmMenu> mList;
	private Listener<FilmMenuAdapter.FilmMenu> mListener;
	private boolean isFirst = true;
	private FocusZoomUtil mFocusZoomUtil;
	private ViewHolder mLastViewHolder;

	public VideoListMenuAdapter(ArrayList<FilmMenuAdapter.FilmMenu> list,
	        Listener<FilmMenuAdapter.FilmMenu> listener)
	{
		mList = list;
		mListener = listener;
		mFocusZoomUtil = new FocusZoomUtil(FocusZoomUtil.ZOOM_FACTOR_LARGE);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_video_list_menu, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		final FilmMenuAdapter.FilmMenu category = mList.get(position);
		holder.category.setText(category.name);
		holder.itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListener.onMenuItemClickListener(v, position, category);
			}
		});
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
                                        mLastViewHolder.category, false, 1.25f);
                                mLastViewHolder.itemView.setSelected(false);
					        }
					        mLastViewHolder = holder;
					        mFocusZoomUtil.onItemFocused(
                                    mLastViewHolder.category, true, 1.25f);
                            mLastViewHolder.itemView.setSelected(true);
				        }
				        mListener.onMenuItemFocusListener(v, position,
				                category, hasFocus);
			        }
		        });
		if (isFirst && position == 0)
		{
			holder.itemView.requestFocus();
			isFirst = false;
		}
	}

	public void clearSelect()
	{
		if (mLastViewHolder != null)
		{
			mFocusZoomUtil.onItemFocused(mLastViewHolder.category, false);
			mLastViewHolder.itemView.setSelected(false);
		}
		mLastViewHolder = null;
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public FilmMenuAdapter.FilmMenu getItem(int position)
	{
		if (position < 0 || position > mList.size())
			return null;
		else
			return mList.get(position);
	}

	public interface Listener<T>
	{
		void onMenuItemClickListener(View view, int position, T result);

		void onMenuItemFocusListener(View view, int position, T t,
		        boolean hasFocus);
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView category;

		public ViewHolder(View view)
		{
			super(view);
			category = (TextView) view.findViewById(R.id.video_list_text_menu);
		}
	}
}
