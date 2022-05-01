package cn.gzsendi.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 应用上下文所有者（这是个工具类，用于不受Spring管理的地方拿到Bean）
 * 另外SpringBoot入口SpringApplication.run(Application.class, args)方法的返回值就是applicationContext实现
 * Created by Mr.XiHui on 2017/8/8.
 */
@Component
public class AppCtxHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    private AppCtxHolder() {}

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
        if (appCtx == null) {
            throw new IllegalArgumentException("传入的applicationContext不能为null");
        }
        if (applicationContext == null) {
            applicationContext = appCtx;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> beanType) {
        assertApplicationContext();
        return applicationContext.getBean(beanType);
    }

    public static <T> T getBean(String name, Class<T> beanType) {
        assertApplicationContext();
        return applicationContext.getBean(name, beanType);
    }

    public static <T> Map<String, T> getBeanOfType(Class<T> beanType) {
        assertApplicationContext();
        return applicationContext.getBeansOfType(beanType);
    }

    public static void destroy() {
        applicationContext = null;
    }

    private static void assertApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext属性未注入");
        }
    }

}
