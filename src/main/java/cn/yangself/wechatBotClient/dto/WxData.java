package cn.yangself.wechatBotClient.dto;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WxData {

    /**
     * 屁宝
     */
    static final Map<String, String> PI_BAO = ImmutableMap.of(
            "wxid", "wxid_s8zwb888wa6z22",
            "account_id", "210051755"
    );

    /**
     * hjunwww
     */
    static final Map<String, String> HJUNWWW = ImmutableMap.of(
            "wxid", "wxid_saajgjbqolbs51",
            "account_id", "88565519"
    );

    /**
     * Fixed
     */
    static final Map<String, String> FIXED = ImmutableMap.of(
            "wxid", "wxid_8vxr5qokmq0q22",
            "account_id", "136214886"
    );

    /**
     * 小曹
     */
    static final Map<String, String> XIAO_CAO = ImmutableMap.of(
            "wxid", "caoka19981123",
            "account_id", "278432370"
    );

    /**
     * 牛奶
     */
    static final Map<String, String> NIU_NAI = ImmutableMap.of(
            "wxid", "wxid_g7ez94r1gozz21",
            "account_id", "189819841"
    );

    /**
     * 叉子
     */
    static final Map<String, String> CHA_ZI = ImmutableMap.of(
            "wxid", "wxid_8bklsmu83y9k22",
            "account_id", "207500631"
    );

    /**
     * 老谭
     */
    static final Map<String, String> LAO_TAN = ImmutableMap.of(
            "wxid", "wxid_bxxhrn8x6qlm21",
            "account_id", "213965030"
    );


    /**
     * 获取wx_id和account_id的集合
     * @return list
     */
    public static List<Map<String, String>> GET_WX_AND_DOTA_ID() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(PI_BAO);
        list.add(HJUNWWW);
        list.add(XIAO_CAO);
//        list.add(FIXED);
        list.add(CHA_ZI);
        list.add(LAO_TAN);
        list.add(NIU_NAI);
        return list;
    }
}
