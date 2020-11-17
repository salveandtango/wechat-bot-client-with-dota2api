package cn.yangself.wechatBotClient.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WxData {

    /**
     * 陈志威
     */
    static Map<String, String> CHEN_ZHI_WEI = new HashMap<String, String>() {{
        CHEN_ZHI_WEI.put("wxid", "wxid_s8zwb888wa6z22");
        CHEN_ZHI_WEI.put("dotaid", "210051755");
    }};

    /**
     * 何秋智
     */
    static Map<String, String> HE_QIU_ZHI = new HashMap<String, String>() {{
        HE_QIU_ZHI.put("wxid", "wxid_saajgjbqolbs51");
        HE_QIU_ZHI.put("dotaid", "");
    }};

    /**
     * 洪伟
     */
    static Map<String, String> HONG_WEI = new HashMap<String, String>() {{
        HONG_WEI.put("wxid", "wxid_8vxr5qokmq0q22");
        HONG_WEI.put("dotaid", "");
    }};

    /**
     * 小曹
     */
    static Map<String, String> XIAO_CAO = new HashMap<String, String>() {{
        XIAO_CAO.put("wxid", "caoka19981123");
        XIAO_CAO.put("dotaid", "");
    }};

    /**
     * 牛奶
     */
    static Map<String, String> NIU_NAI = new HashMap<String, String>() {{
        NIU_NAI.put("wxid", "wxid_g7ez94r1gozz21");
        NIU_NAI.put("dotaid", "");
    }};

    /**
     * 叉子
     */
    static Map<String, String> CHA_ZI = new HashMap<String, String>() {{
        CHA_ZI.put("wxid", "wxid_8bklsmu83y9k22");
        CHA_ZI.put("dotaid", "207500631");
    }};


    /**
     * 获取wx_id和dota数字id的集合
     * @return list
     */
    public static List<Map<String, String>> GET_WX_AND_DOTA_ID() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(CHEN_ZHI_WEI);
        list.add(HE_QIU_ZHI);
        list.add(XIAO_CAO);
        list.add(HONG_WEI);
        list.add(CHA_ZI);
        list.add(NIU_NAI);
        return list;
    }
}
