package cn.cncgroup.tv.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cncgroup.tv.R;

/**
 * 自定义控件-自定义对话框
 *
 * @author Ives
 *
 */
public class CustomDialog extends Dialog
{
	private boolean mHasTitle = false;
	private TextView mTitle;
	private TextView mMsg; // 提示内容文字
    private TextView textViewLeftLine;     //分割线
	private TextView mPositiveButton; // 确定按钮
	private TextView mNegativeButton; // 取消按钮
	private RelativeLayout mNegative;
	private RelativeLayout mPositive;
	private View mLine;
	private LinearLayout mOut;
	private Context mContext;
	private LinearLayout mContent;
	private int mTextSize;
	private View.OnClickListener mNegaListener;
	private View.OnClickListener mPosiListener;
	private RelativeLayout mBtns;
	private boolean hasProgress = false;

	/**
	 * @param context
	 */
	public CustomDialog(Context context)
	{
		super(context, R.style.CustomDialog); // 自定义style主要去掉标题，标题将在setCustomView中自定义设置
		mContext = context;
		setCustomView();

	}
	public CustomDialog(Context context,boolean hasProgress)
	{
		super(context, R.style.CustomDialog); // 自定义style主要去掉标题，标题将在setCustomView中自定义设置
		mContext = context;
		setCustomView();
		this.hasProgress = hasProgress;

	}
	public CustomDialog(Context context, boolean cancelable,
	        OnCancelListener cancelListener)
	{
		super(context, R.style.CustomDialog);
		mContext = context;
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		setCustomView();
	}
	public CustomDialog(Context context, int theme)
	{
		super(context, R.style.CustomDialog);
		mContext = context;
		setCustomView();
	}
	/**
	 * 设置整个弹出框的视图
	 */
	private void setCustomView()
	{
		View view = LayoutInflater.from(getContext()).inflate(
		        R.layout.layout_dialog, null);
		mOut = (LinearLayout) view.findViewById(R.id.ll_out);
		mContent = (LinearLayout) view.findViewById(R.id.dialog_content);
		mBtns = (RelativeLayout) view.findViewById(R.id.ll_btn_content);
		mTitle = (TextView) view.findViewById(R.id.title);
		mMsg = (TextView) view.findViewById(R.id.message);
		mPositiveButton = (TextView) view.findViewById(R.id.positiveButton);
		mNegativeButton = (TextView) view.findViewById(R.id.negativeButton);
		mLine = view.findViewById(R.id.view_line);
		mNegative = (RelativeLayout) view.findViewById(R.id.dialog_rl_negative);
		mPositive = (RelativeLayout) view.findViewById(R.id.dialog_rl_positive);
		super.setContentView(view);
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mHasTitle = true;
		if(mTextSize>0)
			mTitle.setTextSize(mTextSize);
		mTitle.setText(title);
		mLine.setVisibility(View.VISIBLE);
	}

	@Override
	public void setTitle(int titleId)
	{
		mHasTitle = true;
		mLine.setVisibility(View.VISIBLE);
		setTitle(getContext().getString(titleId));
	}

	public void setMessageSize(int size){
		mMsg.setTextSize(size);
	}
	public void setMessage(CharSequence msg)
	{
		if(mTextSize>0)
			mMsg.setTextSize(mTextSize);
		mMsg.setText(msg);
	}

	public void setNegativeButton(CharSequence text,
	        View.OnClickListener listener)
	{
		mBtns.setVisibility(View.VISIBLE);
		if(mTextSize>0)
			mNegativeButton.setTextSize(mTextSize);
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
								  View.OnClickListener listener)
	{

		mBtns.setVisibility(View.VISIBLE);
		if(mTextSize>0)
			mPositiveButton.setTextSize(mTextSize);
		mPositiveButton.setText(text);
		mPositive.setVisibility(View.VISIBLE);
		mPosiListener = listener;
		mPositive.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mPosiListener.onClick(v);
				dismiss();
			}
		});
	}

	public void setCustomView(View view)
	{
		mTitle.setVisibility(View.VISIBLE);
		mContent.removeAllViews();
		mContent.addView(view);
	}

    @Override
    public void onStart()
    {
        super.onStart();
        getWindow().setLayout(
				mContent.getResources().getDimensionPixelSize(R.dimen.dimen_720dp),
				mContent.getResources().getDimensionPixelSize(R.dimen.dimen_410dp));
    }
	@Override
	public void show()
	{
		if (mHasTitle)
		{
			mTitle.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
		}
		else
		{
			mMsg.post(new Runnable() {
				@Override
				public void run() {
					int count = mMsg.getLineCount();
					int padding;
					if (hasProgress) {
						padding = (int) mContext.getResources().getDimension(
								R.dimen.dimen_0100dp);
						mOut.setPadding(0, padding, 0, 0);
					} else {
						if (count == 1) {
							padding = (int) mContext.getResources().getDimension(
									R.dimen.dimen_116dp);
							mOut.setPadding(0, padding, 0, 0);
						}
						if (count == 2 ) {
							padding = (int) mContext.getResources().getDimension(
									R.dimen.dimen_96dp);
							// mMsg.setTe
							float spacing = mContext.getResources().getDimension(
									R.dimen.dimen_8dp);
							mMsg.setLineSpacing(spacing, 1);
							mOut.setPadding(0, padding, 0, 0);
						}
					}
				}
			});
		}
		super.show();
	}

	public void setTextSize(int size) {
		mTextSize = size;
	}
}