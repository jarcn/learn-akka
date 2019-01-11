package com.akkamydb.clinet.test;

import com.akkamydb.client.AkkaMyDbClinet;
import java.util.concurrent.CompletableFuture;
import org.junit.Test;

/**
 * @author chenjia on 2019/1/11
 */
public class AkkaMyDbClinetTest {

    //服务端Actor启动时设置ip与端口号
    AkkaMyDbClinet client = new AkkaMyDbClinet("127.0.0.1:2552");

    @Test
    public void itShouldSetRecord() throws Exception {
        client.set("123", 123);
        Integer result = (Integer) ((CompletableFuture) client.get("123")).get();
        assert (result == 123);
    }

}
