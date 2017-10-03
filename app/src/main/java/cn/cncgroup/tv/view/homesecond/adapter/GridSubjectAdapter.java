package cn.cncgroup.tv.view.homesecond.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by zhang on 2015/11/12.
 */
public class GridSubjectAdapter extends RecyclerView.Adapter<GridSubjectAdapter.ViewHolder> {
    private OnItemClickListener<SubjectSet> mClickListener;
    private OnItemFocusListener<SubjectSet> mFocusListener;
    private SubjectSet[] mList = new SubjectSet[getItemCount()];
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

    public GridSubjectAdapter(OnItemClickListener<SubjectSet> clickListener, OnItemFocusListener<SubjectSet> focusListener, boolean isLoadImage) {
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
            SubjectSet subject = mList[position];
            if (subject == null) {
                return;
            }
            if (isLoadImage) {
                holder.image.setImageURI(Uri.parse(subject.getImage()));
            } else {
                holder.image.setImageURI(null);
            }
            holder.text.setText(subject.getName());
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

    public SubjectSet getItem(int position) {
        return mList[position];
    }

    public SubjectSet[] getList() {
        return mList;
    }

    public void bindData(List<SubjectSet> list) {
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
