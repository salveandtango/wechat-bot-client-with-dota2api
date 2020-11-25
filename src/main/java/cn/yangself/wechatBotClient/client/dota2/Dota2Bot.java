package cn.yangself.wechatBotClient.client.dota2;

import cn.yangself.wechatBotClient.entity.WxidAccountid;
import cn.yangself.wechatBotClient.service.IWxidAccountidService;
import cn.yangself.wechatBotClient.utils.DateUtil;
import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
     *
     * @param wxid wxid
     * @param content   接收信息
     * @return String msg
     */
    public String updateAccountId(String wxid, String content) {
        String resMsg = "数字ID格式不正确";
        Matcher idMatch = Pattern.compile("\\s\\d+|\\w\\d+").matcher(content);
        if (idMatch.find()) {
            String accountId = idMatch.group();
            UpdateWrapper<WxidAccountid> uw = new UpdateWrapper<>();
            uw.eq("wx_id", wxid);
            WxidAccountid wxidAccountid = new WxidAccountid();
            wxidAccountid.setAccountId(accountId);
            try {
                wxidAccountidService.update(wxidAccountid, uw);
                resMsg = "数字ID录入成功";
            } catch (Exception e) {
                resMsg = "数字ID录入失败";
                log.error("数字ID录入失败, 详情信息: {}", e.getMessage());
            }
        }
        return resMsg;
    }

    /**
     * 战报发送
     *
     * @param wxid wxid
     */
    public String report(String wxid, String content) {
        //返回信息
        String resMsg;
        //判断是否指定名称进行战报发送
        Matcher nameMatch = Pattern.compile("\\s\\w+").matcher(content);
        //dota2数字id
        String accountId;
        //数据对象
        WxidAccountid wxidAccountid;
        QueryWrapper<WxidAccountid> qw = new QueryWrapper<>();
        if (nameMatch.find()) {
            String name = nameMatch.group();
            qw.eq("name", name);
        } else {
            qw.eq("wxid", wxid);
        }
        wxidAccountid = wxidAccountidService.getOne(qw);
        if (wxidAccountid == null) {
            resMsg = "还未录入你的信息..\n可以按照以下格式发送信息:\n数字ID 123456 老王\n进行录入";
        } else {
            accountId = wxidAccountid.getAccountId();
            resMsg = battleReportGen.getReport(accountId);
        }
        return resMsg;
    }

}
