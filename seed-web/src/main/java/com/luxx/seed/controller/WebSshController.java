package com.luxx.seed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSshController {
    @RequestMapping("/websshpage")
    public String websshpage() {
        return "webssh";
    }

    @RequestMapping("/login")
    public String client() {
        return "login";
    }

    @RequestMapping("/sftp")
    public String sftp() {
        return "sftp";
    }
}
