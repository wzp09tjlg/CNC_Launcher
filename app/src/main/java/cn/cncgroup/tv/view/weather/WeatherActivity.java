package cn.cncgroup.tv.view.weather;

import android.net.Uri;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.AppOpenHistoryAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.widget.gridview.HorizontalGridView;
import cn.cncgroup.tv.utils.CalendarUtil;
import cn.cncgroup.tv.utils.DateUtil;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.view.weather.Bean.DayWeather;
import cn.cncgroup.tv.view.weather.Bean.WeatherBean;

public class WeatherActivity extends BaseActivity {
    private SimpleDraweeView todayWeatherImage;
    private TextView mTemper1;
    private TextView nowCityTextview;
    private TextView todayWindTextview;
    private TextView todayChinaDateTextview;
    private TextView todayDateTextview;
    private TextView todayWeatherTextview;


    private TextView secondWeekdayTextview;
    private SimpleDraweeView secondWeatherImageview;
    private TextView secondWindTextview;
    private TextView secondWeatherTextview;
    private TextView secondTemper;

    private TextView thirdWeekdayTextview;
    private SimpleDraweeView thirdWeatherImageview;
    private TextView thirdWindTextview;
    private TextView thirdWeatherTextview;
    private TextView thirdTemper;

    private TextView fourthWeekdayTextview;
    private SimpleDraweeView fourthWeatherImageview;
    private TextView fourthWindTextview;
    private TextView fourthWeatherTextview;
    private TextView fourthTemper;
    private HorizontalGridView hgridview_type_title;
    private AppOpenHistoryAdapter adapter;
    private String[] weekdayArr = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private TextView tv_app_open_empty;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_weather);
    }

    @Override
    protected void findView() {
        todayChinaDateTextview = (TextView) findViewById(R.id.china_time);
        todayDateTextview = (TextView) findViewById(R.id.tv_time);
        nowCityTextview = (TextView) findViewById(R.id.city);
        todayWeatherImage = (SimpleDraweeView) findViewById(R.id.image1);
        todayWindTextview = (TextView) findViewById(R.id.wind1);
        todayWeatherTextview = (TextView) findViewById(R.id.weather1);
        mTemper1 = (TextView) findViewById(R.id.temp1);

        secondWeekdayTextview = (TextView) findViewById(R.id.weekday2);
        secondWeatherImageview = (SimpleDraweeView) findViewById(R.id.image2);
        secondWindTextview = (TextView) findViewById(R.id.wind2);
        secondWeatherTextview = (TextView) findViewById(R.id.weather2);
        secondTemper = (TextView) findViewById(R.id.temp2);

        thirdWeekdayTextview = (TextView) findViewById(R.id.weekday3);
        thirdWeatherImageview = (SimpleDraweeView) findViewById(R.id.image3);
        thirdWindTextview = (TextView) findViewById(R.id.wind3);
        thirdWeatherTextview = (TextView) findViewById(R.id.weather3);
        thirdTemper = (TextView) findViewById(R.id.temp3);

        fourthWeekdayTextview = (TextView) findViewById(R.id.weekday4);
        fourthWeatherImageview = (SimpleDraweeView) findViewById(R.id.image4);
        fourthWindTextview = (TextView) findViewById(R.id.wind4);
        fourthWeatherTextview = (TextView) findViewById(R.id.weather4);
        fourthTemper = (TextView) findViewById(R.id.temp4);

        hgridview_type_title = (HorizontalGridView) findViewById(R.id.hgridview_type_title);
        tv_app_open_empty = (TextView) findViewById(R.id.tv_app_open_empty);
    }

    @Override
    protected void initView() {
        WeatherBean weatherBean = (WeatherBean) getIntent().getExtras().getSerializable(GlobalConstant.WEATHERBEAN);
        DayWeather todayWeather = weatherBean.getShowapi_res_body().getF1();
        todayWeatherImage.setImageURI(Uri.parse(todayWeather.getDay_weather_pic()));
        mTemper1.setText(getString(R.string.temp_range, todayWeather.getNight_air_temperature(), todayWeather.getDay_air_temperature()));
        int airNumber = weatherBean.getShowapi_res_body().getNow().getAqi();
        todayWindTextview.setText(airQuailty(airNumber) + ":" + airNumber);
        todayWeatherTextview.setText(todayWeather.getDay_weather());
        todayDateTextview.setText(DateUtil.setDete() + weekdayArr[todayWeather.getWeekday() - 1]);
        todayChinaDateTextview.setText(Html.fromHtml(CalendarUtil.getCurrentDay(Calendar.getInstance())));
        nowCityTextview.setText(weatherBean.getShowapi_res_body().getCityInfo().getC3());

        DayWeather secdayWeather = weatherBean.getShowapi_res_body().getF2();
        secondWeatherImageview.setImageURI(Uri.parse(secdayWeather.getDay_weather_pic()));
        secondTemper.setText(getString(R.string.temp_range, secdayWeather.getNight_air_temperature(), secdayWeather.getDay_air_temperature()));
        secondWindTextview.setText(secdayWeather.getDay_wind_power());
        secondWeatherTextview.setText(secdayWeather.getDay_weather());
        secondWeekdayTextview.setText(weekdayArr[secdayWeather.getWeekday() - 1]);

        DayWeather thirddayWeather = weatherBean.getShowapi_res_body().getF3();
        thirdWeatherImageview.setImageURI(Uri.parse(thirddayWeather.getDay_weather_pic()));
        thirdTemper.setText(getString(R.string.temp_range, thirddayWeather.getNight_air_temperature(), thirddayWeather.getDay_air_temperature()));
        thirdWindTextview.setText(thirddayWeather.getDay_wind_power());
        thirdWeatherTextview.setText(thirddayWeather.getDay_weather());
        thirdWeekdayTextview.setText(weekdayArr[thirddayWeather.getWeekday() - 1]);

        DayWeather fourthdayWeather = weatherBean.getShowapi_res_body().getF4();
        fourthWeatherImageview.setImageURI(Uri.parse(fourthdayWeather.getDay_weather_pic()));
        fourthTemper.setText(getString(R.string.temp_range, fourthdayWeather.getNight_air_temperature(), fourthdayWeather.getDay_air_temperature()));
        fourthWindTextview.setText(fourthdayWeather.getDay_wind_power());
        fourthWeatherTextview.setText(fourthdayWeather.getDay_weather());
        fourthWeekdayTextview.setText(weekdayArr[fourthdayWeather.getWeekday() - 1]);
    }

    public String airQuailty(int airNumber) {
        if (airNumber >= 0 && airNumber <= 50) {
            return getResources().getString(R.string.air_quailty_one);
        } else if (airNumber >= 51 && airNumber <= 100) {
            return getResources().getString(R.string.air_quailty_two);
        } else if (airNumber >= 101 && airNumber <= 150) {
            return getResources().getString(R.string.air_quailty_three);
        } else if (airNumber >= 151 && airNumber <= 200) {
            return getResources().getString(R.string.air_quailty_four);
        } else if (airNumber >= 201 && airNumber <= 300) {
            return getResources().getString(R.string.air_quailty_five);
        } else {
            return getResources().getString(R.string.air_quailty_six);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new AppOpenHistoryAdapter(this, CApplication.openHistoryLists);
        hgridview_type_title.setAdapter(adapter);
        if (CApplication.openHistoryLists.size() == 0) {
            tv_app_open_empty.setVisibility(View.VISIBLE);
            hgridview_type_title.setVisibility(View.GONE);
        } else {
            tv_app_open_empty.setVisibility(View.GONE);
            hgridview_type_title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                finish();
                overridePendingTransition(R.anim.hold, R.anim.push_up_out);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
