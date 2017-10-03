package cn.cncgroup.tv.conf.model.domybox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.baidu.mobstat.StatService;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.cncplayer.PlayerContainerActivity;
import cn.cncgroup.tv.conf.AbsAppConfig;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyFavorListContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomySearchContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyThirdClassListContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoListContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoSetDetailContent;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoSetListContent;
import cn.cncgroup.tv.conf.model.domybox.request.DomyFavorListTransRequest;
import cn.cncgroup.tv.conf.model.domybox.request.DomySearchTransRequest;
import cn.cncgroup.tv.conf.model.domybox.request.DomyThirdClassTransRequest;
import cn.cncgroup.tv.conf.model.domybox.request.DomyVideoListTransRequest;
import cn.cncgroup.tv.conf.model.domybox.request.DomyVideoSetDetailTransRequest;
import cn.cncgroup.tv.conf.model.domybox.request.DomyVideoSetListTransRequest;
import cn.cncgroup.tv.conf.model.domybox.utils.DomyURLUtil;
import cn.cncgroup.tv.conf.model.domybox.utils.LocalDataUtils;
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
import cn.cncgroup.tv.network.request.JsonRequest;
import cn.cncgroup.tv.network.utils.LocalURLUtil;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.detail.VideoDetailActivity;
import cn.cncgroup.tv.view.footmark.FootmarkActivity;
import cn.cncgroup.tv.view.search.SearchActivity;
import cn.cncgroup.tv.view.search.SearchHotDomyboxFragment;
import cn.cncgroup.tv.view.video.VideoListActivity;

/**
 * Created by JIF on 2015/6/1.
 */
public class AppConfig extends AbsAppConfig {
    @Override
    public void openCategory(Context context, Object object) {
        if (object != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_CATEGORY,
                    (Category) object);
            Utils.startActivity(context, VideoListActivity.class, bundle);
            //因fragment可能会变动  故点击“全部”的统计放到这里
            //id也是不确定的，但是Name是确定的，故根据Name来区分“电影”、“电视剧”等
            if (((Category) object).getName().equals(context.getString(R.string.title_movie))) {
                StatService.onEvent(context, "Film_all", context.getString(R.string.film_all), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.title_teleplay))) {
                StatService.onEvent(context, "Teleplay_all", context.getString(R.string.teleplay_all), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.title_variety))) {
                StatService.onEvent(context, "Variety_all", context.getString(R.string.variety_all), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_children))) {
                StatService.onEvent(context, "Channel_kinderkanal", context.getString(R.string.channel_kinderkanal), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_anim))) {
                StatService.onEvent(context, "Channel_anime", context.getString(R.string.channel_anime), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_life))) {
                StatService.onEvent(context, "Channel_life", context.getString(R.string.channel_life), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_music))) {
                StatService.onEvent(context, "Channel_music", context.getString(R.string.channel_music), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_record))) {
                StatService.onEvent(context, "Channel_documentary", context.getString(R.string.channel_documentary), 1);
            } else if (((Category) object).getName().equals(context.getString(R.string.category_3d))) {
                StatService.onEvent(context, "Channel_3D", context.getString(R.string.channel_3D), 1);
            }
        }
    }

    @Override
    public void openSubject(Context context) {
        StatService.onEvent(context, "Channel_project", context.getString(R.string.channel_project), 1);
    }

    @Override
    public void openSubjectDetail(Context context, Object object) {

    }

    @Override
    public void openVideoDetail(Context context, VideoSet set) {
        if (set != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_VIDEOSET, set);
            Utils.startActivity(context, VideoDetailActivity.class, bundle);
        }
    }

    @Override
    public void playVideo(Context context, String videoSetId, String videoId,String position,
                          int startTime, String name, String image, String channelId) {
        //大麦调用播放次数  添加统计
        StatService.onEvent(context, "Playing_count", context.getString(R.string.playing_count), 1);
        Intent intent = new Intent(context, PlayerContainerActivity.class);
        intent.putExtra("id", videoSetId);
        intent.putExtra("serialNumber", position);
        intent.putExtra("playTime", startTime);
        context.startActivity(intent);
    }

    @Override
    public void openHistory(Context context) {
        context.startActivity(new Intent(context, FootmarkActivity.class));
    }

    @Override
    public void openCollect(Context context) {
        context.startActivity(new Intent(context, FootmarkActivity.class));
    }

    @Override
    public void openSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getRecommendData(BaseRequest.Listener<RecommendData> listener,
                                 Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        new JsonRequest(LocalURLUtil.getLocalDOMYRecommendUrl(), tag, listener,
                true, RecommendData.class).performNetwork(
                BaseRequest.Method.GET, isUseCache, isOnlyUseCache);
    }

    @Override
    public void getCategoryData(BaseRequest.Listener<CategoryData> listener,
                                Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        listener.onResponse(LocalDataUtils.getDomyboxCategory(), true);
    }

    @Override
    public void getVideoSetList(String url,
                                BaseRequest.Listener<VideoSetListData> listener, Object tag,
                                int pageSize) {
        new DomyVideoSetListTransRequest(url, tag, listener, false,
                DomyVideoSetListContent.class, VideoSetListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getHotVideoSetList(String type, int pageSize, int pageNo,
                                   BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        new DomyVideoSetListTransRequest(
                DomyURLUtil.getDomyVideoSetListUrl(type, pageSize, pageNo), tag,
                listener, true, DomyVideoSetListContent.class,
                VideoSetListData.class).performNetwork(BaseRequest.Method.GET,
                true, false);
    }

    @Override
    public void getNewVideoSetList(String type, int pageSize, int pageNo,
                                   BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        new DomyVideoSetListTransRequest(
                DomyURLUtil.getDomyVideoSetListUrl(type, pageSize, pageNo), tag,
                listener, true, DomyVideoSetListContent.class,
                VideoSetListData.class).performNetwork(BaseRequest.Method.GET,
                true, false);
    }

    @Override
    public void getTagFilter(String type, BaseRequest.Listener<FilterListData> listener, Object tag) {
        new DomyThirdClassTransRequest(
                DomyURLUtil.getDomyThirdClassListUrl(type), tag, listener, true,
                DomyThirdClassListContent.class, FilterListData.class)
                .performNetwork(BaseRequest.Method.GET, true, false);
    }

    @Override
    public void getVideoSetDetail(String url,
                                  BaseRequest.Listener<VideoSetDetailData> listener, Object tag) {
        new DomyVideoSetDetailTransRequest(url, tag, listener, false,
                DomyVideoSetDetailContent.class, VideoSetDetailData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getRecommendVideoSetList(String url,
                                         BaseRequest.Listener<RecommendVideoSetListData> listener,
                                         Object tag) {
        new DomyFavorListTransRequest(url, tag, listener, false,
                DomyFavorListContent.class, RecommendVideoSetListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getVideoList(String url,
                             BaseRequest.Listener<VideoListData> listener, Object tag) {
        new DomyVideoListTransRequest(url, tag, listener, false,
                DomyVideoListContent.class, VideoListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getSearchResult(String keyword, String mtype, int pageNo, int pageSize,
                                BaseRequest.Listener<SearchData> listener, Object tag) {
    }

    @Override
    public String getVideoPos(Video video, String pos) {
        return pos;
    }

    @Override
    public boolean isShouldPlay() {
        return true;
    }

    @Override
    public void getSearchResult(String keyword, int pageNo, int pageSize,
                                BaseRequest.Listener<SearchData> listener, Object tag) {
        RequestBody body = new FormEncodingBuilder().add("keyword", keyword)
                .add("cast_id", "0").add("select_model", "0")
                .add("pageNo", pageNo + "").add("pageSize", pageSize + "")
                .add("version", DomyURLUtil.VERSION).build();
        new DomySearchTransRequest(DomyURLUtil.getDomySearchUrl(), tag,
                listener, false, DomySearchContent.class, SearchData.class)
                .setRequestBody(body)
                .performNetwork(BaseRequest.Method.POST);
    }

    @Override
    public void getSubjectSetList(String url,
                                  BaseRequest.Listener<SubjectSetData> listener, Object tag) {

    }

    @Override
    public void getSubjectList(String url,
                               BaseRequest.Listener<SubjectData> listener, Object tag) {

    }

    @Override
    public String getNextPageUrl(VideoSetListData data) {
        String currentUrl = data.getCurrentUrl();
        int currentPageNo = data.getResult().getPageNo();
        String flag = "/" + data.getResult().getPageSize() + "/";
        int index = currentUrl.lastIndexOf(flag);
        return currentUrl.substring(0, index + flag.length())
                + (++currentPageNo) + "/" + DomyURLUtil.VERSION;
    }

    @Override
    public String getHorizentalPosterUrl(VideoSet set) {
        if (TextUtils.isEmpty(set.getImage())) {
            return "";
        }
        int pointIndex = set.getImage().lastIndexOf(".");
        return set.getImage().substring(0, pointIndex) + "_320_180"
                + set.getImage().substring(pointIndex);
    }

    @Override
    public String getVerticalPosterUrl(VideoSet set) {
        if (TextUtils.isEmpty(set.getImage())) {
            return "";
        }
        int pointIndex = set.getImage().lastIndexOf(".");
        return set.getImage().substring(0, pointIndex) + "_260_360"
                + set.getImage().substring(pointIndex);
    }

    @Override
    public String getFilterResultUrl(String type, ArrayList<FilterList> filters) {
        String tags = "";
        for (int i = 0; i < filters.size(); i++) {
            String temp;
            if (i == 0) {
                temp = "" + filters.get(i).getId();
            } else {
                temp = "," + filters.get(i).getId();
            }
            tags += temp;
        }
        return DomyURLUtil.getDomyVideoSetListUrlByTag(type, tags, 24, 1);
    }

    @Override
    public String getContentUrl(Category category) {
        return category.contentUrl;
    }

    @Override
    public String getVideoSetSourceUrl(VideoSet set) {
        return DomyURLUtil.getDomyVideoSetDetailUrlByVideoSetId(set.id);
    }

    @Override
    public String getVideoSetRecommendUrl(VideoSet set) {
        return DomyURLUtil.getRecommendVideoListUrl(set.getChannelId(),
                set.getId());
    }

    @Override
    public String getVideoListUrl(VideoSet set) {
        return DomyURLUtil.getDomyVideoListUrl(set.getId(), set.getChannelId(),
                1, set.getTotal());
    }

    @Override
    public String getVarietyYear(Video video) {
        return video.year;
    }

    @Override
    public String getPlayId(VideoSet set, Video video) {
        return set.getId();
    }

    @Override
    public String getPlayLength(VideoSet set) {
        return set.getLength() / 60 + "";
    }

    @Override
    public String getIssueTime(VideoSet set) {
        return set.getTime();
    }

    @Override
    public Fragment getSearchRecommendFragment() {
        return SearchHotDomyboxFragment.getInstance();
    }

}
