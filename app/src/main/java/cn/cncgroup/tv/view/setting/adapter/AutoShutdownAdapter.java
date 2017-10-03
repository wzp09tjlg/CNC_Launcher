package cn.cncgroup.tv.view.setting.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.view.setting.bean.SettingBean;

/**
 * Created by Walter on 2015/11/18.
 */
public class AutoShutdownAdapter extends RecyclerView.Adapter<AutoShutdownAdapter.ViewHolder> {
    private static final String TAG = "AutoShutdownAdapter";
    private ArrayList<SettingBean> mList;
    private OnItemClickListener<SettingBean> mOnItemClickListener;
    private OnItemFocusListener<SettingBean> mOnItemFocusListener;

    public AutoShutdownAdapter(ArrayList<SettingBean> list,
                               OnItemClickListener<SettingBean> onItemClickListener,
                               OnItemFocusListener<SettingBean> onItemFocusListener) {
        mList = list;
        mOnItemClickListener = onItemClickListener;
        mOnItemFocusListener = onItemFocusListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_shutdown, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SettingBean bean = mList.get(position);
        if (bean.isState()) {
            holder.mImgSwitch.setImageLevel(1);
            holder.mImgSwitch.setTag(1);
        } else {
            holder.mImgSwitch.setImageLevel(0);
            holder.mImgSwitch.setTag(0);
        }
        holder.mMenu.setText(bean.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelList = (Integer) holder.mImgSwitch.getTag();
                if (levelList == 0) {
                    holder.mImgSwitch.setImageLevel(1);
                    holder.mImgSwitch.setTag(1);
                } else {
                    holder.mImgSwitch.setImageLevel(0);
                    holder.mImgSwitch.setTag(0);
                }
                mOnItemClickListener.onItemClickLister(v, position, bean);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int levelList = (Integer) holder.mImgSwitch.getTag();
                    if (levelList == 1) {
                        holder.itemView.setSelected(true);
                    }
                } else {
                    holder.itemView.setSelected(false);
                }
                mOnItemFocusListener.onItemFocusLister(v, position, bean, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mSymbol;
        private TextView mMenu;
        private ImageView mImgSwitch;
        private View mLine;

        public ViewHolder(View view) {
            super(view);
            mSymbol = view.findViewById(R.id.shutdown_view_symbol);
            mMenu = (TextView) view.findViewById(R.id.shutdown_text_menu);
            mImgSwitch = (ImageView) view.findViewById(R.id.shutdown_img_switch);
            mLine = view.findViewById(R.id.shutdown_view_line);
        }
    }
}
