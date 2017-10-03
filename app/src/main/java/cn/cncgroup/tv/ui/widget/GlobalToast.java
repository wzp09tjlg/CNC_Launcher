package cn.cncgroup.tv.ui.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.utils.GlobalConstant;

/**
 * 全局的Toast
 * Created by zhangguang on 2015/10/15.
 */
public class GlobalToast {
    private static final String TAG = "GlobalToast";
    private static  Toast toast;
    private static TextView textView;
    private static View mView;
    private static GlobalToast toastCustom = null;

    private GlobalToast(Context context, String text) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mView = layoutInflater.inflate(R.layout.main_toast, null);
        textView = (TextView) mView.findViewById(R.id.main_toast_text);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), GlobalConstant.XJLFONT);
        textView.setTypeface(typeface);                                     //设置字体库
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER|Gravity.BOTTOM, 12, 40);
//        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER;
//        params.gravity = Gravity.BOTTOM;
//        params.y = 40;
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(mView);
//        toast.show();
        //----------------------------------------------//
     /*   mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mView = layoutInflater.inflate(R.layout.main_toast, null);        //加载View
        textView = (TextView) mView.findViewById(R.id.main_toast_text);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), GlobalConstant.XJLFONT);
        textView.setTypeface(typeface);                                     //设置字体库
        textView.setText(text);
        wdm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        timer = new Timer();
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
//      params.windowAnimations = toast.getView().getAnimation().INFINITE;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//      params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER;
        params.gravity = Gravity.BOTTOM;
        params.y = 40;
        this.time = time;
        mView.setVisibility(View.INVISIBLE);*/
    }


    public static GlobalToast makeText(Context context, String text, int time) {
        Log.i(TAG,"context="+context);
        if (toast == null) {
            toastCustom = new GlobalToast(context, text);
        }
        toast.setDuration(time * 1000);
        textView.setText(text);
        /*if (toastCustom == null) {
            toastCustom = new GlobalToast(context, text, time);
        }
        mContext = context;
        toastCustom.textView.setText(text);
        toastCustom.time = time;
        return toastCustom;*/
        return toastCustom;
    }

    public void show() {
        toast.show();
        /*cancel();
        wdm.addView(mView, params);
        mView.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "mViewIsShowIng:" + mView.getVisibility());
                cancel();
            }
        }, (long) time * 1000);*/
    }

    public static void cancel() {
        if(toast!=null) {
            toast.cancel();
        }
       /* Log.i(TAG, "Toast,cancel()");
        if (mView.getParent() != null) {
            wdm.removeView(mView);
            Log.i(TAG, "Toast,cancel() removeView");
        }
        mView.setVisibility(View.GONE);
        mHandler.removeCallbacksAndMessages(null);
        timer.cancel();
        isShowIng = false;*/
    }
}
