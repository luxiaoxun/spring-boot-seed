package com.luxx.seed.model.webssh;

import lombok.Data;

@Data
public class WebSSHMsg {
    //操作
    private String operate;
    private String host;
    //端口号默认为22
    private Integer port = 22;
    private String username;
    private String password;
    private String command = "";
}
