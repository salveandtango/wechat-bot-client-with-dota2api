package cn.yangself;

import cn.yangself.wechatBotClient.service.WXServerListener;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WechatBotClientApplication {

    @Value("${config.weChat.url}")
    private String weChatUrl;

    public static String[] args;
    public static ConfigurableApplicationContext context ;

    public static void main(String[] args) {
        WechatBotClientApplication.args = args;
        WechatBotClientApplication.context = SpringApplication.run(WechatBotClientApplication.class, args);
    }

    @Bean
    public WXServerListener getWXServerListener() throws Exception {
        WXServerListener client = new WXServerListener(weChatUrl);
        client.connect();
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            Thread.sleep(500);
            System.out.println("正在建立连接......");
        }
        return client;
    }
}
