package com.akkamy.db;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.actor.Status.Failure;
import akka.japi.pf.ReceiveBuilder;
import com.akkamy.exception.KeyNotFundException;
import com.akkamy.messages.GetRequest;
import com.akkamy.messages.SetRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjia on 2019/1/9
 */
@Slf4j
public class AkkademyDb extends AbstractActor {

    public final Map<String, Object> map = new HashMap<>();

    public AkkademyDb() {
        super.receive(
          ReceiveBuilder.match(SetRequest.class, msg -> {
              log.debug("存储接收到消息:{}", msg);
              map.put(msg.getKey(), msg.getValue());
              sender().tell(new Status.Success(msg.key), self());
          }).match(GetRequest.class, msg -> {
              log.debug("查询接收到消息:{}", msg);
              Object result = Optional.ofNullable(this.map.get(msg.key))
                .orElse(new Failure(new KeyNotFundException("the key " + msg.key + " not have value")));
              sender().tell(result, self());
          }).matchAny(obj -> {
              log.debug("请求参数为:{}", obj);
              sender().tell(new Status.Failure(new IllegalArgumentException("参数类型不匹配")), self());
          })
            .build()
        );
    }


}
