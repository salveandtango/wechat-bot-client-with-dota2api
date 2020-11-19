package cn.yangself.wechatBotClient.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * DATE工具
 *
 * @author Fixed
 * @since 2020-5-20 19:13:28
 */
@Component
public class DateUtil {

    /**
     * 生成当时的LocalDateTime
     *
     * @return localDateTime
     */
    public LocalDateTime getLocalDateTime() {
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @return java.sql.Timestamp
     * @des 生成当前时间戳
     * @author Fixed
     * @date 2020/8/24 17:31
     */
    public Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return java.sql.Timestamp
     * @des 转化为时间戳
     * @author Fixed
     * @date 2020/9/2 15:20
     * @params [date]
     */
    public Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public Timestamp toTimestamp(String time) {
        return new Timestamp(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(time, new ParsePosition(0)).getTime());
    }

    /**
     * @return java.lang.String
     * @des 生成当前simpleDateFormat
     * @author Fixed
     * @date 2020/9/2 15:14
     */
    public String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getTimestamp());
    }

    /**
     * @return java.lang.String
     * @des 转化为SimpleDateFormat
     * @author Fixed
     * @date 2020/9/2 15:16
     */
    public String toSimpleDateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public String toSimpleDateFormat(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }

    public String toSimpleDateFormat(LocalDateTime localDateTime) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public String toSimpleDateFormat(Long timeValue) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeValue), ZoneId.systemDefault()));
    }


    /**
     * 将秒数转化为时分秒
     *
     * @param time 时间段对应的秒数
     * @return 时分秒格式时间戳
     */
    public String secToTime(int time) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            if (time >= 3600) {
                hour = time / 3600;
                time = time % 3600;
            }

            if (time >= 60) {
                minute = time / 60;
                second = time % 60;
            }
            return timeFormat(hour) + ":" + timeFormat(minute) + ":" + timeFormat(second);
        }
    }

    private String timeFormat(int num) {
        String retStr = null;
        if (num >= 0 && num < 10) {
            retStr = "0" + Integer.toString(num);
        } else {
            retStr = "" + num;
        }
        return retStr;
    }
}
