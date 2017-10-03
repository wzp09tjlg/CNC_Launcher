package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.DateUtil;

/**
 * Created by Walter on 2015/9/18.
 */
public class VarietyDetailAdapter extends
        RecyclerView.Adapter<VarietyDetailAdapter.VarietyDetailViewHolder> {
    private final boolean isShouldPlay;
    private Video[] array;
    private OnItemClickListener<Video> mItemClickListener; // 点击事件
    private OnItemFocusListener<Video> mItemFocusListener; // 获得焦点
    private Context mContext;
    private boolean flag = true;
    private int mTotal;
    private int mSelectedPos = -1;
    private VarietyDetailViewHolder mSelectedHolder;
    private View.OnKeyListener mKeyListener;
    private Handler mHandler = new Handler();

    public VarietyDetailAdapter(Context context, int total, OnItemClickListener<Video> clickListener, OnItemFocusListener<Video> focusListener) {
        mContext = context;
        mItemClickListener = clickListener;
        mItemFocusListener = focusListener;
        isShouldPlay = Project.get().getConfig().isShouldPlay();
        mTotal = total;
        mSelectedPos = 0;
    }

    public void setOnKeyListener(View.OnKeyListener listener) {
        mKeyListener = listener;
    }

    @Override
    public VarietyDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VarietyDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_variety_detail_no_pic, parent, false));
    }

    public void setSelectedPos(View view, int pos) {
        int temp = mSelectedPos;
        mSelectedPos = pos;
        notifyItemChanged(temp);
        notifyItemChanged(mSelectedPos);
        view.setSelected(true);
    }

    @Override
    public void onBindViewHolder(final VarietyDetailViewHolder holder, final int position) {
        if (isShouldPlay)
            holder.itemView.setSelected(position == mSelectedPos);
        final Video video = array[position];
        if (video == null) {
            holder.time.setText("");
            holder.desc.setText("");
        } else {
            holder.time.setText("" + DateUtil.getYear(video.getYear()) + mContext.getResources().getString(R.string.detail_stage).trim());
            holder.desc.setText("" + video.getDesc().trim());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShouldPlay) {
                        setSelectedPos(holder.itemView, position);
                    }
                    mItemClickListener.onItemClickLister(v, position, video);
                }
            });
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mItemFocusListener.onItemFocusLister(v, position, video, hasFocus);
                }
            });
            holder.itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (mKeyListener != null)
                        mKeyListener.onKey(v, keyCode, event);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 : array.length;
    }

    public void addData(VideoListData result) {
        if (array == null) {
//            int temp = result.result.size() > mTotal ? result.result.size()
//                    : mTotal;
            array = new Video[result.result.size()];
        }
        int start = (result.pageNo - 1) * result.pageSize;
        int size = result.result.size();
        int length = array.length;
        for (int i = 0; i < size; i++) {
            if (start + i < length) {
                array[start + i] = result.result.get(i);
            }
        }
        if (flag) {
            notifyDataSetChanged();
            flag = false;
        } else {
            notifyItemRangeChanged(start, size);
        }
    }

    public Video getItem(int position) {
        return array[position];
    }

    public static class VarietyDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView desc;

        public VarietyDetailViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            desc = (TextView) view.findViewById(R.id.desc);
        }
    }


}
