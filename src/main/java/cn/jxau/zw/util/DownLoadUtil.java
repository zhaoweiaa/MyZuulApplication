package cn.jxau.zw.util;

import cn.jxau.zw.annotation.ExcelConfig;
import cn.jxau.zw.pojo.TestDTO;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * 导出excel工具类
 */
public class DownLoadUtil {

    private static final Logger log = LoggerFactory.getLogger(DownLoadUtil.class);

    /**
     * 导出excel
     * @param title
     * @param clz
     * @param data
     * @param out
     */
    public static void exportExcel(String title, Class<?> clz, Collection<?> data, OutputStream out) throws Exception {

        //数据不为空
        if(Objects.isNull(data) || Objects.equals(data.size(), 0)){
            throw new Exception("没有数据！");
        }

        //参数不为空
        if(Objects.isNull(title) || Objects.isNull(clz) || Objects.isNull(out)){
            throw new Exception("参数为空");
        }

        //创建一个excel文件对象
        Workbook workbook = new HSSFWorkbook();
        //创建一个工作簿
        Sheet sheet = workbook.createSheet(title);

        //标题
        List<String> exportFieldTitle = new ArrayList<>();
        //字段宽度
        List<Integer> exportFieldWidth = new ArrayList<>();
        //取出方法
        List<Method> methodObj = new ArrayList<>();

        Map<String,Method> convertMethod = new HashMap<>();
        //获取所有字段
        Field[] fields = clz.getDeclaredFields();
//        //是否求和
//        boolean isSum = false;
//        List<BigDecimal> sumList = new ArrayList<>();
//        List<Boolean> isSumList = new ArrayList<>();
//        List<Integer> scaleList = new ArrayList<>();
//        List<Boolean> isMeregeList = new ArrayList<>();
//        List<Method> meregeFlagList = new ArrayList<>();

        //遍历所有字段
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            ExcelConfig excelConfig = field.getAnnotation(ExcelConfig.class);

            if(Objects.nonNull(excelConfig)){

                //添加标题
                exportFieldTitle.add(excelConfig.exportName());
                //添加标题宽度
                exportFieldWidth.add(excelConfig.exportFieldWith());
                //属性名称
                String fieldName = field.getName();

                log.debug(i + excelConfig.exportName() + "" + "列宽" + excelConfig.exportFieldWith());

                StringBuffer getMethodName = new StringBuffer("get");

                getMethodName.append(fieldName.substring(0,1).toUpperCase())
                        .append(fieldName.substring(1));
                Method getMethod = clz.getMethod(getMethodName.toString(), new Class[]{});
                //添加方法
                methodObj.add(getMethod);

                //添加转换方法
                if(excelConfig.exportConvertSign()){
                    StringBuilder getConvertMethodName = new StringBuilder("get");
                    getConvertMethodName.append(fieldName.substring(0,1).toUpperCase())
                            .append(fieldName.substring(1))
                            .append("Convert");

                    log.debug("convert:" + getConvertMethodName.toString());
                    //添加到转换方法map
                    convertMethod.put(getMethodName.toString(),clz.getMethod(getConvertMethodName.toString(),new Class[]{}));
                }
                //是否求和配置
//                if(!Objects.equals(i, 0)){
//                    if(excelConfig.isSum()){
//                        isSum = true;
//                        log.debug(field.getName() + "需要求和");
//                        isSumList.add(true);
//                        sumList.add(new BigDecimal(0));
//                        scaleList.add(excelConfig.scale());
//                    }else{
//                        isSum = false;
//                        isSumList.add(false);
//                        sumList.add(null);
//                        scaleList.add(null);
//                    }
//                }else{
//                    isSumList.add(false);
//                    sumList.add(null);
//                    scaleList.add(null);
//                }

                //是否合并
//                isMeregeList.add(excelConfig.isMerege());
//                if(excelConfig.isMerege()){
//                    StringBuilder getMeregeFlagName = new StringBuilder("get");
//                    String meregeFlag;
//
//                    if(StringUtils.isEmpty(excelConfig.meregeFlag())){
//                        meregeFlag = getMethodName.toString();
//                    }else{
//                        getMeregeFlagName.append(excelConfig.meregeFlag().substring(0,1).toUpperCase())
//                                .append(excelConfig.meregeFlag().substring(1));
//                        meregeFlag = getMeregeFlagName.toString();
//                    }
//                    meregeFlagList.add(clz.getMethod(meregeFlag,new Class[]{}));
//                }else{
//                    meregeFlagList.add(null);
//                }
            }

        }

        int index = 0;

        //标题行
        Row row = sheet.createRow(index);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setColor(HSSFColor.RED.index);
        titleStyle.setFont(titleFont);


        for (int i = 0,exportFieldTitleSize = exportFieldTitle.size(); i < exportFieldTitleSize; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(new HSSFRichTextString(exportFieldTitle.get(i)));
        }

        titleStyle = null;
        titleFont=null;

        //设置列宽
        for (int i = 0; i < exportFieldWidth.size(); i++) {
            sheet.setColumnWidth(i,256*exportFieldWidth.get(i));
        }

        Iterator<?> iterator = data.iterator();

        HashMap<String, Object> poiModalMap = new HashMap<>();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        while(iterator.hasNext()){
            index++;
            row = sheet.createRow(index);

            Object obj = iterator.next();
            for (int i = 0; i <methodObj.size() ; i++) {
                Cell cell = row.createCell(i);
                Method method = methodObj.get(i);
                Object value;

                //是否转换字段
                if(convertMethod.containsKey(method.getName())){
                    Method conMethod = convertMethod.get(method.getName());
                    value = conMethod.invoke(obj);
                }else{
                    value = method.invoke(obj);
                }
                cell.setCellValue(Objects.isNull(value)?"":value.toString());
                //是否求和
//                if(isSumList.get(i)){
//                    BigDecimal bigDecimal = sumList.get(i);
//                    if(value instanceof Number){
//                        sumList.set(index,bigDecimal.add(new BigDecimal(value.toString())));
//                    }else if(value instanceof String){
//                        sumList.set(i,bigDecimal.add(new BigDecimal(1)));
//                    }else{
//                        log.warn("未知类型");
//                    }
//                }
            }
        }
        workbook.write(out);
    }

    public static void main(String[] args) {
        try {

            List<TestDTO> list = new ArrayList<>();
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 2, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 2, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 2, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 0, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 0, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 2, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 2, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 2, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 2, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));
            list.add(new TestDTO(1,"王尼玛", 1, "C语言", 22));
            list.add(new TestDTO(1,"王尼玛", 1, "C++", 33));
            list.add(new TestDTO(2,"葫芦娃", 1, "Python", 50));
            list.add(new TestDTO(2,"葫芦娃", 1, "java", 44));
            list.add(new TestDTO(2,"葫芦娃", 1, "PHP", 66));
            list.add(new TestDTO(3,"佩奇", 1, "Python", 77));
            list.add(new TestDTO(3,"佩奇", 1, "java", 54));
            list.add(new TestDTO(3,"佩奇", 1, "PHP", 82));
            list.add(new TestDTO(4,"乔治", 1, "Python", 63));
            list.add(new TestDTO(4,"乔治", 1, "java", 77));
            list.add(new TestDTO(4,"乔治", 1, "PHP", 72));
            list.add(new TestDTO(5,"熊大", 1, "Python", 88));
            list.add(new TestDTO(5,"熊大", 1, "java", 91));
            list.add(new TestDTO(5,"熊大", 1, "PHP", 12));

            DownLoadUtil.exportExcel("测试", TestDTO.class,list, Files.newOutputStream(Paths.get("cess.xls"), StandardOpenOption.CREATE_NEW));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
