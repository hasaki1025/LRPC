package com.rpc.lrpc;

import com.rpc.lrpc.Annotation.EnableRpcService;
import com.rpc.lrpc.message.RpcAddress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Thread.sleep;

@SpringBootApplication
@EnableRpcService
public class LRpcApplication {


    public static void main(String[] args) {
        SpringApplication.run(LRpcApplication.class, args);
    }

}
