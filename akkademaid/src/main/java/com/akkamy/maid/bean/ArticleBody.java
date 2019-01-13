package com.akkamy.maid.bean;

import lombok.Data;

/**
 * @author chenjia on 2019/1/13
 */
@Data
public class ArticleBody {

    public final String uri;
    public final String body;

    public ArticleBody(String uri, String body) {
        this.uri = uri;
        this.body = body;
    }

}
