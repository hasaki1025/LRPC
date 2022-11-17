package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
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
     final int size;
     final int seq;
     final MessageType messageType;
    @Override
    public int size() {
        return size;
    }


}
