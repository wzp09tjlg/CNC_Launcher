package cn.cncgroup.tv.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.view.app.CleanCacheFragment;
import cn.cncgroup.tv.view.app.UnloadFragment;

/**
 * 关于Adapter Created by zhangguang on .
 */
public class AppSetMenuAdapter extends
        RecyclerView.Adapter<AppSetMenuAdapter.ViewHolder> {

    private ArrayList<AppSetMenuItem> mList = new ArrayList<AppSetMenuItem>();
    private int mSelectedPosition = -1;
    private OnItemFocusListener<AppSetMenuItem> mFocusListener;


    public AppSetMenuAdapter(OnItemFocusListener<AppSetMenuItem> mFocusListener) {
        mList.add(new AppSetMenuItem("卸载应用", UnloadFragment.class));
        mList.add(new AppSetMenuItem("清除缓存", CleanCacheFragment.class));
        this.mFocusListener = mFocusListener;

    }

    @Override
    public AppSetMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_display_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AppSetMenuItem menu = mList.get(position);
        holder.text.setText(menu.name);
        holder.itemView
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        mFocusListener.onItemFocusLister(v, position, menu,
                                hasFocus);
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
        notifyItemChanged(position);
        notifyItemChanged(temp);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }


    public class AppSetMenuItem {
        public String name;
        public Class<? extends Fragment> fragment;

        public AppSetMenuItem(String name,Class<? extends Fragment> fragment) {
            this.name = name;
            this.fragment = fragment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<? extends Fragment> getFragment() {
            return fragment;
        }

        public void setFragment(Class<? extends Fragment> fragment) {
            this.fragment = fragment;
        }
    }


}
