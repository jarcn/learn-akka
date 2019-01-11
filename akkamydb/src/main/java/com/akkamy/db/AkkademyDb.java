package com.akkamy.db;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.actor.Status.Failure;
import akka.japi.pf.ReceiveBuilder;
import com.akkamy.exception.KeyNotFundException;
import com.akkamy.messages.DelRequest;
import com.akkamy.messages.GetRequest;
import com.akkamy.messages.SetIfNotExists;
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
              log.debug("插入接收到消息:{}", msg);
              this.insert(msg);
              sender().tell(new Status.Success(msg.key), self());
          }).match(GetRequest.class, msg -> {
              log.debug("查询接收到消息:{}", msg);
              Object result = this.get(msg);
              sender().tell(result, self());
          }).match(DelRequest.class, msg -> {
              log.debug("删除接收到消息:{}", msg);
              Object delete = this.delete(msg);
              sender().tell(delete, self());
          }).match(SetIfNotExists.class, msg -> {
              log.debug("不存在插入收到消息:{}", msg);
              this.setIfNotExists(msg);
              sender().tell(new Status.Success(msg.key), self());
          }).matchAny(obj -> {
              log.debug("请求参数为:{}", obj);
              sender().tell(new Status.Failure(new IllegalArgumentException("参数类型不匹配")), self());
          }).build()
        );
    }


    private void insert(SetRequest msg) {
        Optional<SetRequest> setRequest = Optional.ofNullable(msg);
        map.put(setRequest.map(SetRequest::getKey).get(), setRequest.map(SetRequest::getValue).get());
    }

    private Object get(GetRequest msg) {
        return Optional.ofNullable(this.map.get(msg.key))
          .orElse(new Failure(new KeyNotFundException("the key " + msg.key + " not have value")));
    }

    private Object delete(DelRequest msg) {
        return Optional.ofNullable(map.remove(msg.key))
          .orElse("the key not have value");
    }

    private void setIfNotExists(SetIfNotExists msg) {
        String key = msg.getKey();
        Object o = map.get(key);
        if (o == null) {
            map.put(key, msg.getValue());
        }
    }


}
