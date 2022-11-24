package com.rpc.lrpc.Enums;

public enum MessageType {
    request(0),response(1),broadcast(2);
    private int value;

    MessageType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final static MessageType[] messageTypes={request,response,broadcast};

    public static MessageType forInt(int i)
    {
        return messageTypes[i];
    }
}
