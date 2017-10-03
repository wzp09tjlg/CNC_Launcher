package cn.cncgroup.tv.view.subject;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by zhang on 2015/10/23.
 */
public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {
    private boolean isLoadImage = true;
    private SubjectSet[] mList;
    private OnItemFocusListener<SubjectSet> mFocusListener;
    private OnItemClickListener<SubjectSet> mClickListener;

    public SubjectListAdapter(OnItemFocusListener<SubjectSet> focusListener,
                              OnItemClickListener<SubjectSet> clickListener) {
        this.mFocusListener = focusListener;
        this.mClickListener = clickListener;
    }

    public void bindData(SubjectSetData data) {
        boolean flag = false;
        if (mList == null) {
            mList = new SubjectSet[data.getTotal()];
            flag = true;
        }
        int start = (data.getPageNo() - 1) * data.getPageSize();
        int size = data.getResult().size();
        if (data.getPageNo() == data.getPageCount()) {
            start = data.getTotal() - data.getPageSize();
        }
        int length = mList.length;
        for (int i = 0; i < size; i++) {
            if (start + i < length) {
                mList[start + i] = data.getResult().get(i);
            }
        }
        if (flag) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(start, start + size);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubjectSet set = getItem(position);
        if (set == null) {
            holder.image.setImageURI(null);
            holder.title.setText(null);
        } else {
            if (isLoadImage) {
                holder.image.setImageURI(Uri.parse(set.getImage()));
            } else {
                holder.image.setImageURI(null);
            }
            holder.title.setText(set.getName());
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = getPosition(v);
            mClickListener.onItemClickLister(v, position, getItem(position));
        }
    };

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = getPosition(v);
            mFocusListener.onItemFocusLister(v, position, getItem(position),
                    hasFocus);
        }
    };

    private int getPosition(View view) {
        return ((RecyclerView.LayoutParams) view.getLayoutParams())
                .getViewAdapterPosition();
    }

    public SubjectSet getItem(int position) {
        return mList[position];
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }

        SimpleDraweeView image;
        TextView title;
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
