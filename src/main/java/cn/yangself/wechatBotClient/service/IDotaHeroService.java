package cn.yangself.wechatBotClient.service;

import cn.yangself.wechatBotClient.entity.DotaHero;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Fixed
 * @since 2020-11-22
 */
public interface IDotaHeroService extends IService<DotaHero> {

    /**
     * 加载所有英雄
     */
    void loadHeros();
}
