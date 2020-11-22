package cn.yangself.wechatBotClient.client;

import cn.yangself.WechatBotClientApplication;
import cn.yangself.wechatBotClient.constant.WxCode;
import cn.yangself.wechatBotClient.dto.WXMsg;
import cn.yangself.wechatBotClient.dto.MessageDto;
import cn.yangself.wechatBotClient.client.dota2.Dota2Bot;
import cn.yangself.wechatBotClient.entity.WechatData;
import cn.yangself.wechatBotClient.service.IWechatDataService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WXServerListener extends WebSocketClient {

    @Autowired
    private Dota2Bot dota2Bot;

    @Autowired
    private IWechatDataService dataService;


    private static final String ROOM_MEMBER_LIST = "op:list member";
    private static final String CONTACT_LIST = "user list";
    private static final String NULL_MSG = "null";


    public WXServerListener(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("正在建立连接......");
    }

    /**
     * 在这里进行消息监听
     *
     * @param s
     */
    @Override
    public void onMessage(String s) {
        //封装msg对象
        JSONObject result = JSONObject.parseObject(s);
        MessageDto msg = JSONObject.toJavaObject(result, MessageDto.class);
        //屏蔽ROOT发送的无意义信息
        if (!"ROOT".equals(msg.getSender())) {
            log.info("接收到的消息 --> " + s);
            String content = msg.getContent();
//            String wxid = msg.getSender();
//            String receiver = msg.getReceiver();
            int type = msg.getType();
            if (content.contains("请求")) {
                getContactList();
//                getRoomMemberList();
            }else if (type == 5001) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray contentArray = jsonObject.getJSONArray("content");
                for(int i = 0; i<contentArray.size(); i++){
                    String name = contentArray.getJSONObject(i).getString("name");
                    String wxid = contentArray.getJSONObject(i).getString("wxid");
                    WechatData wechatData = new WechatData();
                    wechatData.setNickName(name);
                    wechatData.setWxid(wxid);
                    dataService.save(wechatData);
                }
            }

        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("断开连接！");
        //重启客户端
        restartListener();
    }

    @Override
    public void onError(Exception e) {
        log.info("服务器发生异常！");
        log.info(e.getMessage());
        e.printStackTrace();
        //重启客户端
        restartListener();
    }


    /**
     * 发送信息
     *
     * @param json 要发送信息的json字符串
     */
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

    /**
     * 获取会话ID
     *
     * @return
     */
    private String getSessionId() {
        return String.valueOf(new Date().getTime());
    }

    /**
     * 发送文本消息
     *
     * @param wxid 个人的wxid或者群id（xxx@chatroom）
     * @param text 要发送的消息内容
     */
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

    /**
     * 发送图片消息
     *
     * @param wxid      个人的wxid或者群id（xxx@chatroom）
     * @param imgUrlStr 发送图片的绝对路径
     */
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

    /**
     * 发送AT类型消息 ---> 暂不可用
     */
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

    /**
     * 获取联系人列表
     */
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

    /**
     * 获取所有群成员列表
     */
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

    /**
     * 获取群成员昵称
     */
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

    /**
     * Spring重启，实现客户端的自动重连
     */
    public void restartListener() {
        ExecutorService threadPool = new ThreadPoolExecutor(1, 1, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.execute(() -> {
            WechatBotClientApplication.context.close();
            WechatBotClientApplication.context = SpringApplication.run(WechatBotClientApplication.class,
                    WechatBotClientApplication.args);
        });
        threadPool.shutdown();
    }
}
