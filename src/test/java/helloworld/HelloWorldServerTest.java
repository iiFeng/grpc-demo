package helloworld;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class HelloWorldServerTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup=new GrpcCleanupRule();

    @Test
    public void greeterImplReplyMessage() throws IOException {
        String serverName= InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(new HelloWorldServer.GreeterImpl()).build().start());

        GreeterGrpc.GreeterBlockingStub blockingStub=GreeterGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        HelloReply reply=blockingStub.sayHello(HelloRequest.newBuilder().setName("test name").build());
        assertEquals("use sayHello method test name",reply.getMessage());
    }
}
