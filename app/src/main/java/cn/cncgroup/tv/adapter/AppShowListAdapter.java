package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by jinwenchao123 on 15/9/24.
 */
public class AppShowListAdapter extends RecyclerView.Adapter<AppShowListAdapter.ViewHolder> {
    Context context;
	private ArrayList<AppInfo> mList = new ArrayList<AppInfo>();
    private OnItemClickListener<AppInfo> mClickListener;
    private OnItemFocusListener<AppInfo> mFocusListener;

    public AppShowListAdapter(Context context, ArrayList<AppInfo> mList, OnItemClickListener<AppInfo> clickListener,OnItemFocusListener<AppInfo> focusListener) {
        this.mList = mList;
        this.context = context;
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_app_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AppInfo item = mList.get(position);
        holder.icon.setBackground(item.icon);
        holder.name.setText(item.appName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClickLister(view, position, item);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              mFocusListener.onItemFocusLister(v,position,item,hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

	public void setList(ArrayList<AppInfo> list)
	{
		mList = list;
	}

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_open_history);
            name = (TextView) itemView.findViewById(R.id.tv_open_history);
        }
    }
}
