package com.luxx.seed.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResultModel<T> {
    private boolean error;
    private String errorDesc;
    private T result;
}
