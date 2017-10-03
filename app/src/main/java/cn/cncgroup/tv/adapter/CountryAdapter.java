package cn.cncgroup.tv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.view.weather.city.AreaModel;

/**
 * Created by Walter on 2015/9/5.
 */
public class CountryAdapter extends
        RecyclerView.Adapter<CountryAdapter.ViewHolder>
{
	private List<AreaModel> mList;
	private Listener<AreaModel> mListener;
	private int mSelectedPosition = 0;

	public CountryAdapter(List<AreaModel> list, Listener<AreaModel> listener)
	{
		mList = list;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
		        R.layout.item_weather_city, null));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		final AreaModel bean = mList.get(position);
		holder.name.setText(bean.getCityName());
		holder.itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListener.onCountryItemClickListener(v, position, bean);
			}
		});
		holder.itemView
		        .setOnFocusChangeListener(new View.OnFocusChangeListener()
		        {
			        @Override
			        public void onFocusChange(View v, boolean hasFocus)
			        {
				        mListener.onCountryItemFocusListener(v, position, bean,
				                hasFocus);
			        }
		        });
		if (position == mSelectedPosition)
		{
			holder.itemView.setSelected(true);
		}
		else
		{
			holder.itemView.setSelected(false);
		}
	}

	public int getSelectedPosition()
	{
		return mSelectedPosition;
	}

	public void setSelectedPosition(int position)
	{
		int temp = mSelectedPosition;
		mSelectedPosition = position;
//		notifyItemChanged(position);
//		notifyItemChanged(temp);
	}

	@Override
	public int getItemCount()
	{
		return mList == null ? 0 : mList.size();
	}

	public void setSelectPostion(int postion)
	{
		mSelectedPosition = postion;
	}

	public void setData(List<AreaModel> list)
	{
		mList = list;
		notifyDataSetChanged();
	}

	public interface Listener<T>
	{
		void onCountryItemClickListener(View view, int position, T result);

		void onCountryItemFocusListener(View view, int position, T t,
		        boolean hasFocus);
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView name;
		private String code;

		public ViewHolder(View view)
		{
			super(view);
			name = (TextView) view
			        .findViewById(R.id.local_weather_text_city_name);
		}
	}
}
