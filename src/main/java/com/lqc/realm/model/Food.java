package com.lqc.realm.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Author: Glenn
 * Description:
 * Created: 2022/8/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Food {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
    private String material;
    private String steps;
    private String types;
    private String comment;


    /**
     * 简略信息
     */
    public String shortInfo() {
        return this.addStrLength(this.name, 15) + "\t"
                + this.addStrLength(this.types, 10) + "\t"
                + this.comment;
    }

    /**
     * 详细信息
     */
    public String details() {
        StringBuilder result = new StringBuilder();
        if (StrUtil.isNotBlank(this.material)) {
            result.append("食材: ");
            result.append(this.material);
            result.append("\n");
        }
        List<String> steps = JSONUtil.parseArray(this.steps).toList(String.class);
        for (String step : steps) {
            result.append(step).append("\n");
        }
        return result.toString();
    }

    private String addStrLength(String todo, int newLength) {
        int length = 0;
        for (int i = 0; i < todo.length(); i++) {
            if (String.valueOf(todo.charAt(i)).matches("[\u0391-\uFFE5]")) {
                length += 2;
            } else {
                length++;
            }
        }
        StringBuilder result = new StringBuilder(todo);
        if (length < newLength) {
            for (int i = 0; i < newLength - length; i++) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}
