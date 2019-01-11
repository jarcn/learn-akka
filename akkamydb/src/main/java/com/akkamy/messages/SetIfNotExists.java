package com.akkamy.messages;

import java.io.*;
import lombok.Data;

/**
 * @author chenjia on 2019/1/11
 */
@Data
public class SetIfNotExists implements Serializable {

    public final String key;
    public final Object value;

    public SetIfNotExists(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetIfNotExists{" +
          "key='" + key + '\'' +
          ", value=" + value +
          '}';
    }

}
