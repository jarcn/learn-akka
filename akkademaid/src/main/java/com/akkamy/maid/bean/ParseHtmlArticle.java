package com.akkamy.maid.bean;

import lombok.Data;

/**
 * @author chenjia on 2019/1/13
 */
@Data
public class ParseHtmlArticle {

    private final String uri, htmlString;

    public ParseHtmlArticle(String uri, String htmlString) {
        this.uri = uri;
        this.htmlString = htmlString;
    }
}
