package com.lqc.realm.utils;

import java.lang.annotation.*;

/**
 * Author: Glenn
 * Description:
 * Created: 2022/9/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface FileType {

    ServiceType type() default ServiceType.none;
}
