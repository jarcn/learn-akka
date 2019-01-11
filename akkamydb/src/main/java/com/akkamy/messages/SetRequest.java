package com.akkamy.messages;

import java.io.*;
import lombok.Data;

/**
 * @author chenjia on 2019/1/9
 */
@Data
public class SetRequest implements Serializable {

    public final String key;
    public final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Set{" +
          "key='" + key + '\'' +
          ", value=" + value +
          '}';
    }
}
