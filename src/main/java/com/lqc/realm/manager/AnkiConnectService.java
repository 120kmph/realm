package com.lqc.realm.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Glenn
 * Description: Anki 数据访问服务
 * Created: 2022/9/12
 */
@Service
public class AnkiConnectService {

    /**
     * Anki-Connect 地址及端口
     */
    private final String location = "localhost:8765";

    /**
     * 获取所有牌组的名字
     */
    public List<String> getAllDecks() {
        JSONObject toSend = JSONUtil.createObj().set("action", "deckNames").set("version", 6);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return JSONUtil.parseArray(JSONUtil.parseObj(response).getStr("result")).toList(String.class);
    }

    /**
     * 创建牌组
     */
    private boolean createDeck(String deckName) {
        JSONObject params = JSONUtil.createObj().set("deck", deckName);
        JSONObject toSend = JSONUtil.createObj().set("action", "createDeck").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return StrUtil.isNotBlank(JSONUtil.parseObj(response).getStr("result"));
    }

    /**
     * 移动卡片到牌组
     */
    private boolean moveCards(List<Long> cards, String toDeckName) {
        JSONObject params = JSONUtil.createObj().set("cards", cards).set("deck", toDeckName);
        JSONObject toSend = JSONUtil.createObj().set("action", "changeDeck").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return StrUtil.isBlank(JSONUtil.parseObj(response).getStr("result")) && StrUtil.isBlank(JSONUtil.parseObj(response).getStr("error"));
    }

    /**
     * 搜索卡片
     */
    public List<Long> search(String deckName) {
        JSONObject params = JSONUtil.createObj().set("query", "deck:" + deckName);
        JSONObject toSend = JSONUtil.createObj().set("action", "findCards").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return JSONUtil.parseArray(JSONUtil.parseObj(response).getStr("result")).toList(Long.class);
    }

    /**
     * 搜索卡片
     */
    public List<Long> search(String deckName, String keyword) {
        JSONObject params = JSONUtil.createObj().set("query", "deck:" + deckName + " " + keyword);
        JSONObject toSend = JSONUtil.createObj().set("action", "findCards").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return JSONUtil.parseArray(JSONUtil.parseObj(response).getStr("result")).toList(Long.class);
    }

    /**
     * 获取卡片正面内容(问题)
     * {"正面":"","背面":""}
     */
    public List<JSONObject> getCardsContent(List<Long> uids) {
        JSONObject params = JSONUtil.createObj().set("cards", uids);
        JSONObject toSend = JSONUtil.createObj().set("action", "cardsInfo").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        List<String> result = JSONUtil.parseArray(JSONUtil.parseObj(response).getStr("result")).toList(String.class);
        return result.stream().map(card -> {
            JSONObject fields = JSONUtil.parseObj(card).getJSONObject("fields");
            return JSONUtil.createObj()
                    .set("正面", fields.getJSONObject("正面").getStr("value"))
                    .set("背面", fields.getJSONObject("背面").getStr("value"));
        }).collect(Collectors.toList());
    }

    /**
     * 添加卡片
     */
    public boolean saveCard(String deckName, String front, String back) {
        if (StrUtil.isBlank(front) || StrUtil.isBlank(back)) {
            return false;
        }
        // 选项
        JSONObject duplicateScopeOptions = JSONUtil.createObj().set("deckName", "New").set("checkChildren", false).set("checkAllModels", false);
        JSONObject options = JSONUtil.createObj().set("allowDuplicate", false).set("duplicateScope", "deck").set("duplicateScopeOptions", duplicateScopeOptions);
        // 内容
        JSONObject fields = JSONUtil.createObj().set("正面", front).set("背面", back);
        // 卡片信息
        JSONObject note = JSONUtil.createObj().set("deckName", deckName).set("modelName", "Basic").set("fields", fields).set("options", options).set("tags", new ArrayList<>());
        // 操作
        JSONObject params = JSONUtil.createObj().set("note", note);
        JSONObject toSave = JSONUtil.createObj().set("action", "addNote").set("version", 6).set("params", params);
        // 请求
        String response = HttpRequest.post(location).body(toSave.toString()).execute().body();
        String result = JSONUtil.parseObj(response).getStr("result");
        return StrUtil.isNotBlank(result) && !"null".equals(result);
    }

    /**
     * 删除卡片
     */
    public boolean deleteCards(List<Long> cards) {
        JSONObject params = JSONUtil.createObj().set("notes", this.getNoteId(cards));
        JSONObject toSend = JSONUtil.createObj().set("action", "deleteNotes").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        return StrUtil.isBlank(JSONUtil.parseObj(response).getStr("result")) && StrUtil.isBlank(JSONUtil.parseObj(response).getStr("error"));
    }

    /**
     * 获取卡片id对应noteId
     */
    private List<Long> getNoteId(List<Long> cards) {
        JSONObject params = JSONUtil.createObj().set("cards", cards);
        JSONObject toSend = JSONUtil.createObj().set("action", "cardsInfo").set("version", 6).set("params", params);
        String response = HttpRequest.post(location).body(toSend.toString()).execute().body();
        List<String> result = JSONUtil.parseArray(JSONUtil.parseObj(response).getStr("result")).toList(String.class);
        return result.stream().map(card -> JSONUtil.parseObj(card).getLong("note")).collect(Collectors.toList());
    }
}
