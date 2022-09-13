package com.lqc.realm.interaction;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Glenn
 * Description: 终端交互配置模型
 * Created: 2022/9/7
 */
@Data
@Accessors(chain = true)
public class InteractionConfig {

    private List<Command> commands;

    /**
     * 配置构建
     */
    public InteractionConfig(Builder builder) {
        this.commands = builder.commands;
    }

    /**
     * 命令构建
     */
    public static class Builder {

        private List<Command> commands;

        public InteractionConfig build() {
            return new InteractionConfig(this);
        }

        /**
         * 添加命令
         */
        public Builder addCommand(Command command) {
            if (commands == null) {
                commands = new ArrayList<>();
            }
            commands.add(command);
            return this;
        }
    }

    public Command getCommand(int level) {
        return this.commands.stream().filter(commands -> commands.getLevel() == level).limit(1).findFirst().orElse(new Command());
    }
}
