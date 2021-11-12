package com.yan.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
@ConfigurationProperties("rpc.server")
public class ServerProperties {

    private String host;  //注册中心地址
    private int port;     //注册中心端口

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
