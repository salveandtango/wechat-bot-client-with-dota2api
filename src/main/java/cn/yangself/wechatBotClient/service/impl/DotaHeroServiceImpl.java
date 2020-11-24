package cn.yangself.wechatBotClient.service.impl;

import cn.yangself.wechatBotClient.constant.Dota2Api;
import cn.yangself.wechatBotClient.entity.DotaHero;
import cn.yangself.wechatBotClient.mapper.DotaHeroMapper;
import cn.yangself.wechatBotClient.service.IDotaHeroService;
import cn.yangself.wechatBotClient.utils.NetPostRequest.HttpRequestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Fixed
 * @since 2020-11-22
 */
@Service
@Slf4j
public class DotaHeroServiceImpl extends ServiceImpl<DotaHeroMapper, DotaHero> implements IDotaHeroService {
    @Autowired
    private HttpRequestUtil httpRequestUtil;

    @Override
    public void loadHeros() {
        Map<String, String> param = new HashMap<>();
        param.put("key", Dota2Api.KEY);
        param.put("language", "zh_cn");
        JSONObject result = httpRequestUtil.sendGet(Dota2Api.GET_ALL_HEROS, param).getJSONObject("result");
        JSONArray heros = result.getJSONArray("heroes");
        for (int i = 0; i < heros.size(); i++) {
            int heroId = heros.getJSONObject(i).getInteger("id");
            String name = heros.getJSONObject(i).getString("name");
            String translationCn = heros.getJSONObject(i).getString("localized_name");
            UpdateWrapper<DotaHero> uw = new UpdateWrapper<>();
            uw.eq("hero_id", heroId).eq("hero_name", name).eq("translation_cn", translationCn);
            DotaHero dotaHero = new DotaHero();
            dotaHero.setHeroId(heroId);
            dotaHero.setHeroName(name);
            dotaHero.setTranslationCn(translationCn);
            try {
                saveOrUpdate(dotaHero, uw);
            } catch (Exception e) {
                log.error("英雄更新出错, 详情信息:{}", e.getMessage(), e);
            }
        }

    }
}
