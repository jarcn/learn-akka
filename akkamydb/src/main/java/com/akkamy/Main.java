package com.akkamy;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akkamy.db.AkkademyDb;

/**
 * @author chenjia on 2019/1/11
 */
public class Main {

    public static void main(String... args) {
        ActorSystem actorSystem = ActorSystem.create("akka-chenjia");
        actorSystem.actorOf(Props.create(AkkademyDb.class), "akkademy-db");
    }

}
