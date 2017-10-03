package cn.cncgroup.tv.utils;

import java.util.HashMap;

import cn.cncgroup.tv.R;

/**
 * Created by jinwenchao on 15/5/21. 这个类用来保存一些全局常量
 * <p/>
 * 比如文件路径，sharedpreferences文件的名字，使用次数比较多的key值，网络请求的一些参数等
 */
public class GlobalConstant {
    // 例如一个常量可以如下定义一个文件路径
    // public static final FILEPATH = "";
    // 一个网络接口请求的链接，写在这里更方便修改
    // public static final WEBHTTP = "http//:www.address.com/";

    // 默认的sharePreference的文件的名字；
    public static final String CNC_GROUP = "cncgroup";
    public static final String PACKAGENAME_TEAROOM = "com.tea.tv.room";
    public static final String QR_CODE_HTTP = "http://192.168.1.200:8080/feedback-qr.png";//二维码地址
    public static final int NET_NOT_CONNECT = 0x108;
    public static final int SERVER_CONNECT_ERROR = NET_NOT_CONNECT + 0x1;
    public static final int UPGRADE_INFO_ERROR = NET_NOT_CONNECT + 0x2;
    public static final int UPGRADE_PAUSED = NET_NOT_CONNECT + 0x3;
    public static final int DOWNLOADING_START = NET_NOT_CONNECT + 0x4;
    public static final int DOWNLOADING_COMPLETE = NET_NOT_CONNECT + 0x5;
    public static final int DOWNLOADING_STOP_BYUSER = NET_NOT_CONNECT + 0x6;
    public static final int VERTIFY_FAILED = NET_NOT_CONNECT + 0x7;
    public static final int UPATE_SPEED = NET_NOT_CONNECT + 0x8;
    public static final int NOMRE_MEMORY = NET_NOT_CONNECT + 0x9;
    public static final int UPGRADE_ERROR = NET_NOT_CONNECT + 0x10;
    public static final int REBOOT = NET_NOT_CONNECT + 0x11;
    public static final String DOWNLOADFILE = "cncupgrade";
    public static final Object LOCAL_VERSION = "ro.build.version.code";
    public static final int TIME_OUT = 3000;
    public static final String PACKAGE_NAME = "com.cnc.systemsetting";
    public static final String ENCODING = "GBK";
    public static final String MYAPPSEQUENCE = "MyAppSequence";
    public static final String APP_MYAPP = "app_myapp";
    public static final String APP_STORE = "app_store";
    public static final String APP_SETTING = "app_setting";
    public static final String APP_STICK = "app_stick";
    public static final String APP_RECOM = "app_recom";
    public static final String VIDEO = "Video";
    public static final String KEY_VIDEOSET = "VideoSet";
    // public static final String VIDEOSETID = "VideoSetId";
    public static final String ISDELETEITEM = "isDeleteItem";
    public static final String COLLEXTUPDATETIME = "collextupdatetime";
    public static final String VIDEO_ID = "videoID"; // 视频ID
    public static final String EQVID = "epvid"; // 清晰度ID
    public static final int DEFITION_2_360 = 2; // 高清
    public static final int DEFITION_4_720 = 4; // 720P
    public static final int DEFITION_5_1080 = 5; // 1080P
    public static final int DEFITION_10_4k = 10; // 4K
    public static final int DEFITION_14_dolby720P = 14; // 杜比720P
    public static final int DEFITION_15_dolby1080P = 15; // 杜比1080P
    public static final int DEFITION_16_dolby4K = 16; // 杜比4K
    public static final int DEFITION_17_h265_720P = 17; // H.265 720P
    public static final int DEFITION_18_h265_1080P = 18; // H.265 1080P
    public static final int DEFITION_19_h265_4K = 19; // H.265 4K
    public static final int DEFAULT_DEFITION = 4; // 默认分辨率
    public static final int VARIETYIMAGEHINT = 7; //综艺期数
    public static final String RECOMMEND_DETAIL = "detail"; // 推荐页，进入详情
    public static final String RECOMMEND_APP = "app"; // 推荐页，跳转APP
    public static final String RECOMMEND_PLAY = "play"; // 推荐页，直接播放
    public static final String VIDEO_URL = "video_url"; // 视频URL地址
    public static final int ZERO = 0;
    public static final int HOME_LEFT_AND_RIGHT_GAP = R.dimen.dimen_80dp;
    public static final String XJLFONT = "fzjlFont.fon"; // 静蕾字体库文件
    public static final String DS_DIGI = "DS-DIGI.TTF";  //数字时间字体库
    public static final String WEATHERURL = "http://route.showapi.com/9-2";// 天气预报根据城市获取数据的链接路径
    public static final String WEATHERURLIP = "http://route.showapi.com/9-4";// 天气预报根据IP,获取天气数据
    public static final String APPKEY = "3781";// 天气预报必须的appkey
    public static final String APPSECRIT = "dcc827c55f7046c0ab3daaea3365e9f7";// 天气预报请求接口需要的秘钥
    public static final String WEATHERBEAN = "weatherBean";
    public static final boolean ISHISTORY = true;
    public static final String WEBNETIP = "webnetip";
    public static final String WEATHERCITY = "weatherCity";
    public static final String WEATHERCITYCODE = "weatherCityCode";
    public static final String FILE_CITY_NAME = "city.xml";// 城市信息xml文件
    public static final String KEY_TYPE = "type";
    public static final String PROVINCEPOSITION = "provinceposition";
    public static final String CITYPOSITION = "cityposition";
    public static final String COUNTRYPOSITION = "countryposition";
    public static final boolean SupportBreakpoint = true;//是否支持断点续传，此处作为一个开关，应用中无触发按钮，默认为支持
    public static final String UPDATEAPPNAME = "cnc.apk";//更新时候该字段作为应用的文件名
    public static final String APPUSER = "CNC"; //支持多用户下载，该参数暂时不需要使用，与ID一致使用
    public static final String FOOTMARKSTRING = "footmarkstring";
    /**
     * 生活
     */
    public static final String DOMY_TYPE_LIFE = "21";
    /**
     * 时尚
     */
    public static final String DOMY_TYPE_FASHION = "13";
    /**
     * 汽车
     */
    public static final String DOMY_TYPE_CAR = "26";
    /**
     * 片花
     */
    public static final String DOMY_TYPE_SECTION = "10";
    /**
     * 体育
     */
    public static final String DOMY_TYPE_SPORT = "17";
    /**
     * 教育
     */
    public static final String DOMY_TYPE_EDUCATION = "12";
    /**
     * 搞笑
     */
    public static final String DOMY_TYPE_FUN = "22";
    /**
     * 纪录片
     */
    public static final String DOMY_TYPE_RECORD = "3";
    /**
     * 音乐
     */
    public static final String DOMY_TYPE_MUSIC = "5";
    /**
     * 娱乐
     */
    public static final String DOMY_TYPE_ENTERTAINMENT = "7";
    /**
     * 综艺
     */
    public static final String DOMY_TYPE_VARIETY = "6";
    /**
     * 电影
     */
    public static final String DOMY_TYPE_MOVIE = "1";
    /**
     * 少儿
     */
    public static final String DOMY_TYPE_CHILDREN = "15";
    /**
     * 动漫
     */
    public static final String DOMY_TYPE_ANIME = "4";
    /**
     * 电视剧
     */
    public static final String DOMY_TYPE_TV = "2";
    /**
     * 测试用
     */
    public static final int DOMY_TYPE_TEST = 57;
    /**
     * 极清
     */
    public static final int DOMY_TYPE_CLEAR = 2006;
    /**
     * 专题
     */
    public static final int DOMY_TYPE_SUBJECT = 2009;
    public static final HashMap<String, Integer> VIDEOLIST_TYPEFRAGMENT_MAP = new HashMap<String, Integer>()// 影视分类页////
            // 大于零竖图////
            // 小于零横图
    { // 1:一句话描述 2: 集数、期数 3:主演名称
        { // -1:一句话简介 -2:集数、期数 -3前两部名称
            put("专题", -3);
            put("综艺", -2);
            put("娱乐", -1);
            put("生活", -1);
            put("纪录片", 1);
            put("电影", 2);
            put("少儿", 2);
            put("动漫", 2);
            put("音乐", 3);
            put("电视剧", 4);
            put("3D体验", 1);
        }
    };
    public static final String VDEIO_CURRENT_PAGE = "%d/%d页"; // 影视页当前页数
    public static final int GRIDCOLUMN4 = 4; // 娱乐页显示的列数
    public static final int GRIDCOLUMN3 = 3; // 娱乐页显示的列数
    public static final int PER_REQUEST_NUM24 = 24; // 影视分类1页每次请求的个数
    public static final int PER_REQUEST_NUM18 = 18; // 影视分类2页每次请求的个数
    public static final int VDEIO_SERIES1 = 1;// 影视剧集显示更新至集数
    public static final int VDEIO_SERIES2 = 2;// 影视剧集显示更新至期数
    public static final String VDEIO_TYPE = "VDEIO_TYPE";
    public static final String VDEIO_TAG1 = "VEDIO_TAG";
    public static final String VDEIO_SCORE = "VDEIO_SCORE";
    public static final String VDEIO_EPISODE = "VDEIO_EPISODE";
    public static final String VDEIO_CODE = "VDEIO_CODE";
    public static final String VDEIO_PICVERTICAL = "VDEIO_PICVERTICAL";
    public static final String VDEIO_SELECT = "VDEIO_SELECT";
    public static final String CHANNEL_ID = "channelId";
    public final static float MENU_MOVE_PERCENT = 30f;
    public static final String VIDEO_TYPENAME = "VIDEO_TYPENAME";
    public final static String VIDEO_SHOWTYPE = "VIDEO_SHOWTYPE"; // 1一句话描述
    //自动关机
    public static final String AUTO_SHUTDOWN_HOUR = "AUTO_SHUTDOWN_HOUR";
    public static final String AUTO_SHUTDOWN_MINUTE = "AUTO_SHUTDOWN_MINUTE";
    public static final String AUTO_SHUTDOWN_SWITCH = "AUTO_SHUTDOWN_SWITCH";
    /**
     * **** Player ******
     */
    public static final int DEFINITION_SETTINGS_WEIGHT_HIGH = 2;
    public static final int DEFINITION_SETTINGS_WEIGHT_720P = 3;
    // 2显示名称 3剧集期数
    public static final int DEFINITION_SETTINGS_WEIGHT_1080P = 4;
    public static final int DEFINITION_SETTING_WEIGHT_4K = 5;
    public static final int REFRESH_DELAY_TIME = 30 * 60 * 1000;
    public static final String VIDEO_LIST = "VIDEO_LIST";
    public static final String VIDEO_SET = "VIDEO_SET";
    public static final String KEY_CATEGORY = "key_category";
    public static final String KEY_SUBJECT_SET = "key_subject_set";
    public static final String KEY_INDEX = "key_index";
    public static int ToastShowTime = 3; // 提示框显示时间
    public static String LASTSHOWTIM;
}
