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
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("Data Admin").setNextLevel(1),
                            new Command.Option().setText("To Go").setNextLevel(2),
                            new Command.Option().setText("To Eat").setNextLevel(3),
                            new Command.Option().setText("To Life").setNextLevel(4),
                            new Command.Option().setText("Anki").setNextLevel(5)))
                    .setCommand(choose_input).setBackLevel(-2))

            // 数据管理
            .addCommand(new Command().setLevel(1).setType(Command.Type.which)
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("Data Put In").setNextLevel(11),
                            new Command.Option().setText("Data Export").setNextLevel(12),
                            new Command.Option().setText("Type Management").setNextLevel(13)))
                    .setCommand(choose_input).setBackLevel(0))

            // 数据管理 - 导入数据
            .addCommand(new Command().setLevel(11).setType(Command.Type.which_exe)
                    .setNeeds(Collections.singletonList(new Command.Need().setText("From Where? ")))
                    .setQuestion("Put In Which? ")
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("AllType")
                                    .execute(ServiceType.data.service(), "putAllType").setNextLevel(1),
                            new Command.Option().setText("Footprint")
                                    .execute(ServiceType.data.service(), "putFootprint").setNextLevel(1),
                            new Command.Option().setText("Food")
                                    .execute(ServiceType.data.service(), "putFood").setNextLevel(1))))

            // 数据管理 - 导出数据
            .addCommand(new Command().setLevel(12).setType(Command.Type.which_exe)
                    .setQuestion("Export Which? ")
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("AllType")
                                    .execute(ServiceType.data.service(), "exportAllType").setNextLevel(1),
                            new Command.Option().setText("Footprint")
                                    .execute(ServiceType.data.service(), "exportFootprint").setNextLevel(1),
                            new Command.Option().setText("Food")
                                    .execute(ServiceType.data.service(), "exportFood").setNextLevel(1))))

            // 数据管理 - 类型管理
            .addCommand(new Command().setLevel(13).setType(Command.Type.exe)
                    .setExecutions(Arrays.asList(
                            new Command.Execution().setBean(ServiceType.all_type.service()).setMethod("showAll"),
                            new Command.Execution()
                                    .setNeeds(Arrays.asList(new Command.Need().setText("Update Which? "), new Command.Need().setText("New Info: ")))
                                    .setBean(ServiceType.all_type.service()).setMethod("update")))
                    .setNextLevel(13).setBackLevel(1))

            // 足迹
            .addCommand(new Command().setLevel(2).setType(Command.Type.which)
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("Footprint Put In").setNextLevel(21),
                            new Command.Option().setText("Search & Edit").setNextLevel(22),
                            new Command.Option().setText("Data Statistics").setNextLevel(23)))
                    .setCommand(choose_input))

            // 足迹 - 录入
            .addCommand(new Command().setLevel(21).setType(Command.Type.exe)
                    .setExecutions(Arrays.asList(
                            new Command.Execution()
                                    .setNeeds(Collections.singletonList(new Command.Need().setText("Footprint Keyword: ")))
                                    .setBean(ServiceType.foot_print.service()).setMethod("searchAndPrint"),
                            new Command.Execution()
                                    .setTitle("=== Complete Information ===")
                                    .setNeeds(Arrays.asList(
                                            new Command.Need().setText("which one? "),
                                            new Command.Need().setText("Time: "),
                                            new Command.Need().setText("Select the location type: "),
                                            new Command.Need().setText("Evaluate this location : "),
                                            new Command.Need().setText("Comment this location: ")
                                    ))
                                    .setBean(ServiceType.foot_print.service()).setMethod("add")))
                    .setNextLevel(21).setBackLevel(2))

            // 足迹 - 搜索
            .addCommand(new Command().setLevel(22).setType(Command.Type.exe)
                    .setExecutions(Arrays.asList(
                            new Command.Execution()
                                    .setTitle("=== Search ===")
                                    .setBean(ServiceType.foot_print.service()).setMethod("printProvince"),
                            new Command.Execution()
                                    .setTitle("=== Search Keyword ===")
                                    .setNeeds(Arrays.asList(
                                            new Command.Need().setText("Province: "),
                                            new Command.Need().setText("City: "),
                                            new Command.Need().setText("Name: "),
                                            new Command.Need().setText("Type: "),
                                            new Command.Need().setText("Evaluate: "),
                                            new Command.Need().setText("Time: ")))
                                    .setBean(ServiceType.foot_print.service()).setMethod("search")))
                    .setNextLevel(221).setBackLevel(2))

            // 足迹 - 数据统计
            .addCommand(new Command().setLevel(23).setType(Command.Type.exe)
                    .setExecutions(Collections.singletonList(
                            new Command.Execution().setBean(ServiceType.foot_print.service()).setMethod("data")))
                    .setNextLevel(2).setBackLevel(2))

            // 足迹 - 更新删除
            .addCommand(new Command().setLevel(221).setType(Command.Type.command)
                    .setQuestion("==== Data Change ====")
                    .setSupports(Collections.singletonList("delete"))
                    .setExecutions(Collections.singletonList(
                            new Command.Execution().setBean(ServiceType.foot_print.service()).setMethod("delete")))
                    .setNextLevel(221).setBackLevel(22))

            // 好吃的
            .addCommand(new Command().setLevel(3).setType(Command.Type.which)
                    .setOptions(Arrays.asList(
                            new Command.Option().setText("Food Put In").setNextLevel(31),
                            new Command.Option().setText("Search & Edit").setNextLevel(32),
                            new Command.Option().setText("Arbitrary door").setNextLevel(33)))
                    .setCommand(choose_input))

            // Anki
            .addCommand(new Command().setLevel(5).setType(Command.Type.command)
                    .setQuestion("==== Anki Operation ====")
                    .setSupports(Arrays.asList("sync", "new", "decks", "add", "find", "del", "del-tag", "move", "iter", "help"))
                    .setExecutions(Arrays.asList(
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("sync"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("newCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("getDeckName"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("addCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("searchCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("deleteCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("deleteTag"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("moveCard"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("iter"),
                            new Command.Execution().setBean(ServiceType.anki.service()).setMethod("help")
                    ))
                    .setNextLevel(5).setBackLevel(0))


            .build();


}
