package com.lqc.realm.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lqc.realm.config.CommonConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: Glenn
 * Description: Http调用服务
 * Created: 2022/7/27
 */
@Service
public class HttpService {

    /**
     * 百度地图 - 地理编码 地址编码为经纬度
     * https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
     *
     * @param address 地址
     * @return {"lng":116.34337596398342,"lat":39.947735083865818}
     */
    public JSONObject baiduMapCode(String address) {
        Map<String, Object> formMap = MapUtil.<String, Object>builder()
                .put("address", address)
                .put("output", "json")
                .put("ak", CommonConfig.MAP_AK)
                .build();
        String response = HttpRequest.get("https://api.map.baidu.com/geocoding/v3").form(formMap).execute().body();
        String result = JSONUtil.parseObj(response).getStr("result");
        String location = JSONUtil.parseObj(result).getStr("location");
        return JSONUtil.parseObj(location);
    }

    /**
     * 百度地图 - 圆形区域检索
     * https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-placeapi
     * query 搜索关键字
     * lat   搜索范围圆中心点 纬度
     * lng   搜索范围圆中心点 经度
     */
    public List<String> baiduMapSearch(String query, String lat, String lng) {
        Map<String, Object> formMap = MapUtil.<String, Object>builder()
                .put("query", query)
                .put("location", lat + "," + lng)
                .put("radius", 2000)
                .put("output", "json")
                .put("ak", CommonConfig.MAP_AK)
                .build();
        String response = HttpRequest.get("https://api.map.baidu.com/place/v2/search").form(formMap).execute().body();
        String results = JSONUtil.parseObj(response).getStr("results");
        return JSONUtil.parseArray(results).toList(String.class);
    }
}
