package helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class HelloWorldServer {
    private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());
    private Server server;

    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();

        logger.info("Server started,listening on " + port);
    }

    //阻塞线程，需等待主线程终止
    private void blockUntilShutdown() throws InterruptedException {
        if(server!=null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("use sayHello method " + request.getName()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void sayHelloAgain(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("use helloAgain method " + request.getName()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
