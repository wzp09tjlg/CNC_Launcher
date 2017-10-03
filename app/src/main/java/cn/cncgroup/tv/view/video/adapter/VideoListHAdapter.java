package cn.cncgroup.tv.view.video.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.MarqueeTextView;
import cn.cncgroup.tv.utils.DateUtil;

/**
 * Created by Wu on 2015/9/10.
 */
public class VideoListHAdapter extends RecyclerView.Adapter<VideoListHAdapter.ViewHolder> {
    private boolean isLoadImage = true;
    private int mShowType;
    private VideoSet[] mArr;
    private OnItemFocusListener<VideoSet> mFocusListener;
    private OnItemClickListener<VideoSet> mClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, getItem(position));
        }
    };
    private View.OnFocusChangeListener mOnFocuseChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, getItem(position), hasFocus);
        }
    };

    public VideoListHAdapter(OnItemFocusListener<VideoSet> focusListener,
                             OnItemClickListener<VideoSet> clickListener, int showType) {
        mFocusListener = focusListener;
        mClickListener = clickListener;
        mShowType = showType;
    }

    @Override
    public int getItemCount() {
        return mArr == null ? 0 : mArr.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list_h, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoSet videoSet = mArr[position];
        if (videoSet == null) {
            holder.icon.setImageURI(null);
            holder.desc.setText("");
            holder.name.setText("");
            holder.periods.setText("");
        } else {
            if (isLoadImage) {
                holder.icon.setImageURI(Uri.parse(videoSet.getVerticalPosterUrl()));
            } else {
                holder.icon.setImageURI(null);
            }
            switch (mShowType) {
                case -1: // 娱乐 生活
                    holder.desc.setVisibility(View.VISIBLE);
                    if (videoSet.desc == null || videoSet.desc.equals(""))
                        holder.desc.setText("" + videoSet.name);
                    else
                        holder.desc.setText("" + videoSet.desc);
                    break;
                case -2: // 综艺
                    if (videoSet.getSubject() != null && !videoSet.getSubject().equals(""))
                        holder.desc.setText("" + videoSet.getSubject());
                    Log.i("tag","getSubject:"+videoSet.getSubject()+"getName:"+videoSet.getName());
                    break;
                case -3: // 专题
                    break;
                default:
                    holder.desc.setVisibility(View.VISIBLE);
                    if (videoSet.desc == null || videoSet.desc.equals(""))
                        holder.desc.setText("" + videoSet.name);
                    else
                        holder.desc.setText("" + videoSet.desc);
            }
            if (videoSet.getIssueTime() != null && !videoSet.getIssueTime().equals("")) {
                if (videoSet.getIssueTime().contains("不详")) {
                    holder.periods.setText("" +  DateUtil.getYear(videoSet.getIssueTime()));
                } else {
                    holder.periods.setText("" + DateUtil.getYear(videoSet.getIssueTime()) + "期");
                }
            } else {
                holder.periods.setText("不详");
            }
            holder.name.setText(videoSet.name);
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocuseChangeListener);
    }

    public void addData(VideoSetListData result) {
        boolean flag;
        if (mArr == null) {
            mArr = new VideoSet[result.getResult().getTotal()];
            flag = true;
        } else {
            flag = false;
        }
        int start = (result.getResult().getPageNo() - 1) * result.getResult().getPageSize();
        int size = result.getResult().getContent().size(); // TODO 多次点击筛选会崩掉
        int length = mArr.length;
        for (int i = 0; i < size; i++) {
            if (start + i < length) {
                mArr[start + i] = result.getResult().getContent().get(i);
            }
        }
        if (flag) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(start, size);
        }
    }

    public VideoSet getItem(int position) {
        if (position >= mArr.length)
            return mArr[mArr.length];
        else
            return mArr[position];
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView icon;
        private MarqueeTextView desc;
        private TextView name;
        private TextView periods;

        public ViewHolder(View view) {
            super(view);
            icon = (SimpleDraweeView) view.findViewById(R.id.image);
            desc = (MarqueeTextView) view.findViewById(R.id.desc);
            name = (TextView) view.findViewById(R.id.name);
            periods = (TextView) view.findViewById(R.id.periods);
        }
    }

    public void restoreImage() {
        if (!isLoadImage) {
            isLoadImage = true;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void clearImage() {
        if (isLoadImage) {
            isLoadImage = false;
            notifyItemRangeChanged(0, getItemCount());
        }
    }
}
