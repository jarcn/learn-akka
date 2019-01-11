package com.akkamy.test;

import static org.junit.Assert.assertEquals;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import com.akkamy.db.AkkademyDb;
import com.akkamy.messages.DelRequest;
import com.akkamy.messages.SetRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

/**
 * @author chenjia on 2019/1/9
 */
public class SetRequestTest {

    ActorSystem actorSystem = ActorSystem.create();

    @Test
    public void testSetRequest() {
        TestActorRef<AkkademyDb> actorRef = TestActorRef.create(actorSystem, Props.create(AkkademyDb.class));
        actorRef.tell(new SetRequest("hello", "akka"), ActorRef.noSender());
        actorRef.tell("test other msg",ActorRef.noSender());
        System.out.println(actorRef.path());
        AkkademyDb akkademyDb = actorRef.underlyingActor();
        assertEquals(akkademyDb.map.get("hello"), "akka");
    }

    @Test
    public void testDelRequest() throws InterruptedException {
        TestActorRef<AkkademyDb> actorRef = TestActorRef.create(actorSystem, Props.create(AkkademyDb.class));
        actorRef.tell(new DelRequest("hello"), ActorRef.noSender());
        TimeUnit.SECONDS.sleep(5);

    }


    @Test
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        ActorRef actorRef = actorSystem.actorOf(Props.create(AkkademyDb.class), "chenjia");
        Future sFuture = Patterns.ask(actorRef, new DelRequest("hello"), 1000);
        final CompletionStage cs = FutureConverters.toJava(sFuture);
        final CompletableFuture jFuture = (CompletableFuture) cs;
        Object runtimeException = jFuture.get(1000, TimeUnit.MILLISECONDS);
        assertEquals(runtimeException,"the key not have value");
    }

}
