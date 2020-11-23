package cn.yangself.wechatBotClient.service;

import cn.yangself.wechatBotClient.entity.WechatData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Fixed
 * @since 2020-11-22
 */
public interface IWechatDataService extends IService<WechatData> {
    /**
     * 根据回调文本匹配群聊ID
     *
     * @param content 微信回调文本
     */
    void matchRoomId(String content);

    /**
     * 初始化微信数据
     *
     * @param content 微信回调文本
     */
    void loadWxData(String content);
}
