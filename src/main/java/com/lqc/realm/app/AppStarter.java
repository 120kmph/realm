package com.lqc.realm.app;

import cn.hutool.core.util.StrUtil;
import com.lqc.realm.exception.GoBack;
import com.lqc.realm.exception.ReEnter;
import com.lqc.realm.interaction.Command;
import com.lqc.realm.interaction.CommandConfig;
import com.lqc.realm.interaction.InteractionConfig;
import com.lqc.realm.manager.CommonService;
import com.lqc.realm.manager.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Glenn
 * Description: 程序启动入口
 * Created: 2022/7/28
 */
@Service
public class AppStarter {

    @Autowired
    private ReaderService reader;

    @Autowired
    private CommonService commonService;

    private static final InteractionConfig config = CommandConfig.config;

    /**
     * 启动程序
     */
    public void start() {

        int currLevel = 0;
        while (true) {
            // 程序终止
            if (currLevel == -2) {
                System.out.println("----------- bye -----------");
                System.exit(0);
            }
            Command command = config.getCommand(currLevel);
            try {
                // 当前命令
                Command.Type type = command.getType();
                // 选项式 分支
                if (type == Command.Type.which) {

                    boolean inputStatus = true;
                    while (inputStatus) {
                        List<Command.Option> options = command.getOptions();
                        for (Command.Option option : options) {
                            System.out.print("【" + (options.indexOf(option) + 1) + "】" + option.getText() + "  ");
                        }
                        System.out.println();
                        System.out.print(command.getCommand());
                        // 选择选项
                        String function = reader.getString();
                        currLevel = commonService.getNextLevel(options, function);
                        // 输入有误
                        if (currLevel != 0) {
                            inputStatus = false;
                        } else {
                            System.out.println(CommandConfig.wrong_input);
                        }
                    }
                    System.out.println();
                }
                // 选项式 执行
                if (type == Command.Type.which_exe) {

                    List<String> args = new ArrayList<>();
                    // need
                    List<Command.Need> needs = command.getNeeds();
                    if (needs != null && needs.size() > 0) {
                        for (Command.Need need : needs) {
                            System.out.print(need.getText());
                            String content = reader.getString();
                            args.add(content);
                        }
                    }
                    // 选择
                    System.out.print(command.getQuestion());
                    List<Command.Option> options = command.getOptions();
                    options.forEach(option -> System.out.print(options.indexOf(option) + 1 + "-" + option.getText() + "  "));
                    String function = reader.getString();
                    int result = commonService.execute(options, function, args.toArray(new String[0]));
                    if (result != 0) {
                        System.out.println(CommandConfig.execute_success);
                        currLevel = options.get(Integer.parseInt(function) - 1).getNextLevel();
                    } else {
                        System.out.println(CommandConfig.wrong_input);
                    }
                    System.out.println();
                }
                // 执行
                if (type == Command.Type.exe) {

                    List<Command.Execution> executions = command.getExecutions();
                    for (Command.Execution execution : executions) {

                        String title = execution.getTitle();
                        if (StrUtil.isNotEmpty(title)) {
                            System.out.println(title);
                        }

                        List<Command.Need> needs = execution.getNeeds();
                        List<String> args = new ArrayList<>();
                        if (needs != null) {
                            for (Command.Need need : needs) {
                                System.out.print(need.getText());
                                String input = reader.getLine();
                                args.add(input);
                            }
                        }
                        int execute = commonService.execute(execution.getBean(), execution.getMethod(), args.toArray(new String[0]));
                        if (execute == 1) {
                            System.out.println(CommandConfig.execute_success);
                        } else if (execute == 0) {
                            System.out.println(CommandConfig.execute_error);
                        }
                        System.out.println();
                    }
                    currLevel = command.getNextLevel();
                }

                // 命令式
                if (type == Command.Type.command) {
                    String question = command.getQuestion();
                    if (StrUtil.isNotEmpty(question)) {
                        System.out.println(question);
                    }
                    List<String> supports = command.getSupports();
                    System.out.print("Support: ");
                    for (String support : supports) {
                        System.out.print(support + "  ");
                    }
                    System.out.println();
                    System.out.print("> ");
                    String function = reader.getLine();
                    if (supports.contains(function.split(" ")[0])) {
                        int index = supports.indexOf(function.split(" ")[0]);
                        Command.Execution execution = command.getExecutions().get(index);
                        List<String> args = commonService.getCommandArgs(function);
                        int execute = commonService.execute(execution.getBean(), execution.getMethod(), args.toArray(new String[0]));
                        if (execute == 1) {
                            System.out.println(CommandConfig.execute_success);
                        } else {
                            System.out.println(CommandConfig.execute_error);
                        }
                        currLevel = command.getNextLevel();
                    } else {
                        System.out.println(CommandConfig.wrong_input);
                    }
                    System.out.println();
                }

            } catch (ReEnter ignored) {
                System.out.println();
            } catch (GoBack goBack) {
                currLevel = command.getBackLevel();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
