package cn.cncgroup.tv.view.footmark;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.CollectAdapter;
import cn.cncgroup.tv.adapter.HistoryMainAdapter;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.ui.widget.FootmarkDialog;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.BaseGridView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.SystemAndAppMsgUtil;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by jinwenchao123 on 15/11/17.
 */
public class CollectFragment extends BaseFragment implements OnItemClickListener<VideoSetDao>,
        OnItemFocusListener<VideoSetDao> {
    private FootmarkActivity mActivity;
    public String titleType;
    private TextView tv_activity_title;
    private LinearLayout ll_to_sreach;
    private ImageView iv_menu_hearer, iv_history_empty, mArrow, iv_collect_empty;
    public VerticalGridView history_main_gridview;
    public VerticalGridView vgridview_collect_content;
    private LinkedHashMap<String, ArrayList<VideoSetDao>> canUseData = null;
    private HistoryMainAdapter historyAdapter;
    private View mFocusedView;
    private int lineItem = 5;
    private List<VideoSetDao> mData;
    private CollectAdapter collectAdapter;
    private boolean isHistory = true;
    private ShadowView shadow;
    private Handler mHandler = new Handler();
    private int position = 0;
    private FootmarkDialog footmarkDialog;
    private boolean firstin = true;


    public boolean isOpenOrDelete = true;//历史页面用来标识菜单键的操作
    public boolean isDelete = false;//收藏页面用来标识菜单键的操作

    @Override
    protected int getContentView() {
        return R.layout.fragment_footmark;
    }

    @Override
    protected void findView(View view) {
        tv_activity_title = (TextView) view.findViewById(R.id.tv_activity_title);
        ll_to_sreach = (LinearLayout) view.findViewById(R.id.ll_to_sreach);
        iv_menu_hearer = (ImageView) view.findViewById(R.id.iv_menu_hearer);
        mArrow = (ImageView) view.findViewById(R.id.arrow);
        shadow = (ShadowView) view.findViewById(R.id.shadow);
        history_main_gridview = (VerticalGridView) view.findViewById(R.id.history_main_gridview);
        vgridview_collect_content = (VerticalGridView) view.findViewById(R.id.vgridview_collect_content);
        iv_collect_empty = (ImageView) view.findViewById(R.id.iv_collect_empty);
        iv_history_empty = (ImageView) view.findViewById(R.id.iv_history_empty);


    }

    @Override
    protected void initView() {
        tv_activity_title.setText(titleType);

        history_main_gridview
                .setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        vgridview_collect_content.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        ll_to_sreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatService.onEvent(getActivity(), "Search_history", getActivity().getString(R.string.search_history), 1);
                Project.get().getConfig().openSearch(getActivity());
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FootmarkActivity) activity;
        Bundle bundle = getArguments();
        titleType = bundle.getString(GlobalConstant.FOOTMARKSTRING);
        if (getResources().getString(R.string.history).equals(titleType)) {
            isHistory = true;
            lineItem = 5;
        } else {
            isHistory = false;
            lineItem = 6;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //加载历史页数据
        if (isHistory) {
            vgridview_collect_content.setVisibility(View.GONE);
            iv_collect_empty.setVisibility(View.GONE);
            StatService.onEventEnd(getActivity(), "PlayTime", getActivity().getString(R.string.playtime));
            ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                    .queryAll(GlobalConstant.ISHISTORY);
            if (historyData.size() == 0) {
                history_main_gridview.setVisibility(View.GONE);
                iv_history_empty.setVisibility(View.VISIBLE);
            } else {
                history_main_gridview.setVisibility(View.VISIBLE);
                iv_history_empty.setVisibility(View.GONE);
                canUseData = initializeData(historyData);
                showData();
                if (firstin) {
                    firstin = false;
                } else {
                    history_main_gridview.requestFocus();
                    history_main_gridview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            RecyclerView child = (RecyclerView) recyclerView.getFocusedChild().findViewById(R.id.expende_gridview1);
                            View view = child.getFocusedChild();
                            if (mFocusedView != null && view != null) {
                                shadow.moveTo(view.findViewById(R.id.history_img_icon));
                            }
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });
                }
            }
        } else {
            history_main_gridview.setVisibility(View.GONE);
            iv_history_empty.setVisibility(View.GONE);
            iv_collect_empty.setVisibility(View.VISIBLE);
            vgridview_collect_content.setVisibility(View.VISIBLE);
            localInitView();
            if (firstin) {
                firstin = false;
            } else {
                vgridview_collect_content.requestFocus();
                vgridview_collect_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        RecyclerView child = (RecyclerView) recyclerView.getFocusedChild().findViewById(R.id.expende_gridview1);
                        View view = null;
                        try {
                            view = child.getFocusedChild();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mFocusedView != null && view != null) {
                            shadow.moveTo(view.findViewById(R.id.history_img_icon));
                        }
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }

        }
        shadow.init(25);
    }

    private void changeView() {
        if (mData.size() > 0) {
            iv_collect_empty.setVisibility(View.GONE);
            vgridview_collect_content.setVisibility(View.VISIBLE);
        } else {
            isDelete = false;
            iv_collect_empty.setVisibility(View.VISIBLE);
            vgridview_collect_content.setVisibility(View.GONE);
        }
    }

    private void localInitView() {
        mData = new Select().from(VideoSetDao.class).where("Collect = ?", 1)
                .orderBy("SaveTime desc").execute();
        collectAdapter = new CollectAdapter(getActivity(), mData, this, this);
        vgridview_collect_content.setAdapter(collectAdapter);
        changeView();
        iv_menu_hearer.setFocusable(false);

    }


    private void showData() {
        historyAdapter = new HistoryMainAdapter(getActivity(), canUseData, this, this, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mFocusedView != null && recyclerView.getFocusedChild() != null) {
                    shadow.moveTo(mFocusedView.findViewById(R.id.history_img_icon));
                }
            }
        });
        history_main_gridview.setAdapter(historyAdapter);
    }

    private LinkedHashMap<String, ArrayList<VideoSetDao>> initializeData(
            ArrayList<VideoSetDao> historyData) {
        LinkedHashMap<String, ArrayList<VideoSetDao>> myHistoryData = new LinkedHashMap<String, ArrayList<VideoSetDao>>();
        String tempDay = Utils.getDay(historyData.get(0).saveTime);
        int tempNum = 1;
        ArrayList<VideoSetDao> daySingle = new ArrayList<VideoSetDao>();
        for (int i = 0; i < historyData.size(); i++) {
            VideoSetDao videoSetDao = historyData.get(i);
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


    private void adapterNotify(boolean isOpenOrDelete) {
        historyAdapter.updateView(isOpenOrDelete, canUseData);
    }

    @Override
    public void onItemClickLister(View view, int position,
                                  VideoSetDao videoSetDao) {
        if (isHistory) {
            Object key = canUseData.keySet().toArray()[history_main_gridview
                    .getFocusPosition()];
            VideoSetDao videoSet = canUseData.get(key).get(position);
            Log.i("footmark", "播放第" + videoSetDao.count + "集");
            if (isOpenOrDelete) {
                VideoSet videoSetBean = new VideoSet();
                videoSetBean.setId(videoSet.videoSetId);
                Log.i("videoSetBean", "" + videoSetBean.getName() + videoSetBean.getId());
                Project.get().getConfig().openVideoDetail(getActivity(), videoSetBean);
            } else {
                if (canUseData.get(key).size() > 1) {
                    DbUtil.deleteFootmark(videoSet.videoSetId);
//                videoSet.delete();
                    ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                            .queryAll(GlobalConstant.ISHISTORY);
                    canUseData = initializeData(historyData);
                    historyAdapter.updateView(isOpenOrDelete, canUseData);
                } else {
                    DbUtil.deleteFootmark(videoSet.videoSetId);
//                videoSet.delete();
                    ArrayList<VideoSetDao> historyData = (ArrayList<VideoSetDao>) DbUtil
                            .queryAll(GlobalConstant.ISHISTORY);
                    if (historyData.size() == 0) {
                        updateUI(true);
                    } else {
                        canUseData = initializeData(historyData);
                        historyAdapter = new HistoryMainAdapter(getActivity(), canUseData, this,
                                this, new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (mFocusedView != null && recyclerView.getFocusedChild() != null) {
                                    shadow.moveTo(mFocusedView.findViewById(R.id.history_img_icon));
                                }
                            }
                        });
                        history_main_gridview.setAdapter(historyAdapter);
                        history_main_gridview.post(new Runnable() {
                            @Override
                            public void run() {
                                history_main_gridview.requestFocus();
                            }
                        });
                    }
                }
            }
        } else {
            if (isDelete) {
                DbUtil.deleteCollect(videoSetDao.videoSetId);
                mData = new Select().from(VideoSetDao.class).where("Collect = ?", 1)
                        .orderBy("SaveTime desc").execute();
//                collectAdapter.notifyItemRangeChanged(0, mData.size());
                collectAdapter.setmData(mData);
                collectAdapter.notifyDataSetChanged();
                changeView();
            } else {
                VideoSet set = new VideoSet();
                set.setId(videoSetDao.videoSetId);
                Project.get().getConfig().openVideoDetail(getActivity(), set);
            }
        }


    }

    @Override
    public void onItemFocusLister(final View view, int position,
                                  VideoSetDao videoSetDao, boolean hasFocus) {
        if (isHistory) {
            if (hasFocus) {
                mFocusedView = view;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shadow.moveTo(view.findViewById(R.id.history_img_icon));
                    }
                }, 300);

            } else {
                shadow.setVisibility(View.GONE);
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
        } else {
            if (hasFocus) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shadow.moveTo(view.findViewById(R.id.iv_footmark_icon));
                    }
                }, 300);

            } else {
                shadow.setVisibility(View.GONE);
            }
            RelativeLayout delete_cover = (RelativeLayout) view
                    .findViewById(R.id.rl_delete);
            if (isDelete) {
                if (hasFocus) {
                    delete_cover.setVisibility(View.VISIBLE);
                } else {
                    delete_cover.setVisibility(View.INVISIBLE);
                }
            } else {
                if (delete_cover.getVisibility() == View.VISIBLE) {
                    delete_cover.setVisibility(View.INVISIBLE);
                }
            }
        }
        positionHideSlidemenu(hasFocus, position);
    }

    private void positionHideSlidemenu(boolean hasFocus, int position) {
        if (hasFocus) {
            if (position % lineItem == 0 || position % lineItem == 1) {
                mArrow.setVisibility(View.VISIBLE);
            }
            mActivity.doHideSlidingMenu();
        } else {
            if (position % lineItem == 0 || position % lineItem == 1)
                mArrow.setVisibility(View.INVISIBLE);
        }
    }

    private void isEmptyDialog() {
        final CustomDialog dialog = new CustomDialog(getActivity());
        if (getResources().getString(R.string.history).equals(titleType)) {
            dialog.setMessage(getText(R.string.empty_history_dialog_title));
        } else {
            dialog.setMessage(getText(R.string.empty_dialog_title));
        }
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


    public void footmarkMenuClick(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHistory) {
                if (!isOpenOrDelete) {
                    updateUI(false);
                }
            } else {
                position = vgridview_collect_content.getFocusPosition();
                if (isDelete) {
                    isDelete = false;
                    updateUI(false);
                    return;
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (null != footmarkDialog && footmarkDialog.isShowing()) {
                footmarkDialog.dismiss();
                return;
            }
//            shadow.setVisibility(View.INVISIBLE);
            if (isHistory) {         //历史菜单逻辑
                if (DbUtil.queryAll(GlobalConstant.ISHISTORY).size() > 0) {
                    if (isOpenOrDelete) {
                        makeDialog();
                    } else {
                        updateUI(false);
                    }
                } else {
                    GlobalToast.makeText(getActivity(), getString(R.string.history_clean_already),
                            GlobalConstant.ToastShowTime).show();
                }
            } else {                                                                    //收藏菜单逻辑
                if (DbUtil.queryAll(false).size() > 0) {
                    position = vgridview_collect_content.getFocusPosition();
                    if (isDelete) {

                        updateUI(false);
                        isDelete = false;
                    } else {
                        makeDialog();
                    }
                } else {
                    isDelete = false;
                    GlobalToast.makeText(getActivity(), getString(R.string.collect_clean_already),
                            GlobalConstant.ToastShowTime).show();
                }
            }
        }

    }

    public void updateUI(boolean isClean) {
        if (TextUtils.isEmpty(titleType) || getResources().getString(R.string.history).equals(titleType)) {
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
        } else {
//            .
            if (isClean) {
                DbUtil.deleteAll(false);
                mData = new Select().from(VideoSetDao.class)
                        .where("Collect = ?", 1).execute();
                collectAdapter.notifyDataSetChanged();
                changeView();
            } else {
                // 更换为删除状态
                if (isDelete) {
                    iv_menu_hearer.setFocusable(true);
                    ll_to_sreach.setFocusable(false);
                    iv_menu_hearer.requestFocus();

                } else {// 更换为取消删除状态
                    iv_menu_hearer.setFocusable(true);
                    ll_to_sreach.setFocusable(false);
                    iv_menu_hearer.requestFocus();
                }
                try {
                    collectAdapter.notifyDataSetChanged();
                    vgridview_collect_content.getChildAt(position).requestFocus();
                    iv_menu_hearer.setFocusable(false);
                    ll_to_sreach.setFocusable(true);
                } catch (Exception e) {
                }
            }
        }


    }

    public void makeDialog() {
        footmarkDialog = new FootmarkDialog(
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
                        if (!isHistory) {
                            isDelete = true;
                        }
                        updateUI(false);
                    }
                });
        footmarkDialog.show();
    }

}
