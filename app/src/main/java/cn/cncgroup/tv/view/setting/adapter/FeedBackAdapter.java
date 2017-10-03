package cn.cncgroup.tv.view.setting.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;

/**
 * 问题反馈Adapter
 * Created by zhangguang on 2015/11/25.
 */
public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHoler> {

    private OnItemClickListener<FeedBackBean> mOnItemClickListener;

    public FeedBackAdapter(OnItemClickListener<FeedBackBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;

    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_feedback, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, final int position) {
        final FeedBackBean feedBackBean = getFeedBackList().get(position);
        holder.mTextView.setText(feedBackBean.desc);
        holder.mImageView.setImageResource(feedBackBean.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickLister(v, position, feedBackBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getFeedBackList().size();
    }

    static class ViewHoler extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        public ViewHoler(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.feedback_item_image);
            mTextView = (TextView) itemView.findViewById(R.id.feedback_item_desc);
        }
    }

    public static class FeedBackBean {
        public int imageView;
        public String desc;

        public FeedBackBean(int image, String descStr) {
            imageView = image;
            desc = descStr;
        }

    }

    static ArrayList<FeedBackBean> getFeedBackList() {
        ArrayList<FeedBackBean> list = new ArrayList<FeedBackBean>();
        list.add(new FeedBackBean(R.drawable.fk_movie_less, "影片太少"));
        list.add(new FeedBackBean(R.drawable.fk_operation_complex, "操作繁琐"));
        list.add(new FeedBackBean(R.drawable.fk_dodge, "有闪退现象"));
        list.add(new FeedBackBean(R.drawable.fk_unaesthetics, "界面不美观"));
        list.add(new FeedBackBean(R.drawable.fk_unaction, "想要的功能没有"));
        return list;
    }
}
