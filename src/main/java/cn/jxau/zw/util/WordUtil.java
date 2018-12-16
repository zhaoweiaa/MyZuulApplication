package cn.jxau.zw.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.HyperLinkTextRenderData;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.render.RenderAPI;
import com.sun.nio.file.ExtendedOpenOption;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.xwpf.usermodel.XWPFFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static javax.swing.UIManager.put;

/**N
 * word模板测试类（使用freemarker解析ftl模板技术）
 * date 2018-12-16
 */
public class WordUtil {

    private Configuration configuration;

    public WordUtil() {

        this.configuration = new Configuration(Configuration.VERSION_2_3_28);

        configuration.setDefaultEncoding(Charset.forName("UTF-8").name());

    }

    /**
     * 生成word文档
     */
    public void createWord() throws IOException, TemplateException {

        Map<String, Object> dataMap = new HashMap<>();

        getData(dataMap);

        //设置模板文件目录
        configuration.setClassForTemplateLoading(this.getClass(),"");

        //获取模板文件对象
        Template template = configuration.getTemplate("template.xml");

        OutputStream outputStream = Files.newOutputStream(Paths.get("测试wordss.docx"), StandardOpenOption.CREATE_NEW,StandardOpenOption.TRUNCATE_EXISTING);

        template.process(dataMap, new OutputStreamWriter(outputStream));

    }

    /**
     * 数据集
     * @param dataMap
     */
    private void getData(Map<String, Object> dataMap) {

        dataMap.put("title","成绩单");

        dataMap.put("createTime", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        ArrayList<Map> list = new ArrayList<>();

        HashMap hashMap = new HashMap();
        hashMap.put("id","1");
        hashMap.put("content","我 的 英文不太行");

        list.add(hashMap);

        hashMap = new HashMap();
        hashMap.put("id","2");
        hashMap.put("content","我 的 中文不太好");


        list.add(hashMap);

        dataMap.put("list",list);

    }

    /**
     * poi-tl 基于POI语法的简单封装，主要用于生成word模板
     * poi-tl生成word模板
     */
    public void createWordByPoiTl() throws IOException, URISyntaxException {

        //创建模板对象
        String path = this.getClass().getClassLoader().getResource("poitemp.docx").getPath();

        XWPFTemplate template = XWPFTemplate.compile(path);
        //渲染数据
        RenderAPI.render(template,getPoiDataMap());
        //输出word文件
        template.write(Files.newOutputStream(Paths.get(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH24mmss")) + "患者验单.docx"), StandardOpenOption.CREATE_NEW));

        template.close();

    }

    //处理word数据
    private Map<String, Object> getPoiDataMap() {

//        HashMap<String, Object> dataMap = new HashMap<>();
//        //创建表头
//        List<RenderData> tableTitleList = new ArrayList<>();
//        tableTitleList.add(new TextRenderData("FFD39B","疾病"));
//        tableTitleList.add(new TextRenderData("FFD39B","药物"));
//        tableTitleList.add(new TextRenderData("FFD39B","基因"));
//        tableTitleList.add(new TextRenderData("FFD39B","rs"));
//        tableTitleList.add(new TextRenderData("FFD39B","证据等级"));
//        tableTitleList.add(new TextRenderData("FFD39B","基因型"));
//        tableTitleList.add(new TextRenderData("FFD39B","临床指导"));
//
//        //数据列表
//        List<List<?>> arrList = new ArrayList<>();
//
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","3","TT","毒副作用可能较低"));
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","3","TT","毒副作用可能较低"));
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","3","CC","毒副作用可能较低"));
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","2A","GG","毒副作用可能较低"));
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","3","TT","毒副作用可能较高"));
//        arrList.add(Arrays.asList("Neoplasms(PA445062)","irinoctecan(PA4450058)","SLCO1B1(PA134865839)","rs4149056","3","AA","毒副作用可能较高"));
//        arrList.clear();

//        dataMap.put("table", new TableRenderData(tableTitleList, Collections.singletonList(arrList), "no datas", 8600));

        return new HashMap<String, Object>(){{
            put("name","赵伟");
            put("age","1");
            put("gender","男");
//            put("link", new HyperLinkTextRenderData("website","http://www.deepoove.com"));
        }};
    }


    public static void main(String[] args) {

//        try {
//            new WordUtil().createWord();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }

        try {
            new WordUtil().createWordByPoiTl();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
