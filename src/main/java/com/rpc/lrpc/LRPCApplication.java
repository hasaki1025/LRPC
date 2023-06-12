package com.rpc.lrpc;

import com.rpc.lrpc.Annotation.EnableRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Thread.sleep;

@SpringBootApplication
@EnableRpcService
public class LRPCApplication {


    public static void main(String[] args) {
        SpringApplication.run(LRPCApplication.class, args);
    }

}
