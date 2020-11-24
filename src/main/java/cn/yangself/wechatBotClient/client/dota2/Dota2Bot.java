package cn.yangself.wechatBotClient.client.dota2;

import cn.yangself.wechatBotClient.entity.WxidAccountid;
import cn.yangself.wechatBotClient.service.IWxidAccountidService;
import cn.yangself.wechatBotClient.utils.DateUtil;
import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description Dota2战报机器人
 * @Author Fixed
 * @Date 2020/11/20 12:28
 */
@Slf4j
@Component
public class Dota2Bot {

    private IWxidAccountidService wxidAccountidService;
    private BattleReportGen battleReportGen;

    @Autowired
    public Dota2Bot(IWxidAccountidService wxidAccountidService, BattleReportGen battleReportGen) {
        this.wxidAccountidService = wxidAccountidService;
        this.battleReportGen = battleReportGen;
    }

    /**
     * 我的wxid
     */
    private static final String FIXED_WXID = "wxid_8vxr5qokmq0q22";


    /**
     * 战报发送
     *
     * @param wxid wxid
     */
    public String report(String wxid, String content) {
        String resMsg = "";
        //匹配字符
        String matchString = "刀塔战报";
        if (content.length() > matchString.length()) {

        }
//        if (reportIs) {
            List<WxidAccountid> dataList = wxidAccountidService.list();
            for (WxidAccountid entry : dataList) {
                if (entry.getWxId().equals(wxid)) {
                    String accountId = entry.getAccountId();
                    String str = battleReportGen.getReport(accountId);
                    resMsg = str != null ? str : "战报系统出错";
                    break;
                } else {
                    resMsg = "";
                }
            }
            if (resMsg.equals("")) {
                resMsg = "还未录入你的信息, 可以按照以下格式和我说:\n刀塔id 1234...\n进行录入";
            }
//        }
        return resMsg;
    }

}
