package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.RequestContent;

public class RequestMessage<T extends RequestContent> extends AbstractMessage{

    T content;

    public RequestMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType,RequestContent requestContent) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        content=(T)requestContent;
    }

    public RequestMessage(Message msg, RequestContent value) {
        this(msg.getMagicNumber(),msg.getVersion(),msg.getSerializableType(),msg.getCommandType(),msg.size(),msg.getSeq(),msg.getMessageType(),value);
    }


    @Override
    public T content() {
        return content;
    }
}
