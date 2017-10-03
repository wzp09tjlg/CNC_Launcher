package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.gridview.BaseGridView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by jinwenchao123 on 15/9/14.
 */
public class HistoryMainAdapter extends RecyclerView.Adapter<HistoryMainAdapter.ViewHolder> implements  OnItemFocusListener<VideoSetDao> {

    private final RecyclerView.OnScrollListener mScrollListener;
    LinkedHashMap<String, ArrayList<VideoSetDao>> mData;
    private Object[] keyArray;
    private Context context;
    private boolean isOpenOrDelete = true;
    private OnItemClickListener<VideoSetDao> mOnItemClickListener;
    private OnItemFocusListener<VideoSetDao> mOnItemFocuseListener;

    public HistoryMainAdapter(Context context, LinkedHashMap<String, ArrayList<VideoSetDao>> mData,
                              OnItemClickListener<VideoSetDao> mOnItemClickListener,
                              OnItemFocusListener<VideoSetDao> mOnItemFocuseListener,RecyclerView.OnScrollListener scroll) {
        this.mData = mData;
        keyArray = mData.keySet().toArray();
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnItemFocuseListener = mOnItemFocuseListener;
        this.mScrollListener = scroll;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_history_item, parent, false);
        final VerticalGridView gridView = (VerticalGridView) view.findViewById(R.id.expende_gridview1);
        gridView.addOnScrollListener(mScrollListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ArrayList<VideoSetDao> value = mData.get(keyArray[position]);
        if (value != null){
            String day = keyArray[position].toString();
            String today = Utils.getDay(System.currentTimeMillis());
            if (today.equals(day)){
                holder.tv_key_text1.setText(context.getResources().getString(R.string.today));
            }else{
                holder.tv_key_text1.setText(keyArray[position].toString());
            }
            //这里动态的改变gridview的宽高
            ViewGroup.LayoutParams lp = holder.expende_gridview1.getLayoutParams();
            if (value.size() < 6) {
                lp.height = (int) context.getResources().getDimension(R.dimen.dimen_420dp);
            } else {
                lp.height = (int) context.getResources().getDimension(R.dimen.dimen_820dp);
            }
            holder.expende_gridview1.setLayoutParams(lp);
            holder.expende_gridview1.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
            //传入子adapter;
            final HistoryItemAdapter adapterNew;
            if(holder.expende_gridview1.getAdapter()==null){
                adapterNew = new HistoryItemAdapter(context, value, isOpenOrDelete,mOnItemClickListener, this);
                holder.expende_gridview1.setAdapter(adapterNew);
            }else{
                adapterNew = (HistoryItemAdapter) holder.expende_gridview1.getAdapter();
                adapterNew.changeOperate(isOpenOrDelete);
            }
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        adapterNew.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    public void updateView(boolean isOpenOrDelete, LinkedHashMap<String, ArrayList<VideoSetDao>> mData) {
        this.isOpenOrDelete = isOpenOrDelete;
        this.mData = mData;
        notifyItemRangeChanged(0, getItemCount());
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemFocusLister(View view, int position, VideoSetDao videoSetDao, boolean hasFocus) {
        mOnItemFocuseListener.onItemFocusLister(view,position,videoSetDao,hasFocus);
//        if(hasFocus){
//            mFocusedView = view;
//            mShadow.moveTo(view.findViewById(R.id.history_img_icon));
//        }else {
//            mShadow.setVisibility(View.GONE);
//        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_key_text1;
        VerticalGridView expende_gridview1;

        public ViewHolder(View v) {
            super(v);
            tv_key_text1 = (TextView) v.findViewById(R.id.tv_key_text1);
            expende_gridview1 = (VerticalGridView) v.findViewById(R.id.expende_gridview1);
        }
    }
}
