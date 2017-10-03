package cn.cncgroup.tv.view.homesecond.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.ImageReflectedUtil;

/**
 * Created by zhang on 2015/8/31.
 */
public class GalleryHorizontalAdapter extends RecyclerView.Adapter<GalleryHorizontalAdapter.ViewHolder> {
    private static final String TAG = "GalleryHorizontalAdapter";
    private OnItemClickListener<VideoSet> mClickListener;
    private VideoSet[] mList = new VideoSet[getItemCount()];
    private Bitmap[] mReflection = new Bitmap[getItemCount()];
    private OnItemFocusListener<VideoSet> mFocusListener;
    private boolean isLoadImage;
    private Handler mHandler = new Handler();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, mList[position]);
        }
    };
    private View.OnFocusChangeListener mOnFocuseChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, mList[position], hasFocus);
        }
    };

    public GalleryHorizontalAdapter(OnItemClickListener<VideoSet> listener, OnItemFocusListener<VideoSet> focuseListener, boolean isLoadImage) {
        this.mClickListener = listener;
        this.isLoadImage = isLoadImage;
        this.mFocusListener = focuseListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoSet videoSet = mList[position];
        if (videoSet == null) {
            return;
        }
        if (isLoadImage) {
            holder.image.setImageURI(Uri.parse(videoSet.getHorizentalPosterUrl()));
            if (mReflection[position] != null && !mReflection[position].isRecycled()) {
                holder.reflection.setImageBitmap(mReflection[position]);
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mReflection[position] = ImageReflectedUtil.createHomeReflectedImage(holder.image);
                        holder.reflection.setImageBitmap(mReflection[position]);
                    }
                }, 800);
            }
        } else {
            holder.image.setImageURI(null);
            mHandler.removeCallbacksAndMessages(null);
        }
        holder.text.setText(videoSet.getName());
        Log.i(TAG,"GalleryHorizontalAdapter position:"+position);
        holder.text.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocuseChangeListener);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void bindData(ArrayList<VideoSet> list) {
        for (int i = 0; i < getItemCount(); i++) {
            mList[i] = list.get(i);
        }
        Log.i(TAG,"bindData"+"ListSize:"+list.size()+list.get(0).getName());
        notifyItemRangeChanged(0, getItemCount());
    }

    public void restoreImage() {
        if (!isLoadImage) {
            Log.i(TAG,"restoreImage");
            isLoadImage = true;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void clearImage() {
        if (isLoadImage) {
            Log.i(TAG,"clearImage");
            isLoadImage = false;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView text;
        ImageView reflection;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            reflection = (ImageView) itemView.findViewById(R.id.reflection);
        }
    }
}
