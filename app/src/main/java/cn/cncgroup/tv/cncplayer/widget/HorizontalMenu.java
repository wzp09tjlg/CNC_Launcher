package cn.cncgroup.tv.cncplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;

/**
 * Created by yqh on 2015/10/14.
 * yqh
 */
public class HorizontalMenu extends RelativeLayout implements View.OnFocusChangeListener {
    private static final String TAG = "HorizontalMenu";
    private Context mContext;
    private String mTitle;
    private LinearLayout mContener;
    private TextView viewTitle;
    private ArrayList<String> mItemsData;
    private int mItemsCount = 0;
    private int mCurrentIndex =0;
    private LinearLayout mItemsContener;

    public HorizontalMenu(Context context) {
        this(context, null);
    }

    public HorizontalMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttr(attrs);
        init(context);
        findView();
        initView();
    }

    private void init(Context context) {
        setFocusable(true);
        setClickable(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mContener = (LinearLayout) inflater.inflate(R.layout.horizontalmenu, null);
        addView(mContener);
        setOnFocusChangeListener(this);
    }

    private void findView() {
        viewTitle = (TextView) mContener.findViewById(R.id.menu_title);
        mItemsContener = (LinearLayout)mContener.findViewById(R.id.item_contener);
    }

    private void initView() {
        viewTitle.setText(mTitle);
        TextView tv = new TextView(mContext);
        tv.setText("qwewq");
        mItemsContener.addView(tv);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.HorizontalMenu);
        mTitle = typedArray.getString(R.styleable.HorizontalMenu_menu_title);
        Log.i(TAG, "mTitle =menu_title=" + mTitle);
        typedArray.recycle();
    }

    public void setItemsData(ArrayList<String> items) {
        mItemsData = items;
        mItemsCount = items.size();
        for (String str : items) {
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mItemsContener.setVisibility(GONE);
            mItemsContener.getChildAt(mCurrentIndex).requestFocus();
        } else {
            mItemsContener.setVisibility(VISIBLE);
        }
    }
}
