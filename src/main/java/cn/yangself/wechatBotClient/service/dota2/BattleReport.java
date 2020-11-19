package cn.yangself.wechatBotClient.service.dota2;

import cn.yangself.wechatBotClient.dto.DotaHeros;
import cn.yangself.wechatBotClient.dto.PlayerMatchDetail;
import cn.yangself.wechatBotClient.utils.DateUtil;
import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description Dota2战报
 * @Author Fixed
 * @Date 2020/11/17 14:39
 */
@Component
@Slf4j
public class BattleReport {

    private HttpRequestUtil httpRequestUtil;
    private DateUtil dateUtil;

    @Autowired
    public BattleReport(HttpRequestUtil httpRequestUtil, DateUtil dateUtil) {
        this.httpRequestUtil = httpRequestUtil;
        this.dateUtil = dateUtil;
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
    private final static String RADIANT = "天辉";
    private final static String DIRE = "夜魇";


    /**
     * 通过accountId获取战报
     * @param accountId dota2数字id
     * @return 战报
     */
    public String getReport(String accountId) {
        //天辉总伤害量
        int radiantTotalDamage = 0;
        //天辉总击杀
        int radiantTotalKills = 0;
        //夜魇总伤害量
        int direTotalDamage = 0;
        //夜魇总击杀
        int direTotalKills = 0;
        //参战率
        BigDecimal participationRate;
        //参葬率
        BigDecimal mortalityRate;

        PlayerMatchDetail pmd = null;
        JSONObject lastMatchDetail = getLastMatchDetail(accountId).getJSONObject("result");
        //游戏花费时长
        int duration = Integer.parseInt(lastMatchDetail.getString("duration"));

        //一场比赛中的所有玩家游戏详情
        JSONArray players = lastMatchDetail.getJSONArray("players");
        for (int i = 0; i < players.size(); i++) {
            JSONObject onePlayerDetail = players.getJSONObject(i);
            String damage = onePlayerDetail.getString("hero_damage");
            String player_slot = onePlayerDetail.getString("player_slot");
            String kills = onePlayerDetail.getString("kills");
            if (Integer.parseInt(player_slot) < PLAYER_SLOT) {
                //天辉
                radiantTotalDamage += Integer.parseInt(damage);
                radiantTotalKills += Integer.parseInt(kills);
            } else {
                //夜魇
                direTotalDamage += Integer.parseInt(damage);
                direTotalKills += Integer.parseInt(kills);
            }
            String account_id = onePlayerDetail.getString("account_id");

            //提取对应玩家的对局详情
            if (account_id.equals(accountId)) {
                pmd = generator(onePlayerDetail);
            }
        }
        if (pmd != null) {
            pmd.setSpendTime(dateUtil.secToTime(duration));
            String playerSlot = pmd.getPlayer_slot();
            BigDecimal totalKills;
            BigDecimal totalDeaths;
            BigDecimal totalDamage;
            switch (playerSlot) {
                case RADIANT:
                    totalKills = new BigDecimal(radiantTotalKills);
                    totalDeaths = new BigDecimal(direTotalKills);
                    totalDamage = new BigDecimal(radiantTotalDamage);
                    break;
                case DIRE:
                    totalKills = new BigDecimal(direTotalKills);
                    totalDeaths = new BigDecimal(radiantTotalKills);
                    totalDamage = new BigDecimal(direTotalDamage);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + playerSlot);
            }
            //参战率计算
            BigDecimal fightNum = new BigDecimal(pmd.getKills()).add(new BigDecimal(pmd.getAssists()));
            participationRate = fightNum.divide(totalKills, 4, BigDecimal.ROUND_HALF_UP);
            //参葬率计算
            BigDecimal deathNum = new BigDecimal(pmd.getDeaths());
            mortalityRate = deathNum.divide(totalDeaths, 4, BigDecimal.ROUND_HALF_UP);
            NumberFormat percent = NumberFormat.getPercentInstance();
            percent.setMaximumFractionDigits(2);
            pmd.setParticipationRate(percent.format(participationRate.doubleValue()));
            pmd.setMortalityRate(percent.format(mortalityRate.doubleValue()));
            //输出率计算
            BigDecimal outputRate = new BigDecimal(pmd.getHeroDamage()).divide(totalDamage, 4, BigDecimal.ROUND_HALF_UP);
            pmd.setOutputRate(percent.format(outputRate.doubleValue()));
            //输出经济比计算
            BigDecimal totalMoney = new BigDecimal((duration / 60) * Integer.parseInt(pmd.getGoldPerMin()));
            BigDecimal outputEconomyRatio = new BigDecimal(pmd.getHeroDamage()).divide(totalMoney,2,BigDecimal.ROUND_HALF_UP);
            pmd.setOutputEconomyRatio(outputEconomyRatio.toString());
            return pmd.toString();
        }
        return null;
    }

    /**
     * 生成某个玩家的对局详情
     *
     * @param onePlayerDetail JSONObject格式的对局详情
     * @return PlayerMatchDetail
     */
    private PlayerMatchDetail generator(JSONObject onePlayerDetail) {
        PlayerMatchDetail pmd = new PlayerMatchDetail();
        String hero_id = onePlayerDetail.getString("hero_id");
        for (JSONObject jsonObject : DotaHeros.GET_HERO_LIST()) {
            if (Integer.parseInt(hero_id) == jsonObject.getInteger("id")) {
                pmd.setHeroName(jsonObject.getString("localized_name"));
                break;
            } else {
                pmd.setHeroName("未知英雄");
            }
        }
        String player_slot = onePlayerDetail.getString("player_slot");
        if (Integer.parseInt(player_slot) < PLAYER_SLOT) {
            pmd.setPlayer_slot(RADIANT);
        } else {
            pmd.setPlayer_slot(DIRE);
        }
        pmd.setKills(onePlayerDetail.getString("kills"));
        pmd.setDeaths(onePlayerDetail.getString("deaths"));
        pmd.setAssists(onePlayerDetail.getString("assists"));
        pmd.setLevel(onePlayerDetail.getString("level"));
        pmd.setHeroDamage(onePlayerDetail.getString("hero_damage"));
        pmd.setTowerDamage(onePlayerDetail.getString("tower_damage"));
        pmd.setHeroHealing(onePlayerDetail.getString("hero_healing"));
        pmd.setLastHits(onePlayerDetail.getString("last_hits"));
        pmd.setDenies(onePlayerDetail.getString("denies"));
        pmd.setGoldPerMin(onePlayerDetail.getString("gold_per_min"));
        pmd.setXpPerMin(onePlayerDetail.getString("xp_per_min"));
        return pmd;
    }

    /**
     * 查询最后一场比赛的详情记录
     *
     * @param accountId dota2数字ID
     * @return JSONObject
     */
    private JSONObject getLastMatchDetail(String accountId) {
        Map<String, String> param = new HashMap<>();
        param.put("account_id", accountId);
        param.put("key", KEY);
        JSONObject matchHistory = httpRequestUtil.sendGet(GET_MATCH_HISTORY, param);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(":authority", "api.steampowered.com");
//        headers.add(":method", "GET");
//        headers.add(":path", "/IDOTA2Match_570/GetMatchHistory/v1/?account_id=210051755&key=BC0E8C660BB3EAB9F6E7AC1FDF0F0C92");
//        headers.add(":scheme", "https");
//        headers.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        headers.add("accept-encoding", "gzip, deflate, br");
//        headers.add("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
//        headers.add("cache-control", "max-age=0");
//        headers.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        JSONObject matchHistory = httpRequestUtil.sendGetWithHeader(headers, GET_MATCH_HISTORY, param);
        JSONObject result = matchHistory.getJSONObject("result");
        JSONArray matchArray = result.getJSONArray("matches");
        JSONObject lastMatch = matchArray.getJSONObject(0);
        String matchId = lastMatch.getString("match_id");
        Map<String, String> secondParam = new HashMap<>();
        secondParam.put("match_id", matchId);
        secondParam.put("key", KEY);
        return httpRequestUtil.sendGet(GET_MATCH_DETAIL, secondParam);
    }
}
