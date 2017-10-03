package cn.cncgroup.tv.view.homesecond.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by futuo on 2015/9/2.
 */
public class GridHorizontalAdapter
        extends RecyclerView.Adapter<GridHorizontalAdapter.ViewHolder> {
    private OnItemClickListener<VideoSet> mClickListener;
    private OnItemFocusListener<VideoSet> mFocusListener;
    private VideoSet[] mList = new VideoSet[getItemCount()];
    private boolean isLoadImage;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, mList[position]);
        }
    };

    private View.OnFocusChangeListener mOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, mList[position], hasFocus);
        }
    };

    public GridHorizontalAdapter(OnItemClickListener<VideoSet> clickListener, OnItemFocusListener<VideoSet> focusListener, boolean isLoadImage) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
        this.isLoadImage = isLoadImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_horizontal_all, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_horizontal, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position != 0) {
            VideoSet set = mList[position];
            if (set == null) {
                return;
            }
            if (isLoadImage) {
                holder.image.setImageURI(Uri.parse(set.getHorizentalPosterUrl()));
            } else {
                holder.image.setImageURI(null);
            }
            holder.text.setText(set.getName());
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusListener);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public VideoSet getItem(int position) {
        return mList[position];
    }

    public VideoSet[] getList() {
        return mList;
    }

    public void bindData(ArrayList<VideoSet> list) {
        for (int i = 1; i < getItemCount(); i++) {
            mList[i] = list.get(i - 1);
        }
        notifyItemRangeChanged(1, getItemCount());
    }

    public void restoreImage() {
        if (!isLoadImage) {
            isLoadImage = true;
            notifyItemRangeChanged(1, getItemCount());
        }
    }

    public void clearImage() {
        if (isLoadImage) {
            isLoadImage = false;
            notifyItemRangeChanged(1, getItemCount());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
