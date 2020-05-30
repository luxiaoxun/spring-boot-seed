package com.luxx.seed.vo.response.status;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StatusVo {
    private String name;

    private String code;

    private int count;
}
