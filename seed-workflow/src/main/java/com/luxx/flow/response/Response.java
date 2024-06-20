package com.luxx.flow.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T> {
    private String code;
    private String message;
    private T data;
}
