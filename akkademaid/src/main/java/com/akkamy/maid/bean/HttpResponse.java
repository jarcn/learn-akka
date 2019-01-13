package com.akkamy.maid.bean;

import lombok.Data;

/**
 * @author chenjia on 2019/1/13
 */
@Data
public class HttpResponse {

    public final String body;

    public HttpResponse(String body) {
        this.body = body;
    }

}
