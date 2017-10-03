package cn.cncgroup.tv.cncplayer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerConstants;
import cn.cncgroup.tv.cncplayer.callback.OnDefitionFocusChangeListener;
import cn.cncgroup.tv.cncplayer.widget.selectview.Constant;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;

/**
 * 分辨率Adapter
 * Created by zhangguang on 2015/8/28.
 */
public class DefitionAdapter extends RecyclerView.Adapter<DefitionAdapter.ViewHolders> {

    private ArrayList<String> mList = new ArrayList<String>();
    private OnItemClickListener<String> mOnItemClickListener;
    private Context mContext;
    private OnDefitionFocusChangeListener mOnFocusChangeListener;
    private static final String TAG = "DefitionAdapter";

    public DefitionAdapter(Context context, OnItemClickListener<String> onItemClickListener, OnDefitionFocusChangeListener onFocusChangeListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mOnFocusChangeListener = onFocusChangeListener;
    }

    public void setmListData(ArrayList<String> list) {
        this.mList = list;
    }

    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(mContext).inflate(
                R.layout.palyer_defition_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolders holder, final int position) {
        final String defition = mList.get(position);
        Log.i(TAG, "onBindViewHolder:" + position);
        if (defition == null) {
            holder.tv_defition.setText("");
        } else {
            holder.tv_defition.setText(defition);
            holder.itemView.setTag(position);
        }
        if (position == PlayerConstants.CURRENT_DEFINATION_INDEX) {
            holder.tv_defition.setTextColor(Color.WHITE);
            if (Constant.ITEM_COUNT == 1) {
                holder.itemView.requestFocus();
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HorizontalGridView paraent = (HorizontalGridView) view.getParent();
                Resources res = mContext.getResources();
                TextView tv = (TextView) paraent.getChildAt(PlayerConstants.CURRENT_DEFINATION_INDEX).findViewById(R.id.defition_item_tv_defition);
                tv.setTextColor(res.getColor(R.color.white_40));
                mOnItemClickListener.onItemClickLister(view, position, defition);
                TextView tvAfter = (TextView) paraent.getChildAt(PlayerConstants.CURRENT_DEFINATION_INDEX).findViewById(R.id.defition_item_tv_defition);
                tvAfter.setTextColor(res.getColor(R.color.white));

            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mOnFocusChangeListener.onDefitionFocusChange(holder.itemView, hasFocus, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "AdapterListSize:" + mList.size());
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolders extends RecyclerView.ViewHolder {
        private TextView tv_defition;

        public ViewHolders(View itemView) {
            super(itemView);
            tv_defition = (TextView) itemView.findViewById(R.id.defition_item_tv_defition);
        }
    }
}
