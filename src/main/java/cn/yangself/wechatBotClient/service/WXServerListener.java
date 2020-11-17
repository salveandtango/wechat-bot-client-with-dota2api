package cn.yangself.wechatBotClient.service;

import cn.yangself.WechatBotClientApplication;
import cn.yangself.wechatBotClient.domain.WXMsg;
import cn.yangself.wechatBotClient.dto.WxData;
import cn.yangself.wechatBotClient.dto.MessageDto;
import cn.yangself.wechatBotClient.service.dota2.BattleReport;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.boot.SpringApplication;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WXServerListener extends WebSocketClient {

    private static final int HEART_BEAT = 5005;             //服务器返回心跳包
    private static final int RECV_TXT_MSG = 1;              //收到的消息为文字消息
    private static final int RECV_PIC_MSG = 3;              //收到的消息为图片消息
    private static final int USER_LIST = 5000;              //发送消息类型为获取用户列表
    private static final int GET_USER_LIST_SUCCESS = 5001; //获取用户列表成功
    private static final int GET_USER_LIST_FAIL = 5002; //获取用户列表失败
    private static final int TXT_MSG = 555;                 //发送消息类型为文本
    private static final int PIC_MSG = 500;                 //发送消息类型为图片
    private static final int AT_MSG = 550;                  //发送群中@用户的消息
    private static final int CHATROOM_MEMBER = 5010;        //获取群成员
    private static final int CHATROOM_MEMBER_NICK = 5020;
    private static final int PERSONAL_INFO = 6500;
    private static final int DEBUG_SWITCH = 6000;
    private static final int PERSONAL_DETAIL = 6550;
    private static final int DESTROY_ALL = 9999;

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
        //在这里编写应答的一些代码
        //可以在这里通过正则，或者是发送的消息类型进行判断，进行一些文字回复
        //也可以在这里调用的其他的接口，可以使用Utils包下面的NetPostRequest进行相应的调用

        //注意对sender为ROOT时的消息进行过滤，还有对公众号的消息进行过滤(gh_xxxxxx)
        JSONObject result = JSONObject.parseObject(s);
        MessageDto msg = JSONObject.toJavaObject(result, MessageDto.class);
//        log.info("接收到的消息 --> " + s);
        if (!"ROOT".equals(msg.getSender())) {
            log.info("接收到的消息 --> " + s);
            String content = msg.getContent();
            switch (content) {
                case "战报":
                    //获取该用户wxid并匹配其dota2数字ID
                    String wxid = msg.getSender();
                    List<Map<String,String>> mapList = WxData.GET_WX_AND_DOTA_ID();
                    for (Map<String,String> map : mapList){
                        if (wxid.equals(map.get("wxid"))){
                            //查战绩

                        }
                    }
                    break;
                case "1":
                    sendTextMsg(msg.getContent(), "1");
                    break;
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
                .type(TXT_MSG)
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
                .type(PIC_MSG)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送图片消息 --> " + json);
        sendMsg(json);
    }

    /**
     * 发送AT类型消息 ---> 暂不可用
     */
    public void sendAtMsg(String wxid, String roomId, String text) {
        //创建发送消息JSON
        String json = WXMsg.builder()
                .content(text)
                .wxid(wxid)
                .roomId(roomId)
                .type(AT_MSG)
                .id(getSessionId())
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
                .type(USER_LIST)
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
                .type(CHATROOM_MEMBER)
                .id(getSessionId())
                .build()
                .toJson();
        log.info("发送获取所有群成员列表请求 --> " + json);
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
