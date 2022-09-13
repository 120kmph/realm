package com.lqc.realm.utils;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONUtil;
import com.lqc.realm.mapper.AllTypeMapper;
import com.lqc.realm.mapper.FoodMapper;
import com.lqc.realm.mapper.FootprintMapper;
import com.lqc.realm.model.AllType;
import com.lqc.realm.model.Food;
import com.lqc.realm.model.Footprint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Glenn
 * Description: 方法执行后进行数据持久化
 * Created: 2022/9/9
 */
@Aspect
@Component
public class PersistenceAspect {

    @Autowired
    private AllTypeMapper typeMapper;

    @Autowired
    private FootprintMapper footprintMapper;

    @Autowired
    private FoodMapper foodMapper;

    @Pointcut("@annotation(com.lqc.realm.utils.ToPersistence)")
    public void todo() {
    }

    @After("todo()")
    public void persistent(JoinPoint joinPoint) {
        // 类注解类型
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        FileType annotation = clazz.getAnnotation(FileType.class);
        ServiceType type = annotation.type();
        // 持久化
        if (ServiceType.all_type == type) {
            List<AllType> allTypes = this.typeMapper.selectList(null);
            this.save(type.file(), allTypes);
        }
        if (ServiceType.foot_print == type) {
            List<Footprint> footprints = this.footprintMapper.selectList(null);
            this.save(type.file(), footprints);
        }
        if (ServiceType.food == type) {
            List<Food> foods = this.foodMapper.selectList(null);
            this.save(type.file(), foods);
        }
    }

    /**
     * 保存
     */
    private void save(String fileName, List<?> list) {
        ClassPathResource classPathResource = new ClassPathResource("");
        String path = classPathResource.getAbsolutePath();
        path = path.substring(0, path.length() - 44) + "data/" + fileName;
        FileWriter writer = new FileWriter(path);
        String string = JSONUtil.toJsonStr(list);
        writer.writeLines(JSONUtil.toList(JSONUtil.parseArray(string), String.class));
    }

}
