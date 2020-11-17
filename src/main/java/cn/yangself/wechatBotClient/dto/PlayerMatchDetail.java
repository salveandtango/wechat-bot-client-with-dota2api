package cn.yangself.wechatBotClient.dto;

/**
 * @Description ToDo
 * @Author Fixed
 * @Date 2020/11/17 17:31
 */
public class PlayerMatchDetail {
    /**
     * 使用英雄
     */
    String heroName;
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

    @Override
    public String toString() {
        return "PlayerMatchDetail{" +
                "heroName='" + heroName + '\'' +
                ", kills='" + kills + '\'' +
                ", deaths='" + deaths + '\'' +
                ", assists='" + assists + '\'' +
                ", lastHits='" + lastHits + '\'' +
                ", denies='" + denies + '\'' +
                ", goldPerMin='" + goldPerMin + '\'' +
                ", xpPerMin='" + xpPerMin + '\'' +
                ", level='" + level + '\'' +
                ", heroDamage='" + heroDamage + '\'' +
                ", towerDamage='" + towerDamage + '\'' +
                ", heroHealing='" + heroHealing + '\'' +
                ", participationRate='" + participationRate + '\'' +
                ", mortalityRate='" + mortalityRate + '\'' +
                '}';
    }
}
