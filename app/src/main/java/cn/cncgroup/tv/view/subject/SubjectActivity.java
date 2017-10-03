package cn.cncgroup.tv.view.subject;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.modle.Subject;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;

/**
 * Created by Wu on 2015/9/16.
 */
public class SubjectActivity extends BaseActivity
        implements OnItemFocusListener<Subject>, OnItemClickListener<Subject>,
        BaseRequest.Listener<SubjectData> {
    private static final String TAG = "SubjectActivity";
    private HorizontalGridView mGridView;
    private SubjectAdapter mAdapter;
    private SubjectSet mSubjectSet;
    private ShadowView mShadowView;
    private View mFocusedView;
    private View mLeftIndicator;
    private View mRightIndicator;
    private SimpleDraweeView mBackground;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_subject);
    }

    @Override
    protected void findView() {
        mGridView = (HorizontalGridView) findViewById(R.id.gridview);
        mShadowView = (ShadowView) findViewById(R.id.shadow);
        mLeftIndicator = findViewById(R.id.indictor_left);
        mRightIndicator = findViewById(R.id.indictor_right);
        mBackground = (SimpleDraweeView) findViewById(R.id.background);
    }

    @Override
    protected void initView() {
        mAdapter = new SubjectAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        mSubjectSet = (SubjectSet) getIntent()
                .getSerializableExtra(GlobalConstant.KEY_SUBJECT_SET);
        mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mGridView.getFirstVisibleIndex() == 0) {
                        mLeftIndicator.setVisibility(View.INVISIBLE);
                    } else {
                        mLeftIndicator.setVisibility(View.VISIBLE);
                    }
                    if (mGridView
                            .getLastVisibleIndex() == mAdapter.getItemCount()
                            - 1) {
                        mRightIndicator.setVisibility(View.INVISIBLE);
                    } else {
                        mRightIndicator.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mFocusedView != null) {
                    mShadowView.moveTo(mFocusedView.findViewById(R.id.image));
                }
            }
        });
        mShadowView.init(25);
        showLoading();
        NetworkManager.getInstance().getSubjectList(mSubjectSet.getSourceUrl(),
                this, TAG);
    }

    @Override
    public void onResponse(SubjectData result, boolean isFromCache) {
        if (result.getResult() == null) {
            hideLoading();
            showError();
            return;
        }
        hideLoading();
        mAdapter.bindData(result);
        if (mAdapter.getItemCount() > 5) {
            mRightIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(int errorCode, Request request) {
        showError();
    }

    @Override
    public void onItemClickLister(View view, int position, Subject subject) {
        VideoSet set = new VideoSet();
        set.setId(subject.getId() + "");
        if (subject.getType().equals(CategoryUtils.getCategoryByName("综艺").getId()) && subject.getTotalCount() == 1) {
            Project.get().getConfig().playVideo(this, set.getId(), set.getId(), 1 + "", 0, subject.getName(), subject.getImage(), subject.getType());
            return;
        }
        Project.get().getConfig().openVideoDetail(getActivity(), set);
    }

    @Override
    public void onItemFocusLister(View view, int position, Subject subject, boolean hasFocus) {
        if (hasFocus) {
            mFocusedView = view;
            mShadowView.moveTo(view.findViewById(R.id.image));
            mBackground.setImageURI(Uri.parse(subject.getImageBig()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.clearImage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.restoreImage();
    }
}
