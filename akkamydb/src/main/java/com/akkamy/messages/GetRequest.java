package com.akkamy.messages;

import java.io.*;

/**
 * @author chenjia on 2019/1/11
 */
public class GetRequest implements Serializable {

    public final String key;

    public GetRequest(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Get{" + "key='" + key + '\'' + '}';
    }
}
