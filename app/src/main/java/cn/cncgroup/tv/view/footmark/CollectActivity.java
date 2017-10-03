package cn.cncgroup.tv.view.footmark;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.baidu.mobstat.StatService;

import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.CollectAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.CustomDialog;
import cn.cncgroup.tv.ui.widget.FootmarkDialog;
import cn.cncgroup.tv.ui.widget.gridview.BaseGridView;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;

public class CollectActivity extends BaseActivity implements
        OnItemFocusListener<VideoSetDao>, OnItemClickListener<VideoSetDao> {
    private static final String TAG = "CollectActivity";
    private VerticalGridView vgridview_collect_content;
    private List<VideoSetDao> mData;
    private ImageView iv_collect_empty;
    private CollectAdapter adapter;
    private ImageView iv_menu_hearer;
    private boolean isDelete = false;
    private LinearLayout ll_to_sreach;
    private TextView tv_activity_title;
    private int position = 0;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected void findView() {
        vgridview_collect_content = (VerticalGridView) findViewById(R.id.vgridview_collect_content);
        iv_collect_empty = (ImageView) findViewById(R.id.iv_collect_empty);
        iv_menu_hearer = (ImageView) findViewById(R.id.iv_menu_hearer);
        vgridview_collect_content
                .setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        ll_to_sreach = (LinearLayout) findViewById(R.id.ll_to_sreach);

    }

    @Override
    protected void initView() {
//		localInitView();
//		vgridview_collect_content.requestFocus();
        tv_activity_title.setText(getString(R.string.activity_collect_title));
        ll_to_sreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏页搜索 添加统计
                StatService.onEvent(getActivity(), "Search_ collection", getActivity().getString(R.string.search_collection), 1);
                Project.get().getConfig().openSearch(getActivity());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        localInitView();
        vgridview_collect_content.requestFocus();
    }

    private void localInitView() {
        mData = new Select().from(VideoSetDao.class).where("Collect = ?", 1)
                .orderBy("SaveTime desc").execute();
        adapter = new CollectAdapter(getActivity(), mData, this, this);
        vgridview_collect_content.setAdapter(adapter);
        changeView();
        iv_menu_hearer.setFocusable(false);

    }

    @Override
    public void onItemClickLister(View view, int position,
                                  VideoSetDao videoSetDao) {
        if (isDelete) {
            DbUtil.deleteCollect(videoSetDao.videoSetId);
//            videoSetDao.delete();
            mData.remove(videoSetDao);
            adapter.notifyDataSetChanged();
            changeView();
        } else {
            VideoSet set = new VideoSet();
            set.setId(videoSetDao.videoSetId);
            Project.get().getConfig().openVideoDetail(getActivity(), set);
        }

    }

    @Override
    public void onItemFocusLister(View view, int position,
                                  VideoSetDao videoSetDao, boolean hasFocus) {
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

    public void updateUI(boolean isClean) {
        if (isClean) {
            DbUtil.deleteAll(false);
            mData = new Select().from(VideoSetDao.class)
                    .where("Collect = ?", 1).execute();
            adapter.notifyDataSetChanged();
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
                vgridview_collect_content.getChildAt(position).requestFocus();
                iv_menu_hearer.setFocusable(false);
                ll_to_sreach.setFocusable(true);
            } catch (Exception e) {
            }
        }
    }

    private void changeView() {
        if (mData.size() > 0) {
            iv_collect_empty.setVisibility(View.GONE);
            vgridview_collect_content.setVisibility(View.VISIBLE);
        } else {
            iv_collect_empty.setVisibility(View.VISIBLE);
            vgridview_collect_content.setVisibility(View.GONE);

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            position = vgridview_collect_content.getFocusPosition();
            if (isDelete) {
                isDelete = false;
                updateUI(false);
                return true;
            }

        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (DbUtil.queryAll(false).size() > 0) {
                position = vgridview_collect_content.getFocusPosition();
                if (isDelete) {
                    isDelete = false;
                    updateUI(false);
                } else {
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
                                    isDelete = true;
                                    updateUI(false);


                                }
                            });
                    footmarkDialog.show();
                }
            } else {
                showToast(getString(R.string.collect_clean_already), GlobalConstant.ToastShowTime);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void isEmptyDialog() {
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


//            dialog.setMessage(getText(R.string.app_tip_exit));
//
}
