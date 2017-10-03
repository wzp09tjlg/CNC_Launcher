package cn.cncgroup.tv.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;

import cn.cncgroup.tv.conf.IAppConfig;
import cn.cncgroup.tv.conf.Project;
import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLActiveData;
import cn.cncgroup.tv.conf.model.cibn.modle.CIBNXMLLoginData;
import cn.cncgroup.tv.conf.model.cibn.request.CIBNActiveRequest;
import cn.cncgroup.tv.conf.model.cibn.request.CIBNLoginRequest;
import cn.cncgroup.tv.conf.model.cibn.utils.CIBNURLUtil;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyFocusListContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomySubjectListContent;
import cn.cncgroup.tv.conf.model.domybox.utils.DomyURLUtil;
import cn.cncgroup.tv.conf.model.voole.modle.VooleVideoListData;
import cn.cncgroup.tv.conf.model.voole.request.VooleSearchRecommendRequest;
import cn.cncgroup.tv.conf.model.voole.utils.VooleURLUtil;
import cn.cncgroup.tv.modle.AppData;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.modle.RecommendData;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.modle.UpdateData;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.JsonRequest;
import cn.cncgroup.tv.network.utils.HttpUtil;
import cn.cncgroup.tv.network.utils.LocalURLUtil;

/**
 * Created by zhang on 2015/4/21.
 */
public class NetworkManager {
    private static final String DEFAULT_CACHE_DIR = "networkCache";
    /**
     * 网络连接类型
     */
    public static int CONNECTION_TYPE;
    private static NetworkManager sNetworkManger;
    public Handler UIHandler;
    public DiskCache diskCache;
    public IAppConfig mConfig;

    private NetworkManager() {
        mConfig = Project.get().getConfig();
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static NetworkManager getInstance() {
        if (sNetworkManger == null) {
            sNetworkManger = new NetworkManager();
        }
        return sNetworkManger;
    }

    /**
     * 取消相应tag的访问
     */
    public static void cancelRequest(Object tag) {
        HttpUtil.cancelRequest(tag);
    }

    /**
     * 初始化缓存,应该在Application初始化时初始化
     */
    public void initDiskCache(Context context) {
        File cacheDir = new File(context.getFilesDir(), DEFAULT_CACHE_DIR);
        diskCache = new DiskCache(cacheDir);
        diskCache.initialize();
    }

    /**
     * 专辑列表
     */
    public void getVideoSetList(String url, BaseRequest.Listener<VideoSetListData> listener, Object tag, int pageSize) {
        mConfig.getVideoSetList(url, listener, tag, pageSize);
    }

    /**
     * 最热专辑列表
     */
    public void getHotVideoSetList(String type, int pageSize, int pageNo,
                                   BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        mConfig.getHotVideoSetList(type, pageSize, pageNo, listener, tag);
    }

    /**
     * 最新专辑列表
     */
    public void getNewVideoSetList(String type, int pageSize, int pageNo,
                                   BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        mConfig.getNewVideoSetList(type, pageSize, pageNo, listener, tag);
    }

    /**
     * 专辑详情(通过videoSetId获取)
     */
    public void getVideoSetDetail(String url,
                                  BaseRequest.Listener<VideoSetDetailData> listener, Object tag) {
        Project.get().getConfig().getVideoSetDetail(url, listener, tag);
    }

    /**
     * 剧集列表
     */
    public void getVideoList(String url,
                             BaseRequest.Listener<VideoListData> listener, Object tag) {
        Project.get().getConfig().getVideoList(url, listener, tag);
    }

    /**
     * 筛选标签
     */
    public void getTagFilter(String type, BaseRequest.Listener<FilterListData> listener, Object tag) {
        mConfig.getTagFilter(type, listener, tag);
    }

    public void getRecommendVideoSetList(String url, BaseRequest.Listener<RecommendVideoSetListData> listener, Object tag) {
        mConfig.getRecommendVideoSetList(url, listener, tag);
    }

    public void getSubjectSetList(String url, BaseRequest.Listener<SubjectSetData> listener, Object tag) {
        mConfig.getSubjectSetList(url, listener, tag);
    }

    public void getSubjectList(String url, BaseRequest.Listener<SubjectData> listener, Object tag) {
        mConfig.getSubjectList(url, listener, tag);
    }

    /**
     * 获得焦点图列表
     */
    public void getDomyFocusList(String type, BaseRequest.Listener<DomyFocusListContent> listener, Object tag) {
        new JsonRequest(DomyURLUtil.getDomyFocusListUrl(type), tag, listener,
                false, DomyFocusListContent.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    /**
     * 分页获取专题列表
     */
    public void getDomySubjectList(String type, int pageNo, int pageSize, BaseRequest.Listener<DomySubjectListContent> listener, Object tag) {
        new JsonRequest(DomyURLUtil.getDomySubjectListUrl(type, pageNo, pageSize), tag, listener, false, DomySubjectListContent.class).performNetwork(BaseRequest.Method.GET);
    }

    /**
     * 搜索 POST的表单结构： keyword:搜索关键字 select_model:筛选方式[片名：0,演员:1] record_type:频道id
     * pageNo pageSize vsersion
     */
    public void search(String keyword, int pageNo, int pageSize,
                       BaseRequest.Listener<SearchData> listener, Object tag) {
        mConfig.getSearchResult(keyword, pageNo, pageSize, listener, tag);
    }

    public void search(String keyword, String mtype, int pageNo, int pageSize,
                       BaseRequest.Listener<SearchData> listener, Object tag) {
        if (TextUtils.isEmpty(mtype))
            mConfig.getSearchResult(keyword, pageNo, pageSize, listener, tag);
        mConfig.getSearchResult(keyword, mtype, pageNo, pageSize, listener, tag);
    }

    /**
     * 首页推荐
     */
    public void getRecommendData(BaseRequest.Listener<RecommendData> listener,
                                 Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        mConfig.getRecommendData(listener, tag, isUseCache, isOnlyUseCache);
    }

    /**
     * 首页频道
     */
    public void getCategoryData(BaseRequest.Listener<CategoryData> listener,
                                Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        mConfig.getCategoryData(listener, tag, isUseCache, isOnlyUseCache);
    }

    /**
     * 首页应用推荐
     */
    public void getAppData(BaseRequest.Listener<AppData> listener, Object tag,
                           boolean isUseCache, boolean isOnlyUseCache) {
        new JsonRequest(LocalURLUtil.getLocalAppUrl(), tag, listener, true,
                AppData.class).performNetwork(BaseRequest.Method.GET,
                isUseCache, isOnlyUseCache);
    }

    public void activeCIBNTerminal(
            BaseRequest.Listener<CIBNXMLActiveData> listener, Object tag,
            String mac) {
        new CIBNActiveRequest(CIBNURLUtil.getCIBNStaticTerminalActiveUrl(mac),
                tag, listener, false).performNetwork(BaseRequest.Method.GET);
    }

    public void activeCIBNTerminalDynamic(
            BaseRequest.Listener<CIBNXMLActiveData> listener, Object tag,
            String mac, int areaId, int type) {
        new CIBNActiveRequest(
                CIBNURLUtil.getCIBNDynamicTerminalActiveUrl(mac, areaId, type),
                tag, listener, false).performNetwork(BaseRequest.Method.GET);
    }

    public void loginCIBNTerminal(
            BaseRequest.Listener<CIBNXMLLoginData> listener, Object tag,
            String mac, String deviceId, boolean isUseCache,
            boolean isOnlyUseCache) {
        new CIBNLoginRequest(
                CIBNURLUtil.getCIBNStaticTerminalLoginUrl(mac, deviceId), tag,
                listener, true).performNetwork(BaseRequest.Method.GET,
                isUseCache, isOnlyUseCache);
    }

    public void loginCIBNTerminalDynamic(
            BaseRequest.Listener<CIBNXMLLoginData> listener, Object tag,
            String mac, String deviceId, boolean isUseCache,
            boolean isOnlyUseCache) {
        new CIBNLoginRequest(
                CIBNURLUtil.getCIBNDynamicTerminalLoginUrl(mac, deviceId), tag,
                listener, true).performNetwork(BaseRequest.Method.GET,
                isUseCache, isOnlyUseCache);
    }

    public void checkUpdate(BaseRequest.Listener<UpdateData> listener,
                            Object tag) {
        new JsonRequest(LocalURLUtil.getLocalUpdateUrl(), tag, listener, false,
                UpdateData.class).performNetwork(BaseRequest.Method.GET);
    }

    public void getVooleRecommendVideoList(int nopage, BaseRequest.Listener<VideoListData> listener, Object tag) {
        new VooleSearchRecommendRequest(
                VooleURLUtil.getRecommendVideoListUrl(nopage), tag,
                listener, false, VooleVideoListData.class, VideoListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }
}
