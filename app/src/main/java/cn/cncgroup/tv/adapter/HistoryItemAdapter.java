package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by jinwenchao123 on 15/9/14.
 */
public class HistoryItemAdapter
        extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private List<VideoSetDao> mData;
    private Context context;
    private boolean isOpenOrDelete = false;
    private OnItemClickListener<VideoSetDao> mOnItemClickListener;
    private OnItemFocusListener<VideoSetDao> mOnItemFocuseListener;

    public HistoryItemAdapter(Context context, List<VideoSetDao> mData,
                              boolean isOpenOrDelete,
                              OnItemClickListener<VideoSetDao> mOnItemClickListener,
                              OnItemFocusListener<VideoSetDao> mOnItemFocuseListener) {
        this.mData = mData;
        this.context = context;
        this.mOnItemFocuseListener = mOnItemFocuseListener;
        this.mOnItemClickListener = mOnItemClickListener;
        this.isOpenOrDelete = isOpenOrDelete;
    }

    public void changeOperate(boolean isOpenOrDelete) {
        this.isOpenOrDelete = isOpenOrDelete;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoSetDao videoSet = mData.get(position);
        if (videoSet.image != null) {
            holder.history_img_icon.setImageURI(Uri.parse(videoSet.image));
        }
        holder.history_text_name.setText(videoSet.name);
        try {
            holder.history_play_time.setText(Utils.milltimeToHour(videoSet.startTime));
            holder.pb_play_time.setProgress((videoSet.startTime * 100) / videoSet.length);
        } catch (Exception e) {
            holder.history_play_time.setText("00:00");
            holder.pb_play_time.setProgress(0);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClickLister(view, position,
                        videoSet);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mOnItemFocuseListener.onItemFocusLister(view, position, videoSet, b);
            }
        });
        if (holder.itemView.isFocused()) {
            mOnItemFocuseListener.onItemFocusLister(holder.itemView, position, videoSet, true);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView history_img_icon;
        private RelativeLayout rl_history_delete;
        private TextView history_text_name;
        private RelativeLayout rl_focus_history;
        private TextView history_play_time;
        private ProgressBar pb_play_time;

        public ViewHolder(View v) {
            super(v);
            history_img_icon = (SimpleDraweeView) v.findViewById(R.id.history_img_icon);
            history_text_name = (TextView) v.findViewById(R.id.history_text_name);
            history_play_time = (TextView) v.findViewById(R.id.history_play_time);
            rl_history_delete = (RelativeLayout) v.findViewById(R.id.rl_history_delete);
            rl_focus_history = (RelativeLayout) v.findViewById(R.id.rl_focus_history);
            pb_play_time = (ProgressBar) v.findViewById(R.id.pb_play_time);

        }
    }
}
