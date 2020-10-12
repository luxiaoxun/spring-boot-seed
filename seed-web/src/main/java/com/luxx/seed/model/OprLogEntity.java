package com.luxx.seed.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OprLogEntity {
    private String method;

    private String uri;

    private String param;

    private String body;

    private String user;
}
