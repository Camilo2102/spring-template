package com.example.template.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanLocator {
    private final ApplicationContext applicationContext;

    public BeanLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T getBeanByString(String beanName, Class<T> targetType) {
        return applicationContext.getBean(beanName, targetType);
    }

    public Class<?> getClassByName(String name) throws ClassNotFoundException {
        String capitalizaName = name.substring(0, 1).toUpperCase() + name.substring(1);
        return Class.forName("com.example.template.models." + capitalizaName);
    }

}
