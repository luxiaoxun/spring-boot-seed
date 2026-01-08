package com.luxx.seed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSshController {
    @RequestMapping("/websshlogin")
    public String client() {
        return "forward:/page/websshlogin.html";
    }

    @RequestMapping("/websshpage")
    public String websshpage() {
        return "forward:/page/webssh.html";
    }

    @RequestMapping("/sftp")
    public String sftp() {
        return "forward:/page/sftp.html";
    }
}
