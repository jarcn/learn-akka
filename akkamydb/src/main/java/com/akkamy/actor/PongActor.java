package com.akkamy.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import scala.PartialFunction;

/**
 * @author chenjia on 2019/1/10
 */
@Slf4j
public class PongActor extends AbstractActor {

    /**
     * tell()是最基本的单向消息传输模式
     */
    @Override
    public PartialFunction receive() {
        return ReceiveBuilder
          .matchEquals("ping", requestMsg -> sender().tell("pong", ActorRef.noSender()))
          .matchAny(requestMsg -> sender().tell(new Status.Failure(new Exception("unknown message")), self()))
          .build();
    }

}
