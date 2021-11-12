package com.yan.rpc.config.bean;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("server", new MyBeanDefinitionParser(ServerBean.class));
        registerBeanDefinitionParser("provider", new MyBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer", new MyBeanDefinitionParser(ConsumerBean.class));
    }

}
