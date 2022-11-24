package com.rpc.lrpc.Enums;

import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.*;

public enum CommandType {

    Call(0),Register(1),Pull(2),Update(3),DokiDoki(4),Push(5),Simple(6);

    public static final Class<?>[] requestTypeClass={
            DefaultCallServicesRequest.class,
            DefaultRegisterRequest.class,
            DefaultPullServicesRequest.class,
            UpdateServiceRequest.class,
            DokiDokiRequest.class,
            DefaultPushServicesRequest.class
    };

    // TODO SimpleResponse需要添加
    public static final Class<?>[] responseTypeClass={
            DefaultCallServicesResponse.class,
            SimpleResponse.class,
            DefaultPullServicesResponse.class,
            UpdateServiceResponse.class,
            DokiDokiResponse.class,
            SimpleResponse.class,
            SimpleResponse.class
    };

    private int value;

    CommandType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final static CommandType[] commandTypes={Call,Register,Pull,Update,DokiDoki,Push,Simple};

    public static CommandType forInt(int i)
    {
        return commandTypes[i];
    }
}
