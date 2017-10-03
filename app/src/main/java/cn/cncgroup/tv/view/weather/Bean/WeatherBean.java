package cn.cncgroup.tv.view.weather.Bean;

import java.io.Serializable;

/**
 * Created by jinwenchao123 on 15/7/20.
 * json具体参数的值和代表的意义可查看以下链接
 * https://www.showapi.com/api/lookPoint/9/2
 */
public class WeatherBean implements Serializable{

//    showapi_res_code	int	0	易源返回标志，0为成功，其他为失败。
//    showapi_res_error	String	用户输入有误!	错误信息的展示
//    showapi_res_body	String	{"city":"昆明","prov":"云南"}	消息体的JSON封装，所有应用级的返回参数将嵌入此对象 。

    public WeatherBean(){

    }

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBody showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
