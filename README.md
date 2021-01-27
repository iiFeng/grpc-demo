# gRPC java demo
主要参照 https://github.com/grpc/grpc-java examples 文件夹下的 helloworld 项目。
 
将 helloworld 单个项目独立在此项目下,不再依赖过多地文件和包。

## 使用说明
From grpc-java/examples directory:
- 运行 gradlew 会自动运行 helloworld.proto 文件
```
./gradlew installDist
```
会在文件夹 build/generated/source/proto/main 下生成 .proto 文件定义的接口实现类

且在文件夹 build/install/examples/bin/ 生成对于服务端 hello-world-server, 客户端 hello-world-client

- 启动服务端 hello-world-server
```
./build/install/examples/bin/hello-world-server
```

- 启动客户端 hello-world-client

```
./build/install/grpc-demo/bin/hello-world-client
```
客户端启动还可以加上参数，作为调用服务方法的参数

```
./build/install/grpc-demo/bin/hello-world-client hello-world
```
## 测试类
- 服务端
步骤：生成进程名，创建服务，并注册接口实现类
创建客户端通道并注册进程服务名，桩调用接口实现类的方法，验证返回的值。
- 客户端
步骤：使用 mock 创建接口实现类对象
初始化：生成进程名，创建服务，并注册接口实现类，创建客户端通道并注册进程名
调用客户端的方法，发送请求，验证请求行为


## tips
- .proto 文件需要放在 proto 文件夹下，才会自动生成代码
- 启动服务端、客户端 报 ```找不到或无法加载主类 com.Main``` 需执行 ```./gradlew clean```