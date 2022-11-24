package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Util.MessageUtil;
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

     int seq;
     final MessageType messageType;

    @Override
    public int size() {
        return size;
    }





    public AbstractMessage( CommandType commandType, MessageType messageType) {
        this.magicNumber = new String(MessageUtil.MAGIC_NUMBER);
        this.version = MessageUtil.VERSION;
        this.serializableType = SerializableType.JSON;
        this.commandType = commandType;
        this.messageType = messageType;
    }

    public AbstractMessage(SerializableType serializableType, CommandType commandType, MessageType messageType) {
        this.magicNumber = new String(MessageUtil.MAGIC_NUMBER);
        this.version = MessageUtil.VERSION;
        this.serializableType = serializableType;
        this.commandType = commandType;
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
