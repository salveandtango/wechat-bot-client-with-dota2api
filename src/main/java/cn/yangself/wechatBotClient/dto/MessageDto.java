package cn.yangself.wechatBotClient.dto;

import lombok.Data;

/**
 * @Description ToDo
 * @Author Fixed
 * @Date 2020/11/17 10:23
 */
@Data
public class MessageDto {
    /**
     * 接收到的消息
     */
    String content;
    /**
     * 时间戳
     */
    String id;
    /**
     * 发送人来源 (**@chatroom: 消息群)
     */
    String receiver;
    /**
     * 发送人微信id (wxid_****)
     */
    String sender;
    /**
     * 未知
     */
    String srvid;
    /**
     * 格式化日期
     */
    String time;
    /**
     * 接收消息类型
     */
    Integer type;
}
