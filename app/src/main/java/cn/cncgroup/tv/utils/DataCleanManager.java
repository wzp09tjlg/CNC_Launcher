package cn.cncgroup.tv.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

public class DataCleanManager {
	// 代码来自互联网

	/**
	 * @param context
	 *            上下文 删除上下文下的缓存文件
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * @param context
	 *            上下文 删除上下文下的数据库文件
	 */
	public static void cleanDatabase(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * @param context
	 *            上下文 删除上下文下的共享文件
	 */
	public static void cleanSharePreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * @param context
	 *            清除外部的cache 下的文件
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * @param filePath
	 *            清除指定路径的下的文件
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * @param context
	 *            上下文
	 * @param filePaths
	 *            指定的文件路径
	 */
	public static void cleanApplicationData(Context context,
			String... filePaths) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabase(context);
		cleanSharePreference(context);
		if (filePaths == null) {
			return;
		} else {
			for (String filePath : filePaths) {
				cleanCustomCache(filePath);
			}
		}
	}

	/**
	 * @param directory
	 *            删除文件路径中的文件
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				file.delete();
			}
		}
	}

	/**
	 * @param file
	 *            指定的文件
	 * @return 指定文件的大小
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * @param filePath
	 *            删除文件
	 * @param deleteThisPath
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) { // 如果下面还有文件
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) { // 如果是文件
						file.delete();
					} else {
						if (file.listFiles().length == 0) {
							file.delete();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param file  获取文件的大小
	 * @return
	 * @throws Exception
	 */
	public static String getCacheSize(File file) throws Exception {
		return getFormatSize(getFolderSize(file));
	}

	/**
	 * @param size
	 *            格式化单位
	 */
	private static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(gigaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraByte = gigaByte / 1024;
		if (teraByte < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(teraByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}

		return teraByte + "TB";
	}
}
