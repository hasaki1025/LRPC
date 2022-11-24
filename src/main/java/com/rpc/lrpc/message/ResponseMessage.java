package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.MessageContent;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.ResponseContent;

public class ResponseMessage <T extends ResponseContent> extends AbstractMessage{

    T content;

    public ResponseMessage(CommandType commandType, int seq, MessageType messageType, T content) {
        super(commandType, seq, messageType);
        this.content = content;
    }


    public ResponseMessage(SerializableType serializableType, CommandType commandType, int seq, MessageType messageType, T content) {
        super(serializableType, commandType, seq, messageType);
        this.content = content;
    }

    public ResponseMessage(Message message,T content) {
        super(message.getMagicNumber(), message.getVersion(), message.getSerializableType(), message.getCommandType(), message.size(), message.getSeq(), message.getMessageType());
        this.content = content;
    }

    @Override
    public T content() {
        return (T) content;
    }
}
