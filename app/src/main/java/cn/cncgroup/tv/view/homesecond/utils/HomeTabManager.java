package cn.cncgroup.tv.view.homesecond.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.view.homesecond.CategoryFragment;
import cn.cncgroup.tv.view.homesecond.HomeActivity;
import cn.cncgroup.tv.view.homesecond.MovieFragment;
import cn.cncgroup.tv.view.homesecond.SubjectFragment;
import cn.cncgroup.tv.view.homesecond.UseFragment;
import cn.cncgroup.tv.view.homesecond.VarietyFragment;
import cn.cncgroup.tv.view.homesecond.adapter.HomePagerAdapter;
import cn.cncgroup.tv.view.homesecond.adapter.HomeTabAdapter;

/**
 * Created by zhang on 2015/11/16.
 */
public class HomeTabManager {
    private static final String TAG = "HomeTabManager";
    /**
     * 保存tab的key
     */
    private static final String KEY_CUSTOM_TABS = "key_custom_tabs";
    /**
     * 保存默认tab的key
     */
    private static final String KEY_DEFAULT_TAB_INDEX = "selectedItem";
    private static HomeTabManager sManager;
    /**
     * 开始固定的tabs
     */
    private ArrayList<HomeTab> mStartFixedTabs;
    /**
     * 末尾固定的tabs
     */
    private ArrayList<HomeTab> mEndFixedTabs;
    /**
     * 用户添加tabs
     */
    private ArrayList<HomeTab> mAddedCustomTabs;
    /**
     * 所有自定义tabs
     */
    private ArrayList<HomeTab> mAllCustomTabs;
    /**
     * 当前的默认tab
     */
    private int mDefaultTabIndex;
    private HomeTabAdapter mHomeTabAdapter;
    private HomePagerAdapter mHomePagerAdapter;
    private HomeActivity mHomeActivity;

    private HomeTabManager() {
        initHomeTabs();
    }

    public static HomeTabManager getInstance() {
        if (sManager == null) {
            sManager = new HomeTabManager();
        }
        return sManager;
    }

    /**
     * 不同tab与Fragment的对应关系
     */
    public static String getFragmentName(String tabName) {
        if (tabName.equals("综艺") || tabName.equals("生活")) {
            return VarietyFragment.class.getName();
        } else if (tabName.equals("应用")) {
            return UseFragment.class.getName();
        } else if (tabName.equals("频道")) {
            return CategoryFragment.class.getName();
        } else if (tabName.equals("专题")) {
            return SubjectFragment.class.getName();
        } else {
            return MovieFragment.class.getName();
        }
    }

    /**
     * 初始化tabs数据
     */
    private void initHomeTabs() {
        mStartFixedTabs = initStartFixedTabs();
        mAddedCustomTabs = initAddedCustomTabs();
        mAllCustomTabs = initAllCustomTabs();
        mEndFixedTabs = initEndFixedTabs();
        mDefaultTabIndex = initDefaultIndex();
    }

    public void bindHomeActivity(HomeActivity activity) {
        this.mHomeActivity = activity;
    }

    public void bindHomeTabAdapter(HomeTabAdapter adapter) {
        this.mHomeTabAdapter = adapter;
    }

    public void bindHomePagerAdapter(HomePagerAdapter adapter) {
        this.mHomePagerAdapter = adapter;
    }

    public void resetCustomTab(ArrayList<HomeTab> choiceTab) {
        int lastSize = mAddedCustomTabs.size();
        mAddedCustomTabs.clear();
        for (HomeTab tab : choiceTab) {
            mAddedCustomTabs.add(tab);
        }
        int currentSize = mAddedCustomTabs.size();
        if (currentSize == lastSize) {
            mHomeTabAdapter.notifyItemRangeChanged(mStartFixedTabs.size(), mAddedCustomTabs.size());
        } else if (currentSize > lastSize) {
            mHomeTabAdapter.notifyItemRangeChanged(mStartFixedTabs.size(), lastSize);
            mHomeTabAdapter.notifyItemRangeInserted(mStartFixedTabs.size() + lastSize, currentSize - lastSize);
        } else if (currentSize < lastSize) {
            mHomeTabAdapter.notifyItemRangeChanged(mStartFixedTabs.size(), currentSize);
            mHomeTabAdapter.notifyItemRangeRemoved(mStartFixedTabs.size() + currentSize, lastSize - currentSize);
        }
        mHomePagerAdapter.notifyDataSetChanged();
        mHomeActivity.resetAfterNotity();
        saveAddedCustomTabs();
    }

    public ArrayList<HomeTab> getAllCustomTabs() {
        return mAllCustomTabs;
    }

    public ArrayList<HomeTab> getAddedCustomTabs() {
        return mAddedCustomTabs;
    }

    public ArrayList<String> getHomeTabNames() {
        ArrayList<String> list = new ArrayList<String>();
        for (HomeTab tab : mStartFixedTabs) {
            list.add(tab.name);
        }
        for (HomeTab tab : mAddedCustomTabs) {
            list.add(tab.name);
        }
        for (HomeTab tab : mEndFixedTabs) {
            list.add(tab.name);
        }
        list.remove(list.size() - 1);
        return list;
    }

    public void setDefaultTabIndex(int index) {
        this.mDefaultTabIndex = index;
    }

    public HomeTab getHomeTabByIndex(int position) {
        if (position < 0) {
            return null;
        }
        if (position < mStartFixedTabs.size()) {
            return mStartFixedTabs.get(position);
        } else if (position < mStartFixedTabs.size() + mAddedCustomTabs.size()) {
            return mAddedCustomTabs.get(position - mStartFixedTabs.size());
        } else if (position < mStartFixedTabs.size() + mAddedCustomTabs.size() + mEndFixedTabs.size()) {
            return mEndFixedTabs.get(position - mStartFixedTabs.size() - mAddedCustomTabs.size());
        }
        return null;
    }

    public int getHomeTabSize() {
        return mStartFixedTabs.size() + mAddedCustomTabs.size() + mEndFixedTabs.size();
    }

    public int getDefaultIndex() {
        return mDefaultTabIndex;
    }

    /**
     * 获得开始固定tab
     */
    private ArrayList<HomeTab> initStartFixedTabs() {
        ArrayList<HomeTab> list = new ArrayList<HomeTab>();
        list.add(new HomeTab("应用"));
        list.add(new HomeTab("电影"));
        list.add(new HomeTab("电视剧"));
        list.add(new HomeTab("综艺"));
        return list;
    }

    /**
     * 获得频道和自定义tab
     */
    private ArrayList<HomeTab> initEndFixedTabs() {
        ArrayList<HomeTab> list = new ArrayList<HomeTab>();
        list.add(new HomeTab("频道"));
        list.add(new HomeTab("+自定义"));
        return list;
    }

    /**
     * 得到已添加自定义tab
     */
    private ArrayList<HomeTab> initAddedCustomTabs() {
        ArrayList<HomeTab> list = new ArrayList<HomeTab>();
        String json = Preferences.build(CApplication.getInstance()).getString(KEY_CUSTOM_TABS, null);
        if (json != null) {
            return getGson().fromJson(json, new TypeToken<ArrayList<HomeTab>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 得到全部自定义tab
     */
    private ArrayList<HomeTab> initAllCustomTabs() {
        ArrayList<HomeTab> list = new ArrayList<HomeTab>();
        list.add(new HomeTab("专题"));
        list.add(new HomeTab("动漫"));
        list.add(new HomeTab("音乐"));
        list.add(new HomeTab("3D体验"));
        list.add(new HomeTab("少儿"));
        list.add(new HomeTab("生活"));
        list.add(new HomeTab("纪录片"));
        return list;
    }

    /**
     * 保存自定义tab
     */
    public void saveAddedCustomTabs() {
        Preferences.build(CApplication.getInstance()).putString(KEY_CUSTOM_TABS, getGson().toJson(mAddedCustomTabs));
    }

    /**
     * 初始化默认tab的index
     */
    private int initDefaultIndex() {
        String name = getDefaultTabName();
        for (int i = 0; i < getHomeTabSize(); i++) {
            HomeTab menu = getHomeTabByIndex(i);
            if (menu.name.equals(name)) {
                return i;
            }
        }
        return 1;
    }

    public String getDefaultTabName() {
        return Preferences.build(CApplication.getInstance()).getString(KEY_DEFAULT_TAB_INDEX, "电影");
    }

    /**
     * 获得可以去除无用字段的gson对象
     */
    private Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    /**
     * 获得开始固定部分tab的长度
     */
    public int getStartFixedTabsSize() {
        return mStartFixedTabs.size();
    }
}
