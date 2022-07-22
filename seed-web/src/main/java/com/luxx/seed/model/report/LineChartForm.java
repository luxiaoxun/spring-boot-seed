package com.luxx.seed.model.report;

import lombok.Data;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;

@Data
public class LineChartForm extends ChartFrom {
    // X轴数据
    private String[] bottomData;

    // Y轴数据
    private Integer[] leftData;

    // 标记大小 默认为6
    private Short markerSize = 6;

    // 标记样式 默认圆
    private MarkerStyle style = MarkerStyle.CIRCLE;

    // 是否弯曲 默认不
    private Boolean smooth;

    // 是否自动生成颜色
    private Boolean varyColors;

}