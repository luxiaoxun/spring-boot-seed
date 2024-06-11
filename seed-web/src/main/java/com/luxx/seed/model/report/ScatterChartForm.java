package com.luxx.seed.model.report;

import lombok.Data;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScatterChartForm extends ChartFrom {

    private List<AreaData> lists = new ArrayList<>();

    // 标记大小 默认为6
    private Short markerSize = 6;

    // 标记样式 默认圆
    private MarkerStyle style = MarkerStyle.CIRCLE;

    // 是否弯曲 默认不
    private Boolean smooth;

    // 是否自动生成颜色
    private Boolean varyColors;

    @Data
    public static class AreaData {
        // X轴数据
        private String[] bottomData;

        // Y轴数据
        private Double[] leftData;

        // 点的颜色,可以为空 创建方式为 XDDFColor.from(new byte[]{(byte)0xFF, (byte)0xE1, (byte)0xFF})
        private XDDFColor xddfColor;

        // 系列名称
        private String title;
    }

}