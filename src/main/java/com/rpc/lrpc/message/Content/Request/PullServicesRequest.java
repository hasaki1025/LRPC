package com.rpc.lrpc.message.Content.Request;

import lombok.Data;

@Data
public class PullServicesRequest implements RequestContent {
    //空对象序列化问题解决
    Object object;
}
