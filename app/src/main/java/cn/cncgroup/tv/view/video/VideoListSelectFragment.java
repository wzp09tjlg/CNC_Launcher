package cn.cncgroup.tv.view.video;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.VideoListSelectMenuAdapter;
import cn.cncgroup.tv.adapter.VideoListSelectRowAdapter;
import cn.cncgroup.tv.adapter.VideoListSelectRowItemAdapter;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by Wu on 2015/9/23.
 */
public class VideoListSelectFragment extends DialogFragment implements
        OnItemClickListener, OnItemFocusListener {
    public static int chanelId = 0;
    private static ArrayList<Integer> positionList = new ArrayList<Integer>();
    private static VideoListSelectFragment mFragmentDialog;
    private final String TAG = "VideoListSelectFragment";
    // widget
    private VerticalGridView mGridMenu;
    private VerticalGridView mGrid;
    // data
    private ConfirmSelectListener mConfirmSelectListener;
    private VideoListSelectMenuAdapter mVideoListSelectMenuAdapter;
    private VideoListSelectRowAdapter mVideoListSelectRowAdapter;
    private ArrayList<FilterList> mList;
    private ShadowView mShadowView;
    private ArrayList<VideoListSelectRowItemAdapter> mAdapterList;

    public static VideoListSelectFragment getInstance(
            ArrayList<FilterList> list, ConfirmSelectListener listener,
            int chanelId) {
        VideoListSelectFragment mVideoListSelectFragment = null;
        if (mVideoListSelectFragment == null) {
            mVideoListSelectFragment = new VideoListSelectFragment();
        }
        mVideoListSelectFragment.mList = list;
        mVideoListSelectFragment.mConfirmSelectListener = listener;
        if (VideoListSelectFragment.chanelId != chanelId) {
            VideoListSelectFragment.chanelId = chanelId;
            positionList.clear();
        }
        mFragmentDialog = mVideoListSelectFragment;
        return mVideoListSelectFragment;
    }

    public static void clearSeletPosition() {
        positionList.clear();
    }

    @Override
    public void onItemClickLister(View view, int position, Object o) {
        positionList.clear();
        ArrayList<FilterList> list = new ArrayList<FilterList>();
        for (int i = 0; i < mAdapterList.size(); i++) {
            list.add(mAdapterList.get(i).getSelectItem());
            positionList.add(new Integer(mAdapterList.get(i)
                    .getmCurrentPosition()));
        }
        mConfirmSelectListener.confirmSelectListener(list);
    }

    @Override
    public void onItemFocusLister(View view, int position, Object o,
                                  boolean hasFocus) {
        if (hasFocus) {
            move(view);
        } else {
            mShadowView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Video_List_Select_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list_select_voole,
                container, false);
        findView(view);
        initView();
        this.getDialog().setOnKeyListener(new OnKeyListener());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        int mCountType = mList.size();
        int dialogWidtth = (int) getActivity().getResources().getDimension(
                R.dimen.dimen_1920dp);
        // int dialogHeight = (int) mContext.getResources().getDimension(
        // R.dimen.dimen_360dp);
        int dialogHeight = (int) (getActivity().getResources().getDimension(
                R.dimen.dimen_114dp) + getActivity().getResources()
                .getDimension(R.dimen.dimen_82dp) * mCountType);
        // dialog
        getDialog().getWindow().setLayout(dialogWidtth, dialogHeight);
        // selecMenu
        RelativeLayout.LayoutParams paramsMenu = (RelativeLayout.LayoutParams) mGridMenu
                .getLayoutParams();
        paramsMenu.height = (int) getActivity().getResources().getDimension(
                R.dimen.dimen_82dp)
                * mCountType;
        mGridMenu.setLayoutParams(paramsMenu);
        // selectContain
        RelativeLayout.LayoutParams paramsContain = (RelativeLayout.LayoutParams) mGrid
                .getLayoutParams();
        paramsContain.height = (int) (getActivity().getResources()
                .getDimension(R.dimen.dimen_82dp) * mCountType + getActivity()
                .getResources().getDimension(R.dimen.dimen_70dp));
        mGrid.setLayoutParams(paramsContain);
        mVideoListSelectMenuAdapter.bindData(mList);
        mVideoListSelectMenuAdapter.notifyDataSetChanged();
        mAdapterList = new ArrayList<VideoListSelectRowItemAdapter>();
        for (int i = 0; i < mList.size(); i++) {
            VideoListSelectRowItemAdapter mVideoListSelectRowItemAdapter = new VideoListSelectRowItemAdapter(
                    mList.get(i).getResult(), this);
            if (positionList.size() > 0)
                mVideoListSelectRowItemAdapter.setSelectPosition(positionList
                        .get(i).intValue());
            mAdapterList.add(mVideoListSelectRowItemAdapter);
        }
        mVideoListSelectRowAdapter.bindData(mAdapterList);
        mVideoListSelectRowAdapter.notifyDataSetChanged();
        mGrid.setSelectedPositionSmooth(0);
    }

    private void findView(View view) {
        mGridMenu = (VerticalGridView) view
                .findViewById(R.id.video_list_grid_select_menu);
        mGrid = (VerticalGridView) view
                .findViewById(R.id.video_list_grid_select);
        mShadowView = (ShadowView) view.findViewById(R.id.shadow);
    }

    private void initView() {
        mVideoListSelectMenuAdapter = new VideoListSelectMenuAdapter();
        mVideoListSelectRowAdapter = new VideoListSelectRowAdapter();
        mGridMenu.setAdapter(mVideoListSelectMenuAdapter);
        mGrid.setAdapter(mVideoListSelectRowAdapter);
    }

    private void move(View view) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mShadowView
                .getLayoutParams();
        params.width = view.getMeasuredWidth() + 48;
        params.height = view.getMeasuredHeight() + 48;
        mShadowView.setLayoutParams(params);
        mShadowView.setX(view.getX() - 25);
        mShadowView.setY(view.getY() - 25);
        mShadowView.setVisibility(View.VISIBLE);
    }

    interface ConfirmSelectListener {
        void confirmSelectListener(ArrayList<FilterList> list);
    }

    private class OnKeyListener implements DialogInterface.OnKeyListener {

        @Override
        public boolean onKey(DialogInterface dialogInterface, int keyCode,
                             KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_MENU && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (mFragmentDialog != null && mFragmentDialog.getShowsDialog()) {
                    mFragmentDialog.dismiss();
                    Log.i(TAG, "VideoListSelectFragment dismiss()");
                    return true;
                }
            }
            return false;
        }
    }

}
