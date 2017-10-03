package cn.cncgroup.tv.view.weather;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.adapter.CityAdapter;
import cn.cncgroup.tv.adapter.CountryAdapter;
import cn.cncgroup.tv.adapter.ProvinceAdapter;
import cn.cncgroup.tv.base.BaseActivity;
import cn.cncgroup.tv.ui.widget.ItemGridview;
import cn.cncgroup.tv.ui.widget.gridview.VerticalGridView;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.view.weather.Bean.Now;
import cn.cncgroup.tv.view.weather.Bean.WeatherBean;
import cn.cncgroup.tv.view.weather.city.AreaModel;
import cn.cncgroup.tv.view.weather.city.CityModel;
import cn.cncgroup.tv.view.weather.city.ProvicneModel;

public class WeatherCityActivity extends BaseActivity {
    private static final int REFRESHCITY = 1;
    private static final int REFRESHCOUNTRY = 2;
    private static final int PROVINCEFOCUS = 1;
    private static final int CITYFOCUS = 2;
    private static final int COUNTRYFOCUS = 3;
    private CountryAdapter.Listener<AreaModel> countryListener = new CountryAdapter.Listener<AreaModel>() {
        @Override
        public void onCountryItemClickListener(View view, int position,
                                               AreaModel result) {
            updateWeather();
            Preferences preferences = Preferences.build(WeatherCityActivity.this);
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, 0);
            preferences.putInt(GlobalConstant.CITYPOSITION, 0);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, 0);
            int proviance_position = provinceAdapter.getSelectedPosition();
            int country_position = countryAdapter.getSelectedPosition();
            int city_position = cityAdapter.getSelectedPosition();
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, proviance_position);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, country_position);
            preferences.putInt(GlobalConstant.CITYPOSITION, city_position);
        }

        @Override
        public void onCountryItemFocusListener(View view, int position,
                                               AreaModel areaModel, boolean hasFocus) {
            if (hasFocus) {
                countryAdapter.setSelectedPosition(position);
                cityViewChange(COUNTRYFOCUS);
            }
        }
    };
    private static final String TEMPERATURE = "℃";
    final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable e) {
            // 做一些异常处理
            e.printStackTrace();
        }

        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                // 在这里得到了json数据以后要进行解析，然后将结果显示在UI的相应控件上
                CApplication.weatherBean = JSON.parseObject(new String(
                        responseBody, "utf-8"), WeatherBean.class);
                if (CApplication.weatherBean != null
                        && CApplication.weatherBean.getShowapi_res_code() == 0) {
                    showWeather();
                }
                // 在此对返回内容做处理
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };
    private final String TAG = "WeatherCityActivity";
    ArrayList<ProvicneModel> provinceModels = new ArrayList<ProvicneModel>();
    ArrayList<CityModel> cityModels = new ArrayList<CityModel>();
    ArrayList<AreaModel> countryModels = new ArrayList<AreaModel>();
    private ProvinceAdapter.Listener<ProvicneModel> provinceListener = new ProvinceAdapter.Listener<ProvicneModel>() {
        @Override
        public void onProvinceItemClickListener(View view, int position,
                                                ProvicneModel result) {
            updateWeather();
            Preferences preferences = Preferences.build(WeatherCityActivity.this);
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, 0);
            preferences.putInt(GlobalConstant.CITYPOSITION, 0);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, 0);
            int proviance_position = provinceAdapter.getSelectedPosition();
            int country_position = countryAdapter.getSelectedPosition();
            int city_position = cityAdapter.getSelectedPosition();
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, proviance_position);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, country_position);
            preferences.putInt(GlobalConstant.CITYPOSITION, city_position);
        }

        @Override
        public void onProvinceItemFocusListener(View view, int position,
                                                ProvicneModel areaBean, boolean hasFocus) {
            if (hasFocus) {
                cityModels = CApplication.getProvicneModels().get(position)
                        .getCityModels();
                Message message = new Message();
                message.what = REFRESHCITY;
                handler.handleMessage(message);
                if (cityModels.size() > 0) {
                    countryModels = cityModels.get(0).getAreaModels();
                    Message message2 = new Message();
                    message2.what = REFRESHCOUNTRY;
                    handler.handleMessage(message2);
                }
                provinceAdapter.setSelectedPosition(position);
                cityViewChange(PROVINCEFOCUS);
            }
        }
    };
    private CityAdapter.Listener<CityModel> cityListener = new CityAdapter.Listener<CityModel>() {
        @Override
        public void onCtyItemClickListener(View view, int position,
                                           CityModel result) {
            updateWeather();
            Preferences preferences = Preferences.build(WeatherCityActivity.this);
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, 0);
            preferences.putInt(GlobalConstant.CITYPOSITION, 0);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, 0);
            int proviance_position = provinceAdapter.getSelectedPosition();
            int country_position = countryAdapter.getSelectedPosition();
            int city_position = cityAdapter.getSelectedPosition();
            preferences.putInt(GlobalConstant.PROVINCEPOSITION, proviance_position);
            preferences.putInt(GlobalConstant.COUNTRYPOSITION, country_position);
            preferences.putInt(GlobalConstant.CITYPOSITION, city_position);
        }

        @Override
        public void onCityItemFocusListener(View view, int position,
                                            CityModel areaBean, boolean hasFocus) {
            if (hasFocus) {
                countryModels = cityModels.get(position).getAreaModels();
                Message message = new Message();
                message.what = REFRESHCOUNTRY;
                handler.handleMessage(message);
                cityAdapter.setSelectedPosition(position);
                cityViewChange(CITYFOCUS);
            }
        }
    };
    Handler handler;
    private SimpleDraweeView local_weather_img_icon;
    private TextView local_weather_text_temperature;
    private TextView local_weather_text_desc;
    private TextView local_weather_text_wind;
    private TextView local_weather_air_index;
    private TextView local_weather_pollution;
    private VerticalGridView provinceGrid;
    private VerticalGridView cityGrid;
    private VerticalGridView countyGrid;
    private TextSwitcher local_weather_eare;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountryAdapter countryAdapter;
    private ImageView iv_city1, iv_city2, iv_city3;
    private ItemGridview iv_focus_city1, iv_focus_city2, iv_focus_city3;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_local_weather);
    }

    private void showWeather() {
        try {
            Now nowWeather = CApplication.weatherBean.getShowapi_res_body()
                    .getNow();
            local_weather_text_desc.setText(nowWeather.getWeather());
            local_weather_text_wind.setText(nowWeather.getWind_power());
            local_weather_text_temperature.setText(nowWeather.getTemperature() + TEMPERATURE);
            local_weather_air_index.setText(nowWeather.getAqi() + "");
            local_weather_pollution.setText(airQuailty(nowWeather.getAqi()));
            local_weather_img_icon.setImageURI(Uri.parse(nowWeather
                    .getWeather_pic()));
        } catch (Exception e) {
        }
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

    private void updateWeather() {
        AreaModel currCcountry = countryModels.get(countyGrid
                .getSelectedPosition());
        currCcountry.getWeatherCode();
        Preferences.build(WeatherCityActivity.this).putString(
                GlobalConstant.WEATHERCITY, currCcountry.getCityName());
        Preferences.build(WeatherCityActivity.this).putString(
                GlobalConstant.WEATHERCITYCODE, currCcountry.getWeatherCode());
//		保存当前的三个item的位置
        Preferences.build(WeatherCityActivity.this).putInt(
                GlobalConstant.PROVINCEPOSITION, provinceGrid.getSelectedPosition());
        Preferences.build(WeatherCityActivity.this).putInt(
                GlobalConstant.CITYPOSITION, cityGrid.getSelectedPosition());
        Preferences.build(WeatherCityActivity.this).putInt(
                GlobalConstant.COUNTRYPOSITION, countyGrid.getSelectedPosition());
        // 请求网络
        new ShowApiRequest(GlobalConstant.WEATHERURL, GlobalConstant.APPKEY,
                GlobalConstant.APPSECRIT)
                .setResponseHandler(resHandler)
                .addTextPara(
                        "areaid",
                        Preferences.build(this).getString(
                                GlobalConstant.WEATHERCITYCODE, "101010100"))
                .addTextPara(
                        "area",
                        Preferences.build(this).getString(
                                GlobalConstant.WEATHERCITY, "北京"))
                        // 参数包括0和1两个，0之请求三天，1就请求七天
                .addTextPara("needMoreDay", "1")
                        // 是否需要返回指数数据，比如穿衣指数、紫外线指数等。1为返回，0 为不返回。
                .addTextPara("needIndex", "0").post();
        // 更新ui
        local_weather_eare.setText(currCcountry.getCityName());
    }

    @Override
    protected void findView() {
        iv_city1 = (ImageView) findViewById(R.id.iv_city1);
        iv_focus_city1 = (ItemGridview) findViewById(R.id.iv_focus_city1);
        iv_city2 = (ImageView) findViewById(R.id.iv_city2);
        iv_focus_city2 = (ItemGridview) findViewById(R.id.iv_focus_city2);
        iv_city3 = (ImageView) findViewById(R.id.iv_city3);
        iv_focus_city3 = (ItemGridview) findViewById(R.id.iv_focus_city3);
        local_weather_eare = (TextSwitcher) findViewById(R.id.local_weather_eare);
        provinceGrid = (VerticalGridView) findViewById(R.id.local_weather_province);
        cityGrid = (VerticalGridView) findViewById(R.id.local_weather_city);
        countyGrid = (VerticalGridView) findViewById(R.id.local_weather_county);
        local_weather_img_icon = (SimpleDraweeView) findViewById(R.id.local_weather_img_icon);
        local_weather_text_temperature = (TextView) findViewById(R.id.local_weather_text_temperature);
        local_weather_text_desc = (TextView) findViewById(R.id.local_weather_text_desc);
        local_weather_text_wind = (TextView) findViewById(R.id.local_weather_text_wind);
        local_weather_air_index = (TextView) findViewById(R.id.local_weather_air_index);
        local_weather_pollution = (TextView) findViewById(R.id.local_weather_pollution);
    }

    private void cityViewChange(int city) {
        switch (city) {
            case PROVINCEFOCUS:
                iv_city1.setVisibility(View.GONE);
                iv_focus_city1.setVisibility(View.VISIBLE);
                iv_city2.setVisibility(View.VISIBLE);
                iv_focus_city2.setVisibility(View.GONE);
                iv_city3.setVisibility(View.VISIBLE);
                iv_focus_city3.setVisibility(View.GONE);
                break;
            case CITYFOCUS:
                iv_city1.setVisibility(View.VISIBLE);
                iv_focus_city1.setVisibility(View.GONE);
                iv_city2.setVisibility(View.GONE);
                iv_focus_city2.setVisibility(View.VISIBLE);
                iv_city3.setVisibility(View.VISIBLE);
                iv_focus_city3.setVisibility(View.GONE);
                break;
            case COUNTRYFOCUS:
                iv_city1.setVisibility(View.VISIBLE);
                iv_focus_city1.setVisibility(View.GONE);
                iv_city2.setVisibility(View.VISIBLE);
                iv_focus_city2.setVisibility(View.GONE);
                iv_city3.setVisibility(View.GONE);
                iv_focus_city3.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initView() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESHCITY:
                        cityAdapter.setData(cityModels);
                        cityAdapter.notifyDataSetChanged();
                        cityGrid.setSelectedPosition(0, 0);
                        cityAdapter.setSelectedPosition(cityGrid
                                .getSelectedPosition());
                        break;
                    case REFRESHCOUNTRY:
                        countryAdapter.setData(countryModels);
                        countryAdapter.notifyDataSetChanged();
                        countyGrid.setSelectedPosition(0, 0);
                        countryAdapter.setSelectedPosition(countyGrid
                                .getSelectedPosition());
                        break;
                }
            }
        };
        provinceGrid.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        cityGrid.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        countyGrid.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        local_weather_eare.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(WeatherCityActivity.this);
                t.setTextSize(getResources().getDimension(R.dimen.dimen_25sp));
                t.setGravity(Gravity.BOTTOM);
                return t;
            }
        });
        local_weather_eare.setInAnimation(this, android.R.anim.fade_in);
        local_weather_eare.setOutAnimation(this, android.R.anim.fade_out);
        // 这里不是写死，而是要动态的加载进来，而且要记住，他们层级的id,一打开页面的时候进来就要显示在对应的位置上
        local_weather_eare.setText(Preferences.build(this).getString(
                GlobalConstant.WEATHERCITY, "北京"));
        provinceModels.addAll(CApplication.getProvicneModels());
        initAdapter();
        showWeather();
    }

    private void initAdapter() {
        provinceAdapter = new ProvinceAdapter(provinceModels, provinceListener);
        provinceGrid.setAdapter(provinceAdapter);
        cityAdapter = new CityAdapter(cityModels, cityListener);
        cityGrid.setAdapter(cityAdapter);
        countryAdapter = new CountryAdapter(countryModels, countryListener);
        countyGrid.setAdapter(countryAdapter);
//		这里选择进来时显示的神马位置
        firstInCityPosition();
    }

    private void firstInCityPosition() {
        int provincePos = Preferences.build(getActivity()).getInt(GlobalConstant.PROVINCEPOSITION, 0);
        int cityPos = Preferences.build(getActivity()).getInt(GlobalConstant.CITYPOSITION, 0);
        int countryPos = Preferences.build(getActivity()).getInt(GlobalConstant.COUNTRYPOSITION, 0);

        provinceGrid.setSelectedPosition(provincePos);
        cityModels = CApplication.getProvicneModels().get(provincePos)
                .getCityModels();
        Message message = new Message();
        message.what = REFRESHCITY;
        handler.handleMessage(message);
        if (cityModels.size() > 0) {
            countryModels = cityModels.get(0).getAreaModels();
            Message message2 = new Message();
            message2.what = REFRESHCOUNTRY;
            handler.handleMessage(message2);
        }
        provinceAdapter.setSelectedPosition(provincePos);
        cityGrid.requestFocus();
        cityAdapter.setSelectedPosition(cityPos);
        cityGrid.setSelectedPosition(cityPos);
        countryModels = cityModels.get(cityPos).getAreaModels();
        Message message3 = new Message();
        message3.what = REFRESHCOUNTRY;
        handler.handleMessage(message3);
        cityAdapter.setSelectedPosition(cityPos);
        countryAdapter.setSelectedPosition(countryPos);
        countyGrid.setSelectedPosition(countryPos);
        countyGrid.requestFocus();
        cityViewChange(COUNTRYFOCUS);
    }

    // 这里可以添加城市的循环选择，根据需求而定
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                finish();
                overridePendingTransition(R.anim.hold, R.anim.push_up_out);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
