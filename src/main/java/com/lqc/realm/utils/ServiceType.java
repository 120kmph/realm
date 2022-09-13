package com.lqc.realm.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description:
 * Created: 2022/9/9
 */
public enum ServiceType {

    none(0, "未知", "none", ""),
    data(1, "数据服务", "dataService", ""),
    all_type(2, "类型", "typeService", "all_type.txt"),
    foot_print(3, "足迹", "mapService", "foot_print.txt"),
    food(4, "好吃的", "foodService", "food.txt"),
    anki(5, "Anki", "ankiService", "");


    private final int code;
    private final String desc;
    private final String serviceName;
    private final String fileName;

    public static List<String> getFileNames() {
        return Arrays.stream(values()).map(ServiceType::file).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
    }

    ServiceType(int code, String desc, String serviceName, String fileName) {
        this.code = code;
        this.desc = desc;
        this.serviceName = serviceName;
        this.fileName = fileName;
    }

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public String service() {
        return serviceName;
    }

    public String file() {
        return fileName;
    }
}
