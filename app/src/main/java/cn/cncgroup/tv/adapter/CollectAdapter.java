package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * 收藏adapter
 * Created by jinwenchao
 */
public class CollectAdapter extends
        RecyclerView.Adapter<CollectAdapter.ViewHolder> {
    private List<VideoSetDao> mData;
    private Context context;
    private OnItemClickListener<VideoSetDao> mOnItemClickListener;
    private OnItemFocusListener<VideoSetDao> mOnItemFocuseListener;
    public CollectAdapter(Context context, List<VideoSetDao> mData,
                          OnItemClickListener<VideoSetDao> onItemClickListener,
                          OnItemFocusListener<VideoSetDao> focusListener) {
        this.mData = mData;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnItemFocuseListener = focusListener;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.collect_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final VideoSetDao videoSet = mData.get(position);
//        if (videoSet.seriesType == 1 && (videoSet.count != videoSet.total)){
//            holder.tv_footmark_msg.setVisibility(View.VISIBLE);
//            holder.tv_footmark_msg.setText(context.getString(R.string.detail_renewal,
//                    videoSet.count));
//        }else{
//            holder.tv_footmark_msg.setVisibility(View.GONE);
//        }
        holder.iv_footmark_icon.setImageURI(Uri.parse(videoSet.image));
        holder.tv_footmark_name.setText(videoSet.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClickLister(view,position,videoSet);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mOnItemFocuseListener.onItemFocusLister(view,position,videoSet,b);
            }
        });
        if(holder.itemView.isFocused()){
            mOnItemFocuseListener.onItemFocusLister(holder.itemView,position,videoSet,true);
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setmData(List<VideoSetDao> mData){
        this.mData = mData;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_footmark_icon;
        RelativeLayout rl_delete;
        TextView tv_footmark_name;
//        TextView tv_footmark_msg;

        public ViewHolder(View v) {
            super(v);
            iv_footmark_icon = (SimpleDraweeView) v
                    .findViewById(R.id.iv_footmark_icon);
            tv_footmark_name = (TextView) v.findViewById(R.id.tv_footmark_name);
//            tv_footmark_msg = (TextView) v.findViewById(R.id.tv_footmark_msg);
            rl_delete = (RelativeLayout) v.findViewById(R.id.rl_delete);
        }
    }
}
