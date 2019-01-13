package com.akkamy.maid.bean;

import lombok.Data;

/**
 * @author chenjia on 2019/1/13
 */
@Data
public class ParseArticle {

    public final String url;

    public ParseArticle(String url) {
        this.url = url;
    }
}
