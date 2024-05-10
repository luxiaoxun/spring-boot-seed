package com.luxx.seed.util;

import com.google.common.io.Files;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.delete()) {
            logger.error("Delete file {} error", filePath);
        }
    }

    public static void upload(String fileName, byte[] data) throws IOException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(data);
            outputStream.flush();
        }
    }

    public static void download(String fileName, byte[] data, HttpServletResponse response) throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(data.length);
            response.setContentType("application/octet-stream");

            outputStream.write(data);
            outputStream.flush();
        }
    }

    public static void download(File localFile, HttpServletResponse response) throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            String fileName = URLEncoder.encode(localFile.getName(), "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + localFile.length());
            response.setContentType("application/octet-stream;charset=UTF-8");

            Files.copy(localFile, outputStream);
            outputStream.flush();
        }
    }

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
}
