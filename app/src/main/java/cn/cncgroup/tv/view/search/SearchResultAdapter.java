package cn.cncgroup.tv.view.search;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.Search;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by zhangbo on 15-9-15.
 */
public class SearchResultAdapter
        extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private Search[] mList;
    private OnItemClickListener<Search> mClickListener;
    private OnItemFocusListener<Search> mFocusListener;
    private int mCount = -1;

    public SearchResultAdapter(OnItemClickListener<Search> clickListener, OnItemFocusListener<Search> focusListener) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
    }

    public SearchResultAdapter(OnItemClickListener<Search> clickListener, OnItemFocusListener<Search> focusListener, int i) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
        mCount = i;
    }

    public void addData(SearchData data, boolean isClear) {
        if (mList == null || isClear) {
            mList=null;
            notifyDataSetChanged();
            mList = new Search[data.getResult().getTotal()];
        }
        int size = data.getResult().getPageSize();
        int number = data.getResult().getPageNo();
        int start = (number - 1) * size;
        for (int i = 0; i < data.getResult().getContent().size(); i++) {
            if (i + start < getItemCount()) {
                mList[i + start] = data.getResult().getContent().get(i);
            }
        }
        int total = data.getResult().getTotal();
        notifyItemRangeChanged(start, total);
    }

    public void changeData(SearchData data) {
        if (data == null) {
            mList = null;
        } else {
            mList = new Search[data.getResult().getTotal()];
            for (int i = 0; i < data.getResult().getContent().size(); i++) {
                mList[i] = data.getResult().getContent().get(i);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Search search = getItem(position);
        if (search != null) {
            holder.text.setText(search.getName());
            holder.image.setImageURI(Uri.parse(search.getImage()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClickLister(view, position, search);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mFocusListener.onItemFocusLister(view, position, search, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCount > -1) {
            return mCount;
        }
        return mList == null ? 0 : mList.length;
    }
    public Search getItem(int position) {
        return mList[position];
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
