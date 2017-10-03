package cn.cncgroup.tv.view.homesecond.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by zhang on 2015/10/19.
 */
public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryItem> mList = new ArrayList<CategoryItem>();
    private OnItemFocusListener<CategoryItem> mFocusListener;
    private OnItemClickListener<CategoryItem> mClickListener;
    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, getItem(position), hasFocus);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, getItem(position));
        }
    };

    public CategoryAdapter(OnItemFocusListener<CategoryItem> focusListener,
                           OnItemClickListener<CategoryItem> clickListener) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
        mList.add(new CategoryItem(2, R.drawable.category_subject,
                R.color.category_subject, "专题"));
        mList.add(new CategoryItem(2, R.drawable.category_children,
                R.color.category_children, "少儿"));
        mList.add(new CategoryItem(2, R.drawable.category_anim,
                R.color.category_anim, "动漫"));
        mList.add(new CategoryItem(1, R.drawable.category_life,
                R.color.category_life, "生活"));
        mList.add(new CategoryItem(1, R.drawable.category_music,
                R.color.category_music, "音乐"));
        mList.add(new CategoryItem(1, R.drawable.category_3d,
                R.color.category_3D, "3D体验"));
        mList.add(new CategoryItem(1, R.drawable.category_record,
                R.color.category_record, "纪录片"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_small, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_big, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryItem item = getItem(position);
        holder.switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image = new ImageView(holder.itemView.getContext());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return image;
            }
        });
        holder.switcher.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(item.image));
        holder.text.setText(item.name);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        GradientDrawable drawable = (GradientDrawable) holder.text.getBackground();
        drawable.setColor(holder.itemView.getContext().getResources().getColor(item.color));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public CategoryItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageSwitcher switcher;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            switcher = (ImageSwitcher) itemView.findViewById(R.id.categroy_switcher);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public static class CategoryItem {
        public int type;
        public int image;
        public int color;
        public String name;

        public CategoryItem(int type, int image, int color, String name) {
            this.type = type;
            this.image = image;
            this.color = color;
            this.name = name;
        }
    }
}