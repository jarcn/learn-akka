package com.akkamydb.client;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.akkamy.messages.GetRequest;
import com.akkamy.messages.SetRequest;
import java.util.concurrent.CompletionStage;

/**
 * @author chenjia on 2019/1/11
 */
public class AkkaMyDbClinet {

    private final ActorSystem system = ActorSystem.create("akkamydb-clinet");
    private final ActorSelection remoteDb;

    public AkkaMyDbClinet(String remoteAddress){
        remoteDb = system.actorSelection("akka.tcp://akka-chenjia@" + remoteAddress + "/user/akkademy-db");
    }

    public CompletionStage set(String key, Object value) {
        return toJava(ask(remoteDb, new SetRequest(key, value), 2000));
    }

    public CompletionStage<Object> get(String key){
        return toJava(ask(remoteDb, new GetRequest(key), 2000));
    }

}
