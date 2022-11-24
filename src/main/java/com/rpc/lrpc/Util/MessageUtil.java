package com.rpc.lrpc.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static final String SEQ_COUNTER_NAME ="SeqCounter";

    /**
     * 字符串反序列化并转换为RequestMessage
     * @param msg 内容为字符串类型的Message,
     * @return 内容为字符串序列化后的Message
     * @throws JsonProcessingException
     */
    public static RequestMessage<RequestContent> defaultMessageToRequest(DefaultMessage msg) throws JsonProcessingException {

       RequestContent content=null;
        if (msg.getCommandType().equals(CommandType.Call)) {
            content = (DefaultCallServicesRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }else if (msg.getCommandType().equals(CommandType.Register)){
            content = (DefaultRegisterRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }else if (msg.getCommandType().equals(CommandType.DokiDoki)){
            content = (DokiDokiRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }else if (msg.getCommandType().equals(CommandType.Pull)){
            content = (DefaultPullServicesRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }else if (msg.getCommandType().equals(CommandType.Push)){
            content = (DefaultPushServicesRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }else if (msg.getCommandType().equals(CommandType.Delete)){
            content = (DefaultDeleteServiceRequest)
                    new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        }
        return new RequestMessage<>(
                msg.getMagicNumber(),
                msg.getVersion(),
                msg.getSerializableType(),
                msg.getCommandType(),
                msg.getSize(),
                msg.getSeq(),
                msg.getMessageType(),
                content
        );
    }

    /**
     * 将原生请求内容序列化后重新包装为Message,同时设置了Size字段的大小
     * @param request 原生请求
     * @return 原生RequestContent转换为字符串后的请求
     * @throws JsonProcessingException JSON异常
     */
    public static DefaultMessage requestToDefaultMessage(RequestMessage<RequestContent> request) throws JsonProcessingException {
        String content = new ObjectMapper().writeValueAsString(request.content());
        return new DefaultMessage(
                request.getMagicNumber(),
                request.getVersion(),
                request.getSerializableType(),
                request.getCommandType(),
                content.getBytes(StandardCharsets.UTF_8).length,
                request.getSeq(),
                request.getMessageType(),
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

    /**
     * 序列化和设置size字段
     * @param response 响应Message
     * @return 序列化后响应Message
     * @throws JsonProcessingException
     */

    public static DefaultMessage rpcResponseToDefaultMessage(ResponseMessage<? extends ResponseContent> response) throws JsonProcessingException {
        String content = new ObjectMapper().writeValueAsString(response.content());

        return new DefaultMessage(
                response.getMagicNumber(),
                response.getVersion(),
                response.getSerializableType(),
                response.getCommandType(),
                content.getBytes(StandardCharsets.UTF_8).length,
                response.getSeq(),
                response.getMessageType(),
                content
        );
    }

    /**
     *
     * @param msg 内容为字符串的Message
     * @return 反序列化后的Message
     * @throws JsonProcessingException
     */
    public static ResponseMessage<ResponseContent> DefaultMessageToResponse(DefaultMessage msg) throws JsonProcessingException {
        Object content = new ObjectMapper().readValue(msg.content(), CommandType.responseTypeClass[msg.getCommandType().getValue()]);
        return new ResponseMessage<>(
                msg.getMagicNumber(),
                msg.getVersion(),
                msg.getSerializableType(),
                msg.getCommandType(),
                msg.getSize(),
                msg.getSeq(),
                msg.getMessageType(),
                (ResponseContent) content
        );
    }


    /**
     * 解析Url地址为RpcURL实体类,URL格式为rpc://服务名称:mapping,但是此时返回的RpcURL实体类并没有设置host和port
     * @param url String类型请求url
     * @return URL实体类
     */
   public static RpcUrl parseUrl(String url)
   {
       if (!url.startsWith("rpc://") || !url.contains(":"))
       {
           throw new RuntimeException("Not correct URL");
       }
       String s = url.substring(6);
       String[] split = s.split(":");
       if (split.length!=2)
       {
           throw new RuntimeException("Not correct URL");
       }
       String serviceName=split[0];
       String mapping=split[1];
       return new RpcUrl(new RpcAddress(serviceName), mapping);
   }

    /**
     * 解析Rpc请求的远程地址如:rpc://ip:端口
     * @param address RPC请求地址
     * @return 拆分后的地址
     */
    public static RpcAddress parseAddress(String address)
    {
        if (!address.startsWith("rpc://") || !address.contains(":"))
        {
            throw new RuntimeException("Not correct URL");
        }
        String s = address.substring(6);
        String[] split = s.split(":");
        String host=split[0];
        String port=split[1];
        return new RpcAddress(host,Integer.parseInt(port));
    }



}
