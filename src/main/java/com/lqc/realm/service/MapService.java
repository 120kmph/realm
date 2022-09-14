package com.lqc.realm.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.config.CommonConfig;
import com.lqc.realm.manager.HttpService;
import com.lqc.realm.mapper.FootprintMapper;
import com.lqc.realm.model.AllType;
import com.lqc.realm.model.Footprint;
import com.lqc.realm.utils.FileType;
import com.lqc.realm.utils.ServiceType;
import com.lqc.realm.utils.ToPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description: 足迹服务
 * Created: 2022/9/12
 */
@Service
@FileType(type = ServiceType.foot_print)
public class MapService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private FootprintMapper footprintMapper;

    /**
     * 搜索并打印
     */
    public int searchAndPrint(String address) {
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        List<String> poiList = this.searchPoiList(address);
        poiList.forEach(System.out::println);
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        Console.log("type: {}", CommonCacheConfig.toShow(1));
        Console.log("evaluate: {}", CommonCacheConfig.toShow(2));
        return 1;
    }

    /**
     * 地名搜索poi列表
     */
    private List<String> searchPoiList(String address) {
        List<String> result = new ArrayList<>();
        // 地名搜索经纬度编码
        JSONObject location = httpService.baiduMapCode(address);
        String lat = location.getStr("lat");
        String lng = location.getStr("lng");
        // 经纬度中心点范围查询poi
        List<String> poiList = httpService.baiduMapSearch(address, lat, lng);
        // 暂存
        CommonCacheConfig.addTemp(poiList);
        // 信息整理
        int index = 1;
        for (String poi : poiList) {
            JSONObject poiObj = JSONUtil.parseObj(poi);
            String name = poiObj.getStr("name");
            String loc = poiObj.getStr("address");
            result.add(index++ + "  -  " + name + ", 地址: " + loc);
        }
        return result;
    }

    /**
     * 添加
     */
    @ToPersistence
    public int add(String which, String time, String type, String evaluateType, String comment) {
        if (StrUtil.isEmpty(which)) {
            return 0;
        }
        if (StrUtil.isBlank(time)) {
            String now = DateUtil.today();
            time = now.substring(0, now.length() - 3);
        }
        if (StrUtil.isEmpty(type)) {
            type = "0";
        }
        if (StrUtil.isEmpty(evaluateType)) {
            evaluateType = "0";
        }
        // 信息填充
        List<String> poiList = CommonCacheConfig.getTemp();
        JSONObject todo = JSONUtil.parseObj(poiList.get(Integer.parseInt(which) - 1));
        String province = todo.getStr("province");
        boolean isThree = false;
        for (String to : CommonConfig.MAP_PROVINCE_3) {
            if (province.contains(to)) {
                isThree = true;
                break;
            }
        }
        Footprint footprint = new Footprint()
                .setUid(todo.getStr("uid"))
                .setName(todo.getStr("name"))
                .setLat(JSONUtil.parseObj(todo.getStr("location")).getStr("lat"))
                .setLng(JSONUtil.parseObj(todo.getStr("location")).getStr("lng"))
                .setProvince(isThree ? province.substring(0, 3) : province.substring(0, 2))
                .setCity(todo.getStr("city"))
                .setDate(time)
                .setType(Integer.parseInt(type))
                .setEvaluateType(Integer.parseInt(evaluateType))
                .setComment(comment);
        return this.footprintMapper.insert(footprint);
    }

    /**
     * 搜索条件信息 - 省份列表
     */
    public int printProvince() {
        List<String> provinces = CommonConfig.MAP_PROVINCE;
        int index = 1;
        for (String province : provinces) {
            System.out.print(province + "\t");
            if (index++ > 6) {
                System.out.println();
                index = 1;
            }
        }
        System.out.println();
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        Console.log("type: {}", CommonCacheConfig.toShow(1));
        Console.log("evaluate: {}", CommonCacheConfig.toShow(2));
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        return -1;
    }

    /**
     * 搜索
     */
    public int search(String inProvince, String inCity, String inName, String inType, String inEvaluate, String inTime) {
        CommonCacheConfig.addTemp(ListUtil.toList(inProvince, inCity, inName, inType, inType, inEvaluate, inTime));
        List<Footprint> footprints = this.getFootprints(inProvince, inCity, inName, inType, inEvaluate, inTime);
        List<String> collect = footprints.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        CommonCacheConfig.addTempJSON(collect);
        System.out.println("# # # # # # # # # # # # # # # # # # # # # # # # # # # Query result # # # # # # # # # # # # # # # # # # # # # # # # # # # #");
        int footIndex = 1;
        for (Footprint footprint : footprints) {
            System.out.println(footIndex++ + " - " + footprint.toString());
        }
        System.out.println("# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #");
        return 1;
    }

    /**
     * 模糊查询 足迹列表
     */
    private List<Footprint> getFootprints(String province, String city, String name, String inType, String evaluate, String time) {
        QueryWrapper<Footprint> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(province)) {
            wrapper.like("province", province);
        }
        if (StrUtil.isNotBlank(city)) {
            wrapper.like("city", city);
        }
        if (StrUtil.isNotBlank(name)) {
            wrapper.like("name", name).or().like("comment", name);
        }
        if (StrUtil.isNotBlank(inType)) {
            wrapper.eq("type", Integer.parseInt(inType));
        }
        if (StrUtil.isNotBlank(evaluate)) {
            wrapper.eq("evaluate_type", Integer.parseInt(evaluate));
        }
        if (StrUtil.isNotBlank(time)) {
            if ("latest".equals(time)) {
                wrapper.orderByDesc("date").last("limit 10");
            } else {
                wrapper.like("date", time).orderByDesc("date");
            }
        }
        return this.footprintMapper.selectList(wrapper);
    }

    /**
     * 删除
     */
    @ToPersistence
    public int delete(String args) {
        List<String> list = CommonCacheConfig.getTempJSON();
        List<Footprint> footprints = list.stream().map(json -> JSONUtil.toBean(json, Footprint.class)).collect(Collectors.toList());
        String[] split = args.split(",");
        List<String> deletes = new ArrayList<>();
        Arrays.stream(split).forEach(curr -> {
            int currIndex = Integer.parseInt(curr);
            if (currIndex > 0 && currIndex <= footprints.size()) {
                deletes.add(footprints.get(currIndex - 1).getUid());
            }
        });
        int delete = this.footprintMapper.deleteBatchIds(deletes);
        new Thread(this::reSearch).start();
        return delete == deletes.size() ? 1 : 0;
    }

    /**
     * 重新搜索并打印
     */
    public void reSearch() {
        try {
            Thread.sleep(100);
            List<String> args = CommonCacheConfig.getTemp();
            this.search(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 信息统计
     */
    public int data() {
        System.out.println("========= Data Statistics ========");
        System.out.println();
        // 总计
        int all = this.footprintMapper.selectCount(null).intValue();
        System.out.println("足迹总计: " + all);
        // 进度
        int provinceDoneCount = this.footprintMapper.selectCount(new QueryWrapper<Footprint>().select("distinct province")).intValue();
        String provinceProgress = new DecimalFormat("0.00").format((float) provinceDoneCount * 100 / CommonConfig.MAP_PROVINCE.size());
        System.out.println("去过的省份: " + provinceDoneCount + "个     进度: " + provinceProgress + "%");
        int cityDoneCount = this.footprintMapper.selectCount(new QueryWrapper<Footprint>().select("distinct city")).intValue();
        String cityProgress = new DecimalFormat("0.00").format((float) cityDoneCount * 100 / CommonConfig.MAP_CITY_NUM);
        System.out.println("去过的城市: " + cityDoneCount + "个     进度: " + cityProgress + "%");
        System.out.println();
        // 分省
        System.out.println("- - - - - - - - - - - - - -");
        System.out.println("分省统计");
        int provinceIndex = 1;
        for (String province : CommonConfig.MAP_PROVINCE) {
            Long provinceCount = this.footprintMapper.selectCount(new QueryWrapper<Footprint>().like("province", province));
            System.out.print(province + " - " + provinceCount + "\t");
            if (provinceIndex++ % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("- - - - - - - - - - - - - -");
        // 分类型
        System.out.println("类型统计");
        for (AllType type : CommonCacheConfig.typeCache.get(1)) {
            Long typeCount = this.footprintMapper.selectCount(new QueryWrapper<Footprint>().eq("type", type.getCode()));
            System.out.println(type.getDesc() + " - " + typeCount);
        }
        return 1;
    }
}
