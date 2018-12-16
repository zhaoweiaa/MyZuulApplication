package cn.jxau.zw.util;

import com.sun.nio.file.ExtendedOpenOption;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * poi Excel工具类
 */

public class POIExcelUtil {


    /**
     * 导出2003 excel
     */
    public static void exportXls(String fileName){

        Objects.requireNonNull(fileName);

        //创建excel文档对象
        HSSFWorkbook workbook = new HSSFWorkbook()  ;


        //创建sheet
        HSSFSheet sheet = workbook.createSheet("学生信息表");

        //创建第一行（0-65535）
        HSSFRow row = sheet.createRow(0);

        //创建第一个单元格（0-255）
        HSSFCell cell = row.createCell(0);

        //设置单元格内容
        cell.setCellValue("学员考试成绩一览表");

//        CellUtil.setCellStyleProperty(cell,workbook,CellUtil.FONT,15);
//
//        CellUtil.setCellStyleProperty(cell,workbook,CellUtil.FILL_BACKGROUND_COLOR,"red");

//        CellUtil.setAlignment(cell,workbook, (short) 2);

        //创建cell样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //创建字体样式
        HSSFFont font = workbook.createFont();

        //设置字号
        font.setFontHeight((short) 500);

        //加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //设置颜色
        font.setColor(HSSFColor.RED.index);

        cellStyle.setFont(font);

        cell.setCellStyle(cellStyle);

        //合并单元格（起始行0，终止行0，起始列0，终止列3）
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));

        //创建第二行
        HSSFRow row1 = sheet.createRow(1);

        //创建第二行单元格并设置内容
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("班级");
        row1.createCell(2).setCellValue("笔试成绩");
        row1.createCell(3).setCellValue("机试成绩");

        HSSFRow row2;
        HSSFCellStyle nameStyle = workbook.createCellStyle();
        HSSFFont nameFont = workbook.createFont();

        //居中
        nameStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //青绿色
        nameFont.setColor(HSSFColor.BRIGHT_GREEN.index2);
        //加粗
        nameFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        nameStyle.setFont(nameFont);

        for (int i = 0; i < 29; i++) {

            row2 = sheet.createRow(2 + i);

            HSSFCell nameCell = row2.createCell(0);
            nameCell.setCellStyle(nameStyle);
            nameCell.setCellValue("李明");

            row2.createCell(1).setCellValue("As178");
            row2.createCell(2).setCellValue(87+i*0.9);
            row2.createCell(3).setCellValue(78+i*0.75);

        }

        try {
//            workbook.write(new FileOutputStream(new File(fileName)));
            workbook.write(Files.newOutputStream(Paths.get(fileName), StandardOpenOption.CREATE_NEW));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取excel
     * @param path
     */
    public static void importExcel(Path path){

        Objects.requireNonNull(path);

        Workbook workbook = null;

        try {

            if(path.toString().endsWith(".xls")){

                workbook = new HSSFWorkbook(Files.newInputStream(path, StandardOpenOption.READ));

            }else{

                workbook = new XSSFWorkbook(Files.newInputStream(path,StandardOpenOption.READ));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //循环sheet
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {

            Sheet sheet = workbook.getSheetAt(numSheet);

            if(Objects.isNull(sheet)){continue;}

            //循环row
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {

                Row row = sheet.getRow(rowNum);

                row.forEach(cell-> {
//                    System.out.println(cell.toString());
                    if(Objects.equals(cell.getCellType(), Cell.CELL_TYPE_BOOLEAN)){
                        System.out.println(cell.getBooleanCellValue());
                    }
                    if(Objects.equals(cell.getCellType(),Cell.CELL_TYPE_STRING)){
                        System.out.println(cell.getStringCellValue());
                    }

//                    //1、判断是否是数值格式
//                    if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
//                        short format = cell.getCellStyle().getDataFormat();
//                        SimpleDateFormat sdf = null;
//                        if(format == 14 || format == 31 || format == 57 || format == 58){
//                            //日期
//                            sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        }else if (format == 20 || format == 32) {
//                            //时间
//                            sdf = new SimpleDateFormat("HH:mm");
//                        }
//                        double value = cell.getNumericCellValue();
//                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
//                        result = sdf.format(date);
//                    }
//                    if(Objects.equals(cell.getCellType(), Cell.CELL_TYPE_NUMERIC)){
//                        System.out.println(cell.getDateCellValue());
//                    }

                    if(Objects.equals(cell.getCellType(),Cell.CELL_TYPE_NUMERIC)){
                        System.out.println(cell.getNumericCellValue());
                    }
                });

            }
        }

    }

    public static void main(String[] args) {

//        POIExcelUtil.exportXls("marks.xls");

        POIExcelUtil.importExcel(Paths.get("marks.xls"));
    }

}
