package com.rpc.lrpc.Enums;

import com.rpc.lrpc.message.Content.Request.*;

public enum CommandType {

    Call(0),Register(1),Pull(2),Update(3),DokiDoki(4);

    public static Class<?>[] requestTypeClass={
            DefaultCallServicesRequest.class,
            DefaultRegisterRequest.class,
            DefaultPullServicesRequest.class,
            UpdateServiceRequest.class,
            DokiDokiRequest.class
    };

    private int value;

    CommandType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final static CommandType[] commandTypes={Call,Register,Pull,Update,DokiDoki};

    public static CommandType forInt(int i)
    {
        return commandTypes[i];
    }
}
