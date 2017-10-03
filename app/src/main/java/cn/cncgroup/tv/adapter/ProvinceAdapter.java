package cn.cncgroup.tv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.view.weather.city.ProvicneModel;

/**
 * Created by Walter on 2015/9/2.
 */
public class ProvinceAdapter extends
        RecyclerView.Adapter<ProvinceAdapter.ViewHolder>
{
	private ArrayList<ProvicneModel> mList;
	private Listener<ProvicneModel> mListener;
	private int mSelectedPosition = -1;

	public ProvinceAdapter(ArrayList<ProvicneModel> list, Listener<ProvicneModel> listener)
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
		final ProvicneModel bean = mList.get(position);
		holder.name.setText(bean.getCityName());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onProvinceItemClickListener(v, position, bean);
			}
		});
		holder.itemView
		        .setOnFocusChangeListener(new View.OnFocusChangeListener()
		        {
			        @Override
			        public void onFocusChange(View v, boolean hasFocus)
			        {
				        mListener.onProvinceItemFocusListener(v, position,
				                bean, hasFocus);
			        }
		        });
		if (position == mSelectedPosition) {
			holder.itemView.setSelected(true);
		} else {
			holder.itemView.setSelected(false);
		}
	}

	public int getSelectedPosition() {
		return mSelectedPosition;
	}

	public void setSelectedPosition(int position) {
		int temp = mSelectedPosition;
		mSelectedPosition = position;
//		notifyItemChanged(position);
//		notifyItemChanged(temp);
	}

	@Override
	public int getItemCount()
	{
		return mList.size();
	}

	public interface Listener<T>
	{
		void onProvinceItemClickListener(View view, int position, T result);

		void onProvinceItemFocusListener(View view, int position, T t,
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
