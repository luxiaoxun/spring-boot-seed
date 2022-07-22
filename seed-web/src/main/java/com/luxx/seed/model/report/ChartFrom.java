package com.luxx.seed.model.report;

import lombok.Data;

@Data
public class ChartFrom {
    // 图表标题
    private String title;

    // X轴标题
    private String bottomTitle;

    // Y轴标题
    private String leftTitle;
}