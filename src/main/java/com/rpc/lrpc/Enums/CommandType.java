package com.rpc.lrpc.Enums;

import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.*;

public enum CommandType {

    Call(0),Register(1),Pull(2),DokiDoki(3),Push(4),Delete(5),Simple(6);

    public static final Class<?>[] requestTypeClass={
            DefaultCallServicesRequest.class,
            DefaultRegisterRequest.class,
            DefaultPullServicesRequest.class,
            DokiDokiRequest.class,
            DefaultPushServicesRequest.class,
            DefaultDeleteServiceRequest.class,
    };


    public static final Class<?>[] responseTypeClass={
            DefaultCallServicesResponse.class,
            SimpleResponse.class,
            DefaultPullServicesResponse.class,
            DokiDokiResponse.class,
            SimpleResponse.class,
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

    private final static CommandType[] commandTypes={Call,Register,Pull,DokiDoki,Push,Delete,Simple};

    public static CommandType forInt(int i)
    {
        return commandTypes[i];
    }
}
