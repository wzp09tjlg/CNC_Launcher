package cn.cncgroup.tv.view.homesecond.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by Wu on 2015/11/2.
 */
public class UseAdapter extends RecyclerView.Adapter<UseAdapter.ViewHolder> {
    private final int APP_COUNT = 10;
    private List<AppInfo> mList;
    private Context mContext;
    private OnItemFocusListener<AppInfo> mFocusListener;
    private OnItemClickListener<AppInfo> mClicklister;
    private DoTopOrDelOperator mDoTopOrDelOperator;
    private boolean mIsDiplayFileManager = false;
    private int mFocusePos = -1;

    public UseAdapter(List<AppInfo> list, Context context,
                      OnItemFocusListener<AppInfo> itemFoceussChangeListener,
                      OnItemClickListener<AppInfo> useOnItemClicklister,
                      DoTopOrDelOperator doTopOrDelOperator, boolean isDisplayFileManager) {
        mList = list;
        mContext = context;
        mFocusListener = itemFoceussChangeListener;
        mClicklister = useOnItemClicklister;
        mDoTopOrDelOperator = doTopOrDelOperator;
        mIsDiplayFileManager = isDisplayFileManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.use_gridview_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int indexPosition;
        if (position <= 4) {
            indexPosition = position;
        } else {
            if (mIsDiplayFileManager)
                indexPosition = position - 3;
            else
                indexPosition = position - 2;
        }

        initItemView(holder, position, indexPosition); // 设置ItemView显示
        setSpeacialItem(holder, position, indexPosition); // 设置特殊的ItemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClicklister.onItemClickLister(v, position, getItem(position));
            }
        });

        setTopOrDelCoverImage(holder, position, indexPosition);// 设置置顶和删除的浮层
    }

    public AppInfo getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return APP_COUNT;
    }

    private void initItemView(final ViewHolder holder, int position,
                              int indexPosition) {
        if (position <= 4) {
            if (indexPosition < mList.size()) {
                holder.txtAppAdd.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.GONE);
                holder.appIcon.setVisibility(View.VISIBLE);
                holder.appIcon.setImageDrawable(mList.get(indexPosition)
                        .getIcon());
                holder.appName.setVisibility(View.VISIBLE);
                holder.appName.setText(mList.get(indexPosition).appName);
            } else {
                holder.appIcon.setImageDrawable(null);
                holder.appIcon.setVisibility(View.GONE);
                holder.appName.setText("");
                holder.appName.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.VISIBLE);
                holder.imgAdd.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.img_use_add_normal));
            }
        } else if (position == 5) { // 针对5 特殊处理
            holder.txtAppAdd.setVisibility(View.GONE);
            holder.imgAdd.setVisibility(View.GONE);
            holder.appIcon.setVisibility(View.VISIBLE);
            holder.appIcon.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.img_use_all));
            holder.appName.setVisibility(View.VISIBLE);
            holder.appName.setText(mContext.getResources().getString(
                    R.string.all_app));
        } else if (position == 6) { // 针对6 特殊处理
            holder.txtAppAdd.setVisibility(View.GONE);
            holder.imgAdd.setVisibility(View.GONE);
            holder.appIcon.setVisibility(View.VISIBLE);
            holder.appIcon.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.img_use_setting));
            holder.appName.setVisibility(View.VISIBLE);
            holder.appName.setText(mContext.getResources().getString(
                    R.string.setting));
        } else if (mIsDiplayFileManager && position == 7) {//针对显示文件管理器的位置做处理
            holder.txtAppAdd.setVisibility(View.GONE);
            holder.imgAdd.setVisibility(View.GONE);
            holder.appIcon.setVisibility(View.VISIBLE);
            holder.appIcon.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.icon_use_filemanage));
            holder.appName.setVisibility(View.VISIBLE);
            holder.appName.setText(mContext.getResources().getString(
                    R.string.filemanager));
        } else { // 针对 7(不显示文件管理的位置处理)、8、9做处理
            if (indexPosition < mList.size()) {
                holder.txtAppAdd.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.GONE);
                holder.appIcon.setVisibility(View.VISIBLE);
                holder.appIcon.setImageDrawable(mList.get(indexPosition)
                        .getIcon());
                holder.appName.setVisibility(View.VISIBLE);
                holder.appName.setText(mList.get(indexPosition).appName);
            } else {
                holder.appIcon.setImageDrawable(null);
                holder.appIcon.setVisibility(View.GONE);
                holder.appName.setText("");
                holder.appName.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.VISIBLE);
                holder.imgAdd.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.img_use_add_normal));
            }
        }
    }

    private void setSpeacialItem(final ViewHolder holder, final int position,
                                 final int indexPosition) {// 设置特殊位置的情况

        if (position == 5 || position == 6) { // 针对5 和 6位置 icon大小处理
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.appIcon
                    .getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelSize(
                    R.dimen.dimen_78dp);
            layoutParams.height = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_78dp);
            layoutParams.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_83dp);
            holder.appIcon.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.appName
                    .getLayoutParams();
            layoutParams2.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_35dp);
            holder.appName.setLayoutParams(layoutParams2);
        } else if (mIsDiplayFileManager && position == 7) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.appIcon
                    .getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelSize(
                    R.dimen.dimen_78dp);
            layoutParams.height = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_78dp);
            layoutParams.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_83dp);
            holder.appIcon.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.appName
                    .getLayoutParams();
            layoutParams2.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_35dp);
            holder.appName.setLayoutParams(layoutParams2);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.appIcon
                    .getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelSize(
                    R.dimen.dimen_160dp);
            layoutParams.height = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_160dp);
            layoutParams.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_40dp);
            holder.appIcon.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.appName
                    .getLayoutParams();
            layoutParams2.topMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dimen_26dp);
            holder.appName.setLayoutParams(layoutParams2);
        }

        holder.itemView
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        mFocusListener.onItemFocusLister(view,
                                position, getItem(position), b);
                    }
                });
    }

    private void setTopOrDelCoverImage(final ViewHolder holder,
                                       final int position, final int indexPosition) {
        boolean flag = true;
        if (mIsDiplayFileManager)
            flag = position != 5 && position != 6 && position != 7 && mList.size() > 0
                    && indexPosition < mList.size();
        else
            flag = position != 5 && position != 6 && mList.size() > 0
                    && indexPosition < mList.size();
        if (flag)
            holder.itemView.setOnKeyListener(new View.OnKeyListener() {
                                                 @Override
                                                 public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                     final boolean sequenceLeftKeyOrRightKey = (holder.imgMenu.getVisibility() == View.VISIBLE)
                                                             && (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                                                             || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT);
                                                     if (sequenceLeftKeyOrRightKey)
                                                         return true;
                                                     final boolean uniqueDown = event.getRepeatCount() == 0
                                                             && event.getAction() == KeyEvent.ACTION_DOWN;
                                                     if (!uniqueDown)
                                                         return false;
                                                     if (v.isFocused()
                                                             && holder.imgMenu.getVisibility() == View.VISIBLE) {
                                                         switch (keyCode) {
                                                             case KeyEvent.KEYCODE_DPAD_UP:
                                                                 holder.imgMenu.setTag(1);
                                                                 holder.imgMenu
                                                                         .setBackgroundResource(R.drawable.icon_app_top_focus);
                                                                 return true;
                                                             case KeyEvent.KEYCODE_DPAD_DOWN:
                                                                 holder.imgMenu.setTag(0);
                                                                 holder.imgMenu
                                                                         .setBackgroundResource(R.drawable.icon_app_del_focus);
                                                                 return true;
                                                             case KeyEvent.KEYCODE_MENU:
                                                                 if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                                                     holder.imgMenu.setVisibility(View.GONE);
                                                                     holder.viewBg.setVisibility(View.GONE);
                                                                 }
                                                                 return true;
                                                             case KeyEvent.KEYCODE_BACK:
                                                                 holder.imgMenu
                                                                         .setBackgroundResource(R.drawable.icon_app_top_focus);
                                                                 holder.imgMenu
                                                                         .setBackgroundResource(R.drawable.icon_app_top_focus);
                                                                 if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                                                     holder.imgMenu.setVisibility(View.GONE);
                                                                     holder.viewBg.setVisibility(View.GONE);
                                                                 }
                                                                 break;
                                                             case KeyEvent.KEYCODE_ENTER:
                                                             case KeyEvent.KEYCODE_DPAD_CENTER:
                                                                 Integer type = (Integer) holder.imgMenu
                                                                         .getTag();
                                                                 if (type == 0)
                                                                     mDoTopOrDelOperator.doTopOrDelOperator(
                                                                             indexPosition, 0);
                                                                 else
                                                                     mDoTopOrDelOperator.doTopOrDelOperator(
                                                                             indexPosition, 1);
                                                                 holder.imgMenu.setVisibility(View.GONE);
                                                                 holder.viewBg.setVisibility(View.GONE);
                                                                 return true;
                                                             case KeyEvent.KEYCODE_DPAD_LEFT:
                                                             case KeyEvent.KEYCODE_DPAD_RIGHT:
                                                                 return true;
                                                         }
                                                     } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                                                         holder.viewBg.setVisibility(View.VISIBLE);
                                                         holder.imgMenu.setVisibility(View.VISIBLE);
                                                         holder.imgMenu
                                                                 .setBackgroundResource(R.drawable.icon_app_top_focus);
                                                         holder.imgMenu.setTag(1);// must set a value,because
                                                         // sometime can be NullPoint
                                                         // Error
                                                         return true;
                                                     }
                                                     return false;
                                                 }
                                             }

            );
    }

    public void addAppChange(int position) {
        if (position <= 4)
            notifyItemChanged(position);
        else {
            if (mIsDiplayFileManager)
                notifyItemChanged(position + 3);
            else
                notifyItemChanged(position + 2);
        }

    }

    public void deleteAppChange(int position) {
        notifyDataSetChanged();
    }

    public interface DoTopOrDelOperator {
        void doTopOrDelOperator(int position, int type);// type 0:Delete
        // 1:Top
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAdd;
        TextView appName;
        ImageView appIcon;
        TextView txtAppAdd;
        View viewBg;
        ImageView imgMenu;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgAdd = (ImageView) itemView.findViewById(R.id.app_img_gv_use);
            appName = (TextView) itemView.findViewById(R.id.app_text_AppName);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon_gv_use);
            txtAppAdd = (TextView) itemView.findViewById(R.id.text_gv_use);
            viewBg = itemView.findViewById(R.id.app_view_bg);
            imgMenu = (ImageView) itemView.findViewById(R.id.app_img_menu);
        }
    }
}
