package com.luxx.seed.model.log;

import lombok.Builder;

@Builder
public class OprLogEntity {
    private String method;

    private String uri;

    private String param;

    private String body;

    private String user;
}
