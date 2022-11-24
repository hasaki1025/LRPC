package com.rpc.lrpc.Enums;

public enum RpcRole {
    Register(0),Consumer(1),Provider(2);
    private final   int value;
    private static final RpcRole[] rpcRoles=new RpcRole[]{
            Register,Consumer,Provider
    };

    public int getValue()
    {
        return value;
    }

    public static RpcRole forInt(int i)
    {
        return rpcRoles[i];
    }

    RpcRole(int i) {
        value=i;
    }
}
