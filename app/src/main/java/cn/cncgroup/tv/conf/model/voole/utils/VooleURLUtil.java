package cn.cncgroup.tv.conf.model.voole.utils;

/**
 * Created by zhang on 2015/9/17.
 */
public class VooleURLUtil {
    public static final String DOMAIN_FILMLIST = "http://filmlist.voole.com";
    public static final String DOMAIN_RECOMMEND = "http://recommend.voole.com";
    public static final String DOMAIN_SEARCH = "http://search.voole.com";
    public static final String CATEGORY_FILMLIST = "/filmlist?";
    public static final String CATEGORY_MOVIEINFO = "/movieInfo?";
    public static final String CATEGORY_RECOMMEND = "/recommend?";
    /**
     * 必填。服务提供商的PORTAL ID，根据此值，可以输出不同的片单。 由优朋普乐统一管理和配发，默认100106。全局有效
     */
    public static final String EPGID = "&epgid=100895";
    /**
     * 必填。运营商ID, 优朋普乐分配给各合作伙伴的ID号，默认10002001，全局有效
     */
    public static final String SPID = "&spid=20120629";
    /**
     * 必填。[0|1|2]，详情页的展现方式。0:最终页中只包括影片对应的介质详情信息 1:最终页包括影片的基本信息和影片介质信息
     * 2:最终页包括影片的基本信息和影片介质信息及产品信息（apk2.1\2.2用）。
     */
    public static final String ISDETAIL = "&isdetail=1";
    /**
     * 选填。专题用,v=2.0专题显示
     */
    public static final String VERSION = "&v=2.0";
    /**
     * 选填。默认是xml。xml/json,生成数据格式,此选项仅针对apk2.4及以上版本，低版本不兼容
     */
    public static final String RESPONSEFORMAT_JSON = "&responseformat=json";
    public static final String FORMAT_JSON = "&format=2";
    /**
     * 选填。内容提供商编号，10001000为搜狐编号
     */
    public static final String CPID = "&cpid=10001000";
    /**
     * 选填。终端厂商某一类终端的编号，优朋普乐分配.默认空，全局有效
     */
    public static final String OEMID = "&oemid=817";
    /**
     * 选填。用户ID，平台自动分配，请求时，如果能获得，需要传递。默认空，全局有效
     */
    public static final String UID = "&uid=4579745";
    /**
     * 选填。终端硬件ID或终端身份标识号，当前为MAC地址或是预先固化到终端的唯一编号.默认空，全局有效.
     */
    public static final String HID = "&hid=00ce39bedcf90000000000000000000000000000";
    /**
     * 选填。是否需要3D频道，0不需要，1需要。不需要：指所有正常非3D频道 需要指：所有正常的频道
     */
    public static final String IS3D = "&is3d=1";
    /**
     * 选填。[0|1]。排序控制[0:按当前排序显示；1:按“最新”排序]。输出结果按顺序排列
     */
    public static final String ORDER = "&order=0";
    /**
     * 选填。版本标示符。透传。
     */
    public static final String APPVERSION = "&app_version=1.0";
    /**
     * 请求类型,一次显示所有栏目信息
     */
    public static final String CTYPE_ALL = "&ctype=1";
    /**
     * 优朋OTV首页推荐位-列表
     */
    public static final String CTYPE_RECDOMMEND = "&ctype=3";
    /**
     * 请求类型,显示子栏目信息
     */
    public static final String CTYPE_SUB = "&ctype=2";

    public static final String CTYPE_FILTER = "&ctype=102";

    /**
     * 影视搜索
     */
    public static final int CTYPE_VIDEO = 5;

    public static final int MTYPE_MOVIE = 0;
    public static final int MTYPE_TV = 1;
    public static final int MTYPE_CHILD = 8;
    public static final int MTYPE_ANIM = 6;
    public static final int MTYPE_VARIETY = 7;
    public static final int MTYPE_LIFE = 9;
    public static final int MTYPE_RECORD = 10;
    public static final int MTYPE_MUSIC = 15;
    public static final int MTYPE_3D = 5;

    public static final int PINYIN_ALL = 0;
    public static final int PINYIN_SAMPLE = 1;

    public static final String KEYWORD_HOT = "&keyword=hot";

    public static final String KEYTYPE_MOVIE = "&keytype=movie";

    public static final String COLUMN_HOT = "&column=12398";
    public static final String COLUMN_RECOMMEND = "&column=cate__ypOTVsytjwlb_1393847192%7C1409726775";
    private static final String NOPAGE = "&nopage=";//优朋OTV首页推荐位-列表的参数
    public static final String CTYPE_HOT = "&ctype=104";

    public static final String MTYPE = "&mtype=";
    public static final String SORT_NEW = "&sort=2";
    public static final String SORT_HOT = "&sort=1";
    public static final String KEYTYPE_SORT = "&typekey=5";
    public static final String CTYPE_SORT = "&ctype=103";

    public static String getCategoryUrl() {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + EPGID + SPID + IS3D
                + CTYPE_ALL + RESPONSEFORMAT_JSON;
    }

    public static String getTagFilter(String type) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + IS3D
                + RESPONSEFORMAT_JSON + CTYPE_FILTER + "&mtype=" + type;
    }

    public static String getFilterResultUrl(String type, int pageSize, int pageNo, String add) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + IS3D
                + RESPONSEFORMAT_JSON + CTYPE_SORT + SORT_HOT + KEYTYPE_SORT
                + "&pagesize=" + pageSize + "&page=" + pageNo + "&mtype=" + type
                + add;
    }

    public static String getHotVideoSetListUrl(String mType, int pageSize, int pageNo) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + IS3D
                + RESPONSEFORMAT_JSON + KEYTYPE_SORT + CTYPE_SORT + "&mtype="
                + mType + "&pagesize=" + pageSize + "&page=" + pageNo
                + SORT_HOT;
    }

    public static String getRecommendVideoListUrl(int nopage) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + CTYPE_RECDOMMEND + VERSION + COLUMN_RECOMMEND + NOPAGE + nopage + RESPONSEFORMAT_JSON;
    }

    public static String getNewVideoSetListUrl(String mType, int pageSize, int pageNo) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + IS3D
                + RESPONSEFORMAT_JSON + KEYTYPE_SORT + CTYPE_SORT + "&mtype="
                + mType + "&pagesize=" + pageSize + "&page=" + pageNo
                + SORT_NEW;
    }

    public static String getRecommendVideoSetListUrl(String id) {
        return DOMAIN_RECOMMEND + CATEGORY_RECOMMEND + FORMAT_JSON + EPGID
                + IS3D + "&mid=" + id + "&limit=" + 10 + "&type=" + 1;
    }

    public static String getVideoSetDetailUrl(String id) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + SPID + EPGID + IS3D
                + RESPONSEFORMAT_JSON + "&ctype=4" + "&filmmid=" + id;
    }

    public static final String SEARCHTYPE = "&searchtype=2";
    public static final String ISPID = "&ispid=20120629";

    public static String getSearchUrl(String keyword, int pageSize, int pageNo) {
        return DOMAIN_SEARCH + "?" + ISPID + EPGID + FORMAT_JSON + SEARCHTYPE
                + IS3D + "&keyword=" + keyword + "&pg=" + pageNo + "&limit="
                + pageSize;
    }

    /**
     * @param mtype:分类，电影、电视剧等
     */
    public static String getSearchUrl(String keyword, String mtype, int pageSize, int pageNo) {
        return DOMAIN_SEARCH + "?" + ISPID + EPGID + FORMAT_JSON + SEARCHTYPE
                + IS3D + MTYPE + mtype + "&keyword=" + keyword + "&pg=" + pageNo + "&limit="
                + pageSize;
    }

    public static final String COLUMN_SUBJECT = "&column=13844";
    public static final String CTYPE_SUBJECT = "&ctype=2";

    public static String getSubjectSetListUrl(int pageSize, int pageNo) {
        return DOMAIN_FILMLIST + CATEGORY_FILMLIST + EPGID + SPID + IS3D
                + CTYPE_SUBJECT + COLUMN_SUBJECT + VERSION + RESPONSEFORMAT_JSON
                + "&pagesize=" + pageSize + "&page=" + pageNo;
    }
}
