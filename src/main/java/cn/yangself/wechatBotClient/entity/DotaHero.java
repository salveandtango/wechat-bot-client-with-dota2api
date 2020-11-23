package cn.yangself.wechatBotClient.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fixed
 * @since 2020-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DotaHero implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 英雄ID
     */
    private Integer heroId;

    /**
     * 英雄名
     */
    private String heroName;

    /**
     * 中文翻译
     */
    private String translationCn;


}
