package com.luxx.seed.util;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
public class FileUtil {
    public static void downloadFile(String path, String name, HttpServletResponse response) throws Exception {
        // path是指欲下载的文件的路径。
        File file = new File(path);
        try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            String fileName = URLEncoder.encode(name, "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream;charset=UTF-8");

            Files.copy(file, outputStream);
            outputStream.flush();
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.delete()) {
            log.error("Delete file {} error", filePath);
        }
    }
}
