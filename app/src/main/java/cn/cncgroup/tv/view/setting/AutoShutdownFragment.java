package cn.cncgroup.tv.view.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.base.BaseFragment;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.ui.widget.ShadowView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;

/**
 * Created by Wu on 2015/11/18.
 */
public class AutoShutdownFragment extends BaseFragment implements
        View.OnFocusChangeListener {
    private static final String TAG = "AutoShutdownFragment";
    private static final String TYPE = "TYPE";
    private static final String TIP_SET_SUCCESS = "关机时间已设置成功";
    //widget
    private TextView mTextDes;
    private TextView mTextHour;
    private TextView mTextMinute;
    private TextView mTextTip;
    private ImageView mImgHourUp;
    private ImageView mImgHourDown;
    private ImageView mImgMinuteUp;
    private ImageView mImgMinuteDown;
    private ImageView mImgTimeSplit;
    private ShadowView mShadowCover;
    //data
    private Preferences shutdownPref;
    private Handler mHandler = new Handler();
    private Runnable mHideCoverRunable = new Runnable() {
        @Override
        public void run() {
            mShadowCover.setVisibility(View.INVISIBLE);
        }
    };
    //interface

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.getId() == R.id.shutdown_text_hour) {
            if (hasFocus) {
                mHandler.removeCallbacks(mHideCoverRunable);
                mShadowCover.moveTo(view);
                mTextTip.setVisibility(View.VISIBLE);
                showHourArrow();
            } else {
                mHandler.postDelayed(mHideCoverRunable, 16);
                mTextTip.setVisibility(View.INVISIBLE);
                hideHourArrow();
            }
        } else if (view.getId() == R.id.shutdown_text_minute) {
            if (hasFocus) {
                mHandler.removeCallbacks(mHideCoverRunable);
                mShadowCover.moveTo(view);
                mTextTip.setVisibility(View.VISIBLE);
                showMinuteArrow();
            } else {
                mHandler.postDelayed(mHideCoverRunable, 16);
                mTextTip.setVisibility(View.INVISIBLE);
                hideMinuteArrow();
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_auto_shutdown;
    }

    @Override
    protected void findView(View view) {
        mTextDes = (TextView) view.findViewById(R.id.shutdown_text_des);
        mTextHour = (TextView) view.findViewById(R.id.shutdown_text_hour);
        mTextMinute = (TextView) view.findViewById(R.id.shutdown_text_minute);
        mTextTip = (TextView) view.findViewById(R.id.shutdown_text_tip);
        mImgHourUp = (ImageView) view.findViewById(R.id.shutdown_img_hour_up);
        mImgHourDown = (ImageView) view.findViewById(R.id.shutdown_img_hour_down);
        mImgMinuteUp = (ImageView) view.findViewById(R.id.shutdown_img_minute_up);
        mImgMinuteDown = (ImageView) view.findViewById(R.id.shutdown_img_minute_down);
        mImgTimeSplit = (ImageView) view.findViewById(R.id.shutdown_img_time_split);
        mShadowCover = (ShadowView) view.findViewById(R.id.shutdown_cover);
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        boolean isAutoShutdown = bundle.getBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false);
        if (isAutoShutdown) {
            hideTextDesc();
            checkShutdown();
            mTextHour.setOnFocusChangeListener(this);
            mTextMinute.setOnFocusChangeListener(this);
            mShadowCover.init(23);
            initListener();
        } else {
            hideShutdownTime();
        }
    }

    private void checkShutdown() {
        shutdownPref = Preferences.build(getActivity());
        if (!shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, false)) return;
        String sHour = "23";
        String sMinute = "00";
        sHour = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_HOUR, "23");
        sMinute = shutdownPref.getString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, "00");
        mTextHour.setText(sHour);
        mTextMinute.setText(sMinute);
    }

    private void initListener() {
        mTextHour.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mImgHourUp.setPressed(true);
                    int mHour = Integer.parseInt(mTextHour.getText().toString());
                    int temp = ++mHour % 24;
                    String sHour = temp < 10 ? "0" + temp : "" + temp;
                    mTextHour.setText(sHour);
                    mImgHourUp.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mImgHourUp.setPressed(false);
                        }
                    }, 100);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mImgHourDown.setPressed(true);
                    int mHour = Integer.parseInt(mTextHour.getText().toString());
                    int temp = (--mHour + 24) % 24;
                    String sHour = temp < 10 ? "0" + temp : "" + temp;
                    mTextHour.setText(sHour);
                    mImgHourDown.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mImgHourDown.setPressed(false);
                        }
                    }, 100);
                } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String sHour = mTextHour.getText().toString();
                    String sMinute = mTextMinute.getText().toString();
                    if (sHour.equals("") || sMinute.equals("")) return false;
                    shutdownPref = Preferences.build(getActivity());
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, sHour);
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, sMinute);
                    shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, true);
                    operateShutdownService(false);
                    operateShutdownService(true);
                } else if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String sHour = mTextHour.getText().toString();
                    String sMinute = mTextMinute.getText().toString();
                    if (sHour.equals("") || sMinute.equals("")) return false;
                    shutdownPref = Preferences.build(getActivity());
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, sHour);
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, sMinute);
                    shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, true);
                    operateShutdownService(false);
                    operateShutdownService(true);
                    GlobalToast.makeText(getActivity(), TIP_SET_SUCCESS, 1).show();
                }
                return false;
            }
        });
        mTextMinute.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mImgMinuteUp.setPressed(true);
                    int mHour = Integer.parseInt(mTextMinute.getText().toString());
                    int temp = ++mHour % 60;
                    String sHour = temp < 10 ? "0" + temp : "" + temp;
                    mTextMinute.setText(sHour);
                    mImgMinuteUp.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mImgMinuteUp.setPressed(false);
                        }
                    }, 100);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mImgMinuteDown.setPressed(true);
                    int mHour = Integer.parseInt(mTextMinute.getText().toString());
                    int temp = (--mHour + 60) % 60;
                    String sHour = temp < 10 ? "0" + temp : "" + temp;
                    mTextMinute.setText(sHour);
                    mImgMinuteDown.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mImgMinuteDown.setPressed(false);
                        }
                    }, 100);
                } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String sHour = mTextHour.getText().toString();
                    String sMinute = mTextMinute.getText().toString();
                    if (sHour.equals("") || sMinute.equals("")) return false;
                    shutdownPref = Preferences.build(getActivity());
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, sHour);
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, sMinute);
                    shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, true);
                    operateShutdownService(false);
                    operateShutdownService(true);
                } else if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String sHour = mTextHour.getText().toString();
                    String sMinute = mTextMinute.getText().toString();
                    if (sHour.equals("") || sMinute.equals("")) return false;
                    shutdownPref = Preferences.build(getActivity());
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_HOUR, sHour);
                    shutdownPref.putString(GlobalConstant.AUTO_SHUTDOWN_MINUTE, sMinute);
                    shutdownPref.putBoolean(GlobalConstant.AUTO_SHUTDOWN_SWITCH, true);
                    operateShutdownService(false);
                    operateShutdownService(true);
                    GlobalToast.makeText(getActivity(), TIP_SET_SUCCESS, 1).show();
                }
                return false;
            }
        });
    }

    public void hideTextDesc() {
        mTextDes.setVisibility(View.INVISIBLE);
    }

    public void hideShutdownTime() {
        mTextHour.setVisibility(View.INVISIBLE);
        mTextMinute.setVisibility(View.INVISIBLE);
        mTextTip.setVisibility(View.INVISIBLE);
        mImgHourUp.setVisibility(View.INVISIBLE);
        mImgHourDown.setVisibility(View.INVISIBLE);
        mImgMinuteUp.setVisibility(View.INVISIBLE);
        mImgMinuteDown.setVisibility(View.INVISIBLE);
        mImgTimeSplit.setVisibility(View.INVISIBLE);
    }

    private void showHourArrow() {
        mImgHourUp.setVisibility(View.VISIBLE);
        mImgHourDown.setVisibility(View.VISIBLE);
    }

    private void hideHourArrow() {
        mImgHourUp.setVisibility(View.INVISIBLE);
        mImgHourDown.setVisibility(View.INVISIBLE);
    }

    private void showMinuteArrow() {
        mImgMinuteUp.setVisibility(View.VISIBLE);
        mImgMinuteDown.setVisibility(View.VISIBLE);
    }

    private void hideMinuteArrow() {
        mImgMinuteUp.setVisibility(View.INVISIBLE);
        mImgMinuteDown.setVisibility(View.INVISIBLE);
    }

    private void operateShutdownService(boolean flag) {
        Intent intentShutdownService = new Intent(getActivity(), AutoShutdownService.class);
        if (flag)
            getActivity().startService(intentShutdownService);
        else
            getActivity().stopService(intentShutdownService);
    }
}
