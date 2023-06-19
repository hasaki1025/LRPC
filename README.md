# 基于Netty的RPC框架

- 基本配置

  ```yaml
  
  RPC:
    Config:
      SerializableType: JSON #配置默认序列化方法
      HeartCheckTime: 60 #心跳检测间隔
      ExpireTime: 90 #服务过期时间
      HeartGap: 30 #心跳发送周期
      RequestTimeOut: 10000 #请求超时时间
      ChannelType: NIO #默认信道类型
      LoadBalancePolicy: com.rpc.lrpc.LoadBalance.RoundRobin #负载均衡策略
      loggingLevel: INFO #日志级别
    #Provider: #服务提供者配置启动端口和服务名称
    #  port: 8080
    #  ServiceName: nihao
    #Server:
    #  Host: 127.0.0.1 #消费者配置注册中心地址
    #  port: 80
    #Register: #注册中心配置启动端口
    #  port: 80
  #
  
  
  
  ```

- 基本使用

  - 加入本框架依赖和springboot依赖

  - 注册中心

    - 基本配置

      ```yaml
      server:
        port: 8080
      RPC:
        Register:
          port: 9080
      ```

    - 基本启动类

      ```java
      @SpringBootApplication
      @EnableRpcService //启动RPC扫描
      public class RegisterApplication {
          public static void main(String[] args) {
              SpringApplication.run(RegisterApplication.class);
          }
      }
      ```

  - 服务提供者

    - 基本配置

      ```yaml
      
      
      RPC:
        Provider:
          port: 9081
          ServiceName: helloRpc
        Server:
          Host: 127.0.0.1
          port: 9080
      ```

    - 启动类(同注册中心只需配置EnableRpcService)

    - 配置对外开发接口

      ```java
      @Slf4j
      @RPCController
      public class ProviderController {
      
      
          @RPCMapping("test")
          public String test(String msg)
          {
              log.info("get message {}",msg);
              return "this Provider";
          }
      }
      ```

  - 服务消费者配置

    ```yaml
    server:
      port: 8082
    
    
    RPC:
      Server:
        Host: 127.0.0.1
        port: 9080
    ```

    - 消费者调用示例

      ```java
      
      @RestController
      @Slf4j
      public class TestProviderController {
      
          @Autowired
          RpcCallRequestSender sender;
      
      
          /**
           * 基本格式为rpc://服务名称:mapping
           */
          final String rpcUrl="rpc://helloRpc:test";
          final String messsage="this is consumer";
      
          /**
           * 异步调用测试用例
           */
          @GetMapping("/testAsyn")
          public void test()
          {
              /**
               * 第二个参数为接受到响应数据后的操作，采用Consumer接口实现
               */
              sender.call(rpcUrl,(x)->{
                  log.info("get message from Provider {}",x);
              },messsage);
          }
      
          @GetMapping("/testSync")
          public void testSync()
          {
              /**
               * 提供url、响应数据类型、message
               */
              sender.callSync(rpcUrl,String.class,messsage);
          }
      }
      
      ```

      
