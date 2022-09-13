package com.lqc.realm.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Author: Glenn
 * Description: 全部类型
 * Created: 2022/8/18
 */
@Data
@Accessors(chain = true)
public class AllType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private int index;          // 类型所属索引
    private String indexName;   // 类型所属索引名
    private int code;
    private String desc;

}
