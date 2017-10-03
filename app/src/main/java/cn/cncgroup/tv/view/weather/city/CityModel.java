package cn.cncgroup.tv.view.weather.city;

import java.util.ArrayList;

/**
 * Created by Ho on 2014/6/26.
 */
public class CityModel extends BaseCityModel {

    /**
     * 下级区/县
     */
    private ArrayList<AreaModel> areaModels;

    public CityModel() {
        areaModels = new ArrayList<AreaModel>();
    }

    public ArrayList<AreaModel> getAreaModels() {
        return areaModels;
    }

    public void setAreaModels(ArrayList<AreaModel> areaModels) {
        this.areaModels = areaModels;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "areaModels=" + areaModels +
                '}';
    }
}
