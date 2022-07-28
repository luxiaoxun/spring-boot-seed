package com.luxx.seed.service;

import com.google.code.appengine.awt.Color;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.luxx.seed.model.report.*;
import com.luxx.seed.util.DocUtil;
import com.luxx.seed.util.FileUtil;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.chart.BarGrouping;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ReportService {
    public void exportReport(String type, HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("report-template.docx");
        //通过word模板生成报告
        XWPFDocument document = null;
        try (InputStream inputStream = resource.getInputStream()) {
            document = new XWPFDocument(inputStream);

            //添加标题
            XWPFParagraph titleParagraph = document.createParagraph();
            DocUtil.setTitleText(titleParagraph, "主机风险报告");

            //段落
            XWPFParagraph firstParagraph = document.createParagraph();
            XWPFRun run = firstParagraph.createRun();
            run.setText("时间范围：" + "2022-05-19 09:40:01 - 2022-07-19 09:40:01");
            run.setColor("696969");
            run.setFontSize(10);
            //设置段落背景颜色
            CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
            cTShd.setVal(STShd.CLEAR);
            cTShd.setFill("97FFFF");
            run.addBreak(); //换行

            //段落1：信息表格
            XWPFParagraph paragraph = document.createParagraph();
            DocUtil.setHeadingText(paragraph, "1.表格信息");

            //构建表格
            List<String> headers = Arrays.asList("字段1", "字段2", "字段3", "字段4");
            XWPFTable table = DocUtil.createTable(document, 8000, headers);
            List<String> data1 = Arrays.asList("1", "2222222222222222222", "3", "4444444444444444444444");
            List<String> data2 = Arrays.asList("5", "6", "7", "8");
            List<List<String>> tableData = new ArrayList<>();
            tableData.add(data1);
            tableData.add(data2);
            DocUtil.setTableData(table, tableData);

            //段落2：信息表格
            XWPFParagraph paragraph2 = document.createParagraph();
            DocUtil.setHeadingText(paragraph2, "2.其他信息");
            //构建表格
            List<String> headers2 = Arrays.asList("字段1", "字段2", "字段3", "字段4", "字段5");
            XWPFTable table2 = DocUtil.createTable(document, 9000, headers2);
            List<String> data3 = Arrays.asList("1", "2222222222222222222", "3", "4444444444444444444444", "55555555555555");
            List<String> data4 = Arrays.asList("6", "7", "8", "9", "10");
            List<List<String>> tableData2 = new ArrayList<>();
            tableData2.add(data3);
            tableData2.add(data4);
            DocUtil.setTableData(table2, tableData2);

            //添加一个段落空行
            DocUtil.addBreak(document);

            addPieChart(document);
            addScatterChart(document);
            addLineChart(document);
            addBarChart(document);

            //添加一个段落空行
            DocUtil.addBreak(document);

            //添加图片
            ClassPathResource imageResource = new ClassPathResource("elastic-log.png");
            try (InputStream imageData = imageResource.getInputStream()) {
                int imageType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageFileName = imageResource.getFilename();
                addImage(document, imageData, imageType, imageFileName, 300, 200);
            }

            //test
//            writeToFile(document);

            if ("word".equals(type)) {
                downloadDoc(document, response, "report-doc.docx");
            } else if ("pdf".equals(type)) {
                downloadPdf(document, response, "report-pdf.pdf");
            }

        } catch (Exception e) {
            log.error("Report export error: {}", e.toString());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void addImage(XWPFDocument document, InputStream imageData, int imageType,
                          String imageFileName, int width, int height) throws IOException, InvalidFormatException {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addPicture(imageData, imageType, imageFileName, Units.toEMU(width), Units.toEMU(height));
    }

    private void addPieChart(XWPFDocument document) throws IOException, InvalidFormatException {
        XWPFChart chart = DocUtil.getChart(document, null, null);
        PieChartForm pieChartForm = new PieChartForm();
        pieChartForm.setTitle("各个国家xx分布");
        pieChartForm.setBottomData(new String[]{"俄罗斯", "加拿大", "美国", "中国", "巴西", "澳大利亚", "印度"});
        pieChartForm.setLeftData(new Integer[]{17098242, 9984670, 9826675, 9596961, 8514877, 7741220, 3287263});
        DocUtil.createPieChart(chart, pieChartForm);
    }

    private void addScatterChart(XWPFDocument document) throws IOException, InvalidFormatException {
        XWPFChart chart = DocUtil.getChart(document, null, null);
        ScatterChartForm scatterChartForm = new ScatterChartForm();
        scatterChartForm.setTitle("测试");
        scatterChartForm.setBottomTitle("X轴");
        scatterChartForm.setLeftTitle("Y轴");
        scatterChartForm.setStyle(MarkerStyle.CIRCLE);
        scatterChartForm.setMarkerSize((short) 10);
        scatterChartForm.setVaryColors(false);

        ScatterChartForm.AreaData areaData = new ScatterChartForm.AreaData();
        areaData.setBottomData(new Integer[]{1, 2, 3, 4, 5, 8, 7});
        areaData.setLeftData(new Integer[]{5, 5, 5, 4, 5, 6, 7});
        areaData.setTitle("测试1");
        scatterChartForm.getLists().add(areaData);

        ScatterChartForm.AreaData areaData2 = new ScatterChartForm.AreaData();
        areaData2.setBottomData(new Integer[]{6, 9});
        areaData2.setLeftData(new Integer[]{1, 9});
        areaData2.setXddfColor(XDDFColor.from(new byte[]{(byte) 0xFF, (byte) 0xE1, (byte) 0xFF}));
        areaData2.setTitle("测试2");
        scatterChartForm.getLists().add(areaData2);
        DocUtil.createScatterChart(chart, scatterChartForm);
    }

    private void addLineChart(XWPFDocument document) throws IOException, InvalidFormatException {
        XWPFChart chart = DocUtil.getChart(document, null, null);
        LineChartForm lineChartForm = new LineChartForm();
        lineChartForm.setTitle("测试");
        lineChartForm.setBottomTitle("X轴");
        lineChartForm.setLeftTitle("Y轴");
        lineChartForm.setStyle(MarkerStyle.STAR);
        lineChartForm.setMarkerSize((short) 6);
        lineChartForm.setSmooth(false);
        lineChartForm.setVaryColors(false);
        lineChartForm.setBottomData(new String[]{"俄罗斯", "加拿大", "美国", "中国", "巴西", "澳大利亚", "印度"});
        lineChartForm.setLeftData(new Integer[]{17098242, 9984670, 9826675, 9596961, 8514877, 7741220, 3287263});
        DocUtil.createLineChart(chart, lineChartForm);
    }

    private void addBarChart(XWPFDocument document) throws Exception {
        XWPFChart chart = DocUtil.getChart(document, null, null);
        String[] categories = new String[]{"Lang 1", "Lang 2", "Lang 3"};
        Double[] valuesA = new Double[]{10d, 20d, 30d};
        Double[] valuesB = new Double[]{15d, 25d, 35d};
        Double[] valuesC = new Double[]{10d, 8d, 20d};
        List<Double[]> list = new ArrayList<>();
        list.add(valuesA);
        list.add(valuesB);
        list.add(valuesC);
        BarChartForm barChartForm = new BarChartForm();
        barChartForm.setTitle("测试");
        barChartForm.setCategories(categories);
        barChartForm.setTableData(list);
        barChartForm.setColorTitles(Arrays.asList("a", "b", "c"));
        barChartForm.setGrouping(BarGrouping.STACKED);
        barChartForm.setNewOverlap((byte) 100);

        BarChartForm.ColorCheck colorCheck = new BarChartForm.ColorCheck();
        colorCheck.setXddfColor(XDDFColor.from(new byte[]{(byte) 0xFF, (byte) 0x33, (byte) 0x00}));
        colorCheck.setNum(0);
        barChartForm.getList().add(colorCheck);

        BarChartForm.ColorCheck colorCheck2 = new BarChartForm.ColorCheck();
        colorCheck2.setXddfColor(XDDFColor.from(new byte[]{(byte) 0x91, (byte) 0x2C, (byte) 0xEE}));
        colorCheck2.setNum(1);
        barChartForm.getList().add(colorCheck2);

        BarChartForm.ColorCheck colorCheck3 = new BarChartForm.ColorCheck();
        colorCheck3.setXddfColor(XDDFColor.from(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x80}));
        colorCheck3.setNum(2);
        barChartForm.getList().add(colorCheck3);

        DocUtil.createBarChart(chart, barChartForm);
    }

    private void writeToFile(XWPFDocument doc) {
        String path = "D:\\work\\test-report.docx";
        try (FileOutputStream out = new FileOutputStream(path)) {
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadDoc(XWPFDocument doc, HttpServletResponse response, String docName) {
        if (doc == null) {
            return;
        }
        //以流的形式下载文件
        try (OutputStream os = response.getOutputStream()) {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(docName, "UTF-8"));
            response.setContentType("application/octet-stream");
            doc.write(os);
        } catch (IOException e) {
            log.error("Download doc exception, doc name:{}, error:{}", docName, e.getMessage());
        }
    }

    public void downloadPdf(XWPFDocument doc, HttpServletResponse response, String pdfName) {
        PdfOptions options = PdfOptions.create();
        //在Linux服务器中支持中文
        options.fontProvider(new IFontProvider() {
            @Override
            public Font getFont(String familyName, String encoding, float size, int style, Color color) {
                try {
                    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    Font fontChinese = new Font(bfChinese, size, style, color);
                    if (familyName != null) {
                        fontChinese.setFamily(familyName);
                    }
                    return fontChinese;
                } catch (Exception e) {
                    log.error("Set font error: {}", e.getMessage());
                    return null;
                }
            }
        });

        //String filePath = "D:\\work\\";
        String filePath = System.getProperty("user.dir") + File.separator;
        log.info("Doc to pdf file path: {}", filePath);
        try (OutputStream outPDF = Files.newOutputStream(Paths.get(filePath + pdfName))) {
            PdfConverter.getInstance().convert(doc, outPDF, options);
            FileUtil.downloadFile(filePath + pdfName, pdfName, response);
            FileUtil.deleteFile(filePath + pdfName);
        } catch (Exception e) {
            log.error("Download Pdf exception, pdf name:{}, error:{}", pdfName, e.getMessage());
        }
    }
}
