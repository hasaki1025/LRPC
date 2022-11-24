package com.rpc.lrpc.message.Content.Broadcast;

import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

@Data
public class DeleteContent implements BroadMassageContent {
    RpcAddress address;


    public DeleteContent(RpcAddress address) {
        this.address = address;
    }


    public RpcAddress getAddress() {
        return null;
    }
}
