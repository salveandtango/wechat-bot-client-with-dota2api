package cn.yangself.wechatBotClient.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WXMsg {
    private String id;
    private String wxid;
    private String content;
    private String roomId;
    private int type;
    private String nick;
    private String nickname;

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
