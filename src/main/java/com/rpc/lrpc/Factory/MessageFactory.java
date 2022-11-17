package com.rpc.lrpc.Factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.ResponseMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static com.rpc.lrpc.Util.MessageUtil.MAGIC;
import static com.rpc.lrpc.Util.MessageUtil.VERSION;

@Component
public class MessageFactory {

    public  Message createMessage(String content, SerializableType serializableType, CommandType commandType, MessageType messageType)
    {

        if (serializableType.equals(SerializableType.JSON))
        {
            return new
                    DefaultMessage(MAGIC, VERSION, serializableType, commandType, content.getBytes(StandardCharsets.UTF_8).length, MessageUtil.getNextSeq(),messageType,content);
        }
        return null;
    }


}
