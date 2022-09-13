package com.lqc.realm.manager;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONUtil;
import com.lqc.realm.mapper.AllTypeMapper;
import com.lqc.realm.mapper.FoodMapper;
import com.lqc.realm.mapper.FootprintMapper;
import com.lqc.realm.model.AllType;
import com.lqc.realm.model.Food;
import com.lqc.realm.model.Footprint;
import com.lqc.realm.service.TypeService;
import com.lqc.realm.utils.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Glenn
 * Description: h2 数据初始化
 * Created: 2022/9/9
 */
@Service
public class DataInitializationService implements ApplicationRunner {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private TypeService typeService;

    @Autowired
    private AllTypeMapper typeMapper;

    @Autowired
    private FootprintMapper footprintMapper;

    /**
     * 数据初始化
     */
    @Override
    public void run(ApplicationArguments args) {
        for (String fileName : ServiceType.getFileNames()) {
            // 获取文件路径
            ClassPathResource classPathResource = new ClassPathResource("");
            String path = classPathResource.getAbsolutePath();
            path = path.substring(0, path.length() - 44) + "data/" + fileName;
            FileReader reader = new FileReader(path);
            List<String> lines = reader.readLines();
            if (ServiceType.all_type.file().equals(fileName)) {
                for (String line : lines) {
                    AllType todo = JSONUtil.parseObj(line).toBean(AllType.class);
                    this.typeMapper.insert(todo);
                }
            }
            if (ServiceType.foot_print.file().equals(fileName)) {
                for (String line : lines) {
                    Footprint todo = JSONUtil.parseObj(line).toBean(Footprint.class);
                    this.footprintMapper.insert(todo);
                }
            }
            if (ServiceType.food.file().equals(fileName)) {
                for (String line : lines) {
                    Food todo = JSONUtil.parseObj(line).toBean(Food.class);
                    this.foodMapper.insert(todo);
                }
            }
        }

        // 类型缓存配置
        typeService.updateCache();
    }
}
