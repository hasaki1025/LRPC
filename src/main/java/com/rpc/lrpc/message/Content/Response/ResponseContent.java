package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.Content.MessageContent;

public interface ResponseContent extends MessageContent {

    boolean hasException();
    Exception getException();

    void setException(Exception e);


}
