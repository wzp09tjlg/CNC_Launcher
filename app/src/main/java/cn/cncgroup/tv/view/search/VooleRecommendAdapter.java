package cn.cncgroup.tv.view.search;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by yqh on 15-11-13.
 */
public class VooleRecommendAdapter
        extends RecyclerView.Adapter<VooleRecommendAdapter.ViewHolder> {

    private Video[] mList;
    private OnItemClickListener<Video> mClickListener;
    private OnItemFocusListener<Video> mFocusListener;

    public VooleRecommendAdapter(OnItemClickListener<Video> clickListener, OnItemFocusListener<Video> focusListener) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
    }

    public void addData(VideoListData data) {
        if (mList == null) {
            mList = new Video[data.result.size()];
        }
        int size = data.pageSize;
        int number = data.pageNo;
        int start = (number - 1) * size;
        for (int i = 0; i < data.result.size(); i++) {
            if (i + start < getItemCount()) {
                mList[i + start] = data.result.get(i);
            }
        }
        int total = data.result.size();
        notifyItemRangeChanged(start, total);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Video video = getItem(position);
        Log.i("video","video="+video);
        if (video != null) {
            holder.text.setText(video.desc);
            holder.image.setImageURI(Uri.parse(video.getImage()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClickLister(view, position, video);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mFocusListener.onItemFocusLister(view, position, video, hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : (mList.length>8?8:mList.length);
    }

    public Video getItem(int position) {
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
