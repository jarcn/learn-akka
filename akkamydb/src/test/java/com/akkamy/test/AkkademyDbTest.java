package com.akkamy.test;

import static org.junit.Assert.assertEquals;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import com.akkamy.db.AkkademyDb;
import com.akkamy.messages.GetRequest;
import com.akkamy.messages.SetRequest;
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
public class AkkademyDbTest {

    ActorSystem actorSystem = ActorSystem.create();

    @Test
    public void testAkkaDb() throws InterruptedException, TimeoutException, ExecutionException {
        TestActorRef<AkkademyDb> actorRef = TestActorRef.create(actorSystem, Props.create(AkkademyDb.class));
        actorRef.tell(new SetRequest("hello", "akka"), ActorRef.noSender());

        Future<Object> test = Patterns.ask(actorRef, new GetRequest("hello"), 1000);
        CompletionStage cs = FutureConverters.toJava(test);
        String s = ((CompletableFuture<String>) cs).get(1000, TimeUnit.MILLISECONDS);

        assertEquals(s, "akka");
    }


}
