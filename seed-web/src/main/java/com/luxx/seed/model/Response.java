package com.luxx.seed.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T> {
    private String code;
    private String message;
    private T result;
}
