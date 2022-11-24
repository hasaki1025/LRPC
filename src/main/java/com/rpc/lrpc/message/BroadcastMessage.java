package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Broadcast.BroadMassageContent;
import lombok.Data;


public class BroadcastMessage<T extends BroadMassageContent> extends AbstractMessage {

    T content;

    public BroadcastMessage(CommandType commandType, MessageType messageType, T content) {
        super(commandType, messageType);
        this.content = content;
    }

    public BroadcastMessage(SerializableType serializableType, CommandType commandType, MessageType messageType, T content) {
        super(serializableType, commandType, messageType);
        this.content = content;
    }

    public BroadcastMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType, T content) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        this.content = content;
    }

    @Override
    public T content() {
        return content;
    }
}
