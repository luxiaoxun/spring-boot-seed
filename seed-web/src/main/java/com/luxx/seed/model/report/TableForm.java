package com.luxx.seed.model.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableForm {
    // 表头行号,从0开始
    private Integer startLine;

    // 要复制数据 长度为复制的行数
    private List<String[]> data = new ArrayList<>();

}