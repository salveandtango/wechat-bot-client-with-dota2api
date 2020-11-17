package cn.yangself.wechatBotClient.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * DATE工具
 * @author Fixed
 * @since 2020-5-20 19:13:28
 */
@Component
public class DateUtil {

    /**
     * 生成当时的LocalDateTime
     * @return localDateTime
     */
    public LocalDateTime getLocalDateTime(){
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @des 生成当前时间戳
     * @author Fixed
     * @date 2020/8/24 17:31
     * @return java.sql.Timestamp
     */
    public Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * @des 转化为时间戳
     * @author Fixed
     * @date 2020/9/2 15:20
     * @params [date]
     * @return java.sql.Timestamp
     */
    public Timestamp toTimestamp(Date date){
        return new Timestamp(date.getTime());
    }
    public Timestamp toTimestamp(String time){
        return new Timestamp(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(time, new ParsePosition(0)).getTime());
    }

    /**
     * @des 生成当前simpleDateFormat
     * @author Fixed
     * @date 2020/9/2 15:14
     * @return java.lang.String
     */
    public String now(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getTimestamp());
    }

    /**
     * @des 转化为SimpleDateFormat
     * @author Fixed
     * @date 2020/9/2 15:16
     * @return java.lang.String
     */
    public String toSimpleDateFormat(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    public String toSimpleDateFormat(Timestamp timestamp){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }
    public String toSimpleDateFormat(LocalDateTime localDateTime){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }
}
