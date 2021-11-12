package com.yan.rpc.config.bean;

import com.alibaba.fastjson.JSON;
import com.yan.rpc.config.ConsumerConfig;
import com.yan.rpc.domain.RpcProviderConfig;
import com.yan.rpc.network.client.ClientSocket;
import com.yan.rpc.network.msg.Request;
import com.yan.rpc.reflect.JDKProxy;
import com.yan.rpc.registry.RedisRegistryCenter;
import com.yan.rpc.until.ClassLoaderUtils;
import io.netty.channel.ChannelFuture;
import org.apache.zookeeper.server.quorum.ServerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author hairui
 * @date 2021/11/12
 * @des
 */
public class ConsumerBean<T> extends ConsumerConfig implements FactoryBean {

    private Logger logger = LoggerFactory.getLogger(ServerBean.class);

    private ChannelFuture channelFuture;

    private RpcProviderConfig rpcProviderConfig;

    @Override
    public Object getObject() throws Exception {
        //从redis获取链接
        if (null == rpcProviderConfig) {
            String infoStr = RedisRegistryCenter.obtainProvider(nozzle, alias);
            rpcProviderConfig = JSON.parseObject(infoStr, RpcProviderConfig.class);
        }
        assert null != rpcProviderConfig;

        //获取通信channel
        if (null == channelFuture) {
            ClientSocket clientSocket = new ClientSocket(rpcProviderConfig.getHost(), rpcProviderConfig.getPort());
            new Thread(clientSocket).start();
            for (int i = 0; i < 100; i++) {
                if (null != channelFuture) break;
                Thread.sleep(500);
                channelFuture = clientSocket.getFuture();
            }
        }

        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setNozzle(nozzle);
        request.setRef(rpcProviderConfig.getRef());
        request.setAlias(alias);
        return (T) JDKProxy.getProxy(ClassLoaderUtils.forName(nozzle), request);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return ClassLoaderUtils.forName(nozzle);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
