package com.zyd.blog.business.aspect;

import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.service.SysConfigService;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 用于监控freemarker自定义标签中共享变量是否发生变化，发生变化时实时更新到内存中
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 2.0
 * @date 2018/5/17 17:06
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class FreemarkerSharedVariableMonitorAspects {

    private static volatile long configLastUpdateTime = 0L;
    @Autowired
    protected freemarker.template.Configuration configuration;
    @Autowired
    private SysConfigService configService;

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut() {
        // 切面切入点
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        //获取config服务的config
        Map config = configService.getConfigs();
        //判断config是否为空
        if (null == config) {
            log.error("config为空");
            return;
        }
        //获取config的更新时间
        Long updateTime = ((Date) config.get(ConfigKeyEnum.UPDATE_TIME.getKey())).getTime();
        //判断config表是否更新
        if (updateTime == configLastUpdateTime) {
            log.debug("config表未更新");
            return;
        }
        log.debug("config表已更新，重新加载config到shared variable");
        //更新config表最后一次更新时间
        configLastUpdateTime = updateTime;
        try {
            //添加config共享变量
            configuration.setSharedVariable("config", config);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
    }
}
