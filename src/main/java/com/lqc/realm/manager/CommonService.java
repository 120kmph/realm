package com.lqc.realm.manager;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lqc.realm.interaction.Command;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description: 公共服务
 * Created: 2022/7/7
 */
@Lazy
@Service
public class CommonService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取当前命令的下一层命令序号
     */
    public int getNextLevel(List<Command.Option> options, String function) {
        try {
            if (Integer.parseInt(function) > options.size()) {
                return 0;
            }
            return options.get(Integer.parseInt(function) - 1).getNextLevel();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 执行方法
     */
    public int execute(List<Command.Option> options, String function, String... args) {
        try {
            if (Integer.parseInt(function) > options.size()) {
                return 0;
            }
            Command.Option option = options.get(Integer.parseInt(function) - 1);
            Object todo = applicationContext.getBean(option.getBean());
            String get = "0";
            if (args.length == 0) {
                get = ReflectUtil.invoke(todo, option.getMethod()).toString();
            }
            if (args.length == 1) {
                get = ReflectUtil.invoke(todo, option.getMethod(), args[0]).toString();
            }
            if (args.length == 2) {
                get = ReflectUtil.invoke(todo, option.getMethod(), args[0], args[1]).toString();
            }
            if (args.length == 3) {
                get = ReflectUtil.invoke(todo, option.getMethod(), args[0], args[1], args[2]).toString();
            }
            return Integer.parseInt(get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 执行方法
     */
    public int execute(String bean, String method, String... args) {
        try {
            Object todo = applicationContext.getBean(bean);
            String get = "0";
            if (args.length == 0) {
                get = ReflectUtil.invoke(todo, method).toString();
            }
            if (args.length == 1) {
                get = ReflectUtil.invoke(todo, method, args[0]).toString();
            }
            if (args.length == 2) {
                get = ReflectUtil.invoke(todo, method, args[0], args[1]).toString();
            }
            if (args.length == 3) {
                get = ReflectUtil.invoke(todo, method, args[0], args[1], args[2]).toString();
            }
            if (args.length == 4) {
                get = ReflectUtil.invoke(todo, method, args[0], args[1], args[2], args[3]).toString();
            }
            if (args.length == 5) {
                get = ReflectUtil.invoke(todo, method, args[0], args[1], args[2], args[3], args[4]).toString();
            }
            if (args.length == 6) {
                get = ReflectUtil.invoke(todo, method, args[0], args[1], args[2], args[3], args[4], args[5]).toString();
            }
            return Integer.parseInt(get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取命令式输入中的参数
     */
    public List<String> getCommandArgs(String function) {
        String[] split = function.split(" ");
        split[0] = "";
        return Arrays.stream(split).filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }
}
