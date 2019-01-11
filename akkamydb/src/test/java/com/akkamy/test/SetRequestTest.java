package com.akkamy.test;

import static org.junit.Assert.assertEquals;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.akkamy.db.AkkademyDb;
import com.akkamy.messages.SetRequest;
import org.junit.Test;

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

}
