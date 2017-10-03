package cn.cncgroup.tv.view.video.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Walter on 2015/9/9.
 */
public class VideoListVAdapter extends RecyclerView.Adapter<VideoListVAdapter.ViewHolder> {
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

    public VideoListVAdapter(OnItemFocusListener<VideoSet> focusListener,
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
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_list_v, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final VideoSet videoSet = mArr[position];
        if (videoSet == null) {
            holder.icon.setImageURI(null);
            holder.desc.setText("");
            holder.name.setText("");
            holder.score.setText("");
        } else {
            // 根据 影视类型 分别显示是 1简介 2剧集数 3主演
            if (isLoadImage) {
                holder.icon.setImageURI(Uri.parse(videoSet.getVerticalPosterUrl()));
            } else {
                holder.icon.setImageURI(null);
            }
            switch (mShowType) {
                case 1:// 纪录片
                case 2:// 电影、少儿、动漫
                    holder.desc.setVisibility(View.VISIBLE);
                    if (videoSet.desc == null || videoSet.desc.equals(""))
                        holder.desc.setText("" + videoSet.name);
                    else
                        holder.desc.setText("" + videoSet.desc);
                    break;
                case 3:// 音乐
                    holder.desc.setVisibility(View.VISIBLE);
                    if (videoSet.actors == null || videoSet.actors.equals(""))
                        holder.desc.setText("" + videoSet.name);
                    else
                        holder.desc.setText("" + videoSet.actors);
                    break;
                case 4:// 电视剧
                    if (videoSet.count == videoSet.total)
                        holder.desc.setText("" + videoSet.total + "集全");
                    else
                        holder.desc.setText("更新至" + videoSet.total + "集");
                    break;
                default:
                    holder.desc.setVisibility(View.VISIBLE);
                    if (videoSet.desc == null || videoSet.desc.equals(""))
                        holder.desc.setText("" + videoSet.name);
                    else
                        holder.desc.setText("" + videoSet.desc);

            }
            if (videoSet.score != null && !videoSet.score.equals(""))
                holder.score.setText("" + videoSet.score + "分");
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
        int start = (result.getResult().getPageNo() - 1)
                * result.getResult().getPageSize();
        int size = result.getResult().getContent().size();
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
        private TextView desc;
        private TextView name;
        private TextView score;

        public ViewHolder(View view) {
            super(view);
            icon = (SimpleDraweeView) view.findViewById(R.id.image);
            desc = (TextView) view.findViewById(R.id.desc);
            name = (TextView) view.findViewById(R.id.name);
            score = (TextView) view.findViewById(R.id.score);
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
