package com.rpc.lrpc.LoadBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobin implements LoadBalancePolicy{

    private final AtomicInteger atomicInteger=new AtomicInteger();
    @Override
    public<T> T getNext(List<T> data) {
        return data.get(atomicInteger.getAndIncrement());
    }

    @Override
    public <T> T getNext(T[] data) {
        return data[atomicInteger.getAndIncrement()];
    }

}
