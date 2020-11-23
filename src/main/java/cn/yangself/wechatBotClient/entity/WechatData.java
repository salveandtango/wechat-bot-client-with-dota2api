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
public class WechatData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 微信ID
     */
    private String wxid;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 群聊ID
     */
    private String roomId;
}
