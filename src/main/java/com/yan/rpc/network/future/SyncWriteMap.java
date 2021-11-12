package com.yan.rpc.network.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by hairui
 */
public class SyncWriteMap {

    public static Map<String, WriteFuture> syncKey = new ConcurrentHashMap<String,WriteFuture>();

}
