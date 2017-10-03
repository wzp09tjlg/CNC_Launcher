package cn.cncgroup.tv.conf.model.voole;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.baidu.mobstat.StatService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.conf.AbsAppConfig;
import cn.cncgroup.tv.conf.model.voole.modle.VooleCategoryData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleDetailData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleFilmData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleFilmDataForVideoList;
import cn.cncgroup.tv.conf.model.voole.modle.VooleFilterData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleRecommendVideoSetListData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleSearchResult;
import cn.cncgroup.tv.conf.model.voole.modle.VooleSubjectData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleSubjectSetData;
import cn.cncgroup.tv.conf.model.voole.modle.VooleVideoListData;
import cn.cncgroup.tv.conf.model.voole.request.VooleCategoryTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleDetailVideoListTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleDetailVideoSetTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleFilmDataForVideoListTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleFilmDataTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleFilterTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleRecommendVideoSetListTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleSearchTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleSubjectDataTransRequest;
import cn.cncgroup.tv.conf.model.voole.request.VooleSubjectSetDataTransRequest;
import cn.cncgroup.tv.conf.model.voole.utils.VooleURLUtil;
import cn.cncgroup.tv.db.DbUtil;
import cn.cncgroup.tv.db.VideoSetDao;
import cn.cncgroup.tv.modle.Category;
import cn.cncgroup.tv.modle.CategoryData;
import cn.cncgroup.tv.modle.FilterList;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.modle.RecommendData;
import cn.cncgroup.tv.modle.RecommendVideoSetListData;
import cn.cncgroup.tv.modle.SearchData;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.modle.SubjectSet;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.modle.Video;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.modle.VideoSet;
import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.JsonRequest;
import cn.cncgroup.tv.network.utils.LocalURLUtil;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Utils;
import cn.cncgroup.tv.view.detail.VideoDetailActivity;
import cn.cncgroup.tv.view.footmark.FootmarkActivity;
import cn.cncgroup.tv.view.homesecond.utils.CategoryUtils;
import cn.cncgroup.tv.view.search.SearchActivity;
import cn.cncgroup.tv.view.search.SearchHotVooleFragment;
import cn.cncgroup.tv.view.subject.SubjectActivity;
import cn.cncgroup.tv.view.subject.SubjectListActivity;
import cn.cncgroup.tv.view.video.VideoListActivity;

/**
 * Created by zhang on 2015/9/10.
 */
public class AppConfig extends AbsAppConfig {
    @Override
    public void openCategory(Context context, Object object) {
        if (object != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_CATEGORY,
                    (Category) object);
            Utils.startActivity(context, VideoListActivity.class, bundle);
        } else {
            GlobalToast.makeText(context, "未找到分类信息", Toast.LENGTH_SHORT).show();
        }

        //因fragment可能会变动  故点击“全部”和“频道”的统计放到这里
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

    @Override
    public void openSubject(Context context) {
        context.startActivity(new Intent(context, SubjectListActivity.class));
        StatService.onEvent(context, "Channel_project", context.getString(R.string.channel_project), 1);
    }

    @Override
    public void openSubjectDetail(Context context, Object object) {
        if (object instanceof SubjectSet) {
            SubjectSet set = (SubjectSet) object;
            Intent intent = new Intent(context, SubjectActivity.class);
            intent.putExtra(GlobalConstant.KEY_SUBJECT_SET, set);
            context.startActivity(intent);
        }
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
    public void playVideo(Context context, String videoSetId, String videoId, String videoPos,
                          int startTime, String name, String image, String channelId) {
        VideoSetDao video = new VideoSetDao();
        //优朋调用播放次数  添加统计
        StatService.onEvent(context, "Playing_count", context.getString(R.string.playing_count), 1);
        //优朋统计播放时长 开始统计
        StatService.onEventStart(context, "PlayTime", context.getString(R.string.start_player));
        try {
            List<VideoSetDao> foundVideo = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetId).execute();
            Log.i("playVideoVideoSetId:", "" + videoSetId);
            Log.i("playVideoVideoId:", "" + videoSetId);
            if (foundVideo.size() > 0) {
                video.collect = foundVideo.get(0).collect;
                foundVideo.get(0).delete();
            }
            video.name = name;
            video.image = image;
            video.footmark = 1;
            video.videoId = videoId;
            video.videoSetId = videoSetId;
            video.channelId = channelId;
            try {
                video.count = Integer.valueOf(videoPos);
            } catch (Exception e) {
                video.count = 0;
            }
            video.startTime = startTime;
            video.saveTime = System.currentTimeMillis();
            video.save();
            Intent intent = new Intent();
            intent.setAction("com.voole.epg.play.one");
            intent.putExtra("mid", videoId);
            intent.putExtra("sid", videoPos);
            intent.putExtra("playTime", startTime);
            context.startActivity(intent);
        } catch (Exception e) {
            GlobalToast.makeText(context, context.getResources().getString(R.string.app_invoke_error), GlobalConstant.ToastShowTime).show();
//            if (video.collect == 1) {
//                video.footmark = 0;
//                video.save();
//            } else {
//                DbUtil.deleteById(video);
//            }

        }
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
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public void getRecommendData(BaseRequest.Listener<RecommendData> listener, Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        new JsonRequest(LocalURLUtil.getLocalDOMYRecommendUrl(), tag, listener,
                true, RecommendData.class).performNetwork(
                BaseRequest.Method.GET, isUseCache, isOnlyUseCache);
    }

    @Override
    public void getCategoryData(BaseRequest.Listener<CategoryData> listener, Object tag, boolean isUseCache, boolean isOnlyUseCache) {
        new VooleCategoryTransRequest(VooleURLUtil.getCategoryUrl(), tag,
                listener, true, VooleCategoryData.class, CategoryData.class)
                .performNetwork(BaseRequest.Method.GET, isUseCache,
                        isOnlyUseCache);
    }

    @Override
    public void getVideoSetList(String url, BaseRequest.Listener<VideoSetListData> listener, Object tag, int pageSize) {
        new VooleFilmDataForVideoListTransRequest(url, tag, listener, false, VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize).performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getHotVideoSetList(String type, int pageSize, int pageNo,
                                   BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        Category variety = CategoryUtils.getCategoryByName("综艺");
        Category d3 = CategoryUtils.getCategoryByName("3D体验");
        Category life = CategoryUtils.getCategoryByName("生活");
        if (type.equals(variety.getId())) {
            String url = variety.getData().get(1).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else if (type.equals(d3.getId())) {
            String url = d3.getData().get(1).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else if (type.equals(life.getId())) {
            String url = life.getData().get(1).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else {
            new VooleFilmDataTransRequest(VooleURLUtil.getHotVideoSetListUrl(type, pageSize, pageNo),
                    tag, listener, true, VooleFilmData.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        }
    }

    @Override
    public void getNewVideoSetList(String type, int pageSize, int pageNo, BaseRequest.Listener<VideoSetListData> listener, Object tag) {
        Category variety = CategoryUtils.getCategoryByName("综艺");
        Category d3 = CategoryUtils.getCategoryByName("3D体验");
        Category life = CategoryUtils.getCategoryByName("生活");
        if (type.equals(variety.getId())) {
            String url = variety.getData().get(0).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else if (type.equals(d3.getId())) {
            String url = d3.getData().get(0).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else if (type.equals(life.getId())) {
            String url = life.getData().get(0).getContentUrl();
            url = url.replace("&pagesize=24", "&pagesize=14");
            new VooleFilmDataForVideoListTransRequest(url, tag, listener, true,
                    VooleFilmDataForVideoList.class, VideoSetListData.class, pageSize)
                    .performNetwork(BaseRequest.Method.GET, true, false);
        } else {
            new VooleFilmDataTransRequest(VooleURLUtil.getNewVideoSetListUrl(type, pageSize, pageNo), tag, listener, true, VooleFilmData.class, VideoSetListData.class, pageSize).
                    performNetwork(BaseRequest.Method.GET, false, false);
        }
    }

    @Override
    public void getTagFilter(String type, BaseRequest.Listener<FilterListData> listener, Object tag) {
        new VooleFilterTransRequest(VooleURLUtil.getTagFilter(type), tag,
                listener, true, VooleFilterData.class, FilterListData.class)
                .performNetwork(BaseRequest.Method.GET, true, false);
    }

    @Override
    public void getVideoSetDetail(String url,
                                  BaseRequest.Listener<VideoSetDetailData> listener, Object tag) {
        new VooleDetailVideoSetTransRequest(url, tag, listener, true,
                VooleDetailData.class, VideoSetDetailData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getRecommendVideoSetList(String url,
                                         BaseRequest.Listener<RecommendVideoSetListData> listener,
                                         Object tag) {
        new VooleRecommendVideoSetListTransRequest(url, tag, listener, false,
                VooleRecommendVideoSetListData.class,
                RecommendVideoSetListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getVideoList(String url,
                             BaseRequest.Listener<VideoListData> listener, Object tag) {
        new VooleDetailVideoListTransRequest(url, tag, listener, false,
                VooleVideoListData.class, VideoListData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getSearchResult(String keyword, int pageNo, int pageSize,
                                BaseRequest.Listener<SearchData> listener, Object tag) {
        new VooleSearchTransRequest(
                VooleURLUtil.getSearchUrl(keyword, pageSize, pageNo), tag,
                listener, false, VooleSearchResult.class, SearchData.class)
                .performNetwork(BaseRequest.Method.GET);
    }

    public void getSearchResult(String keyword, String mtype, int pageNo, int pageSize,
                                BaseRequest.Listener<SearchData> listener, Object tag) {
        new VooleSearchTransRequest(VooleURLUtil.getSearchUrl(keyword, mtype, pageSize, pageNo), tag, listener, false, VooleSearchResult.class, SearchData.class).performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public String getVideoPos(Video video, String pos) {
        if (video.getChannelId().equals(CategoryUtils.getCategoryByName("综艺").getId())) {
            return "1";
        }
        return pos;
    }

    @Override
    public boolean isShouldPlay() {
        return false;
    }

    @Override
    public void getSubjectSetList(String url, BaseRequest.Listener<SubjectSetData> listener, Object tag) {
        new VooleSubjectSetDataTransRequest(url, tag, listener, false, VooleSubjectSetData.class, SubjectSetData.class).performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public void getSubjectList(String url, BaseRequest.Listener<SubjectData> listener, Object tag) {
        new VooleSubjectDataTransRequest(url, tag, listener, false, VooleSubjectData.class, SubjectData.class).performNetwork(BaseRequest.Method.GET);
    }

    @Override
    public String getNextPageUrl(VideoSetListData data) {
        int pageNo = data.getResult().getPageNo() + 1;
        return data.getCurrentUrl().replaceAll("&page=\\d*", "&page=" + pageNo);
    }

    @Override
    public String getHorizentalPosterUrl(VideoSet set) {
        return set.getImage();
    }

    @Override
    public String getVerticalPosterUrl(VideoSet set) {
        return set.getImage();
    }

    @Override
    public String getFilterResultUrl(String type, ArrayList<FilterList> filters) {
        StringBuilder builder = new StringBuilder();
        for (FilterList filter : filters) {
            if (!filter.getName().equals("全部")) {
                if (filter.getUpName().equals("剧情")) {
                    builder.append("&style=" + Base64.encodeToString(filter.getName().getBytes(), Base64.DEFAULT).trim());
                } else if (filter.getUpName().equals("地区")) {
                    builder.append("&zone=" + Base64.encodeToString(filter.getName().getBytes(), Base64.DEFAULT).trim());
                } else if (filter.getUpName().equals("时间")) {
                    builder.append("&time=" + Base64.encodeToString(filter.getName().getBytes(), Base64.DEFAULT).trim());
                }
            }
        }
        return VooleURLUtil.getFilterResultUrl(type, 24, 1, builder.toString());
    }

    @Override
    public String getContentUrl(Category category) {
        return category.contentUrl + "&pagesize=" + 24 + "&page=" + 1;
    }

    @Override
    public String getVideoSetSourceUrl(VideoSet set) {
        if (!TextUtils.isEmpty(set.sourceUrl)) {
            String s = "";
            try {
                s = URLEncoder.encode("|", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return set.sourceUrl.trim().replaceAll("\\|", s);
        } else {
            return VooleURLUtil.getVideoSetDetailUrl(set.getId());
        }
    }

    @Override
    public String getVideoSetRecommendUrl(VideoSet set) {
        return VooleURLUtil.getRecommendVideoSetListUrl(set.getId());
    }

    @Override
    public String getVideoListUrl(VideoSet set) {
        return set.videoListUrl;
    }

    @Override
    public String getVarietyYear(Video video) {
        int start = video.getDesc().indexOf("-");
        int end = video.getDesc().lastIndexOf("-");
        try {
            return video.getDesc().substring(start + 1, end);
        } catch (Exception e) {
            return video.getShowTime();
        }
    }

    @Override
    public String getPlayId(VideoSet set, Video video) {
        return video.getId();
    }

    @Override
    public String getPlayLength(VideoSet set) {
        return set.getLength() + "";
    }

    @Override
    public String getIssueTime(VideoSet set) {
        if (set.channelId.equals("7")) {
            return set.getShowTime();
        }
        return set.getTime();
    }

    @Override
    public Fragment getSearchRecommendFragment() {
        return new SearchHotVooleFragment();
    }
}
