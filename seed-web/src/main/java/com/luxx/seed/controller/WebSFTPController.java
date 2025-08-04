package com.luxx.seed.controller;

import com.luxx.seed.model.webssh.FileDTO;
import com.luxx.seed.model.webssh.SFTPData;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.WebSftpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/sftp")
@Slf4j
public class WebSFTPController {

    @Autowired
    private WebSftpService sftpService;

    @PostMapping("/connect")
    public Response connect(HttpSession session, @RequestBody SFTPData sftpData) throws Exception {
        sftpService.connect(session, sftpData);
        return ResponseUtil.success();
    }

    @PostMapping("/exit")
    public Response disConnect(HttpSession session) throws Exception {
        sftpService.disConnect(session);
        return ResponseUtil.success();
    }


    @PostMapping("/ls")
    public Response<List<FileDTO>> listFiles(HttpSession session, String path) throws Exception {
        return ResponseUtil.success(sftpService.listFiles(session, path));
    }

    @PostMapping("/put")
    public Response put(HttpSession session, MultipartFile uploadFile, String targetDir) throws Exception {
        sftpService.put(session, uploadFile, targetDir);
        return ResponseUtil.success();
    }

    @GetMapping("/get")
    public void get(String fullFileName, HttpSession session, HttpServletResponse response) throws Exception {
        String fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
        //设置下载响应头
        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream inputStream = sftpService.get(session, fullFileName);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copyLarge(inputStream, outputStream);
    }

    @PostMapping("/delete")
    public Response delete(String fullFileName, HttpSession session) throws Exception {
        sftpService.delete(session, fullFileName);
        return ResponseUtil.success();
    }


}
