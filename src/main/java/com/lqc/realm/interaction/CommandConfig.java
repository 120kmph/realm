package com.lqc.realm.interaction;

import com.lqc.realm.utils.ServiceType;

import java.util.Arrays;
import java.util.Collections;

/**
 * Author: Glenn
 * Description: 终端交互命令配置
 * Created: 2022/9/7
 */
public class CommandConfig {

    /* ==================================  常用文案 =====================================  */

    public static final String choose_input = "Choose the feature you need : ";

    public static final String wrong_input = ">>> Input Wrong <<<";

    public static final String wrong_input_re = "Input Wrong, Re : ";

    public static final String execute_success = ">>>>>> Execute Success <<<<<<";

    public static final String execute_error = ">>>>>> Execute Error <<<<<<";

    /**
     * 命令配置
     */
    public static final InteractionConfig config = new InteractionConfig.Builder()

            // 一级
            .addCommand(new Command().setLevel(0).setType(Command.Type.which)
                    .setOptions(Collections.singletonList(
                            new Command.Option().setText("Anki").setNextLevel(1)))
                    .setCommand(choose_input).setBackLevel(-2))

            // Anki
            .addCommand(new Command().setLevel(1).setType(Command.Type.command)
                    .setQuestion("==== Anki Operation ====")
                    .setSupports(Arrays.asList("sync", "new", "decks", "add", "find", "del", "del-tag", "move", "setRe", "setNew", "iter", "count", "help"))
                    .setExecutions(Arrays.asList(
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("sync"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("newCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("getDeckName"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("addCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("searchCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("deleteCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("deleteTag"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("moveCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("setReview"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("setNew"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("iter"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("count"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("help")
                    ))
                    .setNextLevel(1).setBackLevel(0))

            .build();


}
