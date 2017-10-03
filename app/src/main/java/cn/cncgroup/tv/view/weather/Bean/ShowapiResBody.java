package cn.cncgroup.tv.view.weather.Bean;

import java.io.Serializable;

/**
 * Created by jinwenchao123 on 15/7/20.
 */
public class ShowapiResBody implements Serializable{

    private CityInfo cityInfo;
    private DayWeather f1,f2,f3,f4,f5,f6,f7;
    private int ret_code;
    private String time;
    private Now now;

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public DayWeather getF1() {
        return f1;
    }

    public void setF1(DayWeather f1) {
        this.f1 = f1;
    }

    public DayWeather getF2() {
        return f2;
    }

    public void setF2(DayWeather f2) {
        this.f2 = f2;
    }

    public DayWeather getF3() {
        return f3;
    }

    public void setF3(DayWeather f3) {
        this.f3 = f3;
    }

    public DayWeather getF4() {
        return f4;
    }

    public void setF4(DayWeather f4) {
        this.f4 = f4;
    }

    public DayWeather getF5() {
        return f5;
    }

    public void setF5(DayWeather f5) {
        this.f5 = f5;
    }

    public DayWeather getF6() {
        return f6;
    }

    public void setF6(DayWeather f6) {
        this.f6 = f6;
    }

    public DayWeather getF7() {
        return f7;
    }

    public void setF7(DayWeather f7) {
        this.f7 = f7;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }
}
