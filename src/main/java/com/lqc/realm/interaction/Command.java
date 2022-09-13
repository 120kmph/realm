package com.lqc.realm.interaction;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Author: Glenn
 * Description: 单次命令对象
 * Created: 2022/9/7
 */
@Data
@Accessors(chain = true)
public class Command {

    private Type type;
    private int level;            // 所在层级
    private int nextLevel;
    private int backLevel;

    private List<Need> needs;

    private String question;
    private List<Option> options;

    private String command;

    private List<Execution> executions;

    private List<String> supports;


    /**
     * 类型
     */
    public enum Type {

        none(0, "未知"),
        which(1, "选择 选项分支"),
        info(2, "填充信息"),
        which_exe(3, "选择 执行"),
        exe(4, "顺序执行"),
        command(5, "命令式");

        private final int code;
        private final String desc;

        Type(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        public static Type which(int code) {
            for (Type type : Type.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return Type.none;
        }
    }


    @Data
    @Accessors(chain = true)
    public static class Need {

        private String text;
        private String content;
    }

    @Data
    @Accessors(chain = true)
    public static class Option {

        private String text;
        private Integer nextLevel;

        private boolean isExecution;
        private String bean;
        private String method;

        public Option execute(String bean, String method) {
            this.isExecution = true;
            this.bean = bean;
            this.method = method;
            return this;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Execution {

        private String title;

        private List<Need> needs;

        private String bean;
        private String method;
    }
}
