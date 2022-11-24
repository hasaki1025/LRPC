package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import lombok.Data;


public class ResponseMessage <T extends ResponseContent> extends AbstractMessage{

     T content;


    public ResponseMessage(CommandType commandType, MessageType messageType, T content, int seq) {
        super(commandType, messageType);
        this.content = content;
        this.seq=seq;
    }

    public ResponseMessage(SerializableType serializableType, CommandType commandType, MessageType messageType, T content,int seq) {
        super(serializableType, commandType, messageType);
        this.content = content;
        this.seq=seq;
    }

    public ResponseMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType, T content) {
        super(magicNumber, version, serializableType, commandType, size, seq, messageType);
        this.content = content;
    }

    @Override
    public T content() {
        return (T) content;
    }
}
