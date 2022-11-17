package com.rpc.lrpc.message;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.RequestContent;

public interface Message {
    String getMagicNumber();
    int getVersion();
    SerializableType getSerializableType();
    CommandType getCommandType();
    MessageType getMessageType();
    int size();
    Object content();
    int getSeq();
}
