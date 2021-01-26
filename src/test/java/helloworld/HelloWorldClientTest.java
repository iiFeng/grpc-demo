package helloworld;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
@RunWith(JUnit4.class)
public class HelloWorldClientTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    //自己实现接口类
    //创建一个Mock对象
    private final GreeterGrpc.GreeterImplBase serviceImpl =
            mock(GreeterGrpc.GreeterImplBase.class,delegatesTo(
                    new GreeterGrpc.GreeterImplBase() {}));
    private HelloWorldClient client;

    @Before
    public void setUp() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());

        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build());
        client = new HelloWorldClient(channel);
    }
    @Test
    public void greetMessageDeliveredToServer(){
        ArgumentCaptor<HelloRequest> request=ArgumentCaptor.forClass(HelloRequest.class);
        client.greet("test name");//调用 客户端 的方法
        verify(serviceImpl)
                .sayHello(request.capture(), ArgumentMatchers.<StreamObserver<HelloReply>>any());
        assertEquals("test name",request.getValue().getName());
    }
}
