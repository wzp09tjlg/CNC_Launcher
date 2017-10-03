package cn.cncgroup.tv.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;

/**
 * 自定义控件-自定义对话框
 *
 * @author Ives
 */
public class FootmarkDialog extends Dialog {
    private static final String TAG = "FootmarkDialog";
    private TextView mPositiveButton; // 确定按钮
    private TextView mNegativeButton; // 取消按钮
    private RelativeLayout mNegative;
    private RelativeLayout mPositive;
    private FootmarkDialog mFootmarkDialog;
//    private View.OnFocusChangeListener mListener = new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus) {
//                v.setBackgroundResource(R.drawable.bgshap);
//            } else {
//                v.setBackgroundResource(android.R.color.transparent);
//            }
//        }
//    };

    private View.OnClickListener mNegaListener;
    private View.OnClickListener mPosiListener;

    /**
     * @param context
     */
    public FootmarkDialog(Context context) {
        super(context, R.style.CustomDialog); // 自定义style主要去掉标题，标题将在setCustomView中自定义设置
        setCustomView();
    }


    /**
     * 设置整个弹出框的视图
     */
    private void setCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_footmark, null);
        mPositiveButton = (TextView) view.findViewById(R.id.positiveButton);
        mNegativeButton = (TextView) view.findViewById(R.id.negativeButton);
        mNegative = (RelativeLayout) view.findViewById(R.id.dialog_rl_negative);
        mPositive = (RelativeLayout) view.findViewById(R.id.dialog_rl_positive);
//        mNegative.setOnFocusChangeListener(mListener);
//        mPositive.setOnFocusChangeListener(mListener);
        super.setContentView(view);
    }

    public void setNegativeButton(CharSequence text,
                                  View.OnClickListener listener) {
        mNegativeButton.setText(text);
        mNegative.setVisibility(View.VISIBLE);
        mNegaListener = listener;
        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNegaListener.onClick(v);
                dismiss();
            }
        });
    }

    public void setPositiveButton(CharSequence text,
                                  View.OnClickListener listener) {
        mPositiveButton.setText(text);
        mPositive.setVisibility(View.VISIBLE);
        mPosiListener = listener;
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosiListener.onClick(v);
                dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN){
            Log.i(TAG,"onKeyDown");
            dismiss();
        }
        return false;
    }
}