package cn.cncgroup.tv.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhang on 2015/5/19.
 */
public class DownloadUpdateManager
{
	public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
	private static DownloadUpdateManager mManager;
	private DownloadManager mDownloadManager;

	private DownloadUpdateManager(Context context)
	{
		mDownloadManager = (DownloadManager) context
		        .getSystemService(Context.DOWNLOAD_SERVICE);
	}

	public static DownloadUpdateManager getInstance(Context context)
	{
		if (mManager == null)
		{
			mManager = new DownloadUpdateManager(context);
		}
		return mManager;
	}

	public DownloadChangeObserver downloadUpdate(DownloadFileInfo info,
	        DownLoadListener listener)
	{
		// 把任务加入队列并获取id值便于取消任务等操作
		info.id = mDownloadManager.enqueue(new DownloadManager.Request(Uri
		        .parse(info.url)).setDestinationInExternalPublicDir(
		        Environment.DIRECTORY_DOWNLOADS, info.name));
		return new DownloadChangeObserver(new Handler(Looper.getMainLooper()),
		        info, mDownloadManager, listener);
	}

	public void cancelDownload(long id)
	{
		mDownloadManager.remove(id);
	}
}
