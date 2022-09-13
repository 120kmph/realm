package com.lqc.realm.service;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONUtil;
import com.lqc.realm.mapper.AllTypeMapper;
import com.lqc.realm.mapper.FoodMapper;
import com.lqc.realm.mapper.FootprintMapper;
import com.lqc.realm.model.AllType;
import com.lqc.realm.model.Food;
import com.lqc.realm.model.Footprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Glenn
 * Description: 数据管理服务
 * Created: 2022/9/8
 */
@Service
public class DataService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private AllTypeMapper typeMapper;

    @Autowired
    private FootprintMapper footprintMapper;

    public int putAllType(String from) {
        FileReader fileReader = new FileReader(from);
        List<String> strings = fileReader.readLines();
        for (String obj : strings) {
            AllType allType = JSONUtil.parseObj(obj).toBean(AllType.class);
            this.typeMapper.insert(allType);
        }
        return 1;
    }

    public int putFootprint(String from) {
        FileReader fileReader = new FileReader(from);
        List<String> strings = fileReader.readLines();
        for (String obj : strings) {
            Footprint footprint = JSONUtil.parseObj(obj).toBean(Footprint.class);
            this.footprintMapper.insert(footprint);
        }
        return 1;
    }

    public int putFood(String from) {
        FileReader fileReader = new FileReader(from);
        List<String> strings = fileReader.readLines();
        for (String obj : strings) {
            Food food = JSONUtil.parseObj(obj).toBean(Food.class);
            this.foodMapper.insert(food);
        }
        return 1;
    }

    public int exportAllType() {
        List<AllType> types = this.typeMapper.selectList(null);
        String string = JSONUtil.toJsonStr(types);
        FileWriter fileWriter = new FileWriter("C:\\backup\\all_type.txt");
        fileWriter.writeLines(JSONUtil.toList(JSONUtil.parseArray(string), String.class));
        return 1;
    }

    public int exportFootprint() {
        List<Footprint> footprints = footprintMapper.selectList(null);
        String string = JSONUtil.toJsonStr(footprints);
        FileWriter fileWriter = new FileWriter("C:\\backup\\foot_print.txt");
        fileWriter.writeLines(JSONUtil.toList(JSONUtil.parseArray(string), String.class));
        return 1;
    }

    public int exportFood() {
        List<Food> foods = this.foodMapper.selectList(null);
        String string = JSONUtil.toJsonStr(foods);
        FileWriter fileWriter = new FileWriter("C:\\backup\\food.txt");
        fileWriter.writeLines(JSONUtil.toList(JSONUtil.parseArray(string), String.class));
        return 1;
    }
}
