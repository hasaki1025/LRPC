package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.RequestContent;

public class DefaultMessage extends AbstractMessage {

    String content;

    public DefaultMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType, String content) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        this.content = content;
    }


    @Override
    public String  content() {
        return content;
    }
}
