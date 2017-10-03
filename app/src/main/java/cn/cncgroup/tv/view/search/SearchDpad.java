package cn.cncgroup.tv.view.search;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;

/**
 * Created by zhangbo on 15-9-15.
 */
public class SearchDpad extends RelativeLayout{
    private InputCallback mCallback;
    private SearchKeyItem mItem;
    private TextView mTextCenter;
    private TextView mTextLeft;
    private TextView mTextTop;
    private TextView mTextRight;
    private TextView mTextBottom;

    private ImageView mBackground;

    public SearchDpad(Context context, InputCallback callback) {
        super(context);
        this.mCallback = callback;
        initView(context);
    }

    public void setKeyItem(SearchKeyItem item) {
        this.mItem = item;
        mTextCenter.setText(mItem.number);
        mTextLeft.setText(mItem.letter1);
        mTextTop.setText(mItem.letter2);
        mTextRight.setText(mItem.letter3);
        mTextBottom.setText(mItem.letter4);
    }

    public void initView(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_dpad, this, true);
        mBackground = (ImageView) itemView.findViewById(R.id.image);
        mTextCenter = (TextView) itemView.findViewById(R.id.text_center);
        mTextLeft = (TextView) itemView.findViewById(R.id.text_left);
        mTextTop = (TextView) itemView.findViewById(R.id.text_top);
        mTextRight = (TextView) itemView.findViewById(R.id.text_right);
        mTextBottom = (TextView) itemView.findViewById(R.id.text_bottom);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            mBackground.setImageLevel(0);
            mCallback.onInput(mItem.number);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mItem.letter1 != null) {
                mBackground.setImageLevel(1);
            }
            mCallback.onInput(mItem.letter1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mItem.letter2 != null) {
                mBackground.setImageLevel(2);
            }
            mCallback.onInput(mItem.letter2);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mItem.letter3 != null) {
                mBackground.setImageLevel(3);
            }
            mCallback.onInput(mItem.letter3);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mItem.letter4 != null) {
                mBackground.setImageLevel(4);
            }
            mCallback.onInput(mItem.letter4);
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface InputCallback {
        void onInput(String input);
    }
}
