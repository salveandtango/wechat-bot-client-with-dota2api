package cn.yangself;

import cn.yangself.wechatBotClient.client.WXServerListener;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.enums.ReadyState;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.websocket.Session;
import java.net.URISyntaxException;

@SpringBootApplication
@MapperScan("cn.yangself.wechatBotClient.mapper")
@Slf4j
public class WechatBotClientApplication {

    @Value("${config.weChat.url}")
    private String weChatUrl;


    public static String[] args;
    public static ConfigurableApplicationContext context ;

    public static void main(String[] args) {
        WechatBotClientApplication.args = args;
        WechatBotClientApplication.context = SpringApplication.run(WechatBotClientApplication.class, args);
//        WXServerListener.setApplicationContext(context);
    }



    @Bean
    public WXServerListener getWXServerListener() throws Exception {
        WXServerListener client = new WXServerListener(weChatUrl);
        client.connect();
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            Thread.sleep(500);
        }
        return client;
    }
}
