package com.lqc.realm.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.exception.GoBack;
import com.lqc.realm.exception.ReEnter;
import com.lqc.realm.manager.ReaderService;
import com.lqc.realm.mapper.FoodMapper;
import com.lqc.realm.model.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Glenn
 * Description: 食谱服务
 * Created: 2022/08/05
 */
@Service
public class FoodService {

    @Autowired
    private ReaderService reader;

    @Autowired
    private FoodMapper foodMapper;


    /* ------------------------- 交互 ----------------------------- */

    /**
     * 1 食谱录入
     */
    public boolean set() {
        while (true) {
            try {
                // 信息输入
                System.out.println("=== Put In Information ===");
                System.out.print("Name: ");
                String name = reader.getString();

                System.out.println("[" + CommonCacheConfig.toShow(3) + "]");
                System.out.print("Select types, separate by (,) : ");
                String type = reader.getString();
                StringBuilder typeString = new StringBuilder();
                String[] types = type.split(",");
                for (String single : types) {
                    typeString.append(CommonCacheConfig.getTypeDesc(3, Integer.parseInt(single)));
                    typeString.append(",");
                }

                System.out.print("Material: ");
                String material = reader.getLine();

                System.out.println("Steps: ");
                List<String> steps = new ArrayList<>();
                String step = "";
                while (!"#".equals(step)) {
                    step = reader.getString();
                    if (StrUtil.isNotBlank(step) && !"#".equals(step)) {
                        steps.add(step);
                    }
                }

                System.out.print("Add Comment: ");
                String comment = reader.getLine();

                // 信息填充
                Food Food = new Food()
                        .setName(name)
                        .setTypes(typeString.substring(0, typeString.length() - 1))
                        .setMaterial(material)
                        .setSteps(JSONUtil.toJsonStr(steps))
                        .setComment(comment);
                int insert = this.foodMapper.insert(Food);
                if (insert == 1) {
                    System.out.println(">>>>>> Save Success <<<<<<");
                } else {
                    System.out.println(">>>>>> Save Error <<<<<<");
                }
                System.out.println();
            } catch (ReEnter e) {
                System.out.println();
                System.out.println("==== ReEntry ====");
            } catch (GoBack e) {
                System.out.println();
                return true;
            }
        }
    }


    /**
     * 2 检索/编辑/删除
     */
    public boolean update() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== Search ===");
                // 搜索条件信息 - 类型
                System.out.println();
                System.out.println("Type: " + CommonCacheConfig.toShow(3));
                System.out.println();

                // 模糊检索
                System.out.print("KeyWord: ");
                String keyWord = reader.getLine();

                System.out.print("Type: ");
                String inType = reader.getLine();
                System.out.println();

                List<Food> foods = this.getFoods(keyWord, inType);
                System.out.println("# # # # # # # # # Query result # # # # # # # # # #");
                int foodIndex = 1;
                for (Food Food : foods) {
                    System.out.println(foodIndex++ + " - " + Food.shortInfo());
                }
                System.out.println("# # # # # # # # # # # # # # # # # # # # # # # # # #");
                System.out.println();

                // 数据操作
                int function = 0;
                while (function != 2) {
                    System.out.println("==== Data Change ====");
                    System.out.print("1=Details 2=ReSearch 3=Edit 4=DeleteBatch : ");
                    function = reader.getInt();
                    // 详细信息
                    if (function == 1) {
                        System.out.println("Which one ? : ");
                        String detailIndex = reader.getString();
                        int index = Integer.parseInt(detailIndex);
                        if (index > 0 && index <= foods.size()) {
                            Food food = foods.get(index);
                            System.out.println(food.details());
                        }
                    }
                    // 更新数据
                    if (function == 3) {
                        System.out.print("Update Which ? ");
                        int updateIndex = reader.getInt();
                        if (updateIndex > 0 && updateIndex <= foods.size()) {
                            Food toUpdate = foods.get(updateIndex - 1);

                            System.out.println("=== Update Information ===");
                            System.out.print("Name: ");
                            String name = reader.getString();

                            System.out.println("[" + CommonCacheConfig.toShow(3) + "]");
                            System.out.print("Select types, separate by (,) : ");
                            String type = reader.getString();

                            System.out.print("Material: ");
                            String material = reader.getString();

                            System.out.print("Add Comment: ");
                            String comment = reader.getString();

                            if (StrUtil.isNotBlank(name)) toUpdate.setName(name);
                            if (StrUtil.isNotBlank(type)) toUpdate.setTypes(type);
                            if (StrUtil.isNotBlank(material)) toUpdate.setMaterial(material);
                            if (StrUtil.isNotBlank(comment)) toUpdate.setComment(comment);

                            int update = this.foodMapper.updateById(toUpdate);
                            if (update == 1) {
                                System.out.println(">>>>>> Update Success <<<<<<");
                            } else {
                                System.out.println(">>>>>> Update Error <<<<<<");
                            }
                        } else {
                            System.out.println("Put In Wrong Index !");
                            System.out.println();
                            continue;
                        }
                    }
                    // 批量删除
                    if (function == 4) {
                        System.out.print("Delete Which ? Separate By (,) : ");
                        String deleteIndex = reader.getString();
                        String[] split = deleteIndex.split(",");
                        List<Integer> deletes = new ArrayList<>();
                        List<Food> newFoods = foods;
                        Arrays.stream(split).forEach(curr -> {
                            int currIndex = Integer.parseInt(curr);
                            if (currIndex > 0 && currIndex <= newFoods.size()) {
                                deletes.add(newFoods.get(currIndex - 1).getId());
                            }
                        });
                        int delete = this.foodMapper.deleteBatchIds(deletes);
                        if (delete == deletes.size()) {
                            System.out.println(">>>>>> Delete Success <<<<<<");
                        } else {
                            System.out.println(">>>>>> Delete Error <<<<<<");
                        }
                    }
                    System.out.println();
                    // 获取更新后数据
                    if (function != 2) {
                        foods = this.getFoods(keyWord, inType);
                        System.out.println("# # # # # # # # New Query result # # # # # # # # #");
                        int newFoodIndex = 1;
                        for (Food Food : foods) {
                            System.out.println(newFoodIndex++ + " - " + Food.shortInfo());
                        }
                        System.out.println("# # # # # # # # # # # # # # # # # # # # # # # # # #");
                        System.out.println();
                    }
                }
            } catch (ReEnter e) {
                System.out.println();
            } catch (GoBack e) {
                System.out.println();
                return true;
            }
        }
    }

    /**
     * 3 菜单任意门
     */
    public boolean random() {
        while (true) {
            try {
                System.out.println("=== Arbitrary door ===");
                System.out.println("① Home Cooking");
                System.out.println("② All types");
                int index = reader.getIntPlus();
                System.out.println();
                // 家常菜
                if (index == 1) {
                }
                // 随机吃食
                if (index == 2) {

                }
            } catch (ReEnter e) {
                System.out.println();
            } catch (GoBack e) {
                System.out.println();
                return true;
            }
        }
    }



    /* ------------------------- Service ----------------------------- */

    /**
     * 检索 - 模糊查询列表
     */
    private List<Food> getFoods(String keyword, String inType) {
        QueryWrapper<Food> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like("name", keyword)
                    .or()
                    .like("material", keyword)
                    .like("comment", keyword);
        }
        if (StrUtil.isNotBlank(inType)) {
            wrapper.like("types", CommonCacheConfig.getTypeDesc(3, Integer.parseInt(inType)));
        }
        return this.foodMapper.selectList(wrapper);
    }

}
