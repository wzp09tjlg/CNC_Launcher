package cn.cncgroup.tv.settings;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhang on 2015/11/20.
 */
public class TestFragment extends BaseFragment implements View.OnFocusChangeListener {
    int page;
    VerticalGridView mGridView;
    BaseSettingActivity mActivity;
    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mGridView.getFocusedChild() != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mActivity.moveTo(mGridView.getFocusedChild());
                } else {
                    mActivity.cancelMove();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mGridView.getFocusedChild() != null) {
                mActivity.cancelMove();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseSettingActivity) activity;
        page = Integer.valueOf(getTag());
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_test;
    }

    @Override
    protected void findView(View view) {
        mGridView = (VerticalGridView) view.findViewById(R.id.gridview);
    }

    @Override
    protected void initView() {
        mGridView.setAdapter(new Adapter(page, this));
        mGridView.addOnScrollListener(mScrollListener);
        mGridView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.HORIZONTAL));
        mGridView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mActivity.moveTo(v);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        TextView text;
    }

    static class Adapter extends RecyclerView.Adapter<TestFragment.ViewHolder> {
        View.OnFocusChangeListener mOnFocusListener;
        int page;

        public Adapter(int page, View.OnFocusChangeListener onFocusListener) {
            this.mOnFocusListener = onFocusListener;
            this.page = page;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(position + page * 100 + "");
            holder.itemView.setOnFocusChangeListener(mOnFocusListener);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}
