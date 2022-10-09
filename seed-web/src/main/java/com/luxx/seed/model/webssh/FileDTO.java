package com.luxx.seed.model.webssh;

import lombok.Data;

@Data
public class FileDTO {
    private String fileName;
    private String fileSize;
    private Boolean isDir;
    private String fullFileName;
    private String mtime;
}
