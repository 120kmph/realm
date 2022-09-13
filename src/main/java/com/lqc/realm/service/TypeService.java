package com.lqc.realm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.config.CommonConfig;
import com.lqc.realm.mapper.AllTypeMapper;
import com.lqc.realm.model.AllType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Glenn
 * Description: 类型管理服务
 * Created: 2022/9/9
 */
@Service
public class TypeService {

    @Autowired
    private AllTypeMapper typeMapper;

    /**
     * 初始化时更新类型缓存
     */
    public void updateCache() {
        CommonCacheConfig.typeCache = this.getAllTypes();
    }

    /**
     * 获取所有index对应类型信息
     * return Map[index - 类型信息列表]
     */
    public Map<Integer, List<AllType>> getAllTypes() {
        Map<Integer, List<AllType>> result = new HashMap<>(8);
        int all = this.getCurrIndexMax();
        for (int index = 1; index <= all; index++) {
            List<AllType> allTypes = this.typeMapper.selectList(new QueryWrapper<AllType>().eq("index", index).orderByAsc("code"));
            result.put(index, allTypes);
        }
        return result;
    }

    /**
     * 获取当前现有index的最大值 即 有几种类型
     */
    public int getCurrIndexMax() {
        return CommonConfig.TYPE_INDEX_MAP.keySet().stream().max(Integer::compare).orElse(0);
    }

    /**
     * 显示全部类型信息
     */
    public int showAll() {
        Map<Integer, List<AllType>> allTypes = CommonCacheConfig.typeCache;
        for (Integer index : allTypes.keySet()) {
            List<AllType> types = allTypes.get(index);
            System.out.println(types.get(0).getIndexName() + ":  " + CommonCacheConfig.toShow(types));
        }
        return 1;
    }

    /**
     * 更新类型
     */
    public int update(String which, String newInfo) {
        if (this.updateByIndex(Integer.parseInt(which), newInfo)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 更新类型信息
     */
    private boolean updateByIndex(int index, String newInfo) {
        int delete = this.typeMapper.delete(new QueryWrapper<AllType>().eq("index", index));
        if (delete < 1) {
            return false;
        }
        int counter = 0;
        String[] toSave = newInfo.split(" ");
        for (String save : toSave) {
            String code = save.split("=")[0];
            String desc = save.split("=")[1];
            int insert = this.typeMapper.insert(new AllType().setIndex(index)
                    .setIndexName(CommonConfig.TYPE_INDEX_MAP.get(index))
                    .setCode(Integer.parseInt(code)).setDesc(desc));
            counter = insert == 1 ? counter + 1 : counter;
        }
        // 更新缓存
        CommonCacheConfig.typeCache = this.getAllTypes();
        return counter == toSave.length;
    }

}
