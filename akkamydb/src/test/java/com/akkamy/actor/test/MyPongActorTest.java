package com.akkamy.actor.test;

import static org.junit.Assert.assertEquals;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.akkamy.actor.MyPongActor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

/**
 * @author chenjia on 2019/1/11
 */
@Ignore
@Slf4j
public class MyPongActorTest {

    ActorSystem actorSystem = ActorSystem.create();
    ActorRef actorRef = actorSystem.actorOf(Props.create(MyPongActor.class), "chenjia");


    @Test
    public void testMyActor() throws InterruptedException, ExecutionException, TimeoutException {
        //像自定actor发送消息（延迟处理响应值,测试代码中模拟阻塞响应）
        Future ping = Patterns.ask(actorRef, "ping", 1000);
        //将scala的future转成java的CompletableFuture类型
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) FutureConverters.toJava(ping);
        //延迟处理响应值,测试代码中模拟阻塞响应
        String s = jFuture.get(1000, TimeUnit.MILLISECONDS);
        log.debug("响应消息:{}", s);
        assertEquals("pong", s);
    }

    @Test
    public void testPong() throws InterruptedException {
        this.askPong("ping").thenAccept(r -> log.debug("response with: " + r));
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    @Test
    public void applyResultTest() throws Exception {
        char result = (char) get(askPong("ping").thenApply(x -> x.charAt(0)));
        assertEquals('p', result);

    }

    /**
     * 我们可以继续像这样把异步操作连接到一起。这是一种进行流数据处理的 很强大的方法。 我们可以向一个远程服务发起调用，然后使用得到的结果向另一个服 务发起调用。 其中任何一个调用失败都会导致整个 Future失败。
     * 接下来我们就来看一下失败的 情况
     */
    @Test
    public void testComper() throws Exception {
        CompletionStage<String> cs = askPong("ping").thenCompose(x -> askPong("ping"));
        assertEquals(get(cs), "pong");
    }

    /**
     * 处理异常
     */
    @Test
    public void testFailure() {
        this.askPong("error").handle((msg, err) -> {
            log.debug("msg :{}", msg);
            if (null != err) {
                log.error("异常信息:{}", err.getMessage());
            }
            return null;
        });
    }

    @Test
    public void comeOutInExTest() throws Exception {
        CompletionStage<String> error = this.askPong("error").exceptionally(err -> {
            log.debug("err:{}", err);
            return "默认值";
        });

        String o = (String) this.get(error);
        assertEquals("默认值", o);
    }


    @Test
    public void shouldRecoverOnErrorAsync() throws Exception {
        CompletionStage<String> cf = askPong("cause error")
          .handle((pong, ex) -> ex == null
            ? CompletableFuture.completedFuture(pong)
            : askPong("ping")
          ).thenCompose(x -> x);
        assertEquals("pong", get(cf));
    }

    @Test
    public void asynchronousRecoveryException() throws Exception {
        CompletionStage<String> cf = this.askPong("error msg")
          .handle((r, e) -> e == null ? CompletableFuture.completedFuture(r) : askPong("ping"))
          .thenCompose(x -> x);
        System.out.println(get(cf));
        assertEquals("pong", get(cf));
    }


    /**
     * 这个例子展示了一种处理多个不同类型 Future 的机制。通过这种方法，可以并行地 执行任务，同时处理多个请求， 更快地将响应返回给用户。这种对并行的使用可以帮助 我们提高系统的响应速度。
     */
    @Test
    public void test1() throws Exception {
        CompletionStage<String> stringCompletionStage = askPong("ping").thenCombine(askPong("ping"), (a, b) -> {
            log.debug("响应结果 a={},b={}", a, b);
            return a + b;
        });
        assertEquals("pongpong", get(stringCompletionStage));
    }


    //Helpers
    private Object get(CompletionStage cs) throws Exception {
        return ((CompletableFuture<String>) cs).get(1000, TimeUnit.MILLISECONDS);
    }

    private CompletionStage<String> askPong(String msg) {
        Future ask = Patterns.ask(actorRef, msg, 1000);
        return FutureConverters.toJava(ask);
    }

}
