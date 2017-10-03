package cn.cncgroup.tv.view.homesecond.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.view.homesecond.utils.HomeTab;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * Created by futuo on 2015/8/27.
 */
public class HomeTabAdapter extends RecyclerView.Adapter<HomeTabAdapter.ViewHolder> {
    private HomeTabManager mTabManager;
    private OnItemClickListener<HomeTab> mClickListener;
    private OnItemFocusListener<HomeTab> mFocusListener;
    private RecyclerView.ViewHolder mLastViewHolder;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, getItem(position));
        }
    };

    private View.OnFocusChangeListener mOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, getItem(position), hasFocus);
        }
    };

    public HomeTabAdapter(OnItemClickListener<HomeTab> clickListener, OnItemFocusListener<HomeTab> focusListener, HomeTabManager manager) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
        this.mTabManager = manager;
        mTabManager.bindHomeTabAdapter(this);
    }

    public RecyclerView.ViewHolder getLastViewHolder() {
        return mLastViewHolder;
    }

    public void setLastViewHolder(RecyclerView.ViewHolder holder) {
        this.mLastViewHolder = holder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_title, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeTabAdapter.ViewHolder holder, int position) {
        HomeTab menu = getItem(position);
        holder.text.setText(menu.name);
        if (position == getItemCount() - 1) {
            holder.itemView.setAlpha(0);
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusListener);
    }

    @Override
    public int getItemCount() {
        return mTabManager.getHomeTabSize();
    }

    public HomeTab getItem(int position) {
        return mTabManager.getHomeTabByIndex(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
