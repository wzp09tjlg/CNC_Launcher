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
 * Created by zhang on 2015/9/22.
 */
public class GalleryVerticalAdapter
        extends RecyclerView.Adapter<GalleryVerticalAdapter.ViewHolder> {
    private static final String TAG = "GalleryHorizontalAdapter";
    private Bitmap[] mReflection = new Bitmap[getItemCount()];
    private OnItemClickListener<VideoSet> mClickListener;
    private OnItemFocusListener<VideoSet> mFocusListener;
    private VideoSet[] mList = new VideoSet[getItemCount()];
    private boolean isLoadImage;
    private Handler mHandler = new Handler();
    private View.OnFocusChangeListener mOnFocuseChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, mList[position], hasFocus);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, mList[position]);
        }
    };

    public GalleryVerticalAdapter(OnItemClickListener<VideoSet> clickListener, OnItemFocusListener<VideoSet> focuseListener, boolean isLoadImage) {
        this.mClickListener = clickListener;
        this.isLoadImage = isLoadImage;
        this.mFocusListener = focuseListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoSet set = mList[position];
        if (set == null) {
            return;
        }
        Log.i(TAG, "GalleryVerticalAdapter position:" + position);
        if (isLoadImage) {
            holder.image.setImageURI(Uri.parse(set.getVerticalPosterUrl()));
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
        holder.text.setText(set.getName());
        holder.text.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocuseChangeListener);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public void bindData(ArrayList<VideoSet> list) {
        Log.i(TAG,"bindData"+list.get(0).getName());
        for (int i = 0; i < getItemCount(); i++) {
            mList[i] = list.get(i);
        }
        notifyItemRangeChanged(0, getItemCount());
    }

    public void restoreImage() {
        if (!isLoadImage) {
            isLoadImage = true;
            Log.i(TAG,"Adapter restoreImage");
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void clearImage() {
        if (isLoadImage) {
            isLoadImage = false;
            Log.i(TAG,"Adapter clearImage");
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
