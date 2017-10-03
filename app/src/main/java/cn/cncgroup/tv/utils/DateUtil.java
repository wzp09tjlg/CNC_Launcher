package cn.cncgroup.tv.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.Html;
import android.widget.TextView;

/**
 * Created by ZG on 2015/5/6.
 */
public  class DateUtil {

    public static final String SPACE = "&nbsp;";         //空格
    private static final String TAG = "DateUtil";
    public DateUtil(){
    }

    public static void setDete(TextView mTextViewDate,TextView mTextViewTime,TextView weatherDate,TextView weatherTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.getDefault());     //时 分
        SimpleDateFormat date = new SimpleDateFormat("MM月dd日",Locale.getDefault());      //月 日
        String currentTime = time.format(new Date());
        String currentDate = date.format(new Date());
        mTextViewTime.setText(Html.fromHtml(currentTime));
        weatherTime.setText(Html.fromHtml(currentTime));
        mTextViewDate.setText(Html.fromHtml(currentDate + SPACE + getWeekDay(calendar)));
        Calendar cal = Calendar.getInstance();
        String chinaDate = CalendarUtil.getCurrentDay(cal);
        weatherDate.setText(Html.fromHtml(chinaDate));
//        Log.e(TAG,"计算的："+"月"+"日"+dateResult+"，时"+"分："+timeResult);
    }


    /**
     * 获取当前时间的年月日
     *
     * 输出格式为：2015-06-19
     */
    public static String setDete(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd ",Locale.getDefault());
       return  date.format(new Date());
    }


    /**
     * 得到周几
     * @param calendar
     * @return
     */
    public  static String getWeekDay(Calendar calendar){
        String weekStr = null;
        if(calendar == null){
            weekStr = "周一";
        }
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY :
                weekStr = "周一";
                break;
            case Calendar.TUESDAY :
                weekStr = "周二";
                break;
            case Calendar.WEDNESDAY :
                weekStr = "周三";
                break;
            case Calendar.THURSDAY :
                weekStr = "周四";
                break;
            case Calendar.FRIDAY :
                weekStr = "周五";
                break;
            case Calendar.SATURDAY :
                weekStr = "周六";
                break;
            case Calendar.SUNDAY :
                weekStr = "周日";
                break;

        }
        return  weekStr;
    }

    //由秒转换成 "时:分:秒" 形式的时间
    public static String Second2Hour(int totalSecond){
        if(totalSecond <= 0) return "00:00:00";

		int hour;
		int minute;
		int second;

        hour = totalSecond / 3600;
        totalSecond = totalSecond % 3600;
        minute = totalSecond / 60;
        totalSecond = totalSecond % 60;
        second = totalSecond;
        return (hour > 9 ? "" + hour : "0" + hour) + ":" + ( minute > 9 ? "" + minute : "0" + minute) + ":" + (second > 9 ? "" + second : "0"+ second);
    }

    //由秒转换成 "分:秒" 形式的时间
    public static String Second2Minute(int totalSecond){
        if(totalSecond <= 0) return "00:00";

		// int hour = 0;
		int minute;
		int second;

        minute = totalSecond / 60;
        totalSecond = totalSecond % 60;
        second = totalSecond;
        return  ( minute > 9 ? "" + minute : "0" + minute) + ":" + (second > 9 ? "" + second : "0"+ second);
    }

    /**
     * 播放时候调用显示当前时长额总时长
     * @param duration          总时长
     * @return
     */
    public static String setPlayTime(Long duration) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = duration.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

	public static boolean checkSameDate(long oldMinTime, long newMinTime)
	{ // 判断是不是同一天,参数是两个时间点的毫秒点
		final String pattern = "yyy-MM-dd"; // 首先定义时间格式
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String oldTimeStr = "" + oldMinTime;
		String newTimeStr = "" + newMinTime;
		try
		{
			Date oldDate = sdf.parse(sdf.format(Long.parseLong(oldTimeStr)));
			Date newDate = sdf.parse(sdf.format(Long.parseLong(newTimeStr)));
			return oldDate.getDate() == newDate.getDate();
		} catch (Exception e)
		{

		}
		return false;
	}

    /**
     * 20150909 转化为2015-09-09
     * @param timeStr
     * @return
     */
    public static String getYear(String timeStr){
        String Str = null;
        if(timeStr.length() == 8){
            String year = timeStr.substring(0, 4)+"-";
            String month = timeStr.substring(4, 6)+"-";
            String day = timeStr.substring(6, 8);
            Str = year + month + day;
        }else {
            Str = timeStr;
        }
        return Str;
    }
}
