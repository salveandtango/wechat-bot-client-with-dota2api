package cn.yangself.wechatBotClient.service.dota2;

import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.util.*;

/**
 * @Description Dota2战报
 * @Author Fixed
 * @Date 2020/11/17 14:39
 */
@Component
@Slf4j
public class BattleReport {

    private HttpRequestUtil httpRequestUtil;

    @Autowired
    public BattleReport(HttpRequestUtil httpRequestUtil) {
        this.httpRequestUtil = httpRequestUtil;
    }

    /**
     * steam web key
     */
    private final static String KEY = "BC0E8C660BB3EAB9F6E7AC1FDF0F0C92";
    /**
     * 比赛详情查询接口
     */
    private final static String GET_MATCH_DETAIL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v1/";
    /**
     * 比赛历史查询接口
     */
    private final static String GET_MATCH_HISTORY = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v1/";
    /**
     * 英雄查询接口
     */
    private final static String GET_ALL_HEROS = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v1/";
    /**
     * 阵营划分界限, 4以下为天辉, 以上为夜魇
     */
    private final static Integer PLAYER_SLOT = 4;

    //天辉总伤害量
    int radiantTotalDamage = 0;
    //天辉总击杀
    int radiantTotalKills = 0;
    //夜魇总伤害量
    int direTotalDamage = 0;
    //夜魇总击杀
    int direTotalKills = 0;
    //参战率
    int participationRate = 0;
    //参葬率
    int mortalityRate = 0;

    public String getReport(String accountId) {
        JSONObject lastMatchDetail = getLastMatchDetail(accountId);
        //一场比赛中的玩家游戏详情
        JSONArray players = lastMatchDetail.getJSONObject("result").getJSONArray("players");
        for (Object player : players) {
            JSONObject matchDetail = (JSONObject) player;
            String damage = matchDetail.getString("hero_damage");
            String player_slot = matchDetail.getString("player_slot");
            String kills = matchDetail.getString("kills");
            if (Integer.parseInt(player_slot) < PLAYER_SLOT) {
                //天辉
                radiantTotalDamage += Integer.parseInt(damage);
                radiantTotalKills += Integer.parseInt(kills);
            } else {
                //夜魇
                direTotalDamage += Integer.parseInt(damage);
                direTotalKills += Integer.parseInt(kills);
            }

            String account_id = matchDetail.getString("account_id");
            if (account_id.equals(accountId)) {
                String hero_id = matchDetail.getString("hero_id");

                /*String player_slot = matchDetail.getString("player_slot");
                String kills = matchDetail.getString("kills");
                String deaths = matchDetail.getString("deaths");
                String assists = matchDetail.getString("assists");
                String hero_damage = matchDetail.getString("hero_damage");
                String tower_damage = matchDetail.getString("tower_damage");
                String hero_healing = matchDetail.getString("hero_healing");
                String last_hits = matchDetail.getString("last_hits");
                String denies = matchDetail.getString("denies");
                String gold_per_min = matchDetail.getString("gold_per_min");
                String xp_per_min = matchDetail.getString("xp_per_min");*/
            }
        }
        return null;
    }

    private JSONObject getLastMatchDetail(String accountId) {
        //组装参数
        Map<String, String> param = new HashMap<>();
        param.put("account_id", accountId);
        param.put("key", KEY);
        JSONObject matchHistory = httpRequestUtil.sendGet(GET_MATCH_HISTORY, param);
        JSONArray matchArray = (JSONArray) matchHistory.get("matches");
        JSONObject lastMatch = (JSONObject) matchArray.get(0);
        String matchId = (String) lastMatch.get("match_id");
        Map<String, String> secondParam = new HashMap<>();
        secondParam.put("match_id", matchId);
        secondParam.put("key", KEY);
        return httpRequestUtil.sendGet(GET_MATCH_DETAIL, secondParam);
    }
}
