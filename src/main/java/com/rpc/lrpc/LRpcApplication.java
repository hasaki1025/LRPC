package com.rpc.lrpc;

import com.rpc.lrpc.Annotation.EnableRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableRpcService
public class LRpcApplication {


    public static void main(String[] args) {
        SpringApplication.run(LRpcApplication.class, args);
    }

}
