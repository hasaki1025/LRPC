package com.rpc.lrpc.Util;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Exception.IncorrectMagicNumberException;
import com.rpc.lrpc.message.*;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MessageUtil {


    public static  byte[] MAGIC_NUMBER ={1,0,2,6};
    public static  String MAGIC =new String(MAGIC_NUMBER);

    public static  int MESSAGE_PREFIX_LENGTH =16;

    public static  byte VERSION =1;

    public static  SerializableType DEFAULT_SERIALIZABLETYPE=SerializableType.JSON;

    public static  CommandType DEFAULT_COMMANDTYPE;
    public static MessageType messageType;




    private static int seq=1;

    public static DefaultMessage RequestToDefaultMessage(RequestMessage<RequestContent> requestMessage,String content)
    {
        return new DefaultMessage(requestMessage.getMagicNumber(),
                requestMessage.getVersion(),
                requestMessage.getSerializableType(),
                requestMessage.getCommandType(),
                content.getBytes(StandardCharsets.UTF_8).length,
                requestMessage.getSeq(),
                requestMessage.getMessageType(),
                content
        );
    }

    //魔数（4）-版本号（1）-序列化算法（1）-消息类型（1）-指令类型(1)-请求序号(4)-正文长度(4)-消息本体
    public static Message byteToMessage(ByteBuf buf) throws IncorrectMagicNumberException {
        byte[] mn = new byte[4];
        buf.readBytes(mn);
        String magicNumber = new String(mn);
        if (!magicNumber.equals(MAGIC))
        {
            throw new IncorrectMagicNumberException();
        }
        log.debug("magic number :{}",magicNumber);

        int version = buf.readByte();
        log.debug("version :{}",version);

        SerializableType serializableType = SerializableType.forInt(buf.readByte());
        log.debug("serializableType :{}",serializableType);

        MessageType messageType = MessageType.forInt(buf.readByte());
        log.debug("MessageType :{}",messageType);

        CommandType commandType=CommandType.forInt(buf.readByte());
        log.debug("commandType :{}",commandType);

        int seq = buf.readInt();
        log.debug("seq :{}",seq);

        int len = buf.readInt();
        log.debug("len :{}",len);

        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        String content=new String(bytes);
        log.debug("content :{}",content);

        return new DefaultMessage(magicNumber,version,serializableType,commandType,len,seq,messageType,content);

    }

    //魔数（4）-版本号（1）-序列化算法（1）-消息类型（1）-指令类型(1)-请求序号(4)-正文长度(4)-消息本体
    public static void messageToByteBuf(Message message, ByteBuf buffer)
    {
        buffer.writeBytes(message.getMagicNumber().getBytes(StandardCharsets.UTF_8));

        buffer.writeByte((byte)message.getVersion());


        buffer.writeByte((byte)message.getSerializableType().getValue());

        buffer.writeByte((byte)message.getMessageType().getValue());

        buffer.writeByte((byte)message.getCommandType().getValue());

        buffer.writeInt(message.getSeq());

        buffer.writeInt(message.size());

        buffer.writeBytes(((String) message.content()).getBytes(StandardCharsets.UTF_8));
    }


    public static DefaultMessage rpcResponseToDefaultMessage(ResponseMessage<? extends ResponseContent> response,String content)
    {
        return new DefaultMessage(
                response.getMagicNumber(),
                response.getVersion(),
                response.getSerializableType(),
                response.getCommandType(),
                response.size(),
                response.getSeq(),
                response.getMessageType(),
                content
        );
    }



    public static int getNextSeq()
    {
        return seq++;
    }

    public static Byte[] intToByteArray(int i)
    {
        Byte[] bytes = new Byte[4];

        bytes[3]= (byte) (i | 0xff);
        bytes[2]= (byte) (i>>8 | 0xff);
        bytes[1]= (byte) (i>>8 | 0xff);
        bytes[0]= (byte) (i>>8 | 0xff);
        return bytes;
    }



}
