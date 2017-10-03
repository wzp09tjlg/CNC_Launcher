package cn.cncgroup.tv.conf.model.domybox.utils;

/**
 * Created by zhang on 2015/6/2.
 */
public class DomyURLUtil {
    /**
     * 大麦域名
     */
    public static final String DOMAIN = "http://api.newapi.pthv.gitv.tv";
    /**
     * 大麦版本号
     */
    public static final String VERSION = "1.0.json";

    /**
     * 游戏
     */
    public static final String LAUCHER_GAME = "G1001";
    /**
     * 教育
     */
    public static final String LAUCHER_EDUCATION = "E1001";
    /**
     * 极清
     */
    public static final String LAUCHER_BD = "B1001";
    /**
     * 应用
     */
    public static final String LAUCHER_APP = "A1001";
    /**
     * 应用
     */
    public static final String LAUCHER_MOVIE = "M1001";
    /**
     * 首页
     */
    public static final String LAUCHER_HOME = "I1001";

    /**
     * 专辑列表接口
     */
    public static String getDomyVideoSetListUrl(String videoSetType, int pageSize,
                                                int pageNo) {
        return DOMAIN + "/api/open/video/VideoSetList/" + videoSetType + "/"
                + pageSize + "/" + pageNo + "/" + VERSION;
    }

    /**
     * 专辑筛选接口
     */
    public static String getDomyVideoSetListUrlByTag(String videoSetType, String videoTag, int pageSize, int pageNo) {
        return DOMAIN + "/api/open/video/VideoSetListByTag/" + videoSetType
                + "/" + videoTag + "/" + pageSize + "/" + pageNo + "/"
                + VERSION;

    }

    /**
     * 专辑详情接口(通过videoSetId获取)
     */
    public static String getDomyVideoSetDetailUrlByVideoSetId(String videoSetId) {
        return DOMAIN + "/api/open/video/VideoSetDetail/" + videoSetId + "/"
                + VERSION;
    }

    /**
     * 专辑详情接口(通过cpVideoSetId获取)
     */
    public static String getDomyVideoSetDetailUrlByCpVideoSetId(
            String cpVideoSetId) {
        return DOMAIN + "/api/open/video/getVideoSet/" + cpVideoSetId + "/"
                + VERSION;
    }

    /**
     * 剧集列表接口
     */
    public static String getDomyVideoListUrl(String videoSetId,
                                             String videoType, int pageNo, int pageSize) {
        return DOMAIN + "/api/open/video/VideoList/" + videoSetId + "/"
                + videoType + "/" + pageNo + "/" + pageSize + "/" + VERSION;
    }

    /**
     * 标签列表接口
     */
    public static String getDomyThirdClassListUrl(String videoSetType) {
        return DOMAIN + "/api/open/video/ThirdClassList/" + videoSetType + "/"
                + VERSION;
    }

    /**
     * 频道列表接口
     */
    public static String getDomyFirstClassListUrl(int classType) {
        return DOMAIN + "/api/open/video/FirstClassList/" + classType + "/"
                + VERSION;
    }

    public static String getRecommendVideoListUrl(String channelId, String id) {
        return DOMAIN + "/api/open/video/favor_list/" + channelId + "/" + id
                + "/" + VERSION;
    }

    /**
     * 获得焦点图列表接口
     */
    public static String getDomyFocusListUrl(String type) {
        return DOMAIN + "/api/open/focus/getfocusList/" + type + "-" + VERSION;
    }

    /**
     * 获得影院推荐接口
     */
    public static String getDomyVideoSetRecomListUrl() {
        return DOMAIN + "/api/open/home/getVideoSetRecomList/" + VERSION;
    }

    /**
     * 分页获取专题列表接口
     */
    public static String getDomySubjectListUrl(String type, int pageNo,
                                               int pageSize) {
        return DOMAIN + "/api/open/subject/getSubjectList/" + type + "---"
                + pageNo + "-" + pageSize + "-" + VERSION;
    }

    /**
     * 搜索接口 POST record_id:搜索结果id record_img:搜索结果图片 record_type:搜索结果类型
     * record_name:搜索结果名称
     */
    public static String getDomySearchUrl() {
        return DOMAIN + "/api/open/video/so.json";
    }

}
