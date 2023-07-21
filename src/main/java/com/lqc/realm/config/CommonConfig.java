package com.lqc.realm.config;

import cn.hutool.core.map.MapUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author: Glenn
 * Description: 通用配置
 * Created: 2022/7/26
 */
public class CommonConfig {

    /**
     * 对应数学符号(html格式)
     */
    public static final Map<String, String> MATH_FLAGS = MapUtil.<String, String>builder()
            .put("@cheng", "&#215;")
            .put("@chu", "&#247;")
            .put("@budeng", "&#8800;")
            .put("@jiadeng", "&#177;")
            .put("@hanshu", "&#402;")
            .put("@du", "&#176;")
            .put("@renyi", "&#8704;")
            .put("@cunzai", "&#8707;")
            .put("@kongji", "&#8709;")
            .put("@zeng", "&#8710;")
            .put("@shuyu", "&#8712;")
            .put("@bushuyu", "#8713;")
            .put("@wuqiong", "&#8734;")
            .put("@bing", "&#8746;")
            .put("@jiao", "&#8745;")
            .put("@buji", "&#8705;")
            .put("@fouding", "&#172;")
            .put("@yinwei", "&#8757;")
            .put("@suoyi", "&#8756;")
            .put("@yuedeng", "&#8776;")
            .put("@xiaodeng", "&#8804;")
            .put("@dadeng", "&#8805;")
            .put("@yuanxiao", "&#8810;")
            .put("@yuanda", "&#8811;")
            .put("@baohanyu", "&#8838;")
            .put("@beibaohan", "&#8839;")
            .put("@bubaohan", "&#8840;")
            .put("@zhenbaohan", "&#8842;")

            .put("@aerfa", "&alpha;")
            .put("@beita", "&beta;")
            .put("@gama", "&gamma;")
            .put("@deierta", "&delta;")
            .put("@sita", "&theta;")
            .put("@fai", "&phi;")
            .put("@omiga", "&omega;")
            .put("@sigma", "&sigma;")
            .build();
}
