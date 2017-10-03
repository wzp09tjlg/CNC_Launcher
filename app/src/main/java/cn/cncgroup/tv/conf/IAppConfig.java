package cn.cncgroup.tv.conf;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Properties;

import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.modle.RecommendData;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseRequest;

/**
 * Created by JIF on 2015/6/1.
 */
public interface IAppConfig {
    boolean initialize(Context context, Properties props);

    boolean hasSystemPermission();

    String getRevision();

    void openCategory(Context context, Object object);

    void openSubject(Context context);

    void openSubjectDetail(Context context, Object object);

    void openVideoDetail(Context context, VideoSet set);

    //	void playVideo(Context context, String videoSetId, String videoId,
//				   int startTime);
    void playVideo(Context context, String videoSetId, String videoId, String pos,
                   int startTime, String name, String imageUrl, String channelId);

    void openHistory(Context context);

    void openCollect(Context context);

    void openSearch(Context context);

    boolean isEnableDolby();

    boolean isEnableH265();

    int getDefaultDefinitonValue();

    /**
     * 推荐列表列表
     */
    void getRecommendData(BaseRequest.Listener<RecommendData> listener,
                          Object tag, boolean isUseCache, boolean isOnlyUseCache);

    /**
     * 分类列表
     */
    void getCategoryData(BaseRequest.Listener<CategoryData> listener,
                         Object tag, boolean isUseCache, boolean isOnlyUseCache);

    boolean isLauncher();

    void getVideoSetList(String url,
                         BaseRequest.Listener<VideoSetListData> listener, Object tag,
                         int pageSize);

    void getHotVideoSetList(String type, int pageSize, int pageNo,
                            BaseRequest.Listener<VideoSetListData> listener, Object tag);

    void getNewVideoSetList(String type, int pageSize, int pageNo,
                            BaseRequest.Listener<VideoSetListData> listener, Object tag);

    void getTagFilter(String type, BaseRequest.Listener<FilterListData> listener,
                      Object tag);

    void getVideoSetDetail(String url,
                           BaseRequest.Listener<VideoSetDetailData> listener, Object tag);

    void getRecommendVideoSetList(String url,
                                  BaseRequest.Listener<RecommendVideoSetListData> listener,
                                  Object tag);

    void getVideoList(String url, BaseRequest.Listener<VideoListData> listener,
                      Object tag);

    void getSearchResult(String keyword, int pageNo, int pageSize,
                         BaseRequest.Listener<SearchData> listener, Object tag);

    void getSubjectSetList(String url,
                           BaseRequest.Listener<SubjectSetData> listener, Object tag);

    void getSubjectList(String url, BaseRequest.Listener<SubjectData> listener,
                        Object tag);

    String getNextPageUrl(VideoSetListData data);

    String getHorizentalPosterUrl(VideoSet set);

    String getVerticalPosterUrl(VideoSet set);

    String getFilterResultUrl(String type, ArrayList<FilterList> filters);

    String getContentUrl(Category category);

    String getVideoSetSourceUrl(VideoSet set);

    String getVideoSetRecommendUrl(VideoSet set);

    String getVideoListUrl(VideoSet set);

    String getVarietyYear(Video video);

    String getPlayId(VideoSet set, Video video);

    String getPlayLength(VideoSet set);

    String getIssueTime(VideoSet set);

    String getManufacturerModel();

    Fragment getSearchRecommendFragment();

    void getSearchResult(String keyword, String mtype, int pageNo, int pageSize,
                         BaseRequest.Listener<SearchData> listener, Object tag);

    String getVideoPos(Video video, String pos);

    boolean isShouldPlay();

    void openSettingHome(Context context);
}
