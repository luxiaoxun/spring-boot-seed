package com.luxx.seed.controller;

import com.luxx.seed.config.i18n.I18nMessageUtil;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locale")
@Slf4j
public class LocaleController {
    @GetMapping("/current-locale")
    public Response getCurrentLocal() {
        log.info("Current local: " + I18nMessageUtil.getCurrentLocal());
        return ResponseUtil.success();
    }
}
