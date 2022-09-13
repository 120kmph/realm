package com.lqc.realm.config;

import cn.hutool.core.util.StrUtil;
import com.lqc.realm.model.AllType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description: 缓存配置
 * Created: 2022/8/18
 */
public class CommonCacheConfig {

    /* =======================================  类型 ======================================= */

    /**
     * 类型缓存 AllType
     */
    public static Map<Integer, List<AllType>> typeCache = new HashMap<>();

    /**
     * 类型 - 获取类型描述desc
     */
    public static String getTypeDesc(int index, int code) {
        List<AllType> types = typeCache.get(index);
        List<AllType> collect = types.stream().filter(type -> type.getCode() == code).collect(Collectors.toList());
        return collect.size() > 0 ? collect.get(0).getDesc() : "未知";
    }

    /**
     * 类型 - 展示类型信息
     */
    public static String toShow(int index) {
        StringBuilder result = new StringBuilder();
        List<AllType> types = typeCache.get(index);
        for (AllType type : types) {
            result.append(type.getCode()).append("=").append(type.getDesc());
            result.append(" ");
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 类型 - 展示类型信息
     */
    public static String toShow(List<AllType> types) {
        StringBuilder result = new StringBuilder();
        for (AllType type : types) {
            result.append(type.getCode()).append("=").append(type.getDesc());
            result.append(" ");
        }
        return result.substring(0, result.length() - 1);
    }


    /* =============================== 临时中转 ================================== */


    public static List<String> temp_info = new ArrayList<>();

    public static List<String> temp_info_json = new ArrayList();

    /**
     * 添加
     */
    public static void addTemp(List<String> todo) {
        temp_info = todo;
    }

    /**
     * 添加
     */
    public static void addTempJSON(List<String> todo) {
        temp_info_json = todo;
    }

    /**
     * 获取并清空
     */
    public static List<String> getTemp() {
        List<String> result = temp_info;
        temp_info = new ArrayList<>();
        return result;
    }

    /**
     * 获取并清空
     */
    public static List<String> getTempJSON() {
        List<String> result = temp_info_json;
        temp_info_json = new ArrayList<>();
        return result;
    }


    /* ====================================== 外部配置 ======================================= */

    public static Map<String, Map<String, String>> config_map = new HashMap<>();

    /**
     * 获取配置的值
     */
    public static String getConfig(String type, String key) {
        if (StrUtil.isEmpty(type) || StrUtil.isEmpty(key)) {
            return "";
        }
        Map<String, String> map = config_map.get(type);
        if (map == null || map.size() == 1) {
            return "";
        }
        return map.get(key);
    }
}
