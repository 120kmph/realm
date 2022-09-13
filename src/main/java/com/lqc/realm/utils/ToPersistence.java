package com.lqc.realm.utils;

import java.lang.annotation.*;

/**
 * Author: Glenn
 * Description: 持久化注解 注接的方法执行后进行数据持久化
 * Created: 2022/9/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ToPersistence {

}
