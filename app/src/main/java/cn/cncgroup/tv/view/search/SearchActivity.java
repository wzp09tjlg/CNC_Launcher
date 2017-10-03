package cn.cncgroup.tv.view.search;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;

/**
 * Created by zhangbo on 15-9-14.
 */
public class SearchActivity extends BaseActivity
        implements OnItemClickListener<SearchKeyItem>, SearchDpad.InputCallback {

    private VerticalGridView mKeyboard;
    private EditText mSearchInput;

    private PopupWindow mPopupWindow;
    private Handler mHandler = new Handler();
    private Runnable mDismissRunable = new Runnable() {
        @Override
        public void run() {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void findView() {
        mKeyboard = (VerticalGridView) findViewById(R.id.search_keyboard);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    search(mSearchInput.getText().toString());
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.search_content, Project.get()
                            .getConfig().getSearchRecommendFragment());
                    transaction.commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void initView() {
        mKeyboard.setAdapter(new SearchKeyboardAdapter(this));
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.search_content,
                Project.get().getConfig().getSearchRecommendFragment());
        transaction.commit();
        mKeyboard.setFocusPosition(4);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mSearchInput.getText().toString().length() > 0)
            mSearchInput.getText().delete(mSearchInput.getText().toString().length() - 1, mSearchInput.getText().toString().length());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClickLister(View view, int position,
                                  SearchKeyItem keyItem) {
        if (keyItem.type == SearchKeyItem.KeyType.INPUT) {
            if (keyItem.getLetterCount() == 0) {
                onInput(keyItem.number);
            } else {
                showDpad(view, keyItem);
            }
        } else if (keyItem.type == SearchKeyItem.KeyType.DELETE) {
            mSearchInput.dispatchKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else if (keyItem.type == SearchKeyItem.KeyType.CLEAR) {
            mSearchInput.setText("");
        }
    }

    @Override
    public void onInput(String input) {
        if (input != null) {
            mSearchInput.append(input);
            if (mPopupWindow != null) {
                mHandler.postDelayed(mDismissRunable, 200);
            }
        }
    }

    private void search(String keyword) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.search_content,
                SearchResultFragment.getInstance(keyword));
        transaction.commit();
    }

    private void showDpad(View view, SearchKeyItem keyItem) {
        int length = getResources().getDimensionPixelSize(R.dimen.dimen_240dp);
        int anchorWidth = view.getMeasuredWidth();
        int anchorHeight = view.getMeasuredHeight();
        int offsetx = (length - anchorWidth) / 2;
        int offsety = (length + anchorHeight) / 2;
        SearchDpad dpad = new SearchDpad(this, this);
        dpad.setKeyItem(keyItem);
        mPopupWindow = new PopupWindow(dpad, length, length);
        mPopupWindow.setFocusable(true);
        mPopupWindow
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.showAsDropDown(view, -offsetx, -offsety);
    }
}
