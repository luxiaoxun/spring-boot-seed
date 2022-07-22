package com.luxx.seed.model.report;

import lombok.Data;

@Data
public class PieChartForm extends ChartFrom {
    // 数据个数
    private String[] bottomData;

    // 数据大小
    private Integer[] leftData;
}