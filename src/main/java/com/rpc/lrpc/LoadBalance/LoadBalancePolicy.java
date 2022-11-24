package com.rpc.lrpc.LoadBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LoadBalancePolicy {
    Map<String,Object> PROPERTIES = new HashMap<>();
    <T> T getNext(List<T> data);
    <T> T getNext(T[] data);

    default void setPropertise(String key, Object value){
        PROPERTIES.put(key,value);
    }
}
