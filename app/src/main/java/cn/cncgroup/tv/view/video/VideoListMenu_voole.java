package cn.cncgroup.tv.view.video;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.VideoListMenuAdapter;
import cn.cncgroup.tv.ui.listener.OnMenuSearchOrSelectListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.ui.widget.slidingmenu.SlidingMenu;

/**
 * Created by Wu on 2015/9/9.
 */
public class VideoListMenu_voole extends RelativeLayout implements
        SlidingMenu.OnOpenListener, View.OnClickListener {
    private final String TAG = "VideoListMenu_voole";
    // widget
    private RelativeLayout mLayoutSearch;
    private RelativeLayout mLayoutSelect;
    private VerticalGridView mGrid;
    private Context mContext;
    private boolean hasTitle = true;
    // data
    // interface
    private OnMenuSearchOrSelectListener mOnMenuSearchOrSelectListener;

    public VideoListMenu_voole(Context context) {
        super(context);
        mContext = context;
        findView(context);
    }

    public VideoListMenu_voole(Context context, boolean hasTitle) {
        super(context);
        mContext = context;
        this.hasTitle = hasTitle;
        findView(context);
    }

    public VideoListMenu_voole(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        findView(context);
    }

    public VideoListMenu_voole(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        findView(context);
    }

    @Override
    public void onOpen() {
        mGrid.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_list_menu_search:
                //播单搜索 添加统计
                StatService.onEvent(mContext, "Search_list", mContext.getString(R.string.search_list), 1);
                mOnMenuSearchOrSelectListener
                        .onVedioSelectOrSearchClickListener(
                                OnMenuSearchOrSelectListener.SEARCH, null);
                break;
            case R.id.video_list_menu_select:
                //播单筛选 添加统计
                StatService.onEvent(mContext, "Filter_VirtualClass", mContext.getString(R.string.filter_virtualclass), 1);
                Bundle bundle = new Bundle();
                mOnMenuSearchOrSelectListener
                        .onVedioSelectOrSearchClickListener(
                                OnMenuSearchOrSelectListener.SELECT, bundle);
                break;
        }
    }

    private void findView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_video_list_menu, this, true);
        mLayoutSearch = (RelativeLayout) view.findViewById(R.id.video_list_menu_search);
        mLayoutSelect = (RelativeLayout) view.findViewById(R.id.video_list_menu_select);
        View nothing_toshow = view.findViewById(R.id.nothing_toshow);
        View video_list_img_left_split = view.findViewById(R.id.video_list_img_left_split);
        if (hasTitle) {
            nothing_toshow.setVisibility(View.GONE);
            mLayoutSearch.setVisibility(View.VISIBLE);
            mLayoutSelect.setVisibility(View.VISIBLE);
            video_list_img_left_split.setVisibility(View.VISIBLE);
        } else {
            nothing_toshow.setVisibility(View.VISIBLE);
            mLayoutSearch.setVisibility(View.GONE);
            mLayoutSelect.setVisibility(View.GONE);
            video_list_img_left_split.setVisibility(View.GONE);
        }
        mGrid = (VerticalGridView) view.findViewById(R.id.video_list_menu_grid);
        initView();
    }

    private void initView() {
        mLayoutSearch.setOnClickListener(this);
        mLayoutSelect.setOnClickListener(this);
        mGrid.setWindowAlignmentOffsetPercent(30f);
    }

    public void setMenuAdapter(VideoListMenuAdapter videoListMenuAdapter) {
        mGrid.setAdapter(videoListMenuAdapter);
        videoListMenuAdapter.notifyDataSetChanged();
    }

    public void setSearchOrSelectListener(
            OnMenuSearchOrSelectListener onMenuSearchOrSelectListener) {
        mOnMenuSearchOrSelectListener = onMenuSearchOrSelectListener;
    }

    public void setSelectMenuFocus() {
        mLayoutSelect.requestFocus();
    }
}
