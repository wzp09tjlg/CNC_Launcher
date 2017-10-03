package cn.cncgroup.tv.view.homesecond.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.CustomToast;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.view.homesecond.utils.HomeTab;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;

/**
 * Created by zhang on 2015/11/17.
 */
public class CustomTabAdapter extends RecyclerView.Adapter<CustomTabAdapter.ViewHolder> {
    private static final int CAPACITY = 2;
    private ArrayList<HomeTab> mChoiceTab;
    private OnItemFocusListener<HomeTab> mFocusListener;
    private ArrayList<HomeTab> mList;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            HomeTab tab = getItem(position);
            if (mChoiceTab.contains(tab)) {
                mChoiceTab.remove(tab);
                notifyItemChanged(position);
            } else {
                if (mChoiceTab.size() < CAPACITY) {
                    mChoiceTab.add(tab);
                    notifyItemChanged(position);
                } else {
                    GlobalToast.makeText(CApplication.getInstance(), "最多只能添加两个频道", 1000).show();
                }
            }
        }
    };

    private View.OnFocusChangeListener mOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, getItem(position), hasFocus);
        }
    };

    public CustomTabAdapter(OnItemFocusListener<HomeTab> focusListener) {
        this.mFocusListener = focusListener;
        this.mList = HomeTabManager.getInstance().getAllCustomTabs();
        this.mChoiceTab = new ArrayList<HomeTab>(HomeTabManager.getInstance().getAddedCustomTabs());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_tab, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeTab tab = getItem(position);
        holder.text.setText(tab.name);
        if (mChoiceTab.contains(tab)) {
            holder.image.setSelected(true);
        } else {
            holder.image.setSelected(false);
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public HomeTab getItem(int position) {
        return mList.get(position);
    }

    public ArrayList<HomeTab> getChoicePosition() {
        return mChoiceTab;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);
        }

        TextView text;
        ImageView image;
    }
}
