package cn.cncgroup.tv.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.UIUtils;

/**
 * 影视推荐Adapter
 * Created by zhangguang 2015/5/23.
 */
public class MovieDetailRecomAdapter extends
        RecyclerView.Adapter<MovieDetailRecomAdapter.ViewHolder> {
    private RecommendVideoSetListData mData;
    private OnItemClickListener<VideoSet> mOnItemClickListener;
    private OnItemFocusListener<VideoSet> mOnItemFocuseListener;

    public MovieDetailRecomAdapter(RecommendVideoSetListData data, OnItemClickListener<VideoSet> onItemClickListener, OnItemFocusListener<VideoSet> focusListener) {
        this.mData = data;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnItemFocuseListener = focusListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_movie_recommend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoSet videoSet = mData.result.get(position);
        Uri[] requestUriArray = {Uri.parse(videoSet.getVerticalPosterUrl()), Uri.parse(videoSet.getImage())};
        UIUtils.setFrescoImageRequests(holder.image, requestUriArray);
        holder.itemView.setDrawingCacheEnabled(true);
        holder.text.setText(videoSet.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClickLister(view, position, videoSet);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mOnItemFocuseListener.onItemFocusLister(view, position, videoSet, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.result.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView image;
        TextView text;

        public ViewHolder(View v) {
            super(v);
            image = (SimpleDraweeView) v.findViewById(R.id.image);
            text = (TextView) v.findViewById(R.id.text);
        }
    }
}
