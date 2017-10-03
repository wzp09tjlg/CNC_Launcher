package cn.cncgroup.tv.view.weather.city;

import java.util.ArrayList;

/**
 * Created by Ho on 2014/6/30.
 */
public class ProvicneModel extends BaseCityModel {

    /**
     * 城市列表
     */
    private ArrayList<CityModel> cityModels;

    public ProvicneModel() {
        cityModels = new ArrayList<CityModel>();
    }

    public ArrayList<CityModel> getCityModels() {
        return cityModels;
    }

    public void setCityModels(ArrayList<CityModel> cityModels) {
        this.cityModels = cityModels;
    }

    @Override
    public String toString() {
        return "ProvicneModel{" +
                "cityModels=" + cityModels +
                '}';
    }
}
