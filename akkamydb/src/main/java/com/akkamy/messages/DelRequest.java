package com.akkamy.messages;

import java.io.*;

/**
 * @author chenjia on 2019/1/11
 */
public class DelRequest implements Serializable {

    public final String key;

    public DelRequest(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Del{" + "key='" + key + '\'' + '}';
    }

}
