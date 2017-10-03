package cn.cncgroup.tv.view.homesecond.utils;

import java.util.HashMap;

import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.CategoryData;

/**
 * Created by zhang on 2015/9/29.
 */
public class CategoryUtils {
    public static CategoryData sCategoryData;
    private static HashMap<String, Category> mCacheMap;

    /**
     * 根据id获取Category
     */
    public static Category getCategoryById(String id) {
        if (mCacheMap == null) {
            mCacheMap = new HashMap<String, Category>();
        }
        Category category = mCacheMap.get(id);
        if (category != null)
            return category;
        if (sCategoryData != null) {
            for (Category c : sCategoryData.getData()) {
                if (c.getId().equals(id)) {
                    category = c;
                    break;
                }
            }
        }
        mCacheMap.put(id, category);
        return category;
    }

    /**
     * 根据name获取Category
     */
    public static Category getCategoryByName(String name) {
        if (mCacheMap == null) {
            mCacheMap = new HashMap<String, Category>();
        }
        Category category = mCacheMap.get(name);
        if (category != null)
            return category;
        if (sCategoryData != null) {
            for (Category c : sCategoryData.getData()) {
                if (c.getName().equals(name)) {
                    category = c;
                    break;
                }
            }
        }
        mCacheMap.put(name, category);
        return category;
    }
}