package com.lqc.realm.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.lqc.realm.config.CommonCacheConfig;
import com.lqc.realm.manager.AnkiConnectService;
import com.lqc.realm.manager.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description: Anki服务
 * Created: 2022/9/12
 */
@Service
public class AnkiService {

    @Autowired
    private AnkiConnectService connector;

    /**
     * Anki更新
     */
    public int sync() {
        boolean isDone = this.connector.sync();
        return isDone ? 1 : 0;
    }

    /**
     * 批量添加新卡片到牌组
     */
    public int newCard() {
        String title = "";
        // 读取文件
        ClassPathResource classPathResource = new ClassPathResource("");
        String from = classPathResource.getAbsolutePath();
        from = from.substring(0, from.length() - 44) + "anki_from.txt";
        FileReader reader = new FileReader(from);
        int index = 1, done = 0, error = 0, counter = 0;
        String front = "";
        StringBuilder back = new StringBuilder();
        List<String> lines = reader.readLines();
        // 行遍历
        for (String line : lines) {
            if (StrUtil.isBlank(line)) {
                back.append("<br/>");
                index++;
                continue;
            }
            // 标题
            if ("%".equals(line.substring(0, 1))) {
                title = line.substring(1);
                continue;
            }
            // 单张卡片读取结束 存入anki
            if ("#".equals(line)) {
                boolean result = connector.saveCard("Todo::New", front, back.toString());
                if (result) {
                    done++;
                } else {
                    error++;
                }
                index = 1;
                counter++;
                back = new StringBuilder();
                continue;
            }
            if (index == 1) {
                if (StrUtil.isBlank(title)) {
                    front = line;
                } else {
                    front = title + " - " + line;
                    if (line.length() > 8) {
                        front = title + "<br/>" + line;
                    }
                }
                index++;
                continue;
            }
            back.append(line).append("<br/>");
            index++;
        }
        return counter == done ? 1 : 0;
    }

    /**
     * 获取外部配置中的牌组别名
     */
    public int getDeckName() {
        Map<String, String> map = CommonCacheConfig.config_map.get("anki-deck-name");
        for (String key : map.keySet()) {
            Console.log("{} = {}", key, map.get(key));
        }
        return 1;
    }

    /**
     * 添加单张卡片(含图片)
     */
    public int addCard(String deck, String front, String back) {
        String deckName = CommonCacheConfig.getConfig("anki-deck-name", deck);
        String from = CommonCacheConfig.getConfig("path", "anki-pic-from");
        String to = CommonCacheConfig.getConfig("path", "anki-pic-to");
        List<File> files = FileUtil.loopFiles(new File(from));
        for (File file : files) {
            if (front.contains("&&&")) {
                front = front.replaceFirst("&&&", "<img src=\"" + file.getName() + "\">");
            } else {
                if (back.contains("&&&")) {
                    back = back.replaceFirst("&&&", "<img src=\"" + file.getName() + "\">");
                }
            }
            FileUtil.copy(file, new File(to), false);
            FileUtil.del(file);
        }
        boolean isDone = this.connector.saveCard(deckName, front, back);
        return isDone ? 1 : 0;
    }

    /**
     * 搜索卡片
     */
    public int searchCard(String deck, String keyword) {
        String deckName = CommonCacheConfig.getConfig("anki-deck-name", deck);
        keyword = keyword.replaceAll("&", " ");
        List<Long> uids = this.connector.search(deckName, keyword);
        List<JSONObject> cards = this.connector.getCardsContent(uids);
        int index = 1;
        for (JSONObject card : cards) {
            Console.log("{} - {}", index++, card.get("正面"));
            Console.log("    {}", card.get("背面"));
            System.out.println();
        }
        return 1;
    }

    /**
     * 删除卡片
     */
    public int deleteCard(String deck, String keyword, String index) {
        String deckName = CommonCacheConfig.getConfig("anki-deck-name", deck);
        keyword = keyword.replaceAll("&", " ");
        List<Long> uids = this.connector.search(deckName, keyword);
        List<JSONObject> cards = this.connector.getCardsContent(uids);
        String[] indexes = index.split(",");
        List<Long> toDelete = Arrays.stream(indexes).map(todo -> uids.get(Integer.parseInt(todo) - 1)).collect(Collectors.toList());
        boolean isDone = this.connector.deleteCards(toDelete);
        return isDone ? 1 : 0;
    }

    /**
     * 删除tag标记为del的卡片
     */
    public int deleteTag() {
        List<Long> toDel = this.connector.searchByTag("del");
        Console.log("There are {} cards have found.", toDel.size());
        boolean isDone = this.connector.deleteCards(toDel);
        return isDone ? 1 : 0;
    }

    /**
     * 将标记有move的卡片移动到指定牌组
     */
    public int moveCard(String to) {
        List<Long> toMove = this.connector.searchByTag("move");
        Console.log("There are {} cards have found.", toMove.size());
        String deckName = CommonCacheConfig.getConfig("anki-deck-name", to);
        boolean isDone = this.connector.moveCards(toMove, deckName);
        return isDone ? 1 : 0;
    }

    @Autowired
    private ReaderService reader;

    /**
     * iter
     */
    public int iter() {
        while (true) {
            try {
                String where = CommonCacheConfig.getConfig("anki", "iter");
                List<Long> search = connector.search(where);
                Long uid = search.get(0);
                List<JSONObject> content = this.connector.getCardsContent(Collections.singletonList(uid));
                JSONObject object = content.get(0);
                System.out.println(object.get("正面"));
                System.out.println(object.get("背面"));
                System.out.print("> ");
                String line = reader.getLine();
                if (line.contains("del")) {
                    this.connector.deleteCards(Collections.singletonList(uid));
                }
                if (line.contains("to")) {
                    String to = line.split(" ")[1];
                    String deckName = CommonCacheConfig.getConfig("anki-deck-name", to);
                    this.connector.moveCards(Collections.singletonList(uid), deckName);
                }
                if (line.contains("new")) {
                    String deckName = line.split(" ")[1];
                    this.connector.createDeck(deckName);
                }
            } catch (Exception e) {
                return 1;
            }
        }
    }

    /**
     * 指令帮助
     */
    public int help() {
        System.out.println("sync : Anki更新");
        System.out.println("new : 批量从txt写入卡片到anki");
        System.out.println("decks : 查看外部配置的牌组名及其简称");
        System.out.println("add : add deckName &&& &&&  向卡组中添加含有图片的卡片");
        System.out.println("find : find deckName key&key 在牌组中查找卡片");
        System.out.println("del : del deckName key&key 1,2,3 删除搜索结果中的卡片");
        System.out.println("del-tag : 删除标记有del的卡片");
        System.out.println("move-to : move-to deckName 将标记有move的卡片移动到指定牌组");
        System.out.println("iter : 在外部配置中指定的牌组中遍历操作卡片");
        return 1;
    }

}