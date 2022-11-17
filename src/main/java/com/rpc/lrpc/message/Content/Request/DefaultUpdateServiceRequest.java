package com.rpc.lrpc.message.Content.Request;

import lombok.Data;

@Data
public class DefaultUpdateServiceRequest implements UpdateServiceRequest{
    String serviceName;

}
