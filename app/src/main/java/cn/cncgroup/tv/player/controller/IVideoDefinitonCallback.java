package cn.cncgroup.tv.player.controller;

import android.util.SparseArray;

/**
 * Created by JIF on 2015/6/30.
 */
public interface IVideoDefinitonCallback {
	/**
	 * 清晰度信息，清晰度value--对应的vid
	 * @param result
	 */
	void onSuccess(SparseArray<String> result);
	void onFailed(Exception e);
}
