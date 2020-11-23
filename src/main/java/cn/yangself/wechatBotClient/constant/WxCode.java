package cn.yangself.wechatBotClient.constant;

/**
 * @Description 微信回调code
 * @Author Fixed
 * @Date 2020/11/20 11:18
 */
public class WxCode {
    //服务器返回心跳包
    public static final int HEART_BEAT = 5005;
    //收到的消息为文字消息
    public static final int RECV_TXT_MSG = 1;
    //收到的消息为图片消息
    public static final int RECV_PIC_MSG = 3;
    //发送消息类型为获取用户列表
    public static final int USER_LIST = 5000;
    //获取用户列表失败
    public static final int GET_USER_LIST_FAIL = 5002;
    //发送消息类型为文本
    public static final int TXT_MSG = 555;
    //发送消息类型为图片
    public static final int PIC_MSG = 500;
    //发送群中@用户的消息
    public static final int AT_MSG = 550;
    //获取群成员
    public static final int CHATROOM_MEMBER = 5010;
    public static final int CHATROOM_MEMBER_NICK = 5020;
    public static final int PERSONAL_INFO = 6500;
    public static final int DEBUG_SWITCH = 6000;
    public static final int PERSONAL_DETAIL = 6550;
    public static final int DESTROY_ALL = 9999;

}
