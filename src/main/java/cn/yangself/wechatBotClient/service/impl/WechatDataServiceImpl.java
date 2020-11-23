package cn.yangself.wechatBotClient.service.impl;

import cn.yangself.wechatBotClient.entity.WechatData;
import cn.yangself.wechatBotClient.mapper.WechatDataMapper;
import cn.yangself.wechatBotClient.service.IWechatDataService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Fixed
 * @since 2020-11-22
 */
@Service
@Slf4j
public class WechatDataServiceImpl extends ServiceImpl<WechatDataMapper, WechatData> implements IWechatDataService {

    @Override
    public void loadWxData(String content) {
        JSONArray contentArray = JSON.parseArray(content);
        for (int i = 0; i < contentArray.size(); i++) {
            String nickName = contentArray.getJSONObject(i).getString("name");
            String wxidTemple = contentArray.getJSONObject(i).getString("wxid");
            UpdateWrapper<WechatData> uw = new UpdateWrapper<>();
            uw.eq("nick_name", nickName);
            uw.eq("wxid", wxidTemple);
            WechatData wechatData = new WechatData();
            wechatData.setNickName(nickName);
            wechatData.setWxid(wxidTemple);
            try {
                saveOrUpdate(wechatData, uw);
            } catch (Exception e) {
                log.error("初始化微信数据出错, 详情信息: {}", e.getMessage(), e);
            }
        }
        log.info("初始化完成");
    }

    @Override
    public void matchRoomId(String content) {
        //重置roomId所有已有的roomId, 进行更新
        List<WechatData> wechatDataList = list();
        for (WechatData dataTemple : wechatDataList) {
            dataTemple.setRoomId("");
            try {
                updateById(dataTemple);
            } catch (Exception e) {
                log.error("重置roomId出错, 具体信息: {}", e.getMessage(), e);
            }
        }

        JSONArray array = JSON.parseArray(content);
        for (int i = 0; i < array.size(); i++) {
            JSONArray member = array.getJSONObject(i).getJSONArray("member");
            String roomId = array.getJSONObject(i).getString("roomid");
            for (int j = 0; j < member.size(); j++) {
                String wxidTemple = member.getString(j);
                QueryWrapper<WechatData> qw = new QueryWrapper<>();
                qw.eq("wxid", wxidTemple);
                WechatData wechatData = getOne(qw);
                if (wechatData != null) {
                    if (wechatData.getRoomId() != null && !wechatData.getRoomId().equals("")) {
                        JSONArray roomIdArray = JSON.parseArray(wechatData.getRoomId());
                        roomIdArray.add(roomId);
                        wechatData.setRoomId(roomIdArray.toJSONString());
                    } else {
                        wechatData.setRoomId("[\"" + roomId + "\"]");
                    }
                    try {
                        updateById(wechatData);
                    } catch (Exception e) {
                        log.error("更新roomId出现问题, 该条对象: {}", wechatData.toString(), e);
                    }
                }
            }
        }
        log.info("匹配完成");
    }
}
