package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.MessageContent;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.ResponseContent;

public class ResponseMessage <T extends ResponseContent> extends AbstractMessage{

    T content;

    public ResponseMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType,T content) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        this.content=content;
    }




    @Override
    public T content() {
        return (T) content;
    }
}
