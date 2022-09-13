package com.lqc.realm.config;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: Glenn
 * Description: 外部配置初始化
 * Created: 2022/9/13
 */
@Service
public class ExternalConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        // 外部配置文件路径
        ClassPathResource classPathResource = new ClassPathResource("");
        String path = classPathResource.getAbsolutePath();
        path = path.substring(0, path.length() - 44) + "config.txt";
        FileReader reader = new FileReader(path);
        // 读取配置
        Map<String, Map<String, String>> configMap = CommonCacheConfig.config_map;
        List<String> lines = reader.readLines();
        String type = "";
        for (String line : lines) {
            // 标题
            if (line.contains("#")) {
                type = line.substring(1);
                continue;
            }
            Map<String, String> map = configMap.get(type);
            if (map == null || map.size() == 0) {
                configMap.put(type, MapUtil.<String, String>builder().put(line.split("=")[0], line.split("=")[1]).build());
            } else {
                map.put(line.split("=")[0], line.split("=")[1]);
            }
        }
    }
}
