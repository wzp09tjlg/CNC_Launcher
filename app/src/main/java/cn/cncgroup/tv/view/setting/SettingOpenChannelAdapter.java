package cn.cncgroup.tv.view.setting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;

/**
 * Created by zhangtao on 2015/11/5.
 */
public class SettingOpenChannelAdapter extends RecyclerView.Adapter<SettingOpenChannelAdapter.ViewHolder> {
    private ArrayList<String> mList;
    private OnItemClickListener<String> mClickListener;
    private int mSelectedPosition = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_open_channel_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = mList.get(position);
        holder.name.setText(name);
        if (position == mSelectedPosition) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClickLister(v, position, name);
            }
        });
    }

    public void setSelectedPosition(int position) {
        int temp = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(position);
        notifyItemChanged(temp);
    }

    public SettingOpenChannelAdapter(int selectPosition, ArrayList<String> list, OnItemClickListener<String> clickListener) {
        this.mList = list;
        this.mClickListener = clickListener;
        this.mSelectedPosition = selectPosition;
    }

    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.channel_icon_textview);
        }

        TextView name;
    }
}