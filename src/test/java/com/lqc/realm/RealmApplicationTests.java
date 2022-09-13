package com.lqc.realm;

import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.interaction.Command;
import com.lqc.realm.interaction.CommandConfig;
import com.lqc.realm.manager.CommonService;
import com.lqc.realm.mapper.AllTypeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class RealmApplicationTests {

    @Autowired
    private CommonService commonService;

    @Autowired
    private AllTypeMapper typeMapper;

    @Test
    public void t1() {
        Command command = CommandConfig.config.getCommand(11);
        commonService.execute(command.getOptions(), "1", "D:\\all_type.txt");

        command = CommandConfig.config.getCommand(12);
        commonService.execute(command.getOptions(), "1");
    }

    @Test
    public void t2() {
        Map<String, Map<String, String>> config_map = CommonCacheConfig.config_map;
        for (String type : config_map.keySet()) {
            System.out.println(type);
            Map<String, String> map = config_map.get(type);
            for (String key : map.keySet()) {
                System.out.println(key + "-" + map.get(key));
            }
        }
    }

}
