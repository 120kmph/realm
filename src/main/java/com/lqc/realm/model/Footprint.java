package com.lqc.realm.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.config.CommonConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Author: Glenn
 * Description: 足迹模型
 * Created: 2022/7/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Footprint {

    @TableId(type = IdType.INPUT)
    private String uid;           // 百度地图poi唯一标识 uid

    private String name;          // 名称
    private int type;             // 地点类型
    private int status;           // 状态 0=未知 1=已游玩 2=Togo

    private String province;      // 地址 所在 省份
    private String city;          // 地址 所在 城市

    private String lat;           // 地址 纬度
    private String lng;           // 地址 经度

    private String date;          // 旅行日期
    private int evaluateType;     // 游玩评价
    private String comment;       // 游玩评论/备注


    public String toString() {
        return this.addStrLength(this.name, 35) + " \t"
                + this.addStrLength(this.getArea(this.province, this.city), 10) + " \t"
                + (StrUtil.isBlank(this.date) ? "" : this.addStrLength(this.date, 10) + " \t")
                + this.addStrLength(CommonCacheConfig.getTypeDesc(1, this.type), 15) + " \t"
                + this.addStrLength(CommonCacheConfig.getTypeDesc(2, this.evaluateType), 10) + " \t"
                + (this.comment == null ? "" : this.comment);
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

    public String getArea(String province, String city) {
        if (CommonConfig.MAP_BIG_CITY.contains(province)) {
            return province;
        }
        return province + "-" + city.replaceFirst("市", "");
    }

}
