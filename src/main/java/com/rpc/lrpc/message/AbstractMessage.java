package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.MessageContent;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import lombok.Data;

/**
 * @author pcdn
 */
@Data
public abstract class AbstractMessage implements Message{

     final String magicNumber;
     final int version;
     final SerializableType serializableType;
     final CommandType commandType;
     int size;
     final int seq;
     final MessageType messageType;
    @Override
    public int size() {
        return size;
    }

    public AbstractMessage(CommandType commandType, int seq, MessageType messageType) {
        this.magicNumber= MessageUtil.MAGIC;
        this.version=MessageUtil.VERSION;
        this.serializableType=SerializableType.JSON;
        this.commandType = commandType;
        this.seq = seq;
        this.messageType = messageType;
    }


    public AbstractMessage(SerializableType serializableType,CommandType commandType, int seq, MessageType messageType) {
        this.magicNumber= MessageUtil.MAGIC;
        this.version=MessageUtil.VERSION;
        this.serializableType=serializableType;
        this.commandType = commandType;
        this.seq = seq;
        this.messageType = messageType;
    }



    public AbstractMessage(SerializableType serializableType,CommandType commandType, int size, int seq, MessageType messageType) {
        this.magicNumber= MessageUtil.MAGIC;
        this.version=MessageUtil.VERSION;
        this.serializableType=serializableType;
        this.commandType = commandType;
        this.size = size;
        this.seq = seq;
        this.messageType = messageType;
    }


    public AbstractMessage(CommandType commandType, int size, int seq, MessageType messageType) {
        this.magicNumber= MessageUtil.MAGIC;
        this.version=MessageUtil.VERSION;
        this.serializableType=SerializableType.JSON;
        this.commandType = commandType;
        this.size = size;
        this.seq = seq;
        this.messageType = messageType;
    }

    public AbstractMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, int seq, MessageType messageType) {
        this.magicNumber = magicNumber;
        this.version = version;
        this.serializableType = serializableType;
        this.commandType = commandType;
        this.size = size;
        this.seq = seq;
        this.messageType = messageType;
    }
}
