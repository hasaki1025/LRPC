package com.rpc.lrpc;

import com.rpc.lrpc.net.RpcCallRequestSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static java.lang.Thread.sleep;

@SpringBootTest
public class LRPCApplicationTests {




    @Autowired
    RpcCallRequestSender sender;

    @Test
    void testCallSender() {
        Optional<String> s = sender.callSync("rpc://nihao:test", String.class, "123");
        if (s.isPresent())
        {
            System.out.println(s);
        }
    }
}
