package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.RequestContent;

public class RequestMessage<T extends RequestContent> extends AbstractMessage{

    T content;

    public RequestMessage(CommandType commandType, int seq, MessageType messageType, T content) {
        super(commandType, seq, messageType);
        this.content = content;
    }



    public RequestMessage(SerializableType serializableType, CommandType commandType, int seq, MessageType messageType, T content) {
        super(serializableType, commandType, seq, messageType);
        this.content = content;
    }

    public RequestMessage(Message message,T content) {
        super(message.getMagicNumber(), message.getVersion(), message.getSerializableType(), message.getCommandType(), message.size(), message.getSeq(), message.getMessageType());
        this.content = content;
    }


    public RequestMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType, T content) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        this.content = content;
    }

    public RequestMessage(RequestMessage<? extends RequestContent> requestMessage) {
        super(
                requestMessage.getMagicNumber(),
                requestMessage.getVersion(),
                requestMessage.getSerializableType(),
                requestMessage.getCommandType(),
                requestMessage.getSize(),
                requestMessage.getSeq(),
                requestMessage.getMessageType()
        );
        this.content= (T) requestMessage.content();
    }

    @Override
    public T content() {
        return (T) content;
    }



}
