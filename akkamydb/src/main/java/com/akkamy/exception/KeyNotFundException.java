package com.akkamy.exception;

import java.io.*;

/**
 * @author chenjia on 2019/1/11
 */
public class KeyNotFundException extends Exception implements Serializable {

    public final String key;

    public KeyNotFundException(String key) {
        this.key = key;
    }

}
