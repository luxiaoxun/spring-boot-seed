package com.luxx.seed.model.report;

import lombok.Data;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.chart.BarGrouping;

import java.util.ArrayList;
import java.util.List;

@Data
public class BarChartForm extends ChartFrom {

    // 每个柱子的名称
    private String[] categories;

    // 数据 每个数组数据个数必须和categories长度相同
    private List<Double[]> tableData;

    // 每个颜色的标题 长度必须和tableData长度一致
    private List<String> colorTitles;

    // 柱状图类型 只有在tableData个数大于1时需要填写
    private BarGrouping grouping;

    // 柱子偏移量 堆叠柱状图推荐100 簇状柱状图推荐-20 只有在tableData个数大于1时需要填写
    private byte newOverlap;

    // 颜色阈值对象
    private List<ColorCheck> list = new ArrayList<>();

    @Data
    public static class ColorCheck {
        // 颜色对象 创建方式为 XDDFColor.from(new byte[]{(byte)0xFF, (byte)0xE1, (byte)0xFF})
        private XDDFColor xddfColor;
        // 第几个系列的颜色
        private int num;
    }

}