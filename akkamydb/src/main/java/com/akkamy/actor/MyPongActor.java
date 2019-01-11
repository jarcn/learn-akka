package com.akkamy.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建Actor的两种方式
 *
 * @author chenjia on 2019/1/11
 */
@Slf4j
public class MyPongActor extends AbstractActor {

    /**
     * tell()是最基本的单向消息传输模式
     */
    /*@Override
    public PartialFunction receive() {
        return ReceiveBuilder
          .matchEquals("ping", requestMsg -> {
              log.debug("请求消息:{}", requestMsg);
              sender().tell("pong", ActorRef.noSender());
          })
          .matchAny(requestMsg -> {
              log.error("请求参数不正确:{}", requestMsg);
              sender().tell(new Status.Failure(new Exception("unknown message")), self());
          })
          .build();
    }*/

    public MyPongActor() {
        super.receive(
          ReceiveBuilder
            .matchEquals("ping", requestMsg -> {
                log.debug("请求消息:{}", requestMsg);
                sender().tell("pong", ActorRef.noSender());
            })
            .matchAny(requestMsg -> {
                log.error("请求参数不正确:{}", requestMsg);
                sender().tell(new Status.Failure(new Exception("unknown message")), self());
            })
            .build()
        );
    }

}
