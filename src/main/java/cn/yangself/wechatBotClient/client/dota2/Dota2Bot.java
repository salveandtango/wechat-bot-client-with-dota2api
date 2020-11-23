package cn.yangself.wechatBotClient.client.dota2;

import cn.yangself.wechatBotClient.utils.DateUtil;
import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class Dota2Bot {

    private HttpRequestUtil httpRequestUtil;
    private DateUtil dateUtil;
    private BattleReportGen battleReportGen;

    @Autowired
    public Dota2Bot(HttpRequestUtil httpRequestUtil, DateUtil dateUtil, BattleReportGen battleReportGen) {
        this.httpRequestUtil = httpRequestUtil;
        this.dateUtil = dateUtil;
        this.battleReportGen = battleReportGen;
    }

    /**
     * 我的wxid
     */
    private static final String FIXED_WXID = "wxid_8vxr5qokmq0q22";



//    /**
//     * 战报发送
//     *
//     * @param wxid wxid
//     */
//    public String report(String content, String wxid) {
//        String resMsg = null;
//        //匹配字符
//        String report = "战报";
//        String roomList = "1";
//        boolean reportIs = Pattern.matches(report, content);
//        boolean roomListIs = Pattern.matches(roomList, content);
//        if (reportIs) {
//            if("self".equals(wxid)){
//                wxid = FIXED_WXID;
//            }
//            List<Map<String, String>> dataList = WxData.GET_WX_AND_DOTA_ID();
//            for (Map<String, String> map : dataList) {
//                if (map.get("wxid").equals(wxid)) {
//                    String accountId = map.get("account_id");
//                    String str = battleReportGen.getReport(accountId);
//                    resMsg = str != null ? str : "战报系统出错";
//                    break;
//                } else {
//                    resMsg = "还未录入你的信息";
//                }
//            }
//        } else if (roomListIs) {
//            resMsg = "已获取群成员信息";
//        }
//        return resMsg;
//    }

}
