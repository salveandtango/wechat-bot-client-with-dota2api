package cn.yangself.wechatBotClient.client;

import cn.yangself.wechatBotClient.client.dota2.Dota2Bot;
import cn.yangself.wechatBotClient.constant.WxCode;
import cn.yangself.wechatBotClient.dto.MessageDto;
import cn.yangself.wechatBotClient.dto.WXMsg;
import cn.yangself.wechatBotClient.service.IWechatDataService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Slf4j
public class WXServerListener extends WebSocketClient {

    @Autowired
    private IWechatDataService dataService;

    private static final String ROOM_MEMBER_LIST = "op:list member";
    private static final String PERSONAL_DETAIL = "op:personal detail";
    private static final String CONTACT_LIST = "user list";
    private static final String NULL_MSG = "null";
    private static final String LITTLE_SHEEP = "wxid_c8yo19a6379722";


    public WXServerListener(String url) throws URISyntaxException {
        super(new URI(url));
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("正在建立连接......");
    }


    @Override
    public void onMessage(String s) {
        //封装msg对象
        JSONObject result = JSONObject.parseObject(s);
        MessageDto msg = JSONObject.toJavaObject(result, MessageDto.class);
        //屏蔽ROOT发送的无意义信息
        if (!"ROOT".equals(msg.getSender())) {
            log.info("接收到的消息 --> " + s);
            String content = msg.getContent();
            String wxid = msg.getSender();
            String receiver = msg.getReceiver();
            int type = msg.getType();
            if (receiver != null && receiver.equals("filehelper") && content.equals("加载数据")) {
                log.info("准备加载数据");
                getContactList();
            }
            if(content.contains("刀塔")){
//                Dota2Bot(wxid,content);
            }
            switch (type) {
                case 5000:
                case 5001:
                    dataService.loadWxData(content);
                    log.info("准备匹配roomId...");
                    getRoomMemberList();
                    break;
                case 5010:
                    dataService.matchRoomId(content);
                    break;
            }

        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("断开连接！");
        //重启客户端
//        restartListener();
    }

    @Override
    public void onError(Exception e) {
        log.info("服务器发生异常！");
        log.info(e.getMessage());
        e.printStackTrace();
        //重启客户端
//        restartListener();
    }


    private void sendMsg(String json) {
        try {
            send(json);
        } catch (Exception e) {
            //发送消息失败！
            log.info("发送消息失败！");
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }


    private String getSessionId() {
        return String.valueOf(new Date().getTime());
    }

    public void sendTextMsg(String wxid, String text) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(text)
                .wxid(wxid)
                .type(WxCode.TXT_MSG)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送文本消息 --> " + json);
        sendMsg(json);
    }


    public void sendImgMsg(String wxid, String imgUrlStr) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(imgUrlStr)
                .wxid(wxid)
                .type(WxCode.PIC_MSG)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送图片消息 --> " + json);
        sendMsg(json);
    }

    public void sendFileHelper(String text) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(text)
                .receiver(text)
                .type(555)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送文件助手消息 --> " + json);
        sendMsg(json);
    }


    public void sendAtMsg(String wxid, String roomId, String text, String nickName) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(text)
                .wxid(wxid)
                .roomId(roomId)
                .type(WxCode.AT_MSG)
                .id(getSessionId())
                .nick(nickName)
                .build()
                .toJson();
        log.info("发送微信群AT成员消息 --> " + json);
        sendMsg(json);
    }


    public void getContactList() {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(CONTACT_LIST)
                .wxid(NULL_MSG)
                .type(WxCode.USER_LIST)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送获取联系人列表请求 --> " + json);
        sendMsg(json);
    }

    public void getRoomMemberList() {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(ROOM_MEMBER_LIST)
                .wxid(NULL_MSG)
                .type(WxCode.CHATROOM_MEMBER)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送获取所有群成员列表请求 --> " + json);
        sendMsg(json);
    }


    public void checkPersonalDetail(String wxid) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(PERSONAL_DETAIL)
                .wxid(wxid)
                .type(WxCode.PERSONAL_DETAIL)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送个人信息查询 --> " + json);
        sendMsg(json);
    }

    public void debugSwitch() {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content("on")
                .wxid("ROOT")
                .type(WxCode.DEBUG_SWITCH)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("开启调试模式 --> " + json);
        sendMsg(json);
    }

    public void getNicksByRoomId(String roomId) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(roomId)
                .wxid("ROOT")
                .type(WxCode.CHATROOM_MEMBER_NICK)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发起获取群成员昵称列表请求 --> " + json);
        sendMsg(json);
    }


    /*public void restartListener() {
        ExecutorService threadPool = new ThreadPoolExecutor(1, 1, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.execute(() -> {
            WechatBotClientApplication.context.close();
            WechatBotClientApplication.context = SpringApplication.run(WechatBotClientApplication.class,
                    WechatBotClientApplication.args);
        });
        threadPool.shutdown();
    }*/
}
