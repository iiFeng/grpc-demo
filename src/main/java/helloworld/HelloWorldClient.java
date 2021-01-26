package helloworld;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloWorldClient {
    private Logger logger = Logger.getLogger(HelloWorldClient.class.getName());
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    //构造方法初始化字段
    public HelloWorldClient(Channel channel) {
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) throws InterruptedException {
        String name = "java";
        String target = "localhost:50051";
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + name);
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            name = args[0];
        }
        if (args.length > 1) {
            target = args[1];
        }
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        try {
            HelloWorldClient client = new HelloWorldClient(channel);
            client.greet(name);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public void greet(String name) {
        logger.info("will try to greet " + name + "...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
        try {
            response = blockingStub.sayHelloAgain(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }
}
