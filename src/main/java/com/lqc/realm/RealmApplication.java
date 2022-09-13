package com.lqc.realm;

import com.lqc.realm.app.AppStarter;
import lombok.SneakyThrows;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.lqc.realm.mapper")
public class RealmApplication {

    @SneakyThrows
    public static void main(String[] args) {
        // 获取SpringBoot容器
        ConfigurableApplicationContext context = SpringApplication.run(RealmApplication.class, args);
        // 从Spring容器中获取指定的对象
        AppStarter starter = (AppStarter) context.getBean("appStarter");
        starter.start();
    }

}
