package cn.cncgroup.tv.conf.model.cibn.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.widget.GlobalToast;
import cn.cncgroup.tv.utils.GlobalConstant;

/**
 * Created by zhangbo on 15-9-14.
 */
public class CIBNUtils {
    /**
     * 打开影视详情页
     */
    public static final String ACTION_DETAIL = "OPEN_DETAIL";
    /**
     * 打开栏目列表页
     */
    public static final String ACTION_PROGRAM_LIST = "OPEN_PROGRAM_LIST";
    /**
     * 打开搜索
     */
    public static final String ACTION_SEARCH = "OPEN_SEARCH";
    /**
     * 打开猜你喜欢
     */
    public static final String ACTION_YOUR_LIKE = "OPEN_YOUR_LIKE";
    /**
     * 打开排行榜
     */
    public static final String ACTION_TOP_PROGRAM = "OPEN_TOP_PROGRAM";
    /**
     * 打开热播追剧
     */
    public static final String ACTION_BINGE_WATCHING = "OPEN_BINGE_WATCHING";
    /**
     * 打开播放记录
     */
    public static final String ACTION_HISTORY = "OPEN_HISTORY";
    /**
     * 打开我的收藏
     */
    public static final String ACTION_FAVOR = "OPEN_FAVOR";
    /**
     * 打开轮播频道
     */
    public static final String ACTION_LIVEPLAYER = "OPEN_LIVEPLAYER";
    /**
     * 打开播放记录参数
     */
    public static final String PARAM_HISTORY = "{\"select\":\"history\"}";
    /**
     * 打开我的收藏参数
     */
    public static final String PARAM_FAVOR = "{\"select\":\"favorite\"}";
    /**
     * 打开栏目列表页参数
     */
	public static final String PARAM_PROGRAM_LIST = "{\"id\":\"00050861986679719946291\"}";
    /**
     * 打开轮播频道参数
     */
    public static final String PARAM_LIVEPLAYER = "{ \"groupid\": \"9\", \"channelId\": \"44\"}";
    public static final String PROGRAM_ID_FILM = "00050000000000000000000000019596";
    public static final String PROGRAM_ID_SERIAL = "00050000000000000000000000019614";
    public static final String PROGRAM_ID_CHILDREN = "000508620939077472006";
    public static final String PROGRAM_ID_INFOMATION = "00050000000000000000000000019649";
    public static final String PROGRAM_ID_VARIETY = "00050000000000000000000000019627";
    public static final String PROGRAM_ID_ANIMATION = "00050000000000000000000000019633";
    public static final String PROGRAM_ID_MICROFILM = "000508621130312249212";
    public static final String PROGRAM_ID_RECORD = "00050000000000000000000000019645";
    public static final String PROGRAM_ID_COLLECT = "00050861986679719946291";
    /**
     * 打开影视详情页参数
     */
    public static String PARAM_DETAIL = "{\"id\":\"CIBN_37d66582d70311e29748\"}";

    public static void openCIBNEPGPage(Context context, String action) {
        openCIBNEPGPage(context, action, "{}");
    }

    public static void openCIBNEPGPage(Context context, String action, String actionParam) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("cn.cibntv.ott", "cn.cibntv.ott.Bootloader"));
            intent.putExtra("action", action);
            intent.putExtra("actionParam", actionParam);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalToast.makeText(context,context.getString(R.string.app_invoke_error),
                    GlobalConstant.ToastShowTime).show();
        }
    }

}
