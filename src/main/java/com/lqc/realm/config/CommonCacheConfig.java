package com.lqc.realm.config;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Glenn
 * Description: 缓存配置
 * Created: 2022/8/18
 */
public class CommonCacheConfig {


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
        if (map == null || map.size() == 0) {
            return "";
        }
        return map.get(key);
    }
}
