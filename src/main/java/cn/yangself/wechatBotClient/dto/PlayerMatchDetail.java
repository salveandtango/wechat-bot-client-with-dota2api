package cn.yangself.wechatBotClient.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Description 玩家比赛详情
 * @Author Fixed
 * @Date 2020/11/17 17:31
 */
@Data
public class PlayerMatchDetail {
    /**
     * 阵营(天辉 | 夜魇)
     */
    String player_slot;
    /**
     * 胜利方
     */
    String winner;
    /**
     * 使用英雄
     */
    String heroName;
    /**
     * 游戏时间
     */
    String spendTime;
    /**
     * 击杀
     */
    String kills;
    /**
     * 死亡
     */
    String deaths;
    /**
     * 助攻
     */
    String assists;
    /**
     * 正补
     */
    String lastHits;
    /**
     * 反补
     */
    String denies;
    /**
     * gpm
     */
    String goldPerMin;
    /**
     * xpm
     */
    String xpPerMin;
    /**
     * 等级
     */
    String level;
    /**
     * 英雄伤害
     */
    String heroDamage;
    /**
     * 防御塔伤害
     */
    String towerDamage;
    /**
     * 英雄治疗
     */
    String heroHealing;
    /**
     * 参战率
     */
    String participationRate;
    /**
     * 参葬率
     */
    String mortalityRate;
    /**
     * 输出经济比
     */
    String outputEconomyRatio;
    /**
     * 输出率
     */
    String outputRate;

    @Override
    public String toString() {
        return "阵营: " + player_slot + "\t" + winner + "\n" +
                "游戏时间: " + spendTime + "\n" +
                "使用英雄: " + heroName + "\t" + "等级: " + level + "\n" +
                "击杀: " + kills + "\t" + "死亡: " + deaths + "\t" + "助攻: " + assists + "\n" +
                "英雄伤害: " + heroDamage + "\n" +
                "建筑伤害: " + towerDamage + "\n" +
                "英雄治疗: " + heroHealing + "\n" +
                "正补: " + lastHits + "\t" + "反补: " + denies + "\n" +
                "GPM: " + goldPerMin + "\t" + "XPM: " + xpPerMin + "\n" +
                "参战率: " + participationRate + "\t" + "参葬率: " + mortalityRate + "\n" +
                "输出率: " + outputRate + "\t" + "输出经济比: " + outputEconomyRatio;
    }
}
