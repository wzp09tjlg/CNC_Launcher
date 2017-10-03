package cn.cncgroup.tv.view.weather.Bean;

import java.io.Serializable;

/**
 * Created by jinwenchao123 on 15/7/20.
 */
public class DayWeather implements Serializable{
//                "day": "20150726",
//                "day_air_temperature": "23",
//                "day_weather": "小雨",
//                "day_weather_pic": "http://appimg.showapi.com/images/weather/icon/day/07.png",
//                "day_wind_direction": "无持续风向",
//                "day_wind_power": "微风<10m/h",
//                "night_air_temperature": "13",
//                "night_weather": "小雨",
//                "night_weather_pic": "http://appimg.showapi.com/images/weather/icon/night/07.png",
//                "night_wind_direction": "无持续风向",
//                "night_wind_power": "微风<10m/h",
//                "sun_begin_end": "06:37|20:13",
//                "weekday": 7

    private String day;
    private String day_air_temperature;
    private String day_weather;
    private String day_weather_pic;
    private String day_wind_direction;
    private String day_wind_power;
    private String night_air_temperature;
    private String night_weather;
    private String night_weather_pic;
    private String night_wind_direction;
    private String night_wind_power;
    private String sun_begin_end;
    private int weekday;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay_air_temperature() {
        return day_air_temperature;
    }

    public void setDay_air_temperature(String day_air_temperature) {
        this.day_air_temperature = day_air_temperature;
    }

    public String getDay_weather() {
        return day_weather;
    }

    public void setDay_weather(String day_weather) {
        this.day_weather = day_weather;
    }

    public String getDay_weather_pic() {
        return day_weather_pic;
    }

    public void setDay_weather_pic(String day_weather_pic) {
        this.day_weather_pic = day_weather_pic;
    }

    public String getDay_wind_direction() {
        return day_wind_direction;
    }

    public void setDay_wind_direction(String day_wind_direction) {
        this.day_wind_direction = day_wind_direction;
    }

    public String getDay_wind_power() {
        return day_wind_power;
    }

    public void setDay_wind_power(String day_wind_power) {
        this.day_wind_power = day_wind_power;
    }

    public String getNight_air_temperature() {
        return night_air_temperature;
    }

    public void setNight_air_temperature(String night_air_temperature) {
        this.night_air_temperature = night_air_temperature;
    }

    public String getNight_weather() {
        return night_weather;
    }

    public void setNight_weather(String night_weather) {
        this.night_weather = night_weather;
    }

    public String getNight_weather_pic() {
        return night_weather_pic;
    }

    public void setNight_weather_pic(String night_weather_pic) {
        this.night_weather_pic = night_weather_pic;
    }

    public String getNight_wind_direction() {
        return night_wind_direction;
    }

    public void setNight_wind_direction(String night_wind_direction) {
        this.night_wind_direction = night_wind_direction;
    }

    public String getNight_wind_power() {
        return night_wind_power;
    }

    public void setNight_wind_power(String night_wind_power) {
        this.night_wind_power = night_wind_power;
    }

    public String getSun_begin_end() {
        return sun_begin_end;
    }

    public void setSun_begin_end(String sun_begin_end) {
        this.sun_begin_end = sun_begin_end;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }
}
