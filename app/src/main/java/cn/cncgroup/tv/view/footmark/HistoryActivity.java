package cn.cncgroup.tv.view.footmark;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.HistoryMainAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.ui.widget.FootmarkDialog;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.BaseGridView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by jinwenchao123 on 15/8/21.
 */
public class HistoryActivity extends BaseActivity
        implements View.OnClickListener, OnItemClickListener<VideoSetDao>,
        OnItemFocusListener<VideoSetDao> {
    private static final String TAG = "HistoryActivity";
    private ImageView iv_history_empty;
    // private static final String dataFormat = "MM/dd";
    private LinkedHashMap<String, ArrayList<VideoSetDao>> canUseData = null;
    private boolean isOpenOrDelete = true;
    private VerticalGridView history_main_gridview;
    private TextView tv_activity_title;
    private LinearLayout ll_to_sreach;
    private HistoryMainAdapter adapter;
    private ShadowView mShadow;
    private View mFocusedView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_history_new);
    }

    @Override
    protected void findView() {
        iv_history_empty = (ImageView) findViewById(R.id.iv_history_empty);
        history_main_gridview = (VerticalGridView) findViewById(
                R.id.history_main_gridview);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        ll_to_sreach = (LinearLayout) findViewById(R.id.ll_to_sreach);
        mShadow = (ShadowView) findViewById(R.id.shadow);
        history_main_gridview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView child = (RecyclerView) recyclerView.getFocusedChild().findViewById(R.id.expende_gridview1);
                View view = child.getFocusedChild();
                if (mFocusedView != null && view != null) {
                    mShadow.moveTo(view.findViewById(R.id.history_img_icon));
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void initView() {
        mShadow.init(25);
        history_main_gridview
                .setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        tv_activity_title.setText(getString(R.string.activity_history_title));
        ll_to_sreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatService.onEvent(getActivity(), "Search_history", getActivity().getString(R.string.search_history), 1);
                Project.get().getConfig().openSearch(getActivity());
            }
        });
    }

    private void showData() {
        adapter = new HistoryMainAdapter(this, canUseData, this, this, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mFocusedView != null && recyclerView.getFocusedChild() != null) {
                    mShadow.moveTo(mFocusedView.findViewById(R.id.history_img_icon));
                }
            }
        });
        history_main_gridview.setAdapter(adapter);
    }

    private LinkedHashMap<String, ArrayList<VideoSetDao>> initializeData(
            ArrayList<VideoSetDao> historyData) {
        LinkedHashMap<String, ArrayList<VideoSetDao>> myHistoryData = new LinkedHashMap<String, ArrayList<VideoSetDao>>();
        String tempDay = Utils.getDay(historyData.get(0).saveTime);
        int tempNum = 1;
        ArrayList<VideoSetDao> daySingle = new ArrayList<VideoSetDao>();
        for (int i = 0; i < historyData.size(); i++) {
            VideoSetDao videoSetDao = historyData.get(i);
            Log.i(TAG, "videosetdao:image=" + videoSetDao.image + "--length=" + videoSetDao.length + "--time=" + videoSetDao.startTime);
            String oneDay = Utils.getDay(videoSetDao.saveTime);
            if (tempDay.equals(oneDay)) {
                daySingle.add(videoSetDao);
                if (i == historyData.size() - 1) {
                    myHistoryData.put(tempDay, daySingle);
                }
            } else {
                if (!tempDay.equals(getString(R.string.more_early))) {
                    tempNum++;
                    myHistoryData.put(tempDay, daySingle);
                    if (tempNum < 4) {
                        tempDay = oneDay;
                    } else {
                        tempDay = getString(R.string.more_early);
                    }
                    daySingle = new ArrayList<VideoSetDao>();
                    daySingle.add(videoSetDao);
                } else {
                    daySingle.add(videoSetDao);
                    if (i == historyData.size() - 1 || daySingle.size() >= 20) {
                        myHistoryData.put(tempDay, daySingle);
                        break;
                    }
                }
            }
        }
        return myHistoryData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_to_sreach:
                // 跳转到搜索页面
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            historyMenuClick();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && isOpenOrDelete == false) {
            updateUI(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void historyMenuClick(){
        if (DbUtil.queryAll(GlobalConstant.ISHISTORY).size() > 0) {
            if (isOpenOrDelete) {
                FootmarkDialog footmarkDialog = new FootmarkDialog(
                        getActivity());
                footmarkDialog.setPositiveButton(
                        getString(R.string.footmark_clean_text),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isEmptyDialog();
                            }
                        });
                footmarkDialog.setNegativeButton(
                        getString(R.string.footmark_delete_text),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateUI(false);
                            }
                        });
                if(footmarkDialog.isShowing()){
                    footmarkDialog.dismiss();
                }else{
                    footmarkDialog.show();
                }

            } else {
                updateUI(false);
            }
        } else {
            Toast.makeText(this, getString(R.string.collect_clean_already),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void updateUI(boolean isClean) {
        if (isClean) {
            DbUtil.deleteAll(GlobalConstant.ISHISTORY);
            history_main_gridview.setVisibility(View.GONE);
            iv_history_empty.setVisibility(View.VISIBLE);
            isOpenOrDelete = true;
        } else {
            // 更换为删除状态
            if (isOpenOrDelete) {
                isOpenOrDelete = false;
            } else {// 更换为取消删除状态
                isOpenOrDelete = true;
            }
            adapterNotify(isOpenOrDelete);

        }
    }

    private void adapterNotify(boolean isOpenOrDelete) {
        adapter.updateView(isOpenOrDelete, canUseData);
    }

    @Override
    public void onItemClickLister(View view, int position,
                                  VideoSetDao videoSetDao) {
        Object key = canUseData.keySet().toArray()[history_main_gridview
                .getFocusPosition()];
        VideoSetDao videoSet = canUseData.get(key).get(position);
        if (isOpenOrDelete) {
            Project.get().getConfig().playVideo(getActivity(),
                    String.valueOf(videoSet.videoSetId),String.valueOf(videoSet.videoId),
                    String.valueOf(videoSetDao.count), videoSet.startTime, videoSet.name, videoSet.image, videoSet.channelId);
        } else {
//            if (canUseData.get(key).size() > 1) {
//                DbUtil.deleteFootmark(videoSet.videoSetId);
//                ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
//                        .queryAll(GlobalConstant.ISHISTORY);
//                canUseData = initializeData(historyData);
//                adapter.updateView(isOpenOrDelete, canUseData);
//            } else {
                DbUtil.deleteFootmark(videoSet.videoSetId);
                ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                        .queryAll(GlobalConstant.ISHISTORY);
                if (historyData.size() == 0) {
                    updateUI(true);
                } else {
                    canUseData = initializeData(historyData);
                    adapter = new HistoryMainAdapter(this, canUseData, this,
                            this, new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (mFocusedView != null && recyclerView.getFocusedChild() != null) {
                                mShadow.moveTo(mFocusedView.findViewById(R.id.history_img_icon));
                            }
                        }
                    });
                    history_main_gridview.setAdapter(adapter);
                    history_main_gridview.post(new Runnable() {
                        @Override
                        public void run() {
                            history_main_gridview.requestFocus();
                        }
                    });
//                    adapter.updateView(isOpenOrDelete, canUseData);
                }
//            }
        }
    }

    @Override
    public void onItemFocusLister(View view, int position,
                                  VideoSetDao videoSetDao, boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = view;
            mShadow.moveTo(view.findViewById(R.id.history_img_icon));
        } else {
            mShadow.setVisibility(View.GONE);
        }
        if (hasFocus) {
            if (isOpenOrDelete) {
                view.findViewById(R.id.rl_history_delete)
                        .setVisibility(View.INVISIBLE);
            } else {
                view.findViewById(R.id.rl_history_delete)
                        .setVisibility(View.VISIBLE);
            }
            if (videoSetDao.startTime != 0) {
                view.findViewById(R.id.rl_focus_history)
                        .setVisibility(View.VISIBLE);
            }
        } else {
            view.findViewById(R.id.rl_history_delete)
                    .setVisibility(View.INVISIBLE);
            view.findViewById(R.id.rl_focus_history)
                    .setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //优朋播放结束  停止计时
        StatService.onEventEnd(getActivity(), "PlayTime", getActivity().getString(R.string.playtime));
        ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                .queryAll(GlobalConstant.ISHISTORY);
        Log.i(TAG,"onResume listSize:"+historyData.size());
        if (historyData.size() == 0) {
            history_main_gridview.setVisibility(View.GONE);
            iv_history_empty.setVisibility(View.VISIBLE);
        } else {
            history_main_gridview.setVisibility(View.VISIBLE);
            iv_history_empty.setVisibility(View.GONE);
            canUseData = initializeData(historyData);
            showData();
            history_main_gridview.requestFocus();
        }
    }

    private void isEmptyDialog(){
        final CustomDialog dialog = new CustomDialog(this);
        dialog.setMessage(getText(R.string.empty_dialog_title));
        dialog.setPositiveButton(getText(R.string.confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUI(true);
                    }
                });
        dialog.setNegativeButton(getText(R.string.cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
