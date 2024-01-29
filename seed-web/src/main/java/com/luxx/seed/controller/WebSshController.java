package com.luxx.seed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSshController {
    @RequestMapping("/websshlogin")
    public String client() {
        return "websshlogin";
    }

    @RequestMapping("/websshpage")
    public String websshpage() {
        return "webssh";
    }

    @RequestMapping("/sftp")
    public String sftp() {
        return "sftp";
    }
}
