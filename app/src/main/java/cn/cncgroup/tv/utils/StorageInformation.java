package cn.cncgroup.tv.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * Created by jinwenchao123 on 15/6/8.
 */
public final class StorageInformation {
    private final boolean removeable;
    /**
     * 存储空间的根目录
     */
    private final String rootPath;

    /**
     * 存储空间的label
     */
    private final String label;

    /**
     * 构造器。
     *
     * @param rootPath
     * @param label
     */
    StorageInformation(String rootPath, boolean removeable, String label) {
        this.removeable = removeable;
        this.rootPath = rootPath;
        this.label = label;
    }

    /**
     * 构造器。
     */
    StorageInformation() {
        this.removeable = false;
        this.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.label = "";
    }

    public boolean isRemoveable() {
        return removeable;
    }

    /**
     * 获取存储空间的根目录。
     *
     * @return
     */
    public String getRootPath() {
        return rootPath;
    }


    /**
     * 存储的Label。
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * 获取可用存储空间大小。
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public long getAvailableBytes() {
        try {
            StatFs stat = new StatFs(rootPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getAvailableBytes();
            } else {
                return ((long) stat.getBlockSize()) * stat.getAvailableBlocks();
            }
        } catch (Exception ex) {
//                MLog.d(StorageInformation.class.getSimpleName(), "exception", ex);
            return -1;
        }
    }


    public long  getTotalBytes() {
        try {
            StatFs stat = new StatFs(rootPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getTotalBytes();
            } else {
                return ((long) stat.getBlockSize()) * stat.getFreeBlocks();
            }
        } catch (Exception ex) {
//                MLog.d(StorageInformation.class.getSimpleName(), "exception", ex);
            return -1;
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other == null || !StorageInformation.class.isInstance(other)) {
            return false;
        }
        StorageInformation storage = (StorageInformation) other;
        return this.rootPath.equals(storage.rootPath);
    }
}
