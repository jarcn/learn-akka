package com.akkamy.messages;

import lombok.Data;

/**
 * @author chenjia on 2019/1/9
 */
@Data
public class SetRequest {

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
