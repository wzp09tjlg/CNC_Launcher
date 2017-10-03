package cn.cncgroup.tv.view.subject;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.Subject;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zhang on 2015/10/26.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private boolean isLoadImage = true;
    private Subject[] mList;
    private OnItemFocusListener<Subject> mFocusListener;
    private OnItemClickListener<Subject> mClickListener;

    public SubjectAdapter(OnItemFocusListener<Subject> focusListener,
                          OnItemClickListener<Subject> clickListener) {
        this.mFocusListener = focusListener;
        this.mClickListener = clickListener;
    }

    public void bindData(SubjectData data) {
        boolean flag = false;
        if (mList == null) {
            mList = new Subject[data.getTotal()];
            flag = true;
        }
        int start = (data.getPageNo() - 1) * data.getPageSize();
        int size = data.getResult().size();
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
                .inflate(R.layout.item_subject, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subject set = getItem(position);
        if (isLoadImage) {
            holder.image.setImageURI(Uri.parse(set.getImage()));
        } else {
            holder.image.setImageURI(null);
        }
        holder.text.setText(set.getName());
        if (set.getType().equals(CategoryUtils.getCategoryByName("综艺").getId())) {
            holder.desc.setText(set.getFocus());
        } else if (set.getType().equals(CategoryUtils.getCategoryByName("电视剧").getId())) {
            holder.desc.setText("更新至" + set.getUpdateCount() + "集");
        } else {
            holder.desc.setText(set.getActors());
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
    }

    public Subject getItem(int position) {
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
            text = (TextView) itemView.findViewById(R.id.text);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }

        SimpleDraweeView image;
        TextView text;
        TextView desc;
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
        return ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
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
