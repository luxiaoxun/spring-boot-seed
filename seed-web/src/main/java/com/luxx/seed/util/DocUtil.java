package com.luxx.seed.util;

import com.luxx.seed.model.report.BarChartForm;
import com.luxx.seed.model.report.LineChartForm;
import com.luxx.seed.model.report.PieChartForm;
import com.luxx.seed.model.report.ScatterChartForm;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocUtil {

    public static void addBreak(XWPFDocument doc) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("\r");
    }

    public static void setBigTitle(XWPFDocument doc, String bigTitle, String smallTitle) {
        XWPFParagraph bigTitleParagraph = doc.createParagraph(); // 新建一个标题段落对象（就是一段文字）
        bigTitleParagraph.setAlignment(ParagraphAlignment.CENTER);// 样式居中
        XWPFRun titleFun = bigTitleParagraph.createRun(); // 创建文本对象
        titleFun.setText(bigTitle); //设置标题的名字
        setTitleFontStyle(titleFun);
        titleFun.addBreak(); // 换行
        if (smallTitle != null && !smallTitle.isEmpty()) {
            titleFun = bigTitleParagraph.createRun(); // 创建文本对象
            titleFun.setText(smallTitle); //设置标题的名字
            setSmallTitleFontStyle(titleFun);
        }
    }

    public static void setTitleText(XWPFParagraph paragraph, String titleText) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);// 样式居中
        XWPFRun titleFun = paragraph.createRun(); // 创建文本对象
        titleFun.setText(titleText); //设置标题的名字
        setTitleFontStyle(titleFun);
    }

    public static void setTableTitle(XWPFDocument doc, String tableTitleName) {
        XWPFParagraph bigTitleParagraph = doc.createParagraph(); // 新建一个标题段落对象（就是一段文字）
        bigTitleParagraph.setAlignment(ParagraphAlignment.CENTER);// 样式居中
        XWPFRun titleFun = bigTitleParagraph.createRun(); // 创建文本对象
        titleFun.addBreak();
        titleFun.setText(tableTitleName); //设置标题的名字
        setTableTitleFontStyle(titleFun);
    }

    public static void setGroupTitle(XWPFParagraph paragraph, String tableTitleName) {
        XWPFRun titleFun = paragraph.createRun(); // 创建文本对象
        titleFun.addBreak();
        titleFun.addTab();
        titleFun.setText(tableTitleName); //设置标题的名字
        setTitleFontStyle(titleFun);
    }

    public static void setHeadingText(XWPFParagraph paragraph, String text) {
        paragraph.setStyle("Heading2");
        XWPFRun textFun = paragraph.createRun(); // 创建文本对象
        textFun.addBreak();
        textFun.setText(text); //设置标题的名字
        setTextFontStyle(textFun);
    }

    public static XWPFTable createTable(XWPFDocument doc, int width, List<String> headers) {
        XWPFTable table = doc.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);
        // 校验一下grid是否为空，如果为空就创建。转pdf的时候如果为空会报空指针
        CTTblGrid grid = table.getCTTbl().getTblGrid();
        if (grid == null) {
            table.getCTTbl().addNewTblGrid();
        }
        CTTblPr pr = table.getCTTbl().getTblPr();
        if (pr == null) {
            table.getCTTbl().addNewTblPr();
        }
        //去掉表格边框
//        table.getCTTbl().getTblPr().unsetTblBorders();
        setTableWidthAndHAlign(table, width, headers);
        return table;
    }

    public static void setTableWidthAndHAlign(XWPFTable table, int width, List<String> headers) {
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        // 表格宽度
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        CTJcTable cTJc = tblPr.addNewJc();
        cTJc.setVal(STJcTable.CENTER);
        tblWidth.setW(width);
        tblWidth.setType(STTblWidth.DXA);

        // 设置表头
        if (headers != null && !headers.isEmpty()) {
            //默认创建的table，就是一个cell，坐标是(0,0)
            XWPFTableRow tableRow = table.getRow(0);
            XWPFTableCell tableCell = tableRow.getCell(0);
            tableCell.setText(headers.get(0));
            setTableCell(table, tableCell);
            for (int i = 1; i < headers.size(); ++i) {
                XWPFTableCell cell = tableRow.addNewTableCell();
                cell.setText(headers.get(i));
                // 转换pdf的时候如果没有这个可能会报空指针
                setTableCell(table, cell);
            }
        }
    }

    private static void setTableCell(XWPFTable table, XWPFTableCell tableCell) {
        //设置width和type，否则导出pdf报错
        int width = table.getWidth();
        CTTcPr pr = tableCell.getCTTc().addNewTcPr();
        CTTblWidth ctTblWidth = pr.addNewTcW();
        ctTblWidth.setW(width);
        ctTblWidth.setType(STTblWidth.DXA);

        //通过XWPFParagraph设置格式
        List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
        if (paragraphs != null && !paragraphs.isEmpty()) {
            XWPFParagraph paragraph = paragraphs.get(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun tmpRun = paragraph.createRun();
            setTableTextFontStyle(tmpRun);
        }
    }

    public static void setTableData(XWPFTable table, List<List<String>> tableData) {
        if (tableData != null && !tableData.isEmpty()) {
            for (List<String> dataRow : tableData) {
                if (dataRow != null && !dataRow.isEmpty()) {
                    XWPFTableRow tableRow = table.createRow();
                    for (int i = 0; i < dataRow.size(); ++i) {
                        XWPFTableCell tableCell = tableRow.getCell(i);
                        String data = dataRow.get(i);
                        tableCell.setText(data);
                        setTableCell(table, tableCell);
                    }
                }
            }
        }
    }

    public static void setValueForCell(XWPFTableCell cell, String value) {
        if (cell == null) {
            return;
        }
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        if (paragraphs != null && !paragraphs.isEmpty()) {
            cell.setText(value);
        } else {
            XWPFParagraph tmpParagraph = cell.getParagraphs().get(0);
            XWPFRun tmpRun = tmpParagraph.createRun();
            tmpRun.setText(value);
        }
    }

    public static void setTitleFontStyle(XWPFRun fun) {
        fun.setTextPosition(20); // 设置两行之间的行间距
        setFontStyle(fun, true, 20, "000000", "宋体");
    }

    public static void setSmallTitleFontStyle(XWPFRun fun) {
        setFontStyle(fun, false, 16, "000000", "宋体");
    }

    public static void setTableTitleFontStyle(XWPFRun fun) {
        setFontStyle(fun, true, 12, "000000", "宋体");
    }

    public static void setTableTextFontStyle(XWPFRun fun) {
        setFontStyle(fun, false, 10, "000000", "宋体");
    }

    public static void setTextFontStyle(XWPFRun fun) {
        setFontStyle(fun, true, 16, "000000", "宋体");
    }

    public static void setFontStyle(XWPFRun fun, boolean isBold, int fontSize, String color, String fontFamily) {
        fun.setBold(isBold); // 加粗
        fun.setFontSize(fontSize); // 字体大小
        fun.setColor(color);// 设置颜色
        fun.setFontFamily(fontFamily);//设置字体
    }

    public static void setHeaderFooter(XWPFDocument doc, String headerText, String footerText) {
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(doc, sectPr);
        //添加页眉
        if (headerText != null && !footerText.isEmpty()) {
            CTP ctpHeader = CTP.Factory.newInstance();
            CTR ctrHeader = ctpHeader.addNewR();
            CTText ctHeader = ctrHeader.addNewT();
            ctHeader.setStringValue(headerText);
            XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, doc);
            //设置为右对齐
            headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
            XWPFParagraph[] parsHeader = new XWPFParagraph[1];
            parsHeader[0] = headerParagraph;
            policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);
        }
        //添加页脚
        if (footerText != null && !footerText.isEmpty()) {
            CTP ctpFooter = CTP.Factory.newInstance();
            CTR ctrFooter = ctpFooter.addNewR();
            CTText ctFooter = ctrFooter.addNewT();
            ctFooter.setStringValue(footerText);
            XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, doc);
            footerParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFParagraph[] parsFooter = new XWPFParagraph[1];
            parsFooter[0] = footerParagraph;
            policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
        }
    }


    public static XWPFChart getChart(XWPFDocument document, Integer width, Integer height) throws IOException, InvalidFormatException {
        if (width == null) {
            width = 15;
        }
        if (height == null) {
            height = 10;
        }
        return document.createChart(width * Units.EMU_PER_CENTIMETER, height * Units.EMU_PER_CENTIMETER);
    }

    /**
     * 创建折线图
     *
     * @param chart        图表对象
     * @param barChartForm 数据对象
     */
    public static void createBarChart(XWPFChart chart, BarChartForm barChartForm) throws Exception {
        String[] categories = barChartForm.getCategories();
        List<Double[]> tableData = barChartForm.getTableData();
        List<String> colorTitles = barChartForm.getColorTitles();
        String title = barChartForm.getTitle();
        if (colorTitles.size() != tableData.size()) {
            throw new Exception("Color size must be equal to data size");
        }
        for (Double[] tableDatum : tableData) {
            if (tableDatum.length != categories.length) {
                throw new Exception("Category size must be equal to data size");
            }
        }
        // 设置标题
        chart.setTitleText(title);
        //标题覆盖
        chart.setTitleOverlay(false);

        // 处理对应的数据
        int numOfPoints = categories.length;
        String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));
        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
        List<XDDFChartData.Series> seriesList = new ArrayList<>();

        // 创建一些轴
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle(barChartForm.getBottomTitle());
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(barChartForm.getBottomTitle());
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        // 创建柱状图的类型
        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        // 为图表添加数据
        for (int i = 0; i < tableData.size(); i++) {
            XDDFChartData.Series series = data.addSeries(categoriesData, XDDFDataSourcesFactory.fromArray(
                    tableData.get(i), chart.formatRange(new CellRangeAddress(1, numOfPoints, i, i))));
            seriesList.add(series);
        }
        for (int i = 0; i < seriesList.size(); i++) {
            seriesList.get(i).setTitle(colorTitles.get(i), setTitleInDataSheet(chart, colorTitles.get(i), 1));
        }
        // 指定为簇状柱状图
        if (tableData.size() > 1) {
            ((XDDFBarChartData) data).setBarGrouping(barChartForm.getGrouping());
            chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal(barChartForm.getNewOverlap());
        }

        // 指定系列颜色
        for (BarChartForm.ColorCheck colorCheck : barChartForm.getList()) {
            XDDFSolidFillProperties fillMarker = new XDDFSolidFillProperties(colorCheck.getXddfColor());
            XDDFShapeProperties propertiesMarker = new XDDFShapeProperties();
            // 给对象填充颜色属性
            propertiesMarker.setFillProperties(fillMarker);
            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(colorCheck.getNum()).addNewSpPr().set(propertiesMarker.getXmlObject());
        }

        ((XDDFBarChartData) data).setBarDirection(BarDirection.COL);
        // 设置多个柱子之间的间隔
        // 绘制图形数据
        chart.plot(data);
        // create legend
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.LEFT);
        legend.setOverlay(false);
    }

    private static CellReference setTitleInDataSheet(XWPFChart chart, String title, int column) throws Exception {
        XSSFWorkbook workbook = chart.getWorkbook();
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        if (row == null)
            row = sheet.createRow(0);
        XSSFCell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        cell.setCellValue(title);
        return new CellReference(sheet.getSheetName(), 0, column, true, true);
    }

    /**
     * 创建折线图
     *
     * @param chart         图表对象
     * @param lineChartForm 数据对象
     */
    public static void createLineChart(XWPFChart chart, LineChartForm lineChartForm) {
        // 标题
        chart.setTitleText(lineChartForm.getTitle());
        //标题覆盖
        chart.setTitleOverlay(false);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle(lineChartForm.getBottomTitle());
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(lineChartForm.getLeftTitle());
        // 处理数据
        XDDFCategoryDataSource bottomDataSource = XDDFDataSourcesFactory.fromArray(lineChartForm.getBottomData());
        XDDFNumericalDataSource<Integer> leftDataSource = XDDFDataSourcesFactory.fromArray(lineChartForm.getLeftData());

        // 生成数据
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        // 不自动生成颜色
        data.setVaryColors(lineChartForm.getVaryColors());

        //图表加载数据，折线
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(bottomDataSource, leftDataSource);

        //是否弯曲
        series.setSmooth(lineChartForm.getSmooth());

        //设置标记样式
        series.setMarkerStyle(lineChartForm.getStyle());

        //绘制
        chart.plot(data);
    }

    /**
     * 创建散点图
     *
     * @param chart            图表对象
     * @param scatterChartForm 数据对象
     */
    public static void createScatterChart(XWPFChart chart, ScatterChartForm scatterChartForm) {
        //标题
        chart.setTitleText(scatterChartForm.getTitle());
        //标题覆盖
        chart.setTitleOverlay(false);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle(scatterChartForm.getBottomTitle());
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(scatterChartForm.getLeftTitle());
        //创建chart
        XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
        // 是否自动生成颜色
        data.setVaryColors(false);
        for (int i = 0; i < scatterChartForm.getLists().size(); i++) {
            // 处理数据
            XDDFNumericalDataSource<Integer> bottomDataSource = XDDFDataSourcesFactory.fromArray(scatterChartForm.getLists().get(i).getBottomData());
            XDDFNumericalDataSource<Integer> leftDataSource = XDDFDataSourcesFactory.fromArray(scatterChartForm.getLists().get(i).getLeftData());
            //图表加载数据
            XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) data.addSeries(bottomDataSource, leftDataSource);
            //设置标记样式
            series.setMarkerStyle(scatterChartForm.getStyle());
            series.setMarkerSize(scatterChartForm.getMarkerSize());
            // 设置系列标题
            series.setTitle(scatterChartForm.getLists().get(i).getTitle(), null);
            // 去除连接线
            chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(i).addNewSpPr().addNewLn().addNewNoFill();
            if (scatterChartForm.getLists().get(i).getXddfColor() != null) {
                // 创建一个设置对象
                XDDFSolidFillProperties fillMarker = new XDDFSolidFillProperties(scatterChartForm.getLists().get(i).getXddfColor());
                XDDFShapeProperties propertiesMarker = new XDDFShapeProperties();
                // 给对象填充颜色属性
                propertiesMarker.setFillProperties(fillMarker);
                // 修改系列颜色
                chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(i).getMarker()
                        .addNewSpPr().set(propertiesMarker.getXmlObject());
            }
        }
        //绘制
        chart.plot(data);
    }

    /**
     * 创建饼状图
     *
     * @param chart        图表对象
     * @param pieChartForm 数据对象
     */
    public static void createPieChart(XWPFChart chart, PieChartForm pieChartForm) {
        // 标题
        chart.setTitleText(pieChartForm.getTitle());
        //标题覆盖
        chart.setTitleOverlay(false);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        // 处理数据
        XDDFCategoryDataSource bottomDataSource = XDDFDataSourcesFactory.fromArray(pieChartForm.getBottomData());
        XDDFNumericalDataSource<Integer> leftDataSource = XDDFDataSourcesFactory.fromArray(pieChartForm.getLeftData());

        // 生成数据
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        // 自动生成颜色
        data.setVaryColors(true);

        //图表加载数据
        data.addSeries(bottomDataSource, leftDataSource);

        //绘制
        chart.plot(data);
    }
}
